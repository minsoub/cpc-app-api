package com.bithumbsystems.persistence.mongodb.board.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "board_master")
public class BoardMaster {

  /**
   * 게시판 ID
   */
  @Id
  private String id;

  /**
   * 사이트 ID
   */
  private String siteId;

  /**
   * 게시판명
   */
  private String name;

  /**
   * 사용여부
   */
  private Boolean isUse;

  /**
   * 게시판 유형
   */
  private String type;

  /**
   * 댓글 허용 여부
   */
  private Boolean isAllowComment;

  /**
   * 답글 허용 여부
   */
  private Boolean isAllowReply;

  /**
   * 파일 첨부 허용 여부
   */
  private Boolean isAllowAttachFile;

  /**
   * 카테고리 사용 여부
   */
  private Boolean isUseCategory;

  /**
   * 카테고리
   */
  private List<Category> categories;

  /**
   * 페이징 유형
   */
  private String paginationType;

  /**
   * 페이지당 표시 건수
   */
  private Integer countPerPage;

  /**
   * 태그 사용 여부
   */
  private Boolean isUseTag;

  /**
   * 태그
   */
  private List<String> tags;

  /**
   * SNS 공유하기
   */
  private Sns snsShare;

  /**
   * 권한
   */
  private Auth auth;

  /**
   * 카테고리
   */
  @Data
  public class Category {

    /**
     * 카테고리 ID
     */
    private String categoryId;

    /**
     * 카테고리명
     */
    private String categoryName;
  }

  /**
   * SNS
   */
  @Data
  public class Sns {

    /**
     * 카카오톡
     */
    private Boolean kakaotalk;

    /**
     * 페이스북
     */
    private Boolean facebook;

    /**
     * 트위터
     */
    private Boolean twitter;

    /**
     * URL
     */
    private Boolean url;
  }

  /**
   * 권한
   */
  @Data
  public class Auth {

    /**
     * 목록
     */
    private String list;

    /**
     * 읽기
     */
    private String read;

    /**
     * 쓰기
     */
    private String write;

    /**
     * 댓글
     */
    private String comment;
  }
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
}
