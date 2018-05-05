package com.trevorgowing.projectlog.common.types;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.experimental.categories.Category;

@Category(ControllerUnitTests.class)
public abstract class AbstractControllerUnitTests extends AbstractSpringTests {

  protected abstract Object getController();

  @Before
  public void setup() {
    RestAssuredMockMvc.standaloneSetup(getController());
  }
}
