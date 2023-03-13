package APISteps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Затем;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static Utils.ConfProperties.getProperty;
import static io.restassured.RestAssured.given;

public class ReqresSteps {

    public static String nameJson, jobJson;

    @Дано("^Отправить запрос на создание пользователя$")
    public static void createPerson() throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get(getProperty("pathJSON")))));
        body.put("name", "Tomato");
        body.put("job", "Eat market");
        Response postJson = given()
                .header("Content-type", "application/json")
                .baseUri(getProperty("pageReqres"))
                .body(body.toString())
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract()
                .response();
        JSONObject jsonObject = new JSONObject(postJson.getBody().asString());
        nameJson = jsonObject.get("name").toString();
        jobJson = jsonObject.get("job").toString();
        Allure.addAttachment("Отправленный JSON", body.toString());
        Allure.addAttachment("Ответный JSON", jsonObject.toString());
        Allure.addAttachment("ID созданного пользователя", (jsonObject.get("id")).toString());
        Allure.addAttachment("Время создания профиля", (jsonObject.get("createdAt")).toString());
    }

    @Затем("^Сравнение результатов c '(.*)', '(.*)'$")
    public static void CheckPerson(String name, String job) {
        Allure.addAttachment("Имена для сравнения", nameJson + " : " + name);
        Allure.addAttachment("Места работы для сравнения", jobJson + " : " + job);
        Assertions.assertEquals(nameJson, name, "Fail");
        Assertions.assertEquals(jobJson, job, "Fail");
    }

}
