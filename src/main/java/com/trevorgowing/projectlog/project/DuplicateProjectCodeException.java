package com.trevorgowing.projectlog.project;

class DuplicateProjectCodeException extends RuntimeException {

  private static final long serialVersionUID = 6650518367158397293L;

  static final String REASON = "Duplicate project found with code";

  private DuplicateProjectCodeException(String code) {
    super(REASON + ": " + code);
  }

  static DuplicateProjectCodeException codedDuplicateCodeException(String code) {
    return new DuplicateProjectCodeException(code);
  }
}
