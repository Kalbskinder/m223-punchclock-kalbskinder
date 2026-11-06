package ch.zli.m223;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class StatisticsResourceTest {

    private Long createEntry(String checkIn, String checkOut) {
        return given().contentType("application/json")
            .body("{\"checkIn\":\"" + checkIn + "\",\"checkOut\":\"" + checkOut + "\"}")
            .when().post("/entries")
            .then().statusCode(200)
            .extract().jsonPath().getLong("id");
    }

    @Test
    public void testTimeSummaries() {
        Long a = createEntry("2024-08-21T13:00:00", "2024-08-21T16:45:00");
        Long b = createEntry("2024-08-20T08:00:00", "2024-08-20T12:00:00");
        Long c = createEntry("2024-08-20T13:00:00", "2024-08-20T17:30:00");

        String body = given()
            .when().get("/statistics/time-summaries")
            .then().statusCode(200)
            .extract().asString();

        // Days are summed up and sorted ascending (other tests may have left entries on other days)
        assertTrue(body.contains("\"2024-08-20\":\"08:30:00\""), body);
        assertTrue(body.contains("\"2024-08-21\":\"03:45:00\""), body);
        assertTrue(body.indexOf("2024-08-20") < body.indexOf("2024-08-21"), body);

        for (Long id : new Long[] { a, b, c }) {
            given().when().delete("/entries/" + id).then().statusCode(204);
        }
        String after = given().when().get("/statistics/time-summaries")
            .then().statusCode(200).extract().asString();
        assertFalse(after.contains("2024-08-2"), after);
    }
}
