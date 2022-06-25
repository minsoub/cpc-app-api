package com.bithumbsystems.cpc.api.v1.care;

import static org.assertj.core.api.Assertions.assertThat;

import com.bithumbsystems.cpc.api.core.model.validation.ValidationGroups;
import com.bithumbsystems.cpc.api.v1.care.model.request.LegalCounselingRequest;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LegalCounselingValidationTest {
  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  public static void init() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @AfterAll
  public static void close() {
    factory.close();
  }

  @DisplayName("이름 빈 문자열 전송 시 에러 발생")
  @Test
  void blank_validation_test() {
    // given
    LegalCounselingRequest legalCounselingRequest = LegalCounselingRequest.builder()
        .name("")
        .email("name@example.com")
        .cellPhone("010-1234-5678")
        .contents("contents")
        .servicePrivacy(true)
        .termsPrivacy(true)
        .build();

    // when
    Set<ConstraintViolation<LegalCounselingRequest>> violations = validator.validate(legalCounselingRequest,
        ValidationGroups.NotEmptyGroup.class);

    // then
    assertThat(violations).isNotEmpty();
    violations.forEach(error -> {
      assertThat(error.getMessage()).isEqualTo("이름은 필수 입력 값입니다.");
    });
  }

  @DisplayName("이름 특수 문자 포함 시 에러 발생")
  @Test
  void include_special_character_validation_test() {
    // given
    LegalCounselingRequest legalCounselingRequest = LegalCounselingRequest.builder()
        .name("홍길동!")
        .email("name@example.com")
        .cellPhone("010-1234-5678")
        .contents("contents")
        .servicePrivacy(true)
        .termsPrivacy(true)
        .build();

    // when
    Set<ConstraintViolation<LegalCounselingRequest>> violations = validator.validate(legalCounselingRequest,
        ValidationGroups.PatternCheckGroup.class);

    // then
    assertThat(violations).isNotEmpty();
    violations.forEach(error -> {
      assertThat(error.getMessage()).isEqualTo("이름은 특수문자를 포함할 수 없습니다.");
    });
  }

  @DisplayName("유효성 검사 성공")
  @Test
  void validation_success_test() {
    // given
    LegalCounselingRequest legalCounselingRequest = LegalCounselingRequest.builder()
        .name("홍길동")
        .email("name@example.com")
        .cellPhone("010-1234-5678")
        .contents("contents")
        .servicePrivacy(true)
        .termsPrivacy(true)
        .build();

    // when
    Set<ConstraintViolation<LegalCounselingRequest>> violations = validator.validate(legalCounselingRequest);

    // then
    assertThat(violations).isEmpty();
  }
}
