package site.nomoreparties.stellarburgers.order;
import static org.apache.http.HttpStatus.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.user.NewUser;
import site.nomoreparties.stellarburgers.user.UserClient;
import site.nomoreparties.stellarburgers.user.UserParams;
import static org.hamcrest.CoreMatchers.is;

public class OrderGetingTest {
    private OrderClient orderClient;
    private UserClient userClient;
    private NewUser createdUser = UserParams.createdUser();
    private String userAccessToken;
    Order orderWithIngredients = Order.orderWithIngredients();

    @Before
    @Description("Авторизовываемся пользователем и создаем ему заказ")
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        ValidatableResponse validatableResponse = userClient.authUser(createdUser);
        userAccessToken = validatableResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Получаем список заказов пользователя")
    public void gettingOrdersList() {
        orderClient.createOrder(orderWithIngredients, userAccessToken);
        ValidatableResponse validatableResponse = orderClient.getOrdersListWithAuth(userAccessToken).log().all();
        Assert.assertEquals(true, validatableResponse.extract().path("success"));
    }

    @Test
    @DisplayName("Попытка получить список заказов без авторизации")
    public void tryToGetOrdersListWithoutAuth() {
        orderClient.getOrdersListWithoutAuth().assertThat().statusCode(SC_UNAUTHORIZED).body("success", is(false));

    }
}
