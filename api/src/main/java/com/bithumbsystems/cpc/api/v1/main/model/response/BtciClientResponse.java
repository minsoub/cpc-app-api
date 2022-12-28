package com.bithumbsystems.cpc.api.v1.main.model.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BtciClientResponse {

  private BtciDetail altcoinMarketIndex;
  private BtciDetail marketIndex;
  private LocalDateTime date;

  @Getter
  @Setter
  public static class BtciDetail {
    private Double marketIndex;
    private Double width;
    private Double rate;
  }
}
