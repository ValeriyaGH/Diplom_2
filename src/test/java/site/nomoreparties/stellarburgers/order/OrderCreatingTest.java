package site.nomoreparties.stellarburgers.order;
import static org.apache.http.HttpStatus.*;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import site.nomoreparties.stellarburgers.user.NewUser;
import site.nomoreparties.stellarburgers.user.UserClient;
import site.nomoreparties.stellarburgers.user.UserParams;

public class OrderCreatingTest {
Order orderWithIngredients = Order.orderWithIngredients();
Order ordderWithWrongIngredients = Order.orderWithWrongIngredients();
private OrderClient orderClient = new OrderClient();
private UserClient userClient = new UserClient();
private NewUser randomNewUser = UserParams.randomUser();
private String accessToken;
private String errorNoIngredients = "Ingredient ids must be provided";
Order nullOrder = new Order();

@Before
public void setUp(){
    userClient.creatingUser(randomNewUser);
    ValidatableResponse validatableResponse = userClient.authUser(randomNewUser);
    accessToken = validatableResponse.extract().path("accessToken");
}
@Test
@DisplayName("Создание заказа с ингридиентами")
    public void createOrderWithIngredients(){
    ValidatableResponse validatableResponse = orderClient.createOrder(orderWithIngredients,accessToken);
    validatableResponse.assertThat().statusCode(SC_OK).body("success", is(true));
}
@Test
@DisplayName("Попытка создания заказа без ингридиентов")
    public void tryToCreateOrderWithoutIngredients(){
    ValidatableResponse validatableResponse = orderClient.createOrder(nullOrder,accessToken);
    validatableResponse.assertThat().statusCode(SC_BAD_REQUEST).body("message", is(errorNoIngredients));
}
@Test
@DisplayName("Попытка создания заказа с неверным хэшем ингридиентов")
    public void tryToCreateOrderWithWrongIngredients(){
    ValidatableResponse validatableResponse = orderClient.createOrder(ordderWithWrongIngredients, accessToken);
    validatableResponse.assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);

}
@Test
@DisplayName("Попытка создания заказа без авторизации")
//этот тест падает, тк на запрос заказа без авторизации возвращается успешный ответ
//в доке об этом ничего не сказано, но за кем тогда будет закреплен заказ?
    public void tryToCreateOrderWithoutAuth(){
    ValidatableResponse validatableResponse = orderClient.createOrderWithoutAuth(orderWithIngredients);
    validatableResponse.assertThat().statusCode(SC_UNAUTHORIZED).body("success", is(false));
}

}
