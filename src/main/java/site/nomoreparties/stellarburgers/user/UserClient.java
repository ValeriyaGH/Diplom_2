package site.nomoreparties.stellarburgers.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import site.nomoreparties.stellarburgers.Confiq;

import static io.restassured.RestAssured.given;
public class UserClient {
    NewUser newUser;
    CreatedUser createdUser;
    public static final String PATH_CREATING_USER = "/api/auth/register";
    public static final String PATH_AUTH_USER = "/api/auth/login";
    public static final String PATH_GET_OR_CHANGE_USER_INFO = "/api/auth/user";


    @Step("Создание пользователя")
    public ValidatableResponse creatingUser(NewUser newUser) {
        return given()
                .spec(Confiq.getSpec())
                .body(newUser)
                .when()
                .post(PATH_CREATING_USER)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(Confiq.getSpec())
                .headers("Authorization", accessToken)
                .when()
                .delete(PATH_GET_OR_CHANGE_USER_INFO)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse authUser(NewUser newUser) {
        createdUser = new CreatedUser(newUser.getEmail(), newUser.getPassword());
        return given()
                .spec(Confiq.getSpec())
                .body(createdUser)
                .when()
                .post(PATH_AUTH_USER)
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse changeUser(String accessToken, NewUser newUser) {
        return given()
                .spec(Confiq.getSpec())
                .body(newUser)
                .headers("Authorization", accessToken)
                .when()
                .patch(PATH_GET_OR_CHANGE_USER_INFO)
                .then();
    }

    @Step("Получение данных пользователя")
    public ValidatableResponse getUserInfo(String accessToken) {
        return given()
                .spec(Confiq.getSpec())
                .headers("Authorization", accessToken)
                .when()
                .get(PATH_GET_OR_CHANGE_USER_INFO)
                .then();
    }

    @Step("Попытка смены данных неавторизованным пользователем")
    public ValidatableResponse changeUserWithoutAuth(NewUser newUser) {
        return given()
                .spec(Confiq.getSpec())
                .body(newUser)
                .when()
                .patch(PATH_GET_OR_CHANGE_USER_INFO)
                .then();
    }

}
