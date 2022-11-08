package com.bithumbsystems.cpc.api.v1.xangle.service;

import static com.bithumbsystems.cpc.api.core.config.constant.GlobalConstant.EXCHANGE_NAME;

import com.bithumbsystems.cpc.api.core.config.property.XangleProperties;
import com.bithumbsystems.cpc.api.core.util.WebClientUtil;
import com.bithumbsystems.cpc.api.v1.xangle.mapper.DisclosureMapper;
import com.bithumbsystems.cpc.api.v1.xangle.mapper.DisclosureResponseMapper;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.DisclosureClientResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.DisclosureResponse;
import com.bithumbsystems.persistence.mongodb.asset.model.entity.Asset;
import com.bithumbsystems.persistence.mongodb.asset.service.AssetDomainService;
import com.bithumbsystems.persistence.mongodb.disclosure.model.entity.Disclosure;
import com.bithumbsystems.persistence.mongodb.disclosure.service.DisclosureDomainService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException.TooManyRequests;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisclosureService {

  private final DisclosureDomainService disclosureDomainService;
  private final WebClientUtil webClientUtil;
  private final XangleProperties xangleProperties;
  private final AssetService assetService;

  public Mono<Page<DisclosureClientResponse>> getDisclosureList(String search, int pageNo, int pageSize) {
    Pageable page = PageRequest.of(pageNo , pageSize);

    return getDisclosureClientResponse(search, page)
        .collectList()
        .zipWith(disclosureDomainService.countBySearchText(search, page)
            .map(c -> c))
        .map(t -> new PageImpl<>(t.getT1(), page, t.getT2()));

  }

  public Flux<DisclosureClientResponse> getDisclosureClientResponse(String search, Pageable page) {

    return disclosureDomainService.findByOrderByPublishTimestampDesc(search, page).map(
        disclosure -> {
          log.info("disclosure value : {}", disclosure);
          return DisclosureClientResponse.builder()
              .symbol(disclosure.getProjectSymbol())
              .projectLogo(disclosure.getProjectLogo())
              .title(disclosure.getTitle())
              .createDate(disclosure.getPublishTimestamp())
              .xangleUrl(disclosure.getXangleUrl())
              .projectName(disclosure.getCpcAsset().get(0).getProjectName())
              .build();
        }
    ).sort(Comparator.comparing(DisclosureClientResponse::getCreateDate, Comparator.reverseOrder()));
  }

  public Flux<DisclosureClientResponse> findDisclosureFlux(String search, Pageable page) {

    return disclosureDomainService.findByOrderByPublishTimestampDesc(search, page).flatMap(
        disclosure -> {
          return assetService.findById(disclosure.getProjectSymbol()).map(
              asset -> {
                return DisclosureClientResponse.builder()
                    .symbol(disclosure.getProjectSymbol())
                    .projectLogo(disclosure.getProjectLogo())
                    .title(disclosure.getTitle())
                    .createDate(disclosure.getPublishTimestamp())
                    .xangleUrl(disclosure.getXangleUrl())
                    .projectName(asset.getProjectName())
                    .build();
              }
          );
        }
    ).sort(Comparator.comparing(DisclosureClientResponse::getCreateDate, Comparator.reverseOrder()));
  }

  public Mono<DisclosureResponse> getDisclosureResponseFromXangle(int page) {
    return webClientUtil.requestGet(xangleProperties.getHost())
        .get()
        .uri(uriBuilder ->
            uriBuilder.path(xangleProperties.getDisclosurePath())
                .queryParam("exchange_name", EXCHANGE_NAME)
                .queryParam("page", page)
                .build()
        )
        .header("X-XANGLE_API_KEY", xangleProperties.getXangleApiKey())
        .retrieve()
        .bodyToMono(DisclosureResponse.class)
        .retryWhen(
            Retry.backoff(3, Duration.ofSeconds(60))
                .filter(throwable -> throwable instanceof TooManyRequests)
        );
  }

  public void saveDisclosure(int page) {
    log.info("save disclosure page : {}", page);

    if (page >= 10) return;

    Mono<DisclosureResponse> disclosureMono = getDisclosureResponseFromXangle(page);

    disclosureMono.publishOn(Schedulers.boundedElastic()).map(
        disclosureResponse -> {
          List<Disclosure> disclosures =
              getDisclosureListWithoutOverlap(
                  DisclosureMapper.INSTANCE.toEntity(disclosureResponse.getData().getDisclosures())
              );

          disclosureDomainService.saveAll(disclosures).subscribe();

          if (disclosures.size() >= 50) {
            saveDisclosure(page + 1);
          }

          return disclosureResponse;
        }
    ).subscribe();

  }

  public List<Disclosure> getDisclosureListWithoutOverlap(List<Disclosure> disclosureList) {
    Disclosure disclosure = disclosureDomainService.findFirstByOrderByPublishTimestampDesc().block();

    OptionalInt optionalInt = IntStream.range(0, disclosureList.size())
        .filter(index -> {
          if (disclosure == null) {
            return false;
          }
          return disclosureList.get(index).getId().equals(disclosure.getId());
        }).findFirst();

    if (optionalInt.isPresent()) {
      return disclosureList.stream().limit(optionalInt.getAsInt()).collect(Collectors.toList());
    } else {
      return disclosureList;
    }

  }




}
