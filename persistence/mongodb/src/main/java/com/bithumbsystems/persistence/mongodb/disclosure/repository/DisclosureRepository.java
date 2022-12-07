package com.bithumbsystems.persistence.mongodb.disclosure.repository;

import com.bithumbsystems.persistence.mongodb.disclosure.model.entity.Disclosure;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DisclosureRepository extends ReactiveMongoRepository<Disclosure, String> {
  Mono<Disclosure> findFirstByOrderByPublishTimestampDesc();

}
