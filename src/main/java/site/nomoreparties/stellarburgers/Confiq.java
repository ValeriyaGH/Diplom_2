package site.nomoreparties.stellarburgers;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.filter.log.LogDetail;
import static io.restassured.http.ContentType.*;
public class Confiq {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    public static RequestSpecification getSpec(){
        return new RequestSpecBuilder()
            .setContentType(JSON)
            .setBaseUri(BASE_URL)
            .log(LogDetail.ALL)
            .build();

    }
}
