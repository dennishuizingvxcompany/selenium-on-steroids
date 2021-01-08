package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.utils.bdd.SeleniumGivenStage;
import org.huizisoft.selenium.utils.bdd.WhenStage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocatorExtractorTest extends ScenarioTest<SeleniumGivenStage
        , LocatorExtractorTest.When
        , LocatorExtractorTest.Then> {

    @FindBy(css = "button8")
    private WebElement proxyWebElement;

    public LocatorExtractorTest() {
        PageFactory.initElements(SeleniumContext.getDefaultWebDriver(), this);
    }

    private static Stream<Arguments> provideData() {
        return Stream.of(
                Arguments.of(By.className("searchForClassName"), "By.className")
                , Arguments.of(By.linkText("linkText"), "By.linkText")
                , Arguments.of(By.name("nameOfElement"), "By.name")
                , Arguments.of(By.partialLinkText("nkTe"), "By.partialLinkText")
        );
    }

    @Test
    void testExtractorWithToString() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(By.id("button1"));
        when().we_extract_the_locator();
        then().we_should_get_a_by_of_type("By.id");
    }

    @Test
    void testExtractorWithStaleReferenceException() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(By.xpath("//button[@id='button1']"));
        when().the_page_is_refreshed()
                .and().we_extract_the_locator();
        then().we_should_get_a_by_of_type("By.xpath");
    }

    @Test
    void testExtractorWithNoSuchElementException() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_proxy_web_element(proxyWebElement);
        when().we_extract_the_locator();
        then().we_should_get_a_by_of_type("By.cssSelector");
    }

    @Test
    void testExtractorWithNestedLocators() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(By.tagName("selfMadeTagName"));
        when().we_extract_the_locator();
        then().we_should_get_a_by_of_type("By.tagName");
    }

    @ParameterizedTest
    @MethodSource("provideData")
    void testAllMissingLocatorsForCoverage(By usedBy, String expectedBy) {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(usedBy);
        when().we_extract_the_locator();
        then().we_should_get_a_by_of_type(expectedBy);
    }

    static class When extends WhenStage<When> {
        @ExpectedScenarioState
        private WebElement element;
        @ProvidedScenarioState
        private By byOfElement;

        When we_extract_the_locator() {
            byOfElement = LocatorExtractor.extractByFromWebElement(element);
            return self();
        }
    }

    static class Then extends Stage<Then> {
        private static final Logger LOGGER = LogManager.getLogger(Then.class);
        @ExpectedScenarioState
        private By byOfElement;

        Then we_should_get_a_by_of_type(String byTagName) {
            assertNotNull(byOfElement);
            LOGGER.debug(byOfElement.toString());
            assertTrue(byOfElement.toString().startsWith(byTagName));
            return self();
        }

        @AfterScenario
        public void tearDown() {
            SeleniumContext.closeWebDriver();
        }
    }
}