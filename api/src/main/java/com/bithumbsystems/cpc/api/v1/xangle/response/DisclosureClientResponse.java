package com.bithumbsystems.cpc.api.v1.xangle.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisclosureClientResponse {

  private String symbol;
  private String projectName;
  private String projectLogo;
  private String title;
  private LocalDateTime createDate;
  private String xangleUrl;


}
