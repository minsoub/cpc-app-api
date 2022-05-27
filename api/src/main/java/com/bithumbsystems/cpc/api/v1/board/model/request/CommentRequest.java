package com.bithumbsystems.cpc.api.v1.board.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommentRequest {
  private Long boardId;
  private String contents;
}
