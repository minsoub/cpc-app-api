package com.bithumbsystems.cpc.api.v1.care.controller;

import com.bithumbsystems.cpc.api.v1.care.model.request.LegalCounselingRequest;
import com.bithumbsystems.cpc.api.v1.care.service.LegalCounselingService;
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
@RequestMapping("/legal-counseling")
@RequiredArgsConstructor
@Tag(name = "Legal Counseling APIs", description = "법률 상담 API")
public class LegalCounselingController {

  private final LegalCounselingService legalCounselingService;

  /**
   * 법률 상담 등록
   * @param legalCounselingRequest 법률 상담
   * @return
   */
  @PostMapping("/")
  public ResponseEntity<Mono<?>> createBoard(@RequestBody LegalCounselingRequest legalCounselingRequest) {
    return ResponseEntity.ok().body(legalCounselingService.createLegalCounseling(legalCounselingRequest));
  }
}
