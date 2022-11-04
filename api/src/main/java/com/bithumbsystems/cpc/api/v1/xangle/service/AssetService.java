package com.bithumbsystems.cpc.api.v1.xangle.service;

import static com.bithumbsystems.cpc.api.core.config.constant.GlobalConstant.EXCHANGE_NAME;

import com.bithumbsystems.cpc.api.core.config.property.XangleProperties;
import com.bithumbsystems.cpc.api.core.util.WebClientUtil;
import com.bithumbsystems.cpc.api.v1.xangle.mapper.AssetMapper;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetProfileResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetProfileResponse.Data;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetResponse;
import com.bithumbsystems.persistence.mongodb.asset.model.entity.Asset;
import com.bithumbsystems.persistence.mongodb.asset.service.AssetDomainService;
import com.bithumbsystems.persistence.mongodb.disclosure.model.entity.Disclosure;
import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetService {

  private final AssetDomainService assetDomainService;

  private final XangleProperties xangleProperties;

  private final WebClientUtil webClientUtil;

  public Mono<Asset> findById(String symbol) {
    return assetDomainService.findById(symbol);
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
        .map(assets -> {
          for (Asset asset : assets) {
            assetDomainService.findById(asset.getSymbol()).switchIfEmpty(
                Mono.defer(() -> {
                  return assetDomainService.save(asset);
                })
            ).subscribe();
          }
          return assets;
        })
        .subscribe();

    getProjectName(assetMono);
  }

  public void getProjectName(Mono<AssetResponse> assetMono) {
    log.info("assetMono value : {}", assetMono);

    assetMono.map(it -> {
      List<Asset> assetList = AssetMapper.INSTANCE.toEntity(it);
      String  assetIdList = assetList.stream().map(Asset::getAssetId).collect(Collectors.joining(","));
      var profileResponseMono = getAssetProfileResponseFromXangle(assetIdList);

      log.info("profile Response Mono: {}", profileResponseMono);

      profileResponseMono.map(
          data -> {
            log.info("data value : {}", data);
            for (Data responseProfile : data.getData()) {
              assetDomainService.findAssetByAssetId(responseProfile.getAssetId()).map(
                  asset -> {
                    asset.setProjectName(responseProfile.getAssetProfile().getProjectName());
                    asset.setAssetName(responseProfile.getAssetProfile().getAssetName());
                    return assetDomainService.save(asset).subscribe();
                  }
              ).subscribe();
            }
            return data;
          }
      ).subscribe();
      return it;
    }).subscribe();
  }

  public Mono<AssetResponse> getAssetResponseFromXangle(Integer page) {
    log.info("get Asset list xangle api call");
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
        .bodyToMono(AssetResponse.class);
  }

  public Mono<AssetProfileResponse> getAssetProfileResponseFromXangle(String assetId) {
    log.info("get Asset profile xangle api call");
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
        .onErrorMap(
            e -> {
              log.error(e.toString());
              return e;
            }
        );
  }

}
