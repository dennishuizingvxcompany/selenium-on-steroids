package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.utils.bdd.GivenStage;
import org.huizisoft.selenium.utils.bdd.ThenStage;
import org.huizisoft.selenium.utils.bdd.WhenStage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GlobalHooks.class)
class SeleniumContextTest extends ScenarioTest<GivenStage, WhenStage, ThenStage> {

    private static final Logger LOGGER = LogManager.getLogger(SeleniumContextTest.class);

    @ProvidedScenarioState
    SeleniumContext seleniumContext;

    @Test
    void verifyCreateInstance() {
        given().justAnAction();
        when().the_selenium_context_is_created();
        then().the_selenium_context_is_not_null(null);
    }

    @Test
    void getCurrentInstance() {
        when().the_current_instance_is_created_with_create_with_null(true);
        then().the_selenium_context_is_not_null(null);
    }

    @Test
    void setCurrentInstance() {
    }

    @Test
    void testGetCurrentInstance() {
    }

    @Test
    void clearCurrentInstance() {
    }

    @Test
    void getRestartWebDriverAfterScenarios() {
    }

    @Test
    void setRestartWebDriverAfterScenarios() {
    }

    @Test
    void getSeleniumServerBrowserProfile() {
    }

    @Test
    void setSeleniumServerBrowserProfile() {
    }

    @Test
    void before() {
    }

    @Test
    void after() {
    }

    @Test
    void closeWebDriver() {
    }

    @Test
    void getWebDriver() {
    }

    @Test
    void setWebDriver() {
    }

    @Test
    void testGetWebDriver() {
    }

    @Test
    void findWebDriver() {
    }

    @Test
    void testSetWebDriver() {
    }

    @Test
    void getDesiredCapabilities() {
    }

    @Test
    void setDesiredCapabilities() {
    }

    @Test
    void isWebDriverRunning() {
    }

    @Test
    void getSeleniumServerBaseUrl() {
    }

    @Test
    void setSeleniumServerBaseUrl() {
    }

}