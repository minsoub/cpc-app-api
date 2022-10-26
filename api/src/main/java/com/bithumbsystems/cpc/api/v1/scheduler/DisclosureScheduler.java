package com.bithumbsystems.cpc.api.v1.scheduler;

import com.bithumbsystems.cpc.api.v1.xangle.mapper.AssetMapper;
import com.bithumbsystems.cpc.api.v1.xangle.mapper.DisclosureMapper;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.DisclosureResponse;
import com.bithumbsystems.cpc.api.v1.xangle.service.AssetService;
import com.bithumbsystems.cpc.api.v1.xangle.service.DisclosureService;
import com.bithumbsystems.persistence.mongodb.disclosure.model.entity.Disclosure;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class DisclosureScheduler {
  private final DisclosureService disclosureService;

  private final AssetService assetService;

//  @Scheduled (cron = "* * * * * *")
  @SchedulerLock(name = "xangleApiCall", lockAtMostFor = "PT10S", lockAtLeastFor = "PT10S")
  public void getDisclosureXangleApi() {

    saveDisclosure(0);
    saveAsset(0);

  }

  public void saveDisclosure(int page) {

    Mono<DisclosureResponse> disclosureMono = disclosureService.getDisclosureResponseFromXangle(page);

    disclosureMono.map(DisclosureMapper.INSTANCE::toEntity)
        .doOnNext(result -> {
          if (getIndex(result) != null) {
            return;
          }
          int pageNo = page;
          if (result.size() >= 50) {
            saveDisclosure(pageNo++);
          }
        })
        .publishOn(Schedulers.boundedElastic())
        .map(disclosureList -> {
          Integer index = getIndex(disclosureList);
          if (getIndex(disclosureList) != null) {
            disclosureList = disclosureList.stream().limit(index).collect(Collectors.toList());
          }

          return disclosureService.saveAllDisclosure(disclosureList).subscribe();
        })
        .subscribe();
  }

  public Integer getIndex(List<Disclosure> disclosureList) {
    Mono<Disclosure> disclosure = disclosureService.findFirstByOrderByPublishTimestampDesc();

    AtomicReference<OptionalInt> index = new AtomicReference<>();

    disclosure.subscribe(
        result -> {
          index.set(
              IntStream.range(0, disclosureList.size())
              .filter(i -> {
                return result.getDisclosureId().equals(disclosureList.get(i).getDisclosureId());
              }).findFirst()
          );
        }
    );

    System.out.println("@@@@@@@@@");
    System.out.println(index.get());


//    if (index.get()) {
//      return index.get().getAsInt();
//    } else {
//      return null;
//    }
    return null;
  }


  public void saveAsset(int page) {
    Mono<AssetResponse> assetMono = assetService.getAssetResponseFromXangle(page);

    assetMono.map(AssetMapper.INSTANCE::toEntity)
        .doOnNext(result -> {
          int pageNo = page;
          if (result.size() >= 50) {
            saveAsset(++pageNo);
          }
        })
        .publishOn(Schedulers.boundedElastic())
        .map(assets -> assetService.saveAll(assets).subscribe())
        .subscribe();

  }



}
