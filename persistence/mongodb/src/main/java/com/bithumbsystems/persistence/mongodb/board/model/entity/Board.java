package com.bithumbsystems.persistence.mongodb.board.model.entity;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "board")
public class Board {

  /**
   * 게시물 ID
   */
  private @Id String id;

  /**
   * 게시판 ID - boardMaster의 id
   */
  @Indexed
  private String boardMasterId;

  /**
   * 게시물 번호
   */
  private int no;

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
  private boolean isReply;

  /**
   * 부모 게시물 ID
   */
  private String parentId;

  /**
   * 사용 여부
   */
  private boolean isUse;

  /**
   * 조회 수
   */
  private int readCount;

  /**
   * 공지 설정
   */
  private boolean isSetNotice;

  /**
   * 비밀글 여부
   */
  private boolean isSecret;

  /**
   * 비밀번호
   */
  private String password;

  /**
   * 첨부파일 ID
   */
  private String attachFileId;

  /**
   * 댓글
   */
  private List<Comment> comments;

  /**
   * 생성날짜
   */
  private Date createDate;

  /**
   * 생성자 ID
   */
  private String createAdminAccountId;

  /**
   * 수정날짜
   */
  private Date updateDate;

  /**
   * 수정자 ID
   */
  private String updateAdminAccountId;

  /**
   * 댓글
   */
  @Data
  public class Comment {

    /**
     * 댓글 ID
     */
    private @Id String id;

    /**
     * 댓글 내용
     */
    private String contents;

    /**
     * 생성날짜
     */
    private Date createDate;

    /**
     * 생성자 ID
     */
    private String createAdminAccountId;

    /**
     * 수정날짜
     */
    private Date updateDate;

    /**
     * 수정자 ID
     */
    private String updateAdminAccountId;
  }
}
