package ch.zli.m223;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import ch.zli.m223.model.Entry;
import ch.zli.m223.service.TimeSummaryService;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@QuarkusTest
@TestSecurity(user = "test@punchclock.ch", roles = "User")
public class StatisticsResourceTest {

    @Inject
    TimeSummaryService timeSummaryService;

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

    @Test
    public void testCalculateSummaryPerDay() {
        List<Entry> entries = new ArrayList<>();
        entries.add(entry("2024-08-21T13:00:00", "2024-08-21T16:45:00"));
        entries.add(entry("2024-08-20T08:00:00", "2024-08-20T12:00:00"));
        entries.add(entry("2024-07-20T13:00:00", "2024-07-20T17:30:00"));

        Map<LocalDate, String> timeSummariesResult = timeSummaryService.calculateSummaryPerDay(entries);

        // One result per day, with the duration in the format HH:mm:ss
        assertEquals(entries.size(), timeSummariesResult.size());
        assertEquals("03:45:00", timeSummariesResult.get(LocalDate.of(2024, 8, 21)));
        assertEquals("04:00:00", timeSummariesResult.get(LocalDate.of(2024, 8, 20)));
        assertEquals("04:30:00", timeSummariesResult.get(LocalDate.of(2024, 7, 20)));

        // Days are sorted ascending, regardless of the order of the entries
        assertEquals(
            List.of(
                LocalDate.of(2024, 7, 20), 
                LocalDate.of(2024, 8, 20), 
                LocalDate.of(2024, 8, 21)
            ),
            new ArrayList<>(timeSummariesResult.keySet()));
    }

    @Test
    public void testCalculateSummaryPerDaySumsUpEntriesOfTheSameDay() {
        List<Entry> entries = List.of(
            entry("2024-08-20T08:00:00", "2024-08-20T12:00:00"),
            entry("2024-08-20T13:00:00", "2024-08-20T17:30:15"));

        Map<LocalDate, String> timeSummariesResult = timeSummaryService.calculateSummaryPerDay(entries);

        assertEquals(Map.of(LocalDate.of(2024, 8, 20), "08:30:15"), timeSummariesResult);
    }

    @Test
    public void testCalculateSummaryPerDayWithoutEntries() {
        assertTrue(timeSummaryService.calculateSummaryPerDay(List.of()).isEmpty());
    }

    private Entry entry(String checkIn, String checkOut) {
        Entry entry = new Entry();
        entry.setCheckIn(LocalDateTime.parse(checkIn));
        entry.setCheckOut(LocalDateTime.parse(checkOut));
        return entry;
    }
}
