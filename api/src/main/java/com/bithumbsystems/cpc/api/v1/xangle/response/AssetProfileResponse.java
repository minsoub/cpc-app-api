package com.bithumbsystems.cpc.api.v1.xangle.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssetProfileResponse {

  private List<Data> data;

  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  public static class Data {

    private String assetId;
    private Assets assetProfile;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Assets {
      private String assetName;
      private String projectName;
    }

  }

}
