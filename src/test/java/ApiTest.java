import com.jayway.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.jayway.restassured.RestAssured.*;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import static org.hamcrest.CoreMatchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ApiTest {
    @Test
    @Order(1)
    public void getSecondPosts() {
        when().
                get("http://localhost:3000/posts/2").
        then().
                statusCode(200).
                assertThat().body("author", equalTo("bugofnet"), "id", equalTo(2));
        Response response = get("http://localhost:3000/posts/2");
        LOGGER.info("Second post has data: " + response.getBody().asString());
    }

    @Test
    @Order(2)
    public void postNewPost() {
        given().
                contentType("application/json").body("{\n" +
                "      \"id\": 3,\n" +
                "      \"title\": \"second\",\n" +
                "      \"author\": \"noname\"\n" +
                "    }\n").
         when().
                post("http://localhost:3000/posts").
         then().
                statusCode(201);
        LOGGER.info("New post has been added." + "\n");

        when().
                get("http://localhost:3000/posts/3").
         then().
                statusCode(200).
                assertThat().body("author", equalTo("noname"));
        Response response = get("http://localhost:3000/posts/3");
        LOGGER.info("New post has data: " + response.getBody().asString());
    }

    @Test
    @Order(3)
    public void changeThePost() {
        given().
                contentType("application/json").body("{\n" +
                "      \"id\": 3,\n" +
                "      \"title\": \"third\",\n" +
                "      \"author\": \"BUG\"\n" +
                "    }\n").
        when().
                put("http://localhost:3000/posts/3").
        then().
                statusCode(200);
        LOGGER.info("Post has been edited." + "\n");

        when().
                get("http://localhost:3000/posts/3").
        then().
                statusCode(200).
                assertThat().body("title", equalTo("third"))
                .body("author", equalTo("BUG"));
        Response response = get("http://localhost:3000/posts/3");
        LOGGER.info("New data is : " + response.getBody().asString());
    }

    @Test
    @Order(4)
    public void deleteThePost() {
        when().
                delete("http://localhost:3000/posts/3").
        then().
                statusCode(200).
                assertThat().body("{}", Matchers.nullValue());
        LOGGER.info("Post has been deleted .");
    }

}
