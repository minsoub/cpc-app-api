package com.bithumbsystems.cpc.api.v1.main.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BtciResponse {

  private String status;
  private Btci data;

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  @Builder
  public static class Btci {
    private BtciDetail btai;
    private BtciDetail btmi;
    private Long date;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class BtciDetail {

      private Double market_index;
      private Double width;
      private Double rate;

    }

  }

}
