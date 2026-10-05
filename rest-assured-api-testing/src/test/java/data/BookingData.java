package data;

public class BookingData {
    
    public static String getValidBookingPayload() {
        return """
                {
                  "firstname": "Matheus",
                  "lastname": "Mendes",
                  "totalprice": 1500,
                  "depositpaid": true,
                  "bookingdates": {
                    "checkin": "2026-12-01",
                    "checkout": "2026-12-10"
                  },
                  "additionalneeds": "Breakfast"
                }
                """;
    }
}