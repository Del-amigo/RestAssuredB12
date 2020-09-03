import io.restassured.http.ContentType;
import org.junit.*;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Task {

    @BeforeClass
    public static void initCode() {
//        baseURI = "https://httpstat.us";
        baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void firstTask() {
        given()
                .when()
                .get( "/203" )
                .then()
                .statusCode( 203 ) // expect status 203
                .contentType( ContentType.TEXT ) // expect content type text
        ;
    }

    @Test
    public void secondTask() {
        given()
                .when()
                .get( "/418" )
                .then()
                .statusCode( 418 )
                .contentType( ContentType.TEXT )
                .body( equalTo( "418 I'm a teapot" ) )
        ;
    }

    @Test
    public void thirdTask() {
        given()
                .when()
                .get( "/todos/2" )
                .then()
                .statusCode( 200 )
                .contentType( ContentType.JSON )
                .body( "title", equalTo( "quis ut nam facilis et officia qui" ) )
        ;
    }

    @Test
    public void fourthTask() {
        given()
                .when()
                .get( "/todos/2" )
                .then()
                .statusCode( 200 )
                .contentType( ContentType.JSON )
                .body( "completed", equalTo( false ) )
        ;
    }

    @Test
    public void fifthTask() {
        given()
                .when()
                .get( "/todos" )
                .then()
                .statusCode( 200 )
                .contentType( ContentType.JSON )
                .body( "title[2]", equalTo( "fugiat veniam minus" ) )
                .body( "userId[2]", equalTo( 1 ) )
        ;
    }
}
