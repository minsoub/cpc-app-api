package com.bithumbsystems.cpc.api.v1.board.model.response;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardResponse {
  private String id;
  private int no;
  private String title;
  private String contents;
  private Date createDate;
}
