package com.bithumbsystems.cpc.api.v1.education.controller;


import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.bithumbsystems.cpc.api.core.exception.InvalidParameterException;
import com.bithumbsystems.cpc.api.core.model.enums.ErrorCode;
import com.bithumbsystems.cpc.api.core.model.response.SingleResponse;
import com.bithumbsystems.cpc.api.core.util.AES256Util;
import com.bithumbsystems.cpc.api.core.util.ValidationUtils;
import com.bithumbsystems.cpc.api.v1.education.model.request.CreateEductionRequest;
import com.bithumbsystems.cpc.api.v1.education.service.EducationService;
import com.bithumbsystems.persistence.mongodb.education.model.entity.Education;
import io.swagger.v3.oas.annotations.Operation;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/education")
public class EducationController {

  private final EducationService educationService;


  @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "교육 신청", description = "찾아가는 교육 > 교육 신청", tags = "찾아가는 교육 > 교육 신청")
  public Mono<Education> createEducation(CreateEductionRequest request) {

    if (!request.getIsUseAgreement()) {  // !request.getIsConsignmentAgreement() || !request.getIsUseAgreement()) {
        // 에러 출력.
        throw new InvalidParameterException(ErrorCode.INVALID_ITEM_FORMAT);
    }

    return educationService.createEducation(request);
  }
}
