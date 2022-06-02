package com.bithumbsystems.cpc.api.v1.board.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardRequest {
  private Long id;
  private String boardMasterId;
  private String title;
  private String contents;
  private Boolean isReply;
  private String parentId;
  private Boolean isUse;
  private Integer readCount;
  private Boolean isSetNotice;
  private Boolean isSecret;
  private String password;
  private String attachFileId;
  private Thumbnail thumbnail;

  @Data
  public class Thumbnail {
    private String desktop;
    private String mobile;
  }
}
