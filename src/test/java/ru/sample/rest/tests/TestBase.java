package ru.sample.rest.tests;

import org.testng.annotations.BeforeClass;
import ru.sample.rest.helpers.ApplicationManager;
import java.io.IOException;

public class TestBase {
  protected static final ApplicationManager app = new ApplicationManager();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws IOException {
    app.init();
  }
}
