package ru.tsu.hits.kosterror.palindromepartitioning.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;
import ru.tsu.hits.kosterror.palindromepartitioning.PalindromePartitioningApplication;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PalindromePartitioningControllerUITest {

    private static final String inputPageUrl = "http://localhost:8080/";
    private static final String errorPageTitle = "Ошибка";
    private static ChromeOptions chromeOptions;
    private WebDriver driver;

    @BeforeAll
    static void setup() {
        SpringApplication.run(PalindromePartitioningApplication.class);
        chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
    }

    @BeforeEach
    void beforeEach() {
        driver = new ChromeDriver(chromeOptions);
    }

    @AfterEach
    void afterEach() {
        driver.quit();
    }

    @ParameterizedTest
    @MethodSource("inputAndExpectedResult")
    void inputPage_correctInput_returnExpectedResult(String inputString, String expectedResultString) {
        driver.get(inputPageUrl);
        WebElement inputElement = driver.findElement(By.id("input"));
        inputElement.sendKeys(inputString);
        WebElement submitElement = driver.findElement(By.id("submit-button"));
        submitElement.click();

        WebElement resultElement = driver.findElement(By.id("result"));
        String resultString = resultElement.getText();

        assertEquals(expectedResultString, resultString);
    }

    @Test
    void inputPage_emptyInput_doesntGoNext() {
        driver.get(inputPageUrl);
        WebElement submitElement = driver.findElement(By.id("submit-button"));
        submitElement.click();

        String currentUrl = driver.getCurrentUrl();
        assertEquals(inputPageUrl, currentUrl);
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "кириллица", "異体字", "ökonom", "engыeng", "Aaaa", "aaaA", "a1",
            "1a", "aaaaaaaaaaaaaaaaa"})
    void inputPage_incorrectInput_moveToBadRequestPage(String inputString) {
        driver.get(inputPageUrl);

        WebElement inputElement = driver.findElement(By.id("input"));
        inputElement.sendKeys(inputString);
        WebElement submitElement = driver.findElement(By.id("submit-button"));
        submitElement.click();
        String title = driver.getTitle();

        assertEquals(errorPageTitle, title);
    }

    private static Stream<Arguments> inputAndExpectedResult() {
        return Stream.of(
                Arguments.of("a", "[[a]]"),
                Arguments.of("aa", "[[a, a], [aa]]"),
                Arguments.of("ab", "[[a, b]]"),
                Arguments.of("aaaaaaa", "[[a, a, a, a, a, a, a], [a, a, a, a, a, aa], [a, a, a, a, aa" +
                        ", a], [a, a, a, a, aaa], [a, a, a, aa, a, a], [a, a, a, aa, aa], [a, a, a, aaa, a], [a," +
                        " a, a, aaaa], [a, a, aa, a, a, a], [a, a, aa, a, aa], [a, a, aa, aa, a], [a, a, aa, aaa]" +
                        ", [a, a, aaa, a, a], [a, a, aaa, aa], [a, a, aaaa, a], [a, a, aaaaa], [a, aa, a, a, a, " +
                        "a], [a, aa, a, a, aa], [a, aa, a, aa, a], [a, aa, a, aaa], [a, aa, aa, a, a], [a, aa, a" +
                        "a, aa], [a, aa, aaa, a], [a, aa, aaaa], [a, aaa, a, a, a], [a, aaa, a, aa], [a, aaa, aa" +
                        ", a], [a, aaa, aaa], [a, aaaa, a, a], [a, aaaa, aa], [a, aaaaa, a], [a, aaaaaa], [aa, a" +
                        ", a, a, a, a], [aa, a, a, a, aa], [aa, a, a, aa, a], [aa, a, a, aaa], [aa, a, aa, a, a]" +
                        ", [aa, a, aa, aa], [aa, a, aaa, a], [aa, a, aaaa], [aa, aa, a, a, a], [aa, aa, a, aa], " +
                        "[aa, aa, aa, a], [aa, aa, aaa], [aa, aaa, a, a], [aa, aaa, aa], [aa, aaaa, a], [aa, aaa" +
                        "aa], [aaa, a, a, a, a], [aaa, a, a, aa], [aaa, a, aa, a], [aaa, a, aaa], [aaa, aa, a, a" +
                        "], [aaa, aa, aa], [aaa, aaa, a], [aaa, aaaa], [aaaa, a, a, a], [aaaa, a, aa], [aaaa, aa" +
                        ", a], [aaaa, aaa], [aaaaa, a, a], [aaaaa, aa], [aaaaaa, a], [aaaaaaa]]"),
                Arguments.of("qwertyuiopasdfgh", "[[q, w, e, r, t, y, u, i, o, p, a, s, d, f, g, h]]")
        );
    }

}
