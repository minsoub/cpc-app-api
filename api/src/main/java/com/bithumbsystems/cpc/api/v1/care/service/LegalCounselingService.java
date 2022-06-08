package com.bithumbsystems.cpc.api.v1.care.service;

import com.bithumbsystems.cpc.api.core.config.property.AwsProperties;
import com.bithumbsystems.cpc.api.v1.care.mapper.LegalCounselingMapper;
import com.bithumbsystems.cpc.api.v1.care.model.enums.Status;
import com.bithumbsystems.cpc.api.v1.care.model.request.LegalCounselingRequest;
import com.bithumbsystems.persistence.mongodb.care.entity.LegalCounseling;
import com.bithumbsystems.persistence.mongodb.care.service.LegalCounselingDomainService;
import com.bithumbsystems.persistence.mongodb.common.model.entity.File;
import com.bithumbsystems.persistence.mongodb.common.service.FileDomainService;
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
import org.springframework.http.codec.multipart.Part;
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

  @Transactional
  public Mono<Void> saveAll(Mono<FilePart> filePart, LegalCounselingRequest legalCounselingRequest) {
    String fileKey = UUID.randomUUID().toString();
    AtomicReference<String> fileName = new AtomicReference<>();
    AtomicReference<Long> fileSize = new AtomicReference<>();

    var fileJob = filePart.doOnNext(part -> {
      log.debug("file name => " + part.filename());
      fileName.set(part.filename());
    })
    .map(Part::content)
    .log("DataBuffer")
    .flatMap(data -> {
      log.debug("Here is ....");
      return DataBufferUtils.join(data)
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
                })
                .log();
          });
    });

    legalCounselingRequest.setAttachFileId(fileKey);
    var saveJob = saveLegalCounseling(legalCounselingRequest);

    return Mono.when(fileJob, saveJob);
  }

  /**
   * 법률 상담 등록
   * @param legalCounselingRequest 법률 상담
   * @return
   */
  public Mono<LegalCounseling> saveLegalCounseling(LegalCounselingRequest legalCounselingRequest) {
    LegalCounseling legalCounseling = LegalCounselingMapper.INSTANCE.toEntity(legalCounselingRequest);
    legalCounseling.setStatus(legalCounseling.getAnswerToContacts()? Status.REQUEST.getCode() : Status.REGISTER.getCode()); // 연락처로 답변받기 체크 시 '답변요청' 아니면 '접수' 상태
    log.debug("service createLegalCounseling legalCounseling => {}", legalCounseling);
    return legalCounselingDomainService.createLegalCounseling(legalCounseling);
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
    metadata.put("filename", fileName);
    metadata.put("filesize", String.valueOf(fileSize));

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
          log.debug("putObject => {}", res);
          return res;
        })
    );
  }
}
