package ru.sample.rest.tests;

import org.apache.commons.validator.routines.EmailValidator;
import org.testng.annotations.Test;
import ru.sample.rest.helpers.Endpoints;
import ru.sample.rest.helpers.QueryParameters;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertTrue;

public class RestTests extends TestBase{

  @Test
  // The first test to check user "Samantha" presents in the base. If not, another tests with this user make no sense.
  public void searchSamantha() {
    List<String> users = app.users().getUsersByUsername(Endpoints.USERS);
    assertThat(users, hasItem(QueryParameters.MY_USER));
  }

  @Test
  public void searchSamanthaPosts() {
    List<Integer> samanthasPostsIds = app.users().getUserPostsIdsByQuery(Endpoints.USERS, QueryParameters.USERNAME_PARAM, QueryParameters.MY_USER);
    assertThat(samanthasPostsIds.size(), equalTo(10));
  }

  @Test
  public void validateEmailInComments() {
    List<Integer> samanthasPostsIds = app.users().getUserPostsIdsByQuery(Endpoints.USERS, QueryParameters.USERNAME_PARAM, QueryParameters.MY_USER);
    for (int i = 0; i < samanthasPostsIds.size(); i++) {
      Integer postId = samanthasPostsIds.get(i);
      List<String> emails = app.users().getUsersEmailsFromComments(Endpoints.COMMENTS, postId);
      for (int e = 0; e < emails.size(); e++) {
        String email = emails.get(e);
        assertTrue(EmailValidator.getInstance().isValid(email));
      }
    }
  }
}
