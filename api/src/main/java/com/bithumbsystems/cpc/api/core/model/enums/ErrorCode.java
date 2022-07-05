package com.bithumbsystems.cpc.api.core.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  UNKNOWN_ERROR("F001", "error"),
  INVALID_FILE("F002","file is invalid"),
  FAIL_SAVE_FILE("F003","file save fail"),
  NOT_FOUND_CONTENT("F004","not found content"),
  FAIL_UPDATE_CONTENT("F005","cannot update content"),
  FAIL_CREATE_CONTENT("F006","cannot create content"),
  FAIL_DELETE_CONTENT("F007","cannot delete content"),
  DUPLICATE_KEY_ERROR("F008", "duplicate key error"),
  TIMEOUT_ERROR("F009", "timeout error"),
  INVALID_INPUT_VALUE("F010", "invalid input value"),
  INVALID_EMAIL_FORMAT("F012", "invalid email format"),
  INVALID_NAME_FORMAT("F013", "invalid name format"),
  INVALID_PHONE_FORMAT("F014", "invalid phone format");

  private final String code;
  private final String message;
}