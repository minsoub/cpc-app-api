package com.bithumbsystems.cpc.api.v1.care.service;

import com.bithumbsystems.cpc.api.v1.care.mapper.LegalCounselingMapper;
import com.bithumbsystems.cpc.api.v1.care.model.enums.Status;
import com.bithumbsystems.cpc.api.v1.care.model.request.LegalCounselingRequest;
import com.bithumbsystems.persistence.mongodb.care.entity.LegalCounseling;
import com.bithumbsystems.persistence.mongodb.care.service.LegalCounselingDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class LegalCounselingService {

  private final LegalCounselingDomainService legalCounselingDomainService;

  /**
   * 법률 상담 등록
   * @param legalCounselingRequest 법률 상담
   * @return
   */
  public Mono<Void> createLegalCounseling(LegalCounselingRequest legalCounselingRequest) {
    LegalCounseling legalCounseling = LegalCounselingMapper.INSTANCE.toEntity(legalCounselingRequest);
    legalCounseling.setStatus(legalCounseling.getAnswerToContacts()? Status.REQUEST.getCode() : Status.REGISTER.getCode()); // 연락처로 답변받기 체크 시 '답변요청' 아니면 '접수' 상태
    return legalCounselingDomainService.createLegalCounseling(legalCounseling);
  }
}
