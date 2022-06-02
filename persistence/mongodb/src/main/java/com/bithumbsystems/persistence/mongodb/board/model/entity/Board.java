package com.bithumbsystems.persistence.mongodb.board.model.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "boards")
public class Board {

  @Transient
  public static final String SEQUENCE_NAME = "boards_sequence";

  /**
   * 게시물 ID
   */
  @Id
  private Long id;

  /**
   * 게시판 ID - boardMaster의 id
   */
  @Indexed
  private String boardMasterId;

  /**
   * 게시물 제목
   */
  private String title;

  /**
   * 게시물 내용
   */
  private String contents;

  /**
   * 답글 여부
   */
  private Boolean isReply = false;

  /**
   * 부모 게시물 ID
   */
  private String parentId;

  /**
   * 사용 여부
   */
  private Boolean isUse = true;

  /**
   * 조회 수
   */
  private Integer readCount = 0;

  /**
   * 공지 설정
   */
  private Boolean isSetNotice = false;

  /**
   * 비밀글 여부
   */
  private Boolean isSecret = false;

  /**
   * 비밀번호
   */
  private String password;

  /**
   * 첨부파일 ID
   */
  private String attachFileId;

  /**
   * 썸네일
   */
  private Thumbnail thumbnail;

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

  @Data
  public class Thumbnail {
    private String desktop;
    private String mobile;
  }
}
