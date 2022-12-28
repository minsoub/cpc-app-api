package com.bithumbsystems.cpc.api.v1.board.model.response;

import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster.Auth;
import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster.Sns;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoardCategoryResponse {
  private String id;
  private String name;
  private Integer sort;
}
