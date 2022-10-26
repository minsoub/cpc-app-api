package com.bithumbsystems.cpc.api.v1.xangle.service;

import com.bithumbsystems.cpc.api.v1.xangle.response.DisclosureResponse;
import com.bithumbsystems.persistence.mongodb.disclosure.model.entity.Disclosure;
import com.bithumbsystems.persistence.mongodb.disclosure.service.DisclosureDomainService;
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
public class DisclosureService {

  private final DisclosureDomainService disclosureDomainService;

  public Flux<Disclosure> saveAllDisclosure(List<Disclosure> disclosureList) {
    return disclosureDomainService.saveAll(disclosureList);
  }

  public Mono<Disclosure> findFirstByOrderByPublishTimestampDesc() {
    return disclosureDomainService.findFirstByOrderByPublishTimestampDesc();
  }

  public Mono<DisclosureResponse> getDisclosureResponseFromXangle(int page) {
    return WebClient.builder()
        .baseUrl("https://pro-api.xangle.io")
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .build()
        .get()
        .uri(uriBuilder ->
            uriBuilder.path("/v1/disclosure/list/all")
                .queryParam("lang", "ko")
                .queryParam("exchange", "bithumb")
                .queryParam("page", page)
                .build()
        )
        .header("X-XANGLE_API_KEY", "9b9c1a6b-43d7-576d-d94cfd6a7b2f")
        .retrieve()
        .bodyToMono(DisclosureResponse.class)
        .retryWhen(
            Retry.backoff(3, Duration.ofSeconds(60))
                .filter(throwable -> throwable instanceof TooManyRequests)
        );
  }

}
