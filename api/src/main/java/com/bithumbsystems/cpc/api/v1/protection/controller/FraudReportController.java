package com.bithumbsystems.cpc.api.v1.protection.controller;

import com.bithumbsystems.cpc.api.v1.protection.model.request.FraudReportRequest;
import com.bithumbsystems.cpc.api.v1.protection.service.FraudReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/fraud-report")
@RequiredArgsConstructor
@Tag(name = "Fraud Report APIs", description = "사기 신고 API")
public class FraudReportController {

  private final FraudReportService fraudReportService;

  /**
   * 사기 신고 등록
   * @param fraudReportRequest 사기 신고
   * @return
   */
  @PostMapping("/")
  public ResponseEntity<Mono<?>> createBoard(@RequestBody FraudReportRequest fraudReportRequest) {
    return ResponseEntity.ok().body(fraudReportService.createFraudReport(fraudReportRequest));
  }
}
