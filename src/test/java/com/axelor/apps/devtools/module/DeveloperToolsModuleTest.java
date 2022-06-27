package com.axelor.apps.devtools.module;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
