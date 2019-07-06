package ru.sample.rest.helpers;

public final class Endpoints {
  public static final String USERS = "/users";
  public static final String POSTS = "/posts?userId={userId}";
  public static final String COMMENTS = "/comments?postId={postId}";
}
