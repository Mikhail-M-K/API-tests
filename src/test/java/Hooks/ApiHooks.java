package Hooks;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

public class ApiHooks {
    private static boolean started = false;

    @BeforeEach
    public void before() {
        if (!started) {
            started = true;
            RestAssured.filters(new AllureRestAssured());
        }
    }
}
