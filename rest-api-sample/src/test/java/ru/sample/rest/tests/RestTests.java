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
    List<String> users = getUsersByUsername();
    assertThat(users, hasItem(QueryParameters.MY_USER));
  }

  @Test
  public void searchSamanthaPosts() {
    List<Integer> samanthasPostsIds = getUserPostsIdsByQuery();
    assertThat(samanthasPostsIds.size(), equalTo(10));
  }

  @Test
  public void validateEmailInComments() {
    List<Integer> samanthasPostsIds = getUserPostsIdsByQuery();
    for (int i = 0; i < samanthasPostsIds.size(); i++) {
      Integer postId = samanthasPostsIds.get(i);
      List<String> emails = getUsersEmailsFromComments(postId);
      for (int e = 0; e < emails.size(); e++) {
        String email = emails.get(e);
        assertTrue(EmailValidator.getInstance().isValid(email));
      }
    }
  }

  private List<String> getUsersByUsername() {
    Response my_response = getResponse(Endpoints.USERS);
    List<String> users = my_response.jsonPath().getList("username");
    return users;
  }

  // Used queries because if we don't have some specific user, we simply can change him to another
  private List<Integer> getUserPostsIdsByQuery() {
    Response userResponse = getResponseWithQuery(Endpoints.USERS, QueryParameters.USERNAME_PARAM, QueryParameters.MY_USER);
    String idFromJson = userResponse.jsonPath().getString("id");
    Integer userId = Integer.parseInt(idFromJson.replaceAll("[^\\d]",""));
    Response postsResponse = getResponseWithParameter(Endpoints.POSTS, userId);
    List<Integer> userPostsIds = postsResponse.jsonPath().getList("id");
    return userPostsIds;
  }

  private List<String> getUsersEmailsFromComments(int postId) {
    Response commentsResponse = getResponseWithParameter(Endpoints.COMMENTS, postId);
    List<String> emails = commentsResponse.jsonPath().getList("email");
    return emails;
  }

  private Response getResponseWithParameter(String endpoint, Integer id) {
    Response response = get(endpoint, id)
                       .then().statusCode(200)
                       .and().extract().response();
    return response;
  }

  private Response getResponse(String endpoint) {
    Response response = get(endpoint)
                       .then().statusCode(200)
                       .and().extract().response();
    return response;
  }

  private Response getResponseWithQuery(String endpoint, String queryParam, String value) {
    Response response = given().queryParam(queryParam, value)
                       .when().get(endpoint)
                       .then().statusCode(200)
                       .and().extract().response();
    return response;
  }
}
