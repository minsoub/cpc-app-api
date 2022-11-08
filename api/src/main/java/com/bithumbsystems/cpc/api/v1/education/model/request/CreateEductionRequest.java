package com.bithumbsystems.cpc.api.v1.education.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateEductionRequest {

  private String id;
//  @Pattern(regexp = "^[가-힣]")
  private String name;
//  @Future
//  @DateTimeFormat(pattern = "yyyy-MM-dd")
//  @JsonFormat(pattern = "yyyy-MM-dd")
  private String desireDate;
//  @Email
  private String email;
//  @Pattern(regexp = "^\\d+")
  private String cellPhone;
  private String content;
  private Boolean isConsignmentAgreement;
  private Boolean isUseAgreement;

}
