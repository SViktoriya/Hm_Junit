package qa.quru;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import qa.quru.data.People;


import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class JUnitTest {

    static Stream<Arguments> lamodaSearchGoodsForManOrKids() {
        return Stream.of(
                Arguments.of((People.Мужчинам), List.of("Идеи", "Новинки", "Одежда", "Обувь", "Аксессуары", "Бренды", "Premium", "Спорт", "Resale", "Красота", "Дом", "Sale%")),
                Arguments.of((People.Детям), List.of("Новинки", "Девочкам", "Мальчикам", "Малышам", "Premium", "Спорт", "Игрушки", "Дом", "Уход", "Школа", "Sale%"))
        );
    }

    @BeforeAll
    static void setUp() {
        Configuration.holdBrowserOpen = true;
    }

    @DisplayName("Тест на проверку возможности поиска по названию товара")
    @ValueSource(strings = {"Джинсы", "Свитер"})
    @ParameterizedTest(name = "Проверка работы поиска по названию товара {0}")
    void lamodaSearchGoods(String goods) {
        open("https://www.lamoda.ru");
        $("#vue-root > div > div:nth-child(1) > header > div._bottomRow_1jg86_20 > div > div > div > div > input").setValue(goods);
        $("button[type='button']").click();
        $("#vue-root > div > main > div").shouldBe(visible);
    }

    @DisplayName("Тест на проверку результатов поиска по названию товара")
    @CsvSource(value = {
            "Джинсы, Товары по запросу «Джинсы»",
            "Свитер, Товары по запросу «Свитер»"})
    @ParameterizedTest(name = "Проверка результатов поиска по названию товара {0}")
    void lamodaSearchAndCheckGoods(String goods, String resultText) {
        open("https://www.lamoda.ru");
        $("#vue-root > div > div:nth-child(1) > header > div._bottomRow_1jg86_20 > div > div > div > div > input").setValue(goods);
        $("button[type='button']").click();
        $("#vue-root > div > main > div > div._root_5tc8h_2 > div > div > h2").shouldHave(text(resultText));
    }

    @MethodSource("lamodaSearchGoodsForManOrKids")
    @ParameterizedTest(name = "Проверка отображения названия категорий {0}")
    void selenideSiteButtonsText(People people, List<String> buttonsTexts) {
        open("https://www.lamoda.ru");
        $$("nav._root_1o7df_2 a").find(text(people.name())).click();
        $$("nav._root_1416b_2 a").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));
    }

}