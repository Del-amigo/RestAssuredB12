package GoRest;

import GoRest.pojo.User;
import io.restassured.builder.*;
import io.restassured.http.ContentType;
import io.restassured.specification.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;
import java.util.List;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class GoRestUserTests {

    private String userID;
    private String token;
    private RequestSpecification requestSpec;

    @BeforeClass
    public void init(){
        baseURI = "https://gorest.co.in/public-api/users";
        token = "Bearer 80efae0ea8ce342781bdac32a6476ee3f0f3dfb21443b69c445d040620d57147";
        requestSpec = new RequestSpecBuilder()
                .addHeader( "Authorization", token )
                .setContentType( ContentType.JSON )
                .build();
    }

    @Test(enabled = true)
    public void extactingListOfUsers() {
        List<User> userList = given()
                .when()
                .get(  )
                .then()
                //        .log().body()
                .extract().response().jsonPath().getList( "data", User.class );

        for (User user : userList) {
            System.out.println( user );
        }
//        System.out.println(userList.stream().iterator().next());
    }

    @Test(enabled = true)
    public void extactingSpecificUser() {
        User user = given()
                .when()
                .get( )
                .then()
                .extract().response().jsonPath().getObject( "data[3]", User.class );

        System.out.println( user );
    }

    @Test
    public void creatingUser() {
        userID = given()
                .spec( requestSpec )
                .body( "{\n" +
                        "    \"name\": \"Ramiro Keeling\",\n" +
                        "    \"gender\": \"Male\",\n" +
                        "    \"email\": \"" + randomEmail() + "\",\n" +
                        "    \"status\": \"Active\"\n" +
                        "}" )
                .when()
                .post()
                .then()
                .spec( getResponseSpecGorStatus( 201 ) )
                .extract().response().jsonPath().getString( "data.id" );

        System.out.println( userID );
    }

    @Test(dependsOnMethods = "creatingUser")
    public void getUserId() {
        given()
                .when()
                .header( "Authorization", token )
                .get( "/" + userID )
                .then()
                .spec( getResponseSpecGorStatus( 200 ) )
                .body( "data.id", equalTo( Integer.parseInt( userID ) ) ) // couse it was integer, that's why we do convert
        ;
    }

    @Test(dependsOnMethods = "creatingUser")
    public void updateUserById() {
        String updateString = "Techno Study";
        given()
                .spec( requestSpec )
                .body( "{\n" +
                        "    \"name\": \"" + updateString + "\",\n" +
                        "    \"gender\": \"Male\",\n" +
                        "    \"email\": \"" + randomEmail() + "\",\n" +
                        "    \"status\": \"Active\"\n" +
                        "}" )
                .when()
                .patch( "/" + userID )
                .then()
                .spec( getResponseSpecGorStatus( 200 ) )
                .body( "data.name", equalTo( updateString ) )
        ;
    }

    @Test(dependsOnMethods = "creatingUser", priority = 1)
    public void deleteUserById() {
        given()
                .spec( requestSpec )
                .when()
                .delete( "/" + userID )
                .then()
                .spec( getResponseSpecGorStatus( 204 ) )
        ;
    }

    @Test(dependsOnMethods = "deleteUserById")
    public void getUserIdNegative() {
        given()
                .when()
                .header( "Authorization", token )
                .get( "/" + userID )
                .then()
                .spec( getResponseSpecGorStatus( 404 ) )
        ;
    }

    private String randomEmail() {
        return RandomStringUtils.randomAlphabetic( 8 ) + "@gmail.com";
    }

    private ResponseSpecification getResponseSpecGorStatus(int status){
        return new ResponseSpecBuilder()
                .expectStatusCode( 200 )
                .expectBody( "code",equalTo( status ) )
                .build();
    }

}
