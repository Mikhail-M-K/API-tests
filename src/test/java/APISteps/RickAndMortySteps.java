package APISteps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Затем;
import io.cucumber.java.ru.И;
import io.qameta.allure.Allure;
import io.qameta.allure.Flaky;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import static Specifications.ApiSpecifications.REQUEST_SPECIFICATION_R_A_M;
import static io.restassured.RestAssured.given;

public class RickAndMortySteps {

    public static String characterId, characterRace, characterLocation, personRace, personLocation, characterName, personName;

    public static int lastEpisode, idLastPerson;


    @Дано("^Получить инфо про персонажа по ID '(.*)'$")
    public static void getCharacter(String id) {
        Response gettingCharacter = given()
                .spec(REQUEST_SPECIFICATION_R_A_M)
                .when()
                .get("/character/" + id)
                .then()
                .extract()
                .response();
        JSONObject jsonObject = new JSONObject(gettingCharacter.getBody().asString());
        characterId = jsonObject.get("id").toString();
        characterName = jsonObject.get("name").toString();
        characterRace = jsonObject.get("species").toString();
        characterLocation = jsonObject.getJSONObject("location").get("name").toString();
        Allure.addAttachment("Имя персонажа под ID = " + id, characterName);
        Allure.addAttachment("Раса персонажа под ID = " + id, characterRace);
        Allure.addAttachment("Локация персонажа под ID = " + id, characterLocation);

    }

    @Затем("^Получить последний эпизод с участием выбранного персонажа$")
    public static void getEpisode() {
        Response gettingLastEpisode = given()
                .spec(REQUEST_SPECIFICATION_R_A_M)
                .when()
                .get("/character/" + characterId)
                .then()
                .extract()
                .response();
        int episode = (new JSONObject(gettingLastEpisode.getBody().asString()).getJSONArray("episode").length() - 1);
        lastEpisode = Integer.parseInt(new JSONObject(gettingLastEpisode.getBody().asString())
                .getJSONArray("episode").get(episode).toString().replaceAll("[^0-9]", ""));
        Allure.addAttachment("Последний эпизод где присутствовал " + characterName, lastEpisode + "");
    }

    @Затем("^Получить последнего персонажа в эпизоде$")
    public static void getPerson() {
        Response gettingLastPerson = given()
                .spec(REQUEST_SPECIFICATION_R_A_M)
                .when()
                .get("/episode/" + lastEpisode)
                .then()
                .extract()
                .response();
        int person = (new JSONObject(gettingLastPerson.getBody().asString()).getJSONArray("characters").length() - 1);
        idLastPerson = Integer.parseInt(new JSONObject(gettingLastPerson.getBody().asString())
                .getJSONArray("characters").get(person).toString().replaceAll("[^0-9]", ""));
        Allure.addAttachment("ID последнего персонажа в эпизоде", idLastPerson + "");
    }

    @Затем("^Получить информацию о последнем персонаже$")
    public static void getPersonLast() {
        Response gettingParametersPerson = given()
                .spec(REQUEST_SPECIFICATION_R_A_M)
                .when()
                .get("/character/" + idLastPerson)
                .then()
                .extract()
                .response();
        JSONObject jsonObject = new JSONObject(gettingParametersPerson.getBody().asString());
        personName = jsonObject.get("name").toString();
        personRace = jsonObject.get("species").toString();
        personLocation = jsonObject.getJSONObject("location").get("name").toString();
        Allure.addAttachment("Имя персонажа под ID = " + idLastPerson, characterName);
        Allure.addAttachment("Раса персонажа под ID = " + idLastPerson, characterRace);
        Allure.addAttachment("Локация персонажа под ID = " + idLastPerson, characterLocation);
    }

    @Flaky
    @И("^Сравнить совпадение расы и локаций$")
    public static void checkData() {
        Allure.addAttachment("Персонажи для сравнения (параметры находятся в том же порядке)", personName + " : " + characterName);
        Allure.addAttachment("Расы для сравнения", personRace + " : " + characterRace);
        Allure.addAttachment("Места нахождения для сравнения", personLocation + " : " + characterLocation);
        Assertions.assertEquals(personRace, characterRace,"Расы отличаются");
        Assertions.assertEquals(personLocation, characterLocation, "Места нахождения отличаются");
    }
}
