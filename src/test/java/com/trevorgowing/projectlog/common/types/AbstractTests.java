package com.trevorgowing.projectlog.common.types;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractTests {

    protected static final String IRRELEVANT_MESSAGE = "irrelevant.message";
    protected static final LocalDate IRRELEVANT_DATE = LocalDate.now();

    protected static final long IRRELEVANT_USER_ID = 1L;
    protected static final String IRRELEVANT_USER_EMAIL = "irrelevant@irrelevant.com";
    protected static final String IRRELEVANT_USER_PASSWORD = "irrelevant.password";
    protected static final String IRRELEVANT_USER_FIRST_NAME = "irrelevant.first.name";
    protected static final String IRRELEVANT_USER_LAST_NAME = "irrelevant.last.name";
}
