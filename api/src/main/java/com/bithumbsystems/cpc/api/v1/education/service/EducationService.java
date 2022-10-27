package com.bithumbsystems.cpc.api.v1.education.service;

import com.bithumbsystems.cpc.api.core.config.property.AwsProperties;
import com.bithumbsystems.cpc.api.core.exception.InvalidParameterException;
import com.bithumbsystems.cpc.api.core.model.enums.ErrorCode;
import com.bithumbsystems.cpc.api.core.util.AES256Util;
import com.bithumbsystems.cpc.api.core.util.DateUtils;
import com.bithumbsystems.cpc.api.core.util.ValidationUtils;
import com.bithumbsystems.cpc.api.v1.education.model.request.CreateEductionRequest;
import com.bithumbsystems.persistence.mongodb.education.model.entity.Education;
import com.bithumbsystems.persistence.mongodb.education.service.EducationDomainService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationService {
  private final AwsProperties awsProperties;

  private final EducationDomainService educationDomainService;

  public Mono<Education> createEducation(CreateEductionRequest request) {

    log.info("@@@@@@@@@@");
    log.info(awsProperties.getCpcCryptoKey());
    log.info(request.getName());
    log.info(request.getEmail());
    log.info(request.getCellPhone());

    request.setName(AES256Util.decryptAES(awsProperties.getCpcCryptoKey(), request.getName()));
    request.setEmail(AES256Util.decryptAES(awsProperties.getCpcCryptoKey(), request.getEmail()));
    request.setCellPhone(AES256Util.decryptAES(awsProperties.getCpcCryptoKey(), request.getCellPhone()));
    log.info(request.getName());
    log.info(request.getEmail());
    log.info(request.getCellPhone());
    log.info(request.getDesireDate().toString());

    validCreateEducationRequest(request);

    Education education = makeEducation(request);

    return educationDomainService.save(education);

  }

  private void validCreateEducationRequest(CreateEductionRequest request) {
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
        .build();
  }


}
