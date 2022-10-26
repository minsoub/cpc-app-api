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
public class DisclosureResponse {

  private Data data;
  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Data {

    private List<Disclosures> disclosures;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Disclosures {

      private String projectId;
      private String projectSymbol;
      private String projectLogo;
      private String disclosureId;
      private String title;
      private String publishStatus;
      private String publishTimestamp;
      private String publishTimestampUtc;
      private String lastModifiedTimestamp;
      private Boolean valid;
      private String disclosureType;
      private String disclosureContentType;
      private Boolean resolved;
      private String validationMethod;
      private String xangleUrl;
      private String pdfFileUrl;

    }

  }

}
