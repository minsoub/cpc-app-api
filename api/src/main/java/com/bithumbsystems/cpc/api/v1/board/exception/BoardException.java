package com.bithumbsystems.cpc.api.v1.board.exception;

import com.bithumbsystems.cpc.api.core.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BoardException extends RuntimeException{

  public BoardException(ErrorCode errorCode) {
    super(String.valueOf(errorCode));
  }
}
