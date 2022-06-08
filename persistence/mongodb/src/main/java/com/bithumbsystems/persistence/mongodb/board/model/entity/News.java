package com.bithumbsystems.persistence.mongodb.board.model.entity;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "cpc_news")
public class News {

  @Transient
  public static final String SEQUENCE_NAME = "news_sequence";

  /**
   * ID
   */
  @Id
  private Long id;

  /**
   * 언론사
   */
  private String newspaper;

  /**
   * 뉴스 제목
   */
  private String title;

  /**
   * 썸네일 이미지 링크
   */
  private String thumbnailUrl;

  /**
   * 뉴스 링크
   */
  private String linkUrl;

  /**
   * 뉴스 게시일
   */
  private Date postingDate;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
}
