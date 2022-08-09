package com.bithumbsystems.cpc.api.v1.protection.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.bithumbsystems.cpc.api.core.config.property.AwsProperties;
import com.bithumbsystems.cpc.api.core.model.response.SingleResponse;
import com.bithumbsystems.cpc.api.core.model.validation.ValidationSequence;
import com.bithumbsystems.cpc.api.core.util.AES256Util;
import com.bithumbsystems.cpc.api.core.util.ValidationUtils;
import com.bithumbsystems.cpc.api.v1.protection.model.request.FraudReportRequest;
import com.bithumbsystems.cpc.api.v1.protection.service.FraudReportService;
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
@RequestMapping("/fraud-report")
@RequiredArgsConstructor
public class FraudReportController {
  private final AwsProperties awsProperties;
  private final FraudReportService fraudReportService;

  /**
   * 사기 신고 등록
   * @param fraudReportRequest 사기 신고 정보
   * @param filePart 첨부 파일
   * @return
   */
  @PostMapping(consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
  @Operation(summary = "사기 신고 등록", description = "투자자 보호 > 사기 신고: 사기 신고 등록", tags = "투자자 보호 > 사기 신고")
  public ResponseEntity<Mono<?>> createFraudReport(@Validated(ValidationSequence.class) @RequestPart(value = "fraudReportRequest") FraudReportRequest fraudReportRequest,
      @RequestPart(value = "file", required = false) FilePart filePart) {

    return ResponseEntity.ok().body(Mono.just(fraudReportRequest)
            .flatMap(request -> {
              // 1. 개인정보 복호화
              request.setEmail(AES256Util.decryptAES(awsProperties.getCpcCryptoKey(), request.getEmail()));
              log.debug("email : {}", request.getEmail());
              // 2. 유효성 검증
              ValidationUtils.assertEmailFormat(request.getEmail());

              if (filePart != null) {
                String[] ALLOW_FILE_EXT = {"PNG", "GIF", "JPG", "JPEG", "PPTX", "XLSX", "DOC", "DOCS"};
                ValidationUtils.assertAllowFileExt(filePart.filename(), ALLOW_FILE_EXT);
              }

              return fraudReportService.createFraudReport(filePart, request);
            })
            .then(Mono.just(new SingleResponse())));
  }
}
