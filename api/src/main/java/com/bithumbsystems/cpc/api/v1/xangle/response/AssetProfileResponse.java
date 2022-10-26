package com.bithumbsystems.cpc.api.v1.xangle.response;

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
public class AssetProfileResponse {

  private Data data;

  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  public static class Data {

    private String assetId;
    private Assets assetProfile;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Assets {
      private String assetName;
      private String projectName;
    }

  }

}
