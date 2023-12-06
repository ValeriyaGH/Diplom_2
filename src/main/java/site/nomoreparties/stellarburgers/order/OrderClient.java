package site.nomoreparties.stellarburgers.order;
import static io.restassured.RestAssured.given;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.Confiq;

public class OrderClient {
    public static final String PATH_ORDERS = "/api/orders";
    public static final String PATH_INGREDIENTS = "/api/ingredients";
    private Order order;

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given()
                .spec(Confiq.getSpec())
                .body(order)
                .headers("Authorization", accessToken)
                .when()
                .post(PATH_ORDERS)
                .then();

    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(Confiq.getSpec())
                .body(order)
                .when()
                .post(PATH_ORDERS)
                .then();

    }

    @Step("Получить список возможных ингредиентов")
    public ValidatableResponse getingredientsList() {
        return given()
                .spec(Confiq.getSpec())
                .when()
                .get(PATH_INGREDIENTS)
                .then();
    }

    @Step("Получить список заказов без авторизации")
    public ValidatableResponse getOrdersListWithoutAuth() {
        return given()
                .spec(Confiq.getSpec())
                .when()
                .get(PATH_ORDERS)
                .then();
    }

    @Step("Получить список заказов с авторизацией")
    public ValidatableResponse getOrdersListWithAuth(String accessToken) {
        return given()
                .spec(Confiq.getSpec())
                .headers("Authorization", accessToken)
                .when()
                .get(PATH_ORDERS)
                .then();

    }

}

