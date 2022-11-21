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
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
@RequiredArgsConstructor
public class DisclosureScheduler {
  private final DisclosureService disclosureService;
  private final AssetService assetService;


  @Scheduled (cron = "0 0 0/2 * * *")
  @SchedulerLock(name = "xangleApiCall", lockAtMostFor = "PT10S", lockAtLeastFor = "PT10S")
  public void getDisclosureXangleApi() {

    log.info("xangleApi Scheduler Current Thread : {}", Thread.currentThread().getName());

    disclosureService.saveDisclosure(0);
    assetService.saveAsset(0);
//    assetService.insertProjectName();

  }



}
