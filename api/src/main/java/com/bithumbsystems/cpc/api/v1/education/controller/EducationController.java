package com.bithumbsystems.cpc.api.v1.education.controller;


import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.bithumbsystems.cpc.api.core.model.response.SingleResponse;
import com.bithumbsystems.cpc.api.v1.education.model.request.CreateEductionRequest;
import com.bithumbsystems.cpc.api.v1.education.service.EducationService;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
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
@RequiredArgsConstructor
@RequestMapping("/education")
public class EducationController {

  private final EducationService educationService;


  @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "교육 신청", description = "찾아가는 교육 > 교육 신청", tags = "찾아가는 교육 > 교육 신청")
  public ResponseEntity<?> createEducation(CreateEductionRequest request) {
    educationService.createEducation(request).subscribe();

    return ResponseEntity.ok().body(Mono.just(new SingleResponse()));
  }




}
