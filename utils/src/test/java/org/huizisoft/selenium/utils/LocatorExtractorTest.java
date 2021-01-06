package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocatorExtractorTest extends ScenarioTest<LocatorExtractorTest.Given
        , LocatorExtractorTest.When
        , LocatorExtractorTest.Then> {

    @FindBy(css = "button8")
    private WebElement blaat;

    public LocatorExtractorTest() {
        PageFactory.initElements(SeleniumContext.getDefaultWebDriver(), this);
    }

    @Test
    void testExtractorWithToString() {
        given().we_have_a_selenium_context()
                .and().a_web_element(By.id("button1"));
        when().we_extract_the_locator();
        then().we_should_get_a_by();
    }

    @Test
    void testExtractorWithStaleReferenceException() {
        given().we_have_a_selenium_context()
                .and().a_web_element(By.xpath("//button[@id='button1']"));
        when().the_page_is_refreshed()
                .and().we_extract_the_locator();
        then().we_should_get_a_by();
    }

    @Test
    void testExtractorWith() {
        given().we_have_a_selenium_context()
                .and().a_web_element_that_dont_exist(blaat);
        when().we_extract_the_locator();
        then().we_should_get_a_by();
    }

    static class Given extends Stage<Given> {
        private static final String DEFAULT_URL = "http://tomcat:8080/testapp/";
        private static final Logger LOGGER = LogManager.getLogger(Given.class);

        @ProvidedScenarioState
        private WebElement element;

        Given we_have_a_selenium_context() {
            SeleniumContext.getCurrentInstance(true);
            SeleniumContext.getDefaultWebDriver().get(DEFAULT_URL);
            return self();
        }

        Given a_web_element(By locator) {
            element = SeleniumContext.getDefaultWebDriver().findElement(locator);
            BasePage.waitForElementPresentAndDisplayed(element);
            return self();
        }

        Given a_web_element_that_dont_exist(WebElement webElement) {
            element = webElement;
            return self();
        }
    }

    static class When extends Stage<When> {
        private static final Logger LOGGER = LogManager.getLogger(When.class);
        @ExpectedScenarioState
        private WebElement element;
        @ExpectedScenarioState
        private By byOfElement;

        When we_extract_the_locator() {
            byOfElement = LocatorExtractor.extractByFromWebElement(element);
            return self();
        }

        When the_page_is_refreshed() {
            SeleniumContext.getDefaultWebDriver().navigate().refresh();
            return self();
        }
    }

    static class Then extends Stage<Then> {
        private static final Logger LOGGER = LogManager.getLogger(Then.class);
        @ExpectedScenarioState
        private By byOfElement;

        Then we_should_get_a_by() {
            assertNotNull(byOfElement);
            LOGGER.debug(byOfElement.toString());
            return self();
        }

        @AfterScenario
        public void tearDown() {
            SeleniumContext.closeWebDriver();
        }
    }
}