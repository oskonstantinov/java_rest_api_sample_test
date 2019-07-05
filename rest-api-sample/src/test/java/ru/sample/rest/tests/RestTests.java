package ru.sample.rest.tests;

import com.jayway.restassured.response.Response;
import org.testng.annotations.Test;
import ru.sample.rest.helpers.Endpoints;
import java.util.List;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class RestTests extends TestBase{

  @Test
  // The first test to check user "Samantha" presents in the base. If not, another tests make no sense.
  public void searchSamantha() {
    Response response = get(Endpoints.USERS).then().statusCode(200)
            .and().extract().response();
    List<String> users = response.jsonPath().getList("username");
    assertThat(users, hasItem("Samantha"));
  }
}
