package com.bithumbsystems.cpc.api.v1.protection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bithumbsystems.cpc.api.core.exception.InvalidParameterException;
import com.bithumbsystems.cpc.api.core.model.validation.ValidationGroups;
import com.bithumbsystems.cpc.api.core.util.ValidationUtils;
import com.bithumbsystems.cpc.api.v1.protection.model.request.FraudReportRequest;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FraudReportValidationTest {
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

  @DisplayName("이메일 빈 문자열 전송 시 에러 발생")
  @Test
  void blank_validation_test() {
    // given
    FraudReportRequest fraudReportRequest = FraudReportRequest.builder()
        .email("")
        .title("title")
        .contents("contents")
        .termsPrivacy(true)
        .build();

    // when
    Set<ConstraintViolation<FraudReportRequest>> violations = validator.validate(fraudReportRequest,
        ValidationGroups.NotEmptyGroup.class);

    // then
    assertThat(violations).isNotEmpty();
    violations.forEach(error -> {
      assertThat(error.getMessage()).isEqualTo("이메일은 필수 입력 값입니다.");
    });
  }

  @DisplayName("이메일 형식 아닌 경우 에러 발생")
  @Test
  void email_validation_test() {
    // given
    FraudReportRequest fraudReportRequest = FraudReportRequest.builder()
        .email("name")
        .title("title")
        .contents("contents")
        .termsPrivacy(true)
        .build();

    // when
    Throwable exception = assertThrows(InvalidParameterException.class, () -> {
        ValidationUtils.assertEmailFormat(fraudReportRequest.getEmail());
    });

    // then
    assertEquals("잘못된 이메일 형식입니다. 이메일 주소를 확인해 주세요.", exception.getMessage());
  }

  @DisplayName("제목 입력 길이 초과된 경우 에러 발생")
  @Test
  void title_length_over_test() {
    // given
    FraudReportRequest fraudReportRequest = FraudReportRequest.builder()
        .email("name@example.com")
        .title("123456789012345678901234567890123456789012345678901")
        .contents("contents")
        .termsPrivacy(true)
        .build();

    // when
    Set<ConstraintViolation<FraudReportRequest>> violations = validator.validate(fraudReportRequest);

    // then
    assertThat(violations).isNotEmpty();
    violations.forEach(error -> {
      assertThat(error.getMessage()).isEqualTo("제목은 50byte 까지 입력 가능합니다.");
    });
  }

  @DisplayName("유효성 검사 성공")
  @Test
  void validation_success_test() {
    // given
    FraudReportRequest fraudReportRequest = FraudReportRequest.builder()
        .email("name@example.com")
        .title("title")
        .contents("contents")
        .termsPrivacy(true)
        .build();

    // when
    Set<ConstraintViolation<FraudReportRequest>> violations = validator.validate(fraudReportRequest);

    // then
    assertThat(violations).isEmpty();
  }
}
