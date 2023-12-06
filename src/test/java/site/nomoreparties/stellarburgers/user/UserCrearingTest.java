package site.nomoreparties.stellarburgers.user;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class UserCrearingTest {
    private UserClient userClient = new UserClient();
    private NewUser randomNewUser = UserParams.randomUser();
    private String errorNotEnoughtData = "Email, password and name are required fields";
    private String userAccessToken;
    private boolean isCreated = false;
    private String errorMessageUserExists = "User already exists";

    @Test
    @DisplayName("Регистрация с рандомными валидными данными")
    public void userCreating() {
        userClient.creatingUser(randomNewUser).assertThat().statusCode(SC_OK).body("success", is(true));
        isCreated = true;
    }

    @Test
    @DisplayName("Попытка регистрации двух юзеров с одинаковыми данными")
    @Description("Проверяем что регистрация первого юзера успешна, затем пытаемся зарегистрировать второго и проверяем текст и код ответа сервера")
    public void tryingToCreateTwoSameUsers() {
        userClient.creatingUser(randomNewUser).assertThat().statusCode(SC_OK);
        userClient.creatingUser(randomNewUser).assertThat().statusCode(SC_FORBIDDEN).body("message", is(errorMessageUserExists));
        isCreated = true;
    }

    @Test
    @DisplayName("Попытка регистрации без пароля")
    public void tryingToCreateUserWithoutPassword() {
        randomNewUser.setPassword(null);
        userClient.creatingUser(randomNewUser).assertThat().statusCode(SC_FORBIDDEN).body("message", is(errorNotEnoughtData));
    }

    @Test
    @DisplayName("Попытка регистрации без email")
    public void tryingToCreateUserWithoutEmail() {
        randomNewUser.setEmail(null);
        userClient.creatingUser(randomNewUser).assertThat().statusCode(SC_FORBIDDEN).body("message", is(errorNotEnoughtData));
    }

    @Test
    @DisplayName("Попытка регистрации без имени")
    public void tryingToCreateUserWithoutName() {
        randomNewUser.setName(null);
        userClient.creatingUser(randomNewUser).assertThat().statusCode(SC_FORBIDDEN).body("message", is(errorNotEnoughtData));
    }

    @After
    @Description("Получаем токен через авторизацию и удаляем юзера")
    public void deleteCreatedUser() {
        if (isCreated) {
            ValidatableResponse validatableResponse = userClient.authUser(randomNewUser);
            userAccessToken = validatableResponse.extract().path("accessToken");
            userClient.deleteUser(userAccessToken).assertThat().statusCode(SC_ACCEPTED);
        }
    }
}
