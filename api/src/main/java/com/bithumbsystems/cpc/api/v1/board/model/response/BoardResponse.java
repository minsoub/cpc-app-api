package com.bithumbsystems.cpc.api.v1.board.model.response;

import com.bithumbsystems.persistence.mongodb.board.model.entity.Board.Thumbnail;
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
public class BoardResponse {
  private Long id;
  private String title;
  private String contents;
  private List<String> tags;
  private Thumbnail thumbnail;
  private LocalDateTime createDate;
}
