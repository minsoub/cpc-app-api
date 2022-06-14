package com.bithumbsystems.persistence.mongodb.care.service;

import com.bithumbsystems.persistence.mongodb.care.model.entity.LegalCounseling;
import com.bithumbsystems.persistence.mongodb.care.repository.LegalCounselingRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class LegalCounselingDomainService {

  private final LegalCounselingRepository legalCounselingRepository;

  /**
   * 법률 상담 등록
   * @param legalCounseling 법률 상담
   * @return
   */
  public Mono<LegalCounseling> createLegalCounseling(LegalCounseling legalCounseling) {
    legalCounseling.setCreateDate(LocalDateTime.now());
    return legalCounselingRepository.insert(legalCounseling);
  }
}
