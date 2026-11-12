package ch.zli.m223;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

@QuarkusTest
@TestSecurity(user = "test@punchclock.ch", roles = "Admin")
public class CategoryTagResourceTest {

    private Long post(String path, String json) {
        return given().contentType("application/json").body(json)
            .when().post(path)
            .then().statusCode(200)
            .extract().jsonPath().getLong("id");
    }

    @Test
    public void testCategoriesTagsAndEntryDetails() {
        Long categoryId = post("/categories", "{\"title\":\"Projekt\"}");
        Long tag1 = post("/tags", "{\"title\":\"Backend\"}");
        Long tag2 = post("/tags", "{\"title\":\"Meeting\"}");

        // Update category / tag
        given().contentType("application/json").body("{\"title\":\"Projektarbeit\"}")
            .when().put("/categories/" + categoryId)
            .then().statusCode(200).body("title", is("Projektarbeit"));
        given().when().get("/tags/" + tag1).then().statusCode(200).body("title", is("Backend"));

        // Validation
        given().contentType("application/json").body("{\"title\":\"\"}")
            .when().post("/tags").then().statusCode(400);

        // Entry with relations
        Long entryId = given().contentType("application/json")
            .body("{\"checkIn\":\"2026-11-05T08:00:00\",\"checkOut\":\"2026-11-05T12:00:00\","
                + "\"description\":\"API umgesetzt\","
                + "\"categoryId\":" + categoryId + ",\"tagIds\":[" + tag2 + "," + tag1 + "]}")
            .when().post("/entries")
            .then().statusCode(200)
            .body("description", is("API umgesetzt"))
            .body("categoryId", is(categoryId.intValue()))
            .body("tagIds", contains(tag1.intValue(), tag2.intValue()))
            .extract().jsonPath().getLong("id");

        // Entry without relations
        Long plainId = post("/entries",
            "{\"checkIn\":\"2026-11-05T13:00:00\",\"checkOut\":\"2026-11-05T17:00:00\"}");

        // Unknown IDs -> 404
        given().contentType("application/json")
            .body("{\"checkIn\":\"2026-11-05T08:00:00\",\"checkOut\":\"2026-11-05T12:00:00\",\"categoryId\":9999}")
            .when().post("/entries").then().statusCode(404);
        given().contentType("application/json")
            .body("{\"checkIn\":\"2026-11-05T08:00:00\",\"checkOut\":\"2026-11-05T12:00:00\",\"tagIds\":[9999]}")
            .when().put("/entries/" + entryId).then().statusCode(404);

        // Entry details
        given().when().get("/entry-details/" + entryId)
            .then().statusCode(200)
            .body("category.id", is(categoryId.intValue()))
            .body("category.title", is("Projektarbeit"))
            .body("tags.title", contains("Backend", "Meeting"));
        given().when().get("/entry-details/" + plainId)
            .then().statusCode(200)
            .body("category", nullValue())
            .body("tags", empty());
        given().when().get("/entry-details/9999").then().statusCode(404);
        given().when().get("/entry-details").then().statusCode(200).body("size()", is(2));

        // Deleting category / tag detaches them from entries
        given().when().delete("/categories/" + categoryId).then().statusCode(204);
        given().when().delete("/tags/" + tag1).then().statusCode(204);
        given().when().get("/entries")
            .then().statusCode(200)
            .body("find { it.id == " + entryId + " }.categoryId", nullValue())
            .body("find { it.id == " + entryId + " }.tagIds", contains(tag2.intValue()));

        // Cleanup, so other tests start with an empty database
        given().when().delete("/entries/" + entryId).then().statusCode(204);
        given().when().delete("/entries/" + plainId).then().statusCode(204);
        given().when().delete("/tags/" + tag2).then().statusCode(204);
        given().when().get("/tags").then().body("size()", is(0));
    }
}
