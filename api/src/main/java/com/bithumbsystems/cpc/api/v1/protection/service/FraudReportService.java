package com.bithumbsystems.cpc.api.v1.protection.service;

import com.bithumbsystems.cpc.api.core.config.property.AwsProperties;
import com.bithumbsystems.cpc.api.core.model.enums.ErrorCode;
import com.bithumbsystems.cpc.api.core.util.AES256Util;
import com.bithumbsystems.cpc.api.v1.care.model.enums.Status;
import com.bithumbsystems.cpc.api.v1.protection.exception.FraudReportException;
import com.bithumbsystems.cpc.api.v1.protection.mapper.FraudReportMapper;
import com.bithumbsystems.cpc.api.v1.protection.model.request.FraudReportRequest;
import com.bithumbsystems.persistence.mongodb.common.model.entity.File;
import com.bithumbsystems.persistence.mongodb.common.service.FileDomainService;
import com.bithumbsystems.persistence.mongodb.protection.model.entity.FraudReport;
import com.bithumbsystems.persistence.mongodb.protection.service.FraudReportDomainService;
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
public class FraudReportService {

  private final AwsProperties awsProperties;

  private final FraudReportDomainService fraudReportDomainService;
  private final S3AsyncClient s3AsyncClient;
  private final FileDomainService fileDomainService;

  /**
   * ?????? ?????? ??????(?????? ????????? ??? ?????? ?????? ?????? ??????)
   * @param filePart ????????? ??????
   * @param fraudReportRequest ?????? ??????
   * @return
   */
  @Transactional
  @AssertTrue
  public Mono<Void> createFraudReport(FilePart filePart, FraudReportRequest fraudReportRequest) {
    FraudReport fraudReport = FraudReportMapper.INSTANCE.toEntity(fraudReportRequest);
    fraudReport.setStatus(fraudReport.getAnswerToContacts()? Status.REQUEST.getCode() : Status.REGISTER.getCode()); // ???????????? ???????????? ?????? ??? '????????????' ????????? '??????' ??????
    fraudReport.setEmail(AES256Util.encryptAES(awsProperties.getKmsKey(), fraudReportRequest.getEmail(), awsProperties.getSaltKey(), awsProperties.getIvKey())); // DB ?????????

    if (filePart == null) {
      return fraudReportDomainService.createFraudReport(fraudReport)
          .switchIfEmpty(Mono.error(new FraudReportException(ErrorCode.FAIL_CREATE_CONTENT)))
          .then();
    } else {
      String fileKey = UUID.randomUUID().toString();
      fraudReport.setAttachFileId(fileKey);

      return fraudReportDomainService.createFraudReport(fraudReport)
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
          .switchIfEmpty(Mono.error(new FraudReportException(ErrorCode.FAIL_CREATE_CONTENT)))
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
  public Mono<PutObjectResponse> uploadFile(String fileKey, String fileName, Long fileSize, String bucketName, ByteBuffer content) {
    // String fileName = part.filename();
    log.debug("save => fileKey : " + fileKey);
    Map<String, String> metadata = new HashMap<String, String>();
    try {
      metadata.put("filename", URLEncoder.encode(fileName, "UTF-8"));
      metadata.put("filesize", String.valueOf(fileSize));
    } catch (UnsupportedEncodingException e) {
      return Mono.error(new FraudReportException(ErrorCode.FAIL_SAVE_FILE));
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
          }finally {
            //s3AsyncClient.close();
          }
        }).thenApply(res -> {
          log.debug("putObject => {}", res);
          return res;
        })
    );
  }
}
