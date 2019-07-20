package ru.sample.rest.helpers;

import com.jayway.restassured.response.Response;

import java.util.List;

public class UsersHelper extends ResponseHelper {
  public List<String> getUsersByUsername(String endpoint) {
    Response my_response = getResponse(endpoint);
    List<String> users = my_response.jsonPath().getList("username");
    return users;
  }

  // Used queries because if we don't have some specific user, we simply can change him to another
  public List<Integer> getUserPostsIdsByQuery(String endpoint, String parameter, String value) {
    Response userResponse = getResponseWithQuery(endpoint, parameter, value);
    String idFromJson = userResponse.jsonPath().getString("id");
    Integer userId = Integer.parseInt(idFromJson.replaceAll("[^\\d]",""));
    Response postsResponse = getResponseWithParameter(Endpoints.POSTS, userId);
    List<Integer> userPostsIds = postsResponse.jsonPath().getList("id");
    return userPostsIds;
  }

  public List<String> getUsersEmailsFromComments(String endpoint, int postId) {
    Response commentsResponse = getResponseWithParameter(endpoint, postId);
    List<String> emails = commentsResponse.jsonPath().getList("email");
    return emails;
  }
}
