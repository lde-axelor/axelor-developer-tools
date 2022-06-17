package com.axelor.apps.devtools.module;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeveloperToolsModuleTest {

  private DeveloperToolsModule module;

  @Before
  public void setUp() {
    this.module = new DeveloperToolsModule();
  }

  @Test
  public void moduleShouldExists() {
    assertNotNull(this.module);
  }
}
