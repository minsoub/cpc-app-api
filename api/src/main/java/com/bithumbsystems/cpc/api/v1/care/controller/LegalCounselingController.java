package com.bithumbsystems.cpc.api.v1.care.controller;

import static com.bithumbsystems.cpc.api.core.util.AES256Util.CLIENT_AES_KEY_CPC;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.bithumbsystems.cpc.api.core.model.response.SingleResponse;
import com.bithumbsystems.cpc.api.core.model.validation.ValidationSequence;
import com.bithumbsystems.cpc.api.core.util.AES256Util;
import com.bithumbsystems.cpc.api.core.util.ValidationUtils;
import com.bithumbsystems.cpc.api.v1.care.model.request.LegalCounselingRequest;
import com.bithumbsystems.cpc.api.v1.care.service.LegalCounselingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/legal-counseling")
@RequiredArgsConstructor
public class LegalCounselingController {
  private final LegalCounselingService legalCounselingService;

  /**
   * 법률 상담 등록
   * @param legalCounselingRequest 법률 상담 신청 정보
   * @param filePart 첨부 파일
   * @return
   */
  @PostMapping(consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
  @Operation(summary = "법률 상담 신청", description = "케어 프로그램 > 법률상담: 법률 상담 신청", tags = "케어 프로그램 > 법률상담")
  public ResponseEntity<Mono<?>> applyLegalCounseling(@Validated(ValidationSequence.class) @RequestPart(value = "legalCounselingRequest") LegalCounselingRequest legalCounselingRequest,
      @RequestPart(value = "file", required = false) FilePart filePart) {

    return ResponseEntity.ok().body(Mono.just(legalCounselingRequest)
        .flatMap(request -> {
          // 1. 개인정보 복호화
          request.setName(AES256Util.decryptAES(CLIENT_AES_KEY_CPC, request.getName()));
          request.setEmail(AES256Util.decryptAES(CLIENT_AES_KEY_CPC, request.getEmail()));
          request.setCellPhone(AES256Util.decryptAES(CLIENT_AES_KEY_CPC, request.getCellPhone()));

          // 2. 유효성 검증
          ValidationUtils.assertNameFormat(request.getName());
          ValidationUtils.assertEmailFormat(request.getEmail());
          ValidationUtils.assertCellPhoneFormat(request.getCellPhone());

          return legalCounselingService.applyLegalCounseling(filePart, request);
        })
        .then(Mono.just(new SingleResponse()))
        );
  }
}
