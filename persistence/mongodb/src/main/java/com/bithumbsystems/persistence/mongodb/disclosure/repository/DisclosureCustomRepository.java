package com.bithumbsystems.persistence.mongodb.disclosure.repository;

import com.bithumbsystems.persistence.mongodb.disclosure.model.entity.Disclosure;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DisclosureCustomRepository {
  Flux<Disclosure> findByOrderByPublishTimestampDesc(String searchCategory, String search, Pageable pageable);

  Mono<Long> countBySearchText(String searchCategory, String search, Pageable pageable);
}
