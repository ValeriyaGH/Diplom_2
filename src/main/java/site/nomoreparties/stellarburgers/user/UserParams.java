package site.nomoreparties.stellarburgers.user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserParams {
    public static NewUser randomUser() {
        String email = RandomStringUtils.randomAlphabetic(10, 15) + "@ya.ru";
        String password = RandomStringUtils.randomAlphabetic(10, 15);
        String name = RandomStringUtils.randomAlphabetic(5, 10);
        return new NewUser(email, password, name);
    }

    public static NewUser createdUser() {
        String email = "TestTest1234@ya.ru";
        String password = "Qwerty12345";
        String name = "Тестовый Юзер";
        return new NewUser(email, password, name);
    }

    //Юзер с теми же данными, но неверным паролем
    public static NewUser wrongPasswordUser() {
        String email = "TestTest1234@ya.ru";
        String password = "Qwerty";
        String name = "Тестовый Юзер";
        return new NewUser(email, password, name);
    }

}
