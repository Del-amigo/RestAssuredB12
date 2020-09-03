import io.restassured.http.ContentType;
import org.testng.annotations.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TaskAlternativeSolutions {
    @BeforeClass
    public void init(){
//        baseURI="https://httpstat.us";
        baseURI="https://jsonplaceholder.typicode.com/todos";
    }

    @Test
    public void firstMethod(){
        given()
                .when().get("/203")
                .then()
                .statusCode(203)
                .contentType( ContentType.TEXT)
        ;
    }
    @Test
    public void secondMethod(){
        String text="418 I'm a teapot";
        given()
                .log().uri()
                .when().get("/418")
                .then()
                .log().body()
                .statusCode(418)
                .contentType(ContentType.TEXT)
                .body( equalTo(text))

        ;
    }
    @Test
    public void thirdMethod(){
        String title="quis ut nam facilis et officia qui";
        given()
                .log().uri()
                .pathParam("page","2")
                .when()
                .get("/{page}")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo(title))
        ;
    }
    @Test
    public void fourthMethod(){
        given()
                .when()
                .get("/2")
                .then()
                .log().body()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("completed", equalTo(false));
    }

    @Test
    public void fifthMehod(){
        int a=1;
        given()
                .when().get("")
                .then().log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("[2].title",equalTo("fugiat veniam minus"))
                .body("[2].userId",equalTo(a))

        ;
    }
}
