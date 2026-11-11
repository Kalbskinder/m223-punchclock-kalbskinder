package ch.zli.m223;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

@QuarkusTest
public class AuthResourceTest {

    private static final String CREDENTIALS = "{\"email\":\"Anna@Test.ch\",\"password\":\"geheim123\"}";

    @Test
    public void testRegisterLoginAndAuthorization() {
        // Without token -> 401
        given().when().get("/entries").then().statusCode(401);

        // Register
        given().contentType("application/json").body(CREDENTIALS)
            .when().post("/register")
            .then().statusCode(200)
            .body("email", is("anna@test.ch"))
            .body("role", is("User"))
            .body("passwordHash", nullValue());

        // Same email again -> 409
        given().contentType("application/json").body(CREDENTIALS)
            .when().post("/register").then().statusCode(409);

        // Invalid input -> 400
        given().contentType("application/json").body("{\"email\":\"kein-mail\",\"password\":\"kurz\"}")
            .when().post("/register").then().statusCode(400);

        // Wrong password / unknown user -> 401
        given().contentType("application/json").body("{\"email\":\"anna@test.ch\",\"password\":\"falsch123\"}")
            .when().post("/login").then().statusCode(401);
        given().contentType("application/json").body("{\"email\":\"niemand@test.ch\",\"password\":\"geheim123\"}")
            .when().post("/login").then().statusCode(401);

        // Login
        String token = given().contentType("application/json").body(CREDENTIALS)
            .when().post("/login")
            .then().statusCode(200)
            .body("token", notNullValue())
            .extract().jsonPath().getString("token");

        // With token: users may read
        given().auth().oauth2(token).when().get("/entries").then().statusCode(200);
        given().auth().oauth2(token).when().get("/categories").then().statusCode(200);

        // ...but only admins may change categories -> 403
        given().auth().oauth2(token).contentType("application/json").body("{\"title\":\"Test\"}")
            .when().post("/categories").then().statusCode(403);

        // Manipulated token -> 401
        given().auth().oauth2(token + "x").when().get("/entries").then().statusCode(401);
    }
}
