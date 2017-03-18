package com.trevorgowing.projectlog.common.persistence;

import org.apache.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates a DateRange.
 * <p>
 * <ol>
 * <li>A Date Range's end date can either be null or it must be equal to or after the start date
 * <li>A Date Range's start date cannot be null.
 * </ol>
 */
public class DateRangeConstraintValidator implements ConstraintValidator<ValidatedDateRange, DateRange> {

    private static final String VIOLATION_TEMPLATE = "{za.co.bsg.ems.constraints.DateRange.message}";
    private Logger logger = Logger.getLogger(getClass());

    @Override
    public void initialize(ValidatedDateRange constraintAnnotation) {
        logger.debug("DateRangeValidator Initialised");
    }

    @Override
    public boolean isValid(DateRange range, ConstraintValidatorContext context) {
        boolean valid = range.getStartDate() != null &&
                (range.getEndDate() == null || !range.getEndDate().isBefore(range.getStartDate()));

        if (!valid && context != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(VIOLATION_TEMPLATE).addConstraintViolation();
        }

        return valid;
    }
}
