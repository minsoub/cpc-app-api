package com.bithumbsystems.persistence.mongodb.disclosure.service;

import com.bithumbsystems.persistence.mongodb.disclosure.model.entity.Disclosure;
import com.bithumbsystems.persistence.mongodb.disclosure.repository.DisclosureRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisclosureDomainService {

  private final DisclosureRepository disclosureRepository;

  public Flux<Disclosure> saveAll(List<Disclosure> disclosureList) {
    return disclosureRepository.saveAll(disclosureList);
  }

  public Mono<Disclosure> findFirstByOrderByPublishTimestampDesc() {
    return disclosureRepository.findFirstByOrderByPublishTimestampDesc();
  }

}
