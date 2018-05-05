package com.trevorgowing.projectlog.common.persistence;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Add this annotation to a {@link DateRange} attribute on any entity to validate that updates and
 * inserts on that attribute leave it in a valid state.
 */
@Target(value = FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = DateRangeConstraintValidator.class)
@Documented
public @interface ValidatedDateRange {

  String message() default "{com.trevorgowing.constraints.DateRange.default.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
