package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseAPI {

    public static RequestSpecification getBaseRequest() {
        return new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json") // String exata exigida pelo Restful-Booker
                .build();
    }
}