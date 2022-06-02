package com.bithumbsystems.persistence.mongodb.protection.model.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "cpc_fraud_report")
public class FraudReport {

  @Transient
  public static final String SEQUENCE_NAME = "fraud_sequence";

  /**
   * ID
   */
  @Id
  private Long id;

  /**
   * 상태
   */
  private String status;

  /**
   * 제목
   */
  private String title;

  /**
   * 내용
   */
  private String contents;

  /**
   * 첨부파일 ID
   */
  private String attachFileId;

  /**
   * 생성날짜
   */
  @CreatedDate
  private LocalDateTime createDate;

  /**
   * 생성자 ID
   */
  @CreatedBy
  private String createAdminAccountId;

  /**
   * 수정날짜
   */
  @LastModifiedDate
  private LocalDateTime updateDate;

  /**
   * 수정자 ID
   */
  @LastModifiedBy
  private String updateAdminAccountId;
}
