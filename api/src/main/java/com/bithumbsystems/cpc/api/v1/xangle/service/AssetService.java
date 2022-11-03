package com.bithumbsystems.cpc.api.v1.xangle.service;

import static com.bithumbsystems.cpc.api.core.config.constant.GlobalConstant.EXCHANGE_NAME;

import com.bithumbsystems.cpc.api.core.config.property.XangleProperties;
import com.bithumbsystems.cpc.api.core.util.WebClientUtil;
import com.bithumbsystems.cpc.api.v1.xangle.mapper.AssetMapper;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetProfileResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetResponse;
import com.bithumbsystems.persistence.mongodb.asset.model.entity.Asset;
import com.bithumbsystems.persistence.mongodb.asset.service.AssetDomainService;
import java.util.List;
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

//  public void insertProjectName() {
//    findAllByProjectNameIsNull().publishOn(Schedulers.boundedElastic()).map(
//        it -> {
//            getAssetProfileResponseFromXangle(it.getAssetId()).publishOn(Schedulers.boundedElastic()).map(
//                assetProfileResponse -> {
//                  Asset asset = AssetMapper.INSTANCE.profileResponseToEntity(assetProfileResponse);
//
//                  asset = Asset.builder()
//                      .assetId(it.getAssetId())
//                      .symbol(it.getSymbol())
//                      .projectName(asset.getProjectName())
//                      .assetName(asset.getAssetName())
//                      .name(it.getName())
//                      .build();
//
//                  return assetDomainService.save(asset).subscribe();
//                }
//            ).subscribe();
//          return it;
//        }
//    ).subscribe();
//  }

  public Mono<Asset> insertProjectNameBySymbol(Asset asset) {

    return assetDomainService.findById(asset.getSymbol()).flatMap(
        it -> {
          return getAssetProfileResponseFromXangle(asset.getAssetId()).flatMap(
              assetProfileResponse -> {
                Asset assetResponse = AssetMapper.INSTANCE.profileResponseToEntity(assetProfileResponse);

                assetResponse = Asset.builder()
                    .assetId(it.getAssetId())
                    .symbol(it.getSymbol())
                    .projectName(assetResponse.getProjectName())
                    .assetName(assetResponse.getAssetName())
                    .name(it.getName())
                    .build();

                return assetDomainService.save(assetResponse);
              }
          );
        }
    );

//    return assetDomainService.findById(asset.getSymbol()).publishOn(Schedulers.boundedElastic()).map(
//        it -> {
//          getAssetProfileResponseFromXangle(it.getAssetId()).publishOn(Schedulers.boundedElastic()).map(
//              assetProfileResponse -> {
//                Asset assetResponse = AssetMapper.INSTANCE.profileResponseToEntity(assetProfileResponse);
//
//                assetResponse = Asset.builder()
//                    .assetId(it.getAssetId())
//                    .symbol(it.getSymbol())
//                    .projectName(assetResponse.getProjectName())
//                    .assetName(assetResponse.getAssetName())
//                    .name(it.getName())
//                    .build();
//
//                return assetDomainService.save(assetResponse).subscribe();
//              }
//          ).subscribe();
//          return it;
//        }
//    ).map();


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

  public Mono<Asset> checkAsset(String projectSymbol) {
    return assetDomainService.findById(projectSymbol).switchIfEmpty(
        Mono.defer(() -> {
          log.info("check asset start symbol : {}", projectSymbol);
          saveAsset(0);
          return assetDomainService.findById(projectSymbol);
        })
    );
  }

}
