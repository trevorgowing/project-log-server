package com.trevorgowing.projectlog.common.types;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

public abstract class AbstractControllerUnitTests extends AbstractTests {

    protected abstract Object getController();

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(getController());
    }
}
