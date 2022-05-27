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
}
