package ru.sample.rest.tests;

import com.jayway.restassured.response.Response;
import org.apache.commons.validator.routines.EmailValidator;
import org.testng.annotations.Test;
import ru.sample.rest.helpers.Endpoints;
import ru.sample.rest.helpers.QueryParameters;

import java.util.List;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertTrue;

public class RestTests extends TestBase{

  @Test
  // The first test to check user "Samantha" presents in the base. If not, another tests with this user make no sense.
  public void searchSamantha() {
    Response response = get(Endpoints.USERS)
                       .then().statusCode(200)
                       .and().extract().response();
    List<String> users = response.jsonPath().getList("username");
    assertThat(users, hasItem(QueryParameters.MY_USER));
  }

  @Test
  // Used queries because if we don't have some specific user, we simply can change him to another
  public void searchSamanthaPosts() {
    Response userResponse = given().queryParam(QueryParameters.USERNAME_PARAM, QueryParameters.MY_USER)
                           .when().get(Endpoints.USERS)
                           .then().statusCode(200)
                           .and().extract().response();
    String idFromJson = userResponse.jsonPath().getString("id");
    Integer userId = Integer.parseInt(idFromJson.replaceAll("[^\\d]",""));
    Response postsResponse = get(Endpoints.POSTS, userId)
                            .then().statusCode(200)
                            .and().extract().response();
    List<Integer> samanthasPosts = postsResponse.jsonPath().getList("id");
    assertThat(samanthasPosts.size(), equalTo(10));
  }

  @Test
  public void validateEmailInComments() {
    Response userResponse = given().queryParam(QueryParameters.USERNAME_PARAM, QueryParameters.MY_USER)
                           .when().get(Endpoints.USERS)
                           .then().statusCode(200)
                           .and().extract().response();
    String idFromJson = userResponse.jsonPath().getString("id");
    Integer userId = Integer.parseInt(idFromJson.replaceAll("[^\\d]", ""));
    Response postsResponse = get(Endpoints.POSTS, userId)
                            .then().statusCode(200)
                            .and().extract().response();
    List<Integer> samanthasPosts = postsResponse.jsonPath().getList("id");
    for (int i = 0; i < samanthasPosts.size(); i++) {
      Integer postId = samanthasPosts.get(i);
      Response commentsResponse = get(Endpoints.COMMENTS, postId)
                                 .then().statusCode(200)
                                 .and().extract().response();
      List<String> emails = commentsResponse.jsonPath().getList("email");
      for (int e = 0; e < emails.size(); e++) {
        String email = emails.get(e);
        assertTrue(EmailValidator.getInstance().isValid(email));
      }
    }
  }
}
