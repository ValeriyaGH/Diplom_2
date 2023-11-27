package site.nomoreparties.stellarburgers.user;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class UserChangingTest {
    private UserClient userClient;
    private String userAccessToken;
    private String errorMessageShouldBeAuthorised = "You should be authorised";
    private NewUser user = UserParams.randomUser();
    private NewUser newUserData = UserParams.randomUser();

@Before
@Description("Регистрируем пользователя и получаем токен")
public void setUp(){
    userClient = new UserClient();
    userClient.creatingUser(user);
    userAccessToken = userClient.authUser(user).extract().path("accessToken");
}

    @Test
    @DisplayName("Смена данных у авторизованного пользователя")
    @Description("Меняем данные, проверяем статус ответа и само тело ответа,сравниваем его с переданными данными")
    public void changeUserInfo(){
        ValidatableResponse validatableResponse = userClient.changeUser(userAccessToken, newUserData).assertThat().statusCode(SC_OK).body("success", is(true)).log().all();
        //если добавить проверку на соответствие переданного email и email в теле ответа,
        //тест падает тк в теле ответа он всегда в нижнем регистре
        Assert.assertEquals(newUserData.getEmail(), validatableResponse.extract().path("user.email"));
        Assert.assertEquals(newUserData.getName(), validatableResponse.extract().path("user.name"));
    }
    @Test
    @DisplayName("Попытка сменить данные без авторизации")
    public void tryToChangeUserInfoWithoutAuth(){
        userClient.changeUserWithoutAuth(newUserData).assertThat().statusCode(SC_UNAUTHORIZED).body("message", is(errorMessageShouldBeAuthorised));

}
@After
@Description("Удаляем созданного пользователя")
public void deleteCreatedUser(){
    userClient.deleteUser(userAccessToken).assertThat().statusCode(SC_ACCEPTED);
}
}
