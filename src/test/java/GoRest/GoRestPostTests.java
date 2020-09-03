package GoRest;

import GoRest.pojo.Post;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;
import java.util.List;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class GoRestPostTests {

    private String postID;
    @BeforeClass
    public void init(){
        baseURI = "https://gorest.co.in/public-api/posts";
    }

    @Test(enabled = false)
    public void extactingListOfPosts() {
        List<Post> postList = given()
                .when()
                .get()
                .then()
                //        .log().body()
                .extract().response().jsonPath().getList( "data", Post.class );

        for (Post post : postList) {
            System.out.println( post );
        }
//        System.out.println(userList.stream().iterator().next());
    }

    @Test
    public void creatingPost() {
        postID = given()
                .header( "Authorization", "Bearer 80efae0ea8ce342781bdac32a6476ee3f0f3dfb21443b69c445d040620d57147" )
                .contentType( ContentType.JSON )
                .body( "{\n" +
                        "    \"user_id\": 500,\n" +
                        "    \"title\": \"Vulgo deorsum aptus hic patruus cohaero terminatio vulgaris sol.\",\n" +
                        "    \"body\": \"Sodalitas talus annus. Caelestis conor usque. Tempora cupio concedo. Vita dolorem est." +
                        " Cresco antea conservo. Victoria compono vindico. Qui spiculum cicuta. Deorsum adiuvo tabgo. Atrox quod" +
                        " terror. Summisse sub ater. Similique synagoga compello. Conqueror demonstro amoveo. Vulgivagus" +
                        " deserunt validus. Damno virgo curvus. Cum tumultus confugo. Virgo utor aliqua. Rerum angustus vilitas." +
                        " Tego turpis consequuntur. Quibusdam verumtamen solum.\"\n" +
                        "}" )
                .when()
                .post()
                .then()
                .statusCode( 200 )
                .body( "code", equalTo( 201 ) )
                .extract().response().jsonPath().getString( "data.id" );

        System.out.println( postID );
    }

    @Test(enabled = false)
    public void getUserId() {
        int num = 200;
        given()
                .when()
                .header( "Authorization", "Bearer 80efae0ea8ce342781bdac32a6476ee3f0f3dfb21443b69c445d040620d57147" )
                .get( "/" + postID )
                .then()
                .statusCode( num )
                .body( "code", equalTo( num ) )
                .body( "data.id", equalTo( Integer.parseInt( postID ) ) ) // couse it was integer, that's why we do convert
        ;
    }

    @Test(dependsOnMethods = "creatingPost")
    public void updatePostById() {
        given()
                .header( "Authorization", "Bearer 80efae0ea8ce342781bdac32a6476ee3f0f3dfb21443b69c445d040620d57147" )
                .contentType( ContentType.JSON )
                .body( "{\n" +
                        "    \"user_id\": 500,\n" +
                        "    \"title\": \""+randomText()+"\",\n" +
                        "    \"body\": \"Sodalitas talus annus. Caelestis conor usque. Tempora cupio concedo. Vita dolorem est." +
                        " Cresco antea conservo. Victoria compono vindico. Qui spiculum cicuta. Deorsum adiuvo tabgo. Atrox quod" +
                        " terror. Summisse sub ater. Similique synagoga compello. Conqueror demonstro amoveo. Vulgivagus" +
                        " deserunt validus. Damno virgo curvus. Cum tumultus confugo. Virgo utor aliqua. Rerum angustus vilitas." +
                        " Tego turpis consequuntur. Quibusdam verumtamen solum.\"\n" +
                        "}" )
                .when()
                .patch( "/" + postID )
                .then()
                .statusCode( 200 )
                .body( "code", equalTo( 200 ) )
        ;
    }

    @Test(dependsOnMethods = "creatingPost", priority = 1)
    public void deletePostById() {
        given()
                .header( "Authorization", "Bearer 80efae0ea8ce342781bdac32a6476ee3f0f3dfb21443b69c445d040620d57147" )
         //       .contentType( ContentType.JSON )
                .when()
                .delete( "/" + postID )
                .then()
                .statusCode( 200 )
                .body( "code", equalTo( 204 ) )
        ;
    }

    @Test(dependsOnMethods = "deletePostById")
    public void getPostIdNegative() {
        given()
                .when()
                .header( "Authorization", "Bearer 80efae0ea8ce342781bdac32a6476ee3f0f3dfb21443b69c445d040620d57147" )
                .get( "/" + postID )
                .then()
                .statusCode( 200 )
                .body( "code", equalTo( 404 ) )
        ;
    }
    private String randomText() {
        return RandomStringUtils.randomAlphabetic( 15 );
    }
}
