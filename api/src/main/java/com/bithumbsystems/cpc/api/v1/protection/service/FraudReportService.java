package com.bithumbsystems.cpc.api.v1.protection.service;

import com.bithumbsystems.cpc.api.core.config.property.AwsProperties;
import com.bithumbsystems.cpc.api.core.model.enums.ErrorCode;
import com.bithumbsystems.cpc.api.v1.protection.exception.FraudReportException;
import com.bithumbsystems.cpc.api.v1.protection.mapper.FraudReportMapper;
import com.bithumbsystems.cpc.api.v1.protection.model.enums.Status;
import com.bithumbsystems.cpc.api.v1.protection.model.request.FraudReportRequest;
import com.bithumbsystems.persistence.mongodb.common.model.entity.File;
import com.bithumbsystems.persistence.mongodb.common.service.FileDomainService;
import com.bithumbsystems.persistence.mongodb.protection.model.entity.FraudReport;
import com.bithumbsystems.persistence.mongodb.protection.service.FraudReportDomainService;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
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
   * 사기 신고 등록(파일 업로드 후 사기 신고 정보 저장)
   * @param filePart 업로드 파일
   * @param fraudReportRequest 사기 신고
   * @return
   */
  @Transactional
  public Mono<FraudReport> saveAll(FilePart filePart, FraudReportRequest fraudReportRequest) {
    String fileKey = UUID.randomUUID().toString();
    AtomicReference<String> fileName = new AtomicReference<>();
    AtomicReference<Long> fileSize = new AtomicReference<>();

    fileName.set(filePart.filename());
    log.debug("Here is ....");

    return DataBufferUtils.join(filePart.content())
        .flatMap(dataBuffer -> {
          log.debug("dataBuffer join...");
          ByteBuffer buf = dataBuffer.asByteBuffer();
          log.debug("byte size ===> " + buf.array().length);

          fileSize.set((long) buf.array().length);

          return uploadFile(fileKey, fileName.toString(), fileSize.get(), awsProperties.getBucket(), buf)
              .flatMap(res -> {
                File info = File.builder()
                    .fileKey(fileKey)
                    .fileName(fileName.toString())
                    .delYn(false)
                    .build();
                return fileDomainService.save(info);
              });
        }).publishOn(Schedulers.boundedElastic()).flatMap(file -> {
          fraudReportRequest.setAttachFileId(file.getFileKey());
          return saveFraudReport(fraudReportRequest);
        });
  }

  /**
   * 사기 신고 등록
   * @param fraudReportRequest 사기 신고
   * @return
   */
  public Mono<FraudReport> saveFraudReport(FraudReportRequest fraudReportRequest) {
    FraudReport fraudReport = FraudReportMapper.INSTANCE.toEntity(fraudReportRequest);
    fraudReport.setStatus(fraudReportRequest.getAnswerToContacts()? Status.REQUEST.getCode() : Status.REGISTER.getCode()); // 연락처로 답변받기 체크 시 '답변요청' 아니면 '접수' 상태
    return fraudReportDomainService.createFraudReport(fraudReport);
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
