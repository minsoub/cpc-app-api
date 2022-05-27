package com.bithumbsystems.cpc.api.v1.board.model.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommentResponse {
  private String id;
  private String contents;
  private LocalDateTime createDate;
  private String createAdminAccountId;
}
