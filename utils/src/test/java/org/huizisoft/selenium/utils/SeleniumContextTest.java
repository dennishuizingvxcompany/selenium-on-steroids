package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.utils.bdd.GivenStage;
import org.huizisoft.selenium.utils.bdd.ThenStage;
import org.huizisoft.selenium.utils.bdd.WhenStage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class SeleniumContextTest extends ScenarioTest<GivenStage, WhenStage, ThenStage> {

    private static final Logger LOGGER = LogManager.getLogger(SeleniumContextTest.class);

    @ProvidedScenarioState
    private final SeleniumContext seleniumContext = SeleniumContext.createInstance();

    @Test
    void verifyCreateInstance() {
        given().justAnAction();
        when().the_selenium_context_is_created(seleniumContext);
        then().the_selenium_context_is_not_$(null);
    }

    @Test
    void getCurrentInstance() {
        when().the_current_instance_is_created_with_new_object_or_not(false);
        then().the_selenium_context_is_not_$(seleniumContext);
    }

    @Test
    void setCurrentInstance() {
        when().the_selenium_context_is_created(seleniumContext)
                .and().the_current_instance_is_set_to_current_selenium_context();
        then().the_selenium_context_is_not_$(null);
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
        when().the_selenium_context_is_created(seleniumContext)
                .and().the_selenium_server_base_url_is_set_to("http://fake.base.url");
        then().the_base_url_is_$("http://fake.base.url");
    }

    @AfterEach
    public void cleanUp() {
        try {
            LOGGER.info("Context not null, closing webdriver.");
            seleniumContext.closeWebDriver();
        } catch (Exception e) {
            LOGGER.error("Could not close webdriver");
        }
    }
}