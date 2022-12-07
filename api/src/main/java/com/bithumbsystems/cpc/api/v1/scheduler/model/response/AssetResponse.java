package com.bithumbsystems.cpc.api.v1.scheduler.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponse {

  private Data data;

  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Data {

    private List<Assets> assets;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Assets {
      private String name;
      private String assetId;
      private String symbol;
    }

  }

}
