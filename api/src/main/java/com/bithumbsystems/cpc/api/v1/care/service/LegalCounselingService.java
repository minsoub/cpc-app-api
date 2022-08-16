package com.bithumbsystems.cpc.api.v1.care.service;

import com.bithumbsystems.cpc.api.core.config.property.AwsProperties;
import com.bithumbsystems.cpc.api.core.model.enums.ErrorCode;
import com.bithumbsystems.cpc.api.core.util.AES256Util;
import com.bithumbsystems.cpc.api.v1.care.exception.LegalCounselingException;
import com.bithumbsystems.cpc.api.v1.care.mapper.LegalCounselingMapper;
import com.bithumbsystems.cpc.api.v1.care.model.enums.Status;
import com.bithumbsystems.cpc.api.v1.care.model.request.LegalCounselingRequest;
import com.bithumbsystems.persistence.mongodb.care.model.entity.LegalCounseling;
import com.bithumbsystems.persistence.mongodb.care.service.LegalCounselingDomainService;
import com.bithumbsystems.persistence.mongodb.common.model.entity.File;
import com.bithumbsystems.persistence.mongodb.common.service.FileDomainService;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.validation.constraints.AssertTrue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class LegalCounselingService {

  private final AwsProperties awsProperties;

  private final LegalCounselingDomainService legalCounselingDomainService;
  private final S3AsyncClient s3AsyncClient;
  private final FileDomainService fileDomainService;

  /**
   * 법률 상담 신청(파일 업로드 후 법률 상담 정보 저장)
   * @param filePart 업로드 파일
   * @param legalCounselingRequest 법률 상담
   * @return
   */
  @Transactional
  @AssertTrue
  public Mono<Void> applyLegalCounseling(FilePart filePart, LegalCounselingRequest legalCounselingRequest) {
    LegalCounseling legalCounseling = LegalCounselingMapper.INSTANCE.toEntity(legalCounselingRequest);
    legalCounseling.setStatus(legalCounseling.getAnswerToContacts() ? Status.REQUEST.getCode() : Status.REGISTER.getCode()); // 연락처로 답변받기 체크 시 '답변요청' 아니면 '접수' 상태

    // DB 암호화
    legalCounseling.setName(AES256Util.encryptAES(awsProperties.getKmsKey(), legalCounselingRequest.getName(), awsProperties.getSaltKey(), awsProperties.getIvKey()));
    legalCounseling.setEmail(AES256Util.encryptAES(awsProperties.getKmsKey(), legalCounselingRequest.getEmail(), awsProperties.getSaltKey(), awsProperties.getIvKey()));
    legalCounseling.setCellPhone(AES256Util.encryptAES(awsProperties.getKmsKey(), legalCounselingRequest.getCellPhone(), awsProperties.getSaltKey(), awsProperties.getIvKey()));

    if (filePart == null) {
      return legalCounselingDomainService.createLegalCounseling(legalCounseling)
          .switchIfEmpty(Mono.error(new LegalCounselingException(ErrorCode.FAIL_CREATE_CONTENT)))
          .then();
    } else {
      String fileKey = UUID.randomUUID().toString();
      legalCounseling.setAttachFileId(fileKey);

      return legalCounselingDomainService.createLegalCounseling(legalCounseling)
          .zipWith(
              DataBufferUtils.join(filePart.content())
                  .flatMap(dataBuffer -> {
                    ByteBuffer buf = dataBuffer.asByteBuffer();
                    String fileName = filePart.filename();
                    Long fileSize = (long) buf.array().length;

                    return uploadFile(fileKey, fileName, fileSize, awsProperties.getBucket(), buf)
                        .flatMap(res -> {
                          File info = File.builder()
                              .fileKey(fileKey)
                              .fileName(Normalizer.normalize(fileName, Normalizer.Form.NFC))
                              .createDate(LocalDateTime.now())
                              .delYn(false)
                              .build();
                          return fileDomainService.save(info);
                        });
                  })
          )
          .switchIfEmpty(Mono.error(new LegalCounselingException(ErrorCode.FAIL_CREATE_CONTENT)))
          .then();
    }
  }

  /**
   * S3 File Upload
   *
   * @param fileKey
   * @param fileName
   * @param fileSize
   * @param bucketName
   * @param content
   * @return
   */
  private Mono<PutObjectResponse> uploadFile(String fileKey, String fileName, Long fileSize, String bucketName, ByteBuffer content) {
    log.debug("save => fileKey : " + fileKey);
    Map<String, String> metadata = new HashMap<String, String>();

    try {
      metadata.put("filename", URLEncoder.encode(fileName, "UTF-8"));
      metadata.put("filesize", String.valueOf(fileSize));
    } catch (UnsupportedEncodingException e) {
      return Mono.error(new LegalCounselingException(ErrorCode.FAIL_SAVE_FILE));
    }

    PutObjectRequest objectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .contentType((MediaType.APPLICATION_OCTET_STREAM).toString())
        .contentLength(fileSize)
        .metadata(metadata)
        .key(fileKey)
        .build();

    return Mono.fromFuture(
        s3AsyncClient.putObject(
            objectRequest, AsyncRequestBody.fromByteBuffer(content)
        ).whenComplete((resp, err) -> {
          try {
            if (resp != null) {
              log.info("upload success. Details {}", resp);
            } else {
              log.error("whenComplete error : {}", err);
              err.printStackTrace();
            }
          } finally {
            //s3AsyncClient.close();
          }
        }).thenApply(res -> {
//          log.debug("putObject => {}", res);
          return res;
        })
    );
  }
}
