package com.bithumbsystems.cpc.api.v1.protection.service;

import com.bithumbsystems.cpc.api.v1.protection.mapper.FraudReportMapper;
import com.bithumbsystems.cpc.api.v1.protection.model.enums.Status;
import com.bithumbsystems.cpc.api.v1.protection.model.request.FraudReportRequest;
import com.bithumbsystems.persistence.mongodb.protection.model.entity.FraudReport;
import com.bithumbsystems.persistence.mongodb.protection.service.FraudReportDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FraudReportService {

  private final FraudReportDomainService fraudReportDomainService;

  /**
   * 사기 신고 등록
   * @param fraudReportRequest 사기 신고
   * @return
   */
  public Mono<Void> createFraudReport(FraudReportRequest fraudReportRequest) {
    FraudReport fraudReport = FraudReportMapper.INSTANCE.toEntity(fraudReportRequest);
    fraudReport.setStatus(Status.REGISTER.getCode()); // '접수' 상태
    return fraudReportDomainService.createFraudReport(fraudReport);
  }
}
