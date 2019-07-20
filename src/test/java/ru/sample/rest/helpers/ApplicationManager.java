package ru.sample.rest.helpers;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ApplicationManager {
  private UsersHelper usersHelper;

  public void init() throws IOException {
    Properties properties = new Properties();
    properties.load(new FileReader(new File("src/test/resources/config.properties")));
    RestAssured.requestSpecification = new RequestSpecBuilder()
            .setBaseUri(properties.getProperty("baseUrl"))
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .build();
    usersHelper = new UsersHelper();
  }

  public UsersHelper users() {
    return usersHelper;
  }
}
