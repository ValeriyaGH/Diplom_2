package site.nomoreparties.stellarburgers.user;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserAuthTest {
    private UserClient userClient;
    private String userAccessToken;
    private String errorWrongPasswordOrLogin = "email or password are incorrect";
    private NewUser notRegUser = UserParams.randomUser();
    private NewUser randomNewUser = UserParams.randomUser();
    private NewUser wrongPasswordUser = UserParams.wrongPasswordUser();

    @Before
    @Description("Создаем нового пользователя и получаем токен")
    public void setUp() {
        userClient = new UserClient();
        userClient.creatingUser(randomNewUser);
        userAccessToken = userClient.authUser(randomNewUser).extract().path("accessToken");
    }

    @Test
    @DisplayName("Проверяем авторизацию под только что созданным юзером")
    public void authUser() {
        userClient.authUser(randomNewUser).assertThat().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Попытка авторизоваться под неверными данными")
    public void tryToAuthWithWrongData() {
        userClient.authUser(notRegUser).assertThat().statusCode(SC_UNAUTHORIZED).body("message", is(errorWrongPasswordOrLogin));
    }

    @Test
    @DisplayName("Попытка авторизоваться с неверным паролем")
    public void tryToAuthWithWrongPassword() {
        userClient.authUser(wrongPasswordUser).assertThat().statusCode(SC_UNAUTHORIZED).body("message", is(errorWrongPasswordOrLogin));
    }

    @After
    @Description("Удаляем созданного пользователя")
    public void deleteCreatedUser() {
        userClient.deleteUser(userAccessToken).assertThat().statusCode(SC_ACCEPTED);
    }
}
