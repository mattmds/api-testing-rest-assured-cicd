package api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class BookingAPI {

    public static Response createBooking(String payload) {
        return given()
                .spec(BaseAPI.getBaseRequest())
                .body(payload)
            .when()
                .post("/booking")
            .then()
                .extract()
                .response();
    }

    public static Response getBooking(int id) {
        return given()
                .spec(BaseAPI.getBaseRequest())
            .when()
                .get("/booking/" + id);
    }

    public static Response deleteBooking(int id, String token) {
        return given()
                .spec(BaseAPI.getBaseRequest())
                .header("Cookie", "token=" + token)
            .when()
                .delete("/booking/" + id);
    }
}