package com.bithumbsystems.cpc.api.v1.education.service;

import com.bithumbsystems.cpc.api.core.config.property.AwsProperties;
import com.bithumbsystems.cpc.api.core.exception.InvalidParameterException;
import com.bithumbsystems.cpc.api.core.model.enums.ErrorCode;
import com.bithumbsystems.cpc.api.core.model.response.SingleResponse;
import com.bithumbsystems.cpc.api.core.service.RsaCipherService;
import com.bithumbsystems.cpc.api.core.util.AES256Util;
import com.bithumbsystems.cpc.api.core.util.DateUtils;
import com.bithumbsystems.cpc.api.core.util.ValidationUtils;
import com.bithumbsystems.cpc.api.v1.education.model.request.CreateEductionRequest;
import com.bithumbsystems.persistence.mongodb.education.model.entity.Education;
import com.bithumbsystems.persistence.mongodb.education.service.EducationDomainService;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationService {
  private final AwsProperties awsProperties;

  private final EducationDomainService educationDomainService;

  private final RsaCipherService rsaCipherService;

  public Mono<Education> createEducation(CreateEductionRequest request) throws InvalidParameterException {

    return rsaCipherService.getRsaPrivateKey().flatMap(
        privateKey -> {
          request.setName(rsaCipherService.decryptRSA(request.getName(), privateKey));
          request.setEmail(rsaCipherService.decryptRSA(request.getEmail(), privateKey));
          request.setCellPhone(rsaCipherService.decryptRSA(request.getCellPhone(), privateKey));

          log.info("교육 신청");
          log.info(request.getName());
          log.info(request.getEmail());
          log.info(request.getCellPhone());
          log.info(request.getDesireDate().toString());

          try {
            validCreateEducationRequest(request);
          } catch (InvalidParameterException e) {
            log.info("controller error");
          }

          Education education = makeEducation(request);

          return educationDomainService.save(education);
        }
    );

  }

  private void validCreateEducationRequest(CreateEductionRequest request) throws InvalidParameterException {
    ValidationUtils.assertNameFormat(request.getName());
    ValidationUtils.assertEmailFormat(request.getEmail());
    ValidationUtils.assertCellPhoneFormat(request.getCellPhone());
  }

  private Education makeEducation(CreateEductionRequest request) {
    return Education.builder()
        .id(UUID.randomUUID().toString())
        .name(AES256Util.encryptAES(awsProperties.getKmsKey(), request.getName(), awsProperties.getSaltKey(), awsProperties.getIvKey()))
        .email(AES256Util.encryptAES(awsProperties.getKmsKey(), request.getEmail(), awsProperties.getSaltKey(), awsProperties.getIvKey()))
        .cellPhone(AES256Util.encryptAES(awsProperties.getKmsKey(), request.getCellPhone(), awsProperties.getSaltKey(), awsProperties.getIvKey()))
        .content(request.getContent())
        .desireDate(DateUtils.toLocalDateTime(request.getDesireDate()))
        //.isConsignmentAgreement(request.getIsConsignmentAgreement())
        .isUseAgreement(request.getIsUseAgreement())
        .isAnswerComplete(false)
        .createDate(LocalDateTime.now())
        .build();
  }


}
