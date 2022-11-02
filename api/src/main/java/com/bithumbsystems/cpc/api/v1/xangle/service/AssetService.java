package com.bithumbsystems.cpc.api.v1.xangle.service;

import static com.bithumbsystems.cpc.api.core.config.constant.GlobalConstant.EXCHANGE_NAME;

import com.bithumbsystems.cpc.api.core.config.property.XangleProperties;
import com.bithumbsystems.cpc.api.core.util.WebClientUtil;
import com.bithumbsystems.cpc.api.v1.xangle.mapper.AssetMapper;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetProfileResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetResponse;
import com.bithumbsystems.persistence.mongodb.asset.model.entity.Asset;
import com.bithumbsystems.persistence.mongodb.asset.service.AssetDomainService;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
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
public class AssetService {

  private final AssetDomainService assetDomainService;

  private final XangleProperties xangleProperties;

  private final WebClientUtil webClientUtil;


  public Flux<Asset> saveAll(List<Asset> assetList) {
    return assetDomainService.saveAll(assetList);
  }

  public Flux<Asset> findAllByProjectNameIsNull() {
    return assetDomainService.findAllByProjectNameIsNull();
  }

  public void saveAsset(int page) {
    Mono<AssetResponse> assetMono = getAssetResponseFromXangle(page);

    assetMono.map(AssetMapper.INSTANCE::toEntity)
        .doOnNext(result -> {
          int pageNo = page;
          if (result.size() >= 50) {
            saveAsset(++pageNo);
          }
        })
        .publishOn(Schedulers.boundedElastic())
        .map(assets -> saveAll(assets).subscribe())
        .subscribe();

  }

  public Mono<AssetResponse> getAssetResponseFromXangle(Integer page) {
    return webClientUtil.requestGet(xangleProperties.getHost())
        .get()
        .uri(uriBuilder ->
            uriBuilder.path(xangleProperties.getAssetListPath())
                .queryParam("exchange_name", EXCHANGE_NAME)
                .queryParam("page", page)
                .build()
        )
        .header("X-XANGLE_API_KEY", xangleProperties.getXangleApiKey())
        .retrieve()
        .bodyToMono(AssetResponse.class)
        .retryWhen(
            Retry.backoff(3, Duration.ofSeconds(60))
                .filter(throwable -> throwable instanceof TooManyRequests)
        );
  }

  public void insertProjectName() {

    findAllByProjectNameIsNull().publishOn(Schedulers.boundedElastic()).map(
        it -> {
            getAssetProfileResponseFromXangle(it.getAssetId()).publishOn(Schedulers.boundedElastic()).map(
                assetProfileResponse -> {
                  log.info("asset profile : {}", assetProfileResponse);
                  Asset asset = AssetMapper.INSTANCE.profileResponseToEntity(assetProfileResponse);
                  log.info("asset mapper list : {}", asset);

                  asset = Asset.builder()
                      .assetId(it.getAssetId())
                      .symbol(it.getSymbol())
                      .projectName(asset.getProjectName())
                      .assetName(asset.getAssetName())
                      .name(it.getName())
                      .build();

                  log.info("save asset : {}", asset);

                  return assetDomainService.save(asset).subscribe();
                }
            ).subscribe();
          return it;
        }
    ).subscribe();

  }

  public Mono<AssetProfileResponse> getAssetProfileResponseFromXangle(String assetId) {
    return webClientUtil.requestGet(xangleProperties.getHost())
        .get()
        .uri(uriBuilder ->
            uriBuilder.path(xangleProperties.getAssetProfilePath())
                .queryParam("lang", "ko")
                .queryParam("asset_ids", assetId)
                .build()
        )
        .header("X-XANGLE_API_KEY", xangleProperties.getXangleApiKey())
        .retrieve()
        .bodyToMono(AssetProfileResponse.class)
        .retryWhen(
            Retry.backoff(3, Duration.ofSeconds(60))
                .filter(throwable -> throwable instanceof TooManyRequests)
        );
  }
}
