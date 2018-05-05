package com.trevorgowing.projectlog.common.persistence;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Validates a DateRange.
 *
 * <p>
 *
 * <ol>
 *   <li>A Date Range's end date can either be null or it must be equal to or after the start date
 *   <li>A Date Range's start date cannot be null.
 * </ol>
 */
@Slf4j
public class DateRangeConstraintValidator
    implements ConstraintValidator<ValidatedDateRange, DateRange> {

  private static final String VIOLATION_TEMPLATE = "{za.co.bsg.ems.constraints.DateRange.message}";

  @Override
  public void initialize(ValidatedDateRange constraintAnnotation) {
    log.debug("DateRangeValidator Initialised");
  }

  @Override
  public boolean isValid(DateRange range, ConstraintValidatorContext context) {
    boolean valid =
        range.getStartDate() != null
            && (range.getEndDate() == null || !range.getEndDate().isBefore(range.getStartDate()));

    if (!valid && context != null) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(VIOLATION_TEMPLATE).addConstraintViolation();
    }

    return valid;
  }
}
