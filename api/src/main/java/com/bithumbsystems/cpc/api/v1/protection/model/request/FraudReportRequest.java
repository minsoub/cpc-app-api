package com.bithumbsystems.cpc.api.v1.protection.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FraudReportRequest {
  private String title;
  private String contents;
}
