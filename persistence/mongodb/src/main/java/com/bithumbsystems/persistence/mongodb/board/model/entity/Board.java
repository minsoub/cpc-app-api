package com.bithumbsystems.persistence.mongodb.board.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
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
  private Boolean isReply;

  /**
   * 부모 게시물 ID
   */
  private String parentId;

  /**
   * 사용 여부
   */
  private Boolean isUse;

  /**
   * 조회 수
   */
  private Integer readCount;

  /**
   * 공지 설정
   */
  private Boolean isSetNotice;

  /**
   * 비밀글 여부
   */
  private Boolean isSecret;

  /**
   * 비밀번호
   */
  private String password;

  /**
   * 첨부파일 ID
   */
  private String attachFileId;

  /**
   * 태그
   */
  private List<String> tags;

  /**
   * 썸네일
   */
  private String thumbnail;

  private LocalDateTime createDate;
  private LocalDateTime updateDate;
}
