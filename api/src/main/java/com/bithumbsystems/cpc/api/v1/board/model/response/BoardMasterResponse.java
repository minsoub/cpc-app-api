package com.bithumbsystems.cpc.api.v1.board.model.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardMasterResponse {
  private Long id;
  private String type;
  private Boolean isAllowComment;
  private Boolean isAllowReply;
  private Boolean isAllowAttachFile;
  private Boolean isUseCategory;
  private List<Category> categories;
  private String paginationType;
  private Integer countPerPage;
  private Boolean isUseTag;
  private List<String> tags;
  private Sns snsShare;
  private Auth auth;
  private LocalDateTime createDate;

  @Data
  public class Category {
    private String categoryId;
    private String categoryName;
  }

  @Data
  public class Sns {
    private Boolean kakaotalk;
    private Boolean facebook;
    private Boolean twitter;
    private Boolean url;
  }

  @Data
  public class Auth {
    private String list;
    private String read;
    private String write;
    private String comment;
  }
}
