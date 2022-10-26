package com.bithumbsystems.cpc.api.v1.xangle.service;

import com.bithumbsystems.cpc.api.v1.xangle.response.AssetProfileResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetResponse;
import com.bithumbsystems.persistence.mongodb.asset.model.entity.Asset;
import com.bithumbsystems.persistence.mongodb.asset.service.AssetDomainService;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException.TooManyRequests;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
public class AssetService {

  private final AssetDomainService assetDomainService;

  public Flux<Asset> saveAll(List<Asset> assetList) {
    return assetDomainService.saveAll(assetList);
  }

  public Mono<AssetResponse> getAssetResponseFromXangle(Integer page) {
    return WebClient.builder()
        .baseUrl("https://pro-api.xangle.io")
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .build()
        .get()
        .uri(uriBuilder ->
            uriBuilder.path("/v1/asset/list")
                .queryParam("exchange_name", "bithumb")
                .queryParam("page", page)
                .build()
        )
        .header("X-XANGLE_API_KEY", "9b9c1a6b-43d7-576d-d94cfd6a7b2f")
        .retrieve()
        .bodyToMono(AssetResponse.class)
        .retryWhen(
            Retry.backoff(3, Duration.ofSeconds(60))
                .filter(throwable -> throwable instanceof TooManyRequests)
        );
  }

  public Mono<AssetProfileResponse> getAssetProfileResponseFromXangle(String assetId) {
    return WebClient.builder()
        .baseUrl("https://pro-api.xangle.io")
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .build()
        .get()
        .uri(uriBuilder ->
            uriBuilder.path("/v1/asset/list")
                .queryParam("lang", "ko")
                .queryParam("asset_id", assetId)
                .build()
        )
        .header("X-XANGLE_API_KEY", "9b9c1a6b-43d7-576d-d94cfd6a7b2f")
        .retrieve()
        .bodyToMono(AssetProfileResponse.class)
        .retryWhen(
            Retry.backoff(3, Duration.ofSeconds(60))
                .filter(throwable -> throwable instanceof TooManyRequests)
        );
  }
}
