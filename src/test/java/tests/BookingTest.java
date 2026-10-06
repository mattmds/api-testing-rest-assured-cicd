package tests;

import api.AuthAPI;
import api.BookingAPI;
import data.BookingData;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingTest {

    private static String token;
    private static int bookingId;
    private static String payload;

    @BeforeAll
    static void setup() {
        System.out.println("\n==================================================================");
        System.out.println("  [SETUP GLOBAL] Autenticando e preparando dados de teste...       ");
        System.out.println("==================================================================");
        token = AuthAPI.getToken();
        payload = BookingData.getValidBookingPayload();
    }

    @AfterAll
    static void teardown() {
        if (bookingId != 0 && token != null) {
            System.out.println("\n==================================================================");
            System.out.println("  [TEARDOWN GLOBAL] Limpando dados remanescentes no servidor...   ");
            System.out.println("==================================================================");
            BookingAPI.deleteBooking(bookingId, token);
        }
    }

    @BeforeEach
    void startTest(TestInfo testInfo) {
        System.out.println("\n==================================================================");
        System.out.println(">>> [INICIO] " + testInfo.getDisplayName());
        System.out.println("------------------------------------------------------------------");
    }

    @AfterEach
    void finishTest(TestInfo testInfo) {
        System.out.println("------------------------------------------------------------------");
        System.out.println("<<< [FIM] " + testInfo.getDisplayName());
        System.out.println("==================================================================");
    }

    @Test
    @Order(1)
    @DisplayName("Deve criar uma nova reserva com sucesso")
    void testCreateBooking() {
        System.out.println("  -> Enviando requisicao POST /booking...");
        Response response = BookingAPI.createBooking(payload);

        System.out.println("  -> Validando resposta (Status 200, Headers, SLA, Schema e Payload)...");
        response.then()
                // 1. Status Code
                .statusCode(201)
                // 2. Headers
                .header("Content-Type", containsString("application/json"))
                // 3. Performance SLA (< 1500ms)
                .time(lessThan(1500L))
                // 4. Validação de Contrato (Schema JSON)
                .body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"))
                // 5. Integridade de Dados
                .body("booking.firstname", equalTo("Matheus"))
                .body("booking.lastname", equalTo("Mendes"))
                .body("booking.totalprice", equalTo(1500));

        // Extrai o ID para os próximos testes
        bookingId = response.jsonPath().getInt("bookingid");
        System.out.println("  -> Reserva criada com sucesso! ID gerado: " + bookingId);
    }

    @Test
    @Order(2)
    @DisplayName("Deve consultar a reserva criada")
    void testGetBooking() {
        System.out.println("  -> Enviando requisicao GET /booking/" + bookingId + "...");
        Response response = BookingAPI.getBooking(bookingId);

        System.out.println("  -> Validando Status 200 e dados da reserva...");
        response.then()
                .statusCode(200)
                .body("firstname", equalTo("Matheus"))
                .body("lastname", equalTo("Mendes"));

        System.out.println("  -> Dados da reserva confirmados com sucesso.");
    }

    @Test
    @Order(3)
    @DisplayName("Deve retornar 404 ao buscar uma reserva inexistente")
    void testGetNonExistentBooking() {
        int invalidId = 999999999;
        System.out.println("  -> Enviando requisicao GET para ID inexistente (/booking/" + invalidId + ")...");
        Response response = BookingAPI.getBooking(invalidId);

        System.out.println("  -> Validando codigo de erro retornado...");
        response.then()
                .statusCode(404);

        System.out.println("  -> Resposta 404 Not Found confirmada conforme esperado.");
    }

    @Test
    @Order(4)
    @DisplayName("Deve deletar a reserva com sucesso usando o Token de acesso para operacoes administrativas")
    void testDeleteBooking() {
        System.out.println("  -> Enviando requisicao DELETE /booking/" + bookingId + "...");
        Response response = BookingAPI.deleteBooking(bookingId, token);

        System.out.println("  -> Validando Status HTTP 201 Created no DELETE...");
        response.then()
                .statusCode(201); // A API retorna 201 Created no DELETE

        System.out.println("  -> Verificando se a reserva foi realmente removida (esperando 404)...");
        BookingAPI.getBooking(bookingId).then().statusCode(404);

        System.out.println("  -> Exclusao validada com sucesso.");
        bookingId = 0; // Prevenir falha no teardown
    }
}