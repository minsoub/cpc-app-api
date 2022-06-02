package com.bithumbsystems.persistence.mongodb.care.entity;

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
@Document(collection = "cpc_legal_counseling")
public class LegalCounseling {

  @Transient
  public static final String SEQUENCE_NAME = "counseling_sequence";

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
   * 이름
   */
  private String name;

  /**
   * 이메일
   */
  private String email;

  /**
   * 휴대폰번호
   */
  private String cellPhone;

  /**
   * 연락처로 답변하기
   */
  private Boolean answerToContacts;

  /**
   * 개인정보 수집 및 이용 동의
   */
  private Boolean termsPrivacy;

  /**
   * 답변
   */
  private String answer;

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
