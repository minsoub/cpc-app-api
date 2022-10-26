package com.bithumbsystems.cpc.api.v1.education.model.request;

import java.time.LocalDateTime;
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
public class CreateEductionRequest {

  private String id;
//  @Pattern(regexp = "^[가-힣]")
  private String name;
//  @Future
  private LocalDateTime desireDate;
//  @Email
  private String email;
//  @Pattern(regexp = "^\\d+")
  private String cellPhone;
  private String content;



}
