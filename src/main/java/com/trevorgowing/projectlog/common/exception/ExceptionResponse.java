package com.trevorgowing.projectlog.common.exception;

import java.io.Serializable;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@NoArgsConstructor
public class ExceptionResponse implements Serializable {

  private static final long serialVersionUID = 5795604402500068825L;

  private int status;
  private String error;
  private String message;

  @Builder
  public ExceptionResponse(HttpStatus status, String message) {
    this.status = status.value();
    this.error = status.getReasonPhrase();
    this.message = message;
  }

  public static ExceptionResponse from(HttpStatus status, String message) {
    return new ExceptionResponse(status, message);
  }

  @Override
  public boolean equals(Object objectToCompareTo) {
    if (this == objectToCompareTo) return true;
    if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
    ExceptionResponse exceptionResponseToCompareTo = (ExceptionResponse) objectToCompareTo;
    return getStatus() == exceptionResponseToCompareTo.getStatus()
        && Objects.equals(getError(), exceptionResponseToCompareTo.getError())
        && Objects.equals(getMessage(), exceptionResponseToCompareTo.getMessage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getStatus(), getError(), getMessage());
  }
}
