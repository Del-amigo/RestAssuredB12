import io.restassured.http.ContentType;
import org.junit.*;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @BeforeClass
    public static void initCode() {
        baseURI = "http://api.zippopotam.us";
    }

    @Test
    public void getTest() {
        given()
                // prior conditions*
                .when()
                .get( "/us/90210" ) // action
                .then()
                .statusCode( 200 ); // assertion

    }

    @Test
    public void contentTypeTest() {
        given()
                // prior conditions*
                .when()
                .get( "/us/90210" ) // action
                .then()
                .contentType( ContentType.JSON );
    }

    @Test
    public void chainingAssertionsTest() {
        given()
                // prior conditions*
                .when()
                .get( "/us/90210" ) // action
                .then()  // cheks comes after "then()"
                .statusCode( 200 )
                .contentType( ContentType.JSON );
    }

    @Test
    public void logTest() {
        given()
                .log().all() // print out everything about request
                .when()
                .get( "/us/90210" ) // action
                .then()  // cheks comes after "then()"
                .log().all(); // print out everything about response

    }

    @Test
    public void checkingResponseBody() {
        given()
                .when()
                .get( "/us/19116" ) // state zip code
                .then()  // cheks comes after "then()"
                .body( "places[0].state", equalTo( "Pennsylvania" ) );
    }

    @Test
    public void checkingSizeOfBody() {
        given()
                .when()
                .get( "/us/19116" ) // state zip code
                .then()  // cheks comes after "then()"
                .log().body()
                .body( "places", hasSize( 1 ) );
    }

    @Test
    public void chainingTest2() {
        given()
                .when()
                .get( "/us/07652" )
                .then()
                .log().body()
                .statusCode( 200 ) // assertion checks
                .contentType( ContentType.JSON ) // assertion checks
                .body( "places", hasSize( 1 ) )
                .body( "places[0].state", equalTo( "New Jersey" ) )
                .body( "places[0].'place name'", equalTo( "Paramus" ) )
        ;
    }

    @Test
    public void usingPathParameters() {
        given()
                .log().uri()
                .pathParam( "countryCode", "us" )
                .pathParam( "zipCode", "07652" )
                .when()
                .get( "/{countryCode}/{zipCode}" )
                .then()
                .body( "places", hasSize( 1 ) )
        ;
    }

    @Test
    public void testingArrays() {
        given()
                .log().uri()
                .pathParam( "countryCode", "tr" )
                .pathParam( "zipCode", "34840" )
                .when()
                .get( "/{countryCode}/{zipCode}" )
                .then()
                .body( "places", hasSize( 2 ) )
                .body( "places.'place name'", hasItem( "Altintepe Mah." ) )
        ;
    }

    @Test
    public void extractingArrays() {
        List<String> listOfPlaces = given()
                .log().uri()
                .pathParam( "countryCode", "tr" )
                .pathParam( "zipCode", "34840" )
                .when()
                .get( "/{countryCode}/{zipCode}" )
                .then()
                .extract().path( "places.'place name'" );
   //     System.out.println(listOfPlaces);
       Assert.assertTrue(listOfPlaces.contains("Altintepe Mah."));
    }

    @Test
    public void extractingArrays01() {
        List<String> listOfLongtidude = given()
                .log().uri()
                .pathParam( "countryCode", "tr" )
                .pathParam( "zipCode", "34840" )
                .when()
                .get( "/{countryCode}/{zipCode}" )
                .then()
                .extract().path( "places.longitude" );
             System.out.println(listOfLongtidude);
       // Assert.assertTrue(listOfPlaces.contains("Altintepe Mah."));
    }

    @Test
    public void testingArraysNotEmpty() {
        given()
                .log().uri()
                .pathParam("countryCode", "tr")
                .pathParam("zipCode", "34840")
                .when()
                .get("/{countryCode}/{zipCode}")
                .then()
                .log().body()
                .body("places", not(empty()))
        ;
    }

    @Test
    public void extractingPojo() {
        pojo.Location location = given()
                .log().uri()
                .pathParam("countryCode", "tr")
                .pathParam("zipCode", "34840")
                .when()
                .get("/{countryCode}/{zipCode}")
                .then()
                .log().body()
                .extract().as(pojo.Location.class);

        System.out.println(location);
        System.out.println(location.getPostCode());
    }
}
