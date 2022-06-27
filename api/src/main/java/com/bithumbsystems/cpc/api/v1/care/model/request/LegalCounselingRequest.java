package com.bithumbsystems.cpc.api.v1.care.model.request;

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
public class LegalCounselingRequest {

  @NotBlank(message="이름은 필수 입력 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
  private String name;

  @NotBlank(message="이메일은 필수 입력 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
  private String email;

  @NotBlank(message="휴대폰번호는 필수 입력 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
  private String cellPhone;

  @NotBlank(message="내용은 필수 입력 값입니다.")
  @Size(max=500, message = "내용은 500byte 까지 입력 가능합니다.")
  private String contents;

  @AssertTrue
  private Boolean servicePrivacy;

  @AssertTrue
  private Boolean termsPrivacy;

  private Boolean answerToContacts;
  private String attachFileId;
}
