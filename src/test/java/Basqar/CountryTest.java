package Basqar;

import Basqar.Model.Country;
import io.restassured.http.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;
import java.util.*;
import static com.fasterxml.jackson.databind.cfg.ConfigOverride.empty;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;

public class CountryTest {

    private Cookies cookies;
    private String id;
    private String randomName;
    private String code;

    @BeforeClass
    public void init() {
        baseURI = "https://test.basqar.techno.study";
        Map<String, String> body = new HashMap<>();
        body.put( "username", "daulet2030@gmail.com" );
        body.put( "password", "TechnoStudy123@" );

        cookies = given()
                .contentType( ContentType.JSON )
                .body( body )
                .when()
                .post( "/auth/login" )
                .then()
                .statusCode( 200 )
                .extract().response().detailedCookies();
        ;
    }

    @Test
    public void createTest() {
        Country body = new Country();
        randomName = randomText( 8 );
        body.setName( randomName );
        code = randomText( 4 );
        body.setCode( code );

        id = given()
                .cookies( cookies )
                .body( body )
                .contentType( ContentType.JSON )
                .when()
                .post( "/school-service/api/countries" )
                .then()
                .statusCode( 201 )
                .extract().jsonPath().getString( "id" );

        System.out.println(id);
        System.out.println(randomName);
    }

    @Test(dependsOnMethods = "createTest")
    public void searchTest() {
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + randomName + "\"}")
                .when()
                .post("/school-service/api/countries/search")
                .then()
                .statusCode(200)
                .body(not(empty()))
                .body("name", hasItem(randomName))
        ;
    }

    @Test
    public void searchTestNegative() {
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + RandomStringUtils.randomAlphabetic(8) + "\"}")
                .when()
                .post("/school-service/api/countries/search")
                .then()
                .statusCode(200)
                .body(equalTo("[]"))
        ;
    }

    @Test(dependsOnMethods = "createTest")
    public void createTestNegative() {
        Country body = new Country();
        body.setName(randomName);
        body.setCode(RandomStringUtils.randomAlphabetic(4));

        given()
                .cookies(cookies)
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("/school-service/api/countries")
                .then()
                .statusCode(400)
                .body("message", equalTo("The Country with Name \"" +
                        randomName + "\" already exists."));
    }

    @Test(dependsOnMethods = "createTest")
    public void updateTest() {
        Country body = new Country();
        body.setId(id);
        body.setName(RandomStringUtils.randomAlphabetic(8));
        body.setCode(RandomStringUtils.randomAlphabetic(4));

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/school-service/api/countries")
                .then()
                .statusCode(200)
                .body("name", equalTo(body.getName()))
                .body("code", equalTo(body.getCode()))
        ;
    }

    @Test(dependsOnMethods = "createTest", priority = 1)
    public void deleteTest() {
        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/countries/" + id)
                .then()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteTest")
    public void updateTestNegative() {
        Country body = new Country();
        body.setId(id);
        body.setName(RandomStringUtils.randomAlphabetic(8));
        body.setCode(RandomStringUtils.randomAlphabetic(4));

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/school-service/api/countries")
                .then()
                .statusCode(404)
                .body("message", equalTo("Country not found"))
        ;
    }

    @Test(dependsOnMethods = "deleteTest")
    public void searchTestNegativeAfterDelete() {
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + randomName + "\"}")
                .when()
                .post("/school-service/api/countries/search")
                .then()
                .statusCode(200)
                .body(equalTo("[]"))
        ;
    }

    @Test(dependsOnMethods = "deleteTest")
    public void deleteTestNegative() {
        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/countries/" + id)
                .then()
                .statusCode(404)
                .body("message", equalTo("Country not found"))
        ;
    }

    private String randomText(int num) {
        return RandomStringUtils.randomAlphabetic( num );
    }
}
