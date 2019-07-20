package ru.sample.rest.helpers;

import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

public class ResponseHelper {
  protected Response getResponseWithParameter(String endpoint, Integer id) {
    Response response = get(endpoint, id)
            .then().statusCode(200)
            .and().extract().response();
    return response;
  }

  protected Response getResponse(String endpoint) {
    Response response = get(endpoint)
            .then().statusCode(200)
            .and().extract().response();
    return response;
  }

  protected Response getResponseWithQuery(String endpoint, String queryParam, String value) {
    Response response = given().queryParam(queryParam, value)
            .when().get(endpoint)
            .then().statusCode(200)
            .and().extract().response();
    return response;
  }
}
