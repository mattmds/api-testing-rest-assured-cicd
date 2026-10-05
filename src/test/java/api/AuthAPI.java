package api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class AuthAPI {

    public static String getToken() {
        String payload = """
                {
                    "username": "admin",
                    "password": "password123"
                }
                """;

        Response response = given()
                .spec(BaseAPI.getBaseRequest())
                .body(payload)
                .when()
                .post("/auth");

        return response.jsonPath().getString("token");
    }
}