package com.bithumbsystems.persistence.mongodb.board.model.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "comments")
public class Comment {

  /**
   * 댓글 ID
   */
  @Id
  private String id;

  /**
   * 게시글 ID - board의 id
   */
  @Indexed
  private Long boardId;

  /**
   * 댓글 내용
   */
  private String contents;

  /**
   * 생성날짜
   */
  private LocalDateTime createDate;

  /**
   * 생성자 ID
   */
  private String createAdminAccountId;

  /**
   * 수정날짜
   */
  private LocalDateTime updateDate;

  /**
   * 수정자 ID
   */
  private String updateAdminAccountId;
}
