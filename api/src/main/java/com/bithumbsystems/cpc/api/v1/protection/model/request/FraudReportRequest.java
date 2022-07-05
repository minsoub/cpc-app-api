package com.bithumbsystems.cpc.api.v1.protection.model.request;

import com.bithumbsystems.cpc.api.core.model.validation.ValidationGroups;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FraudReportRequest {
  @NotBlank(message="이메일은 필수 입력 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
  private String email;

  @NotBlank(message="제목은 필수 입력 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
  @Size(max=50, message = "제목은 50byte 까지 입력 가능합니다.")
  private String title;

  @NotBlank(message="내용은 필수 입력 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
  @Size(max=500, message = "내용은 500byte 까지 입력 가능합니다.")
  private String contents;

  @AssertTrue
  private Boolean entrustPrivacy;

  @AssertTrue
  private Boolean termsPrivacy;

  private Boolean answerToContacts;
  private String attachFileId;
}
