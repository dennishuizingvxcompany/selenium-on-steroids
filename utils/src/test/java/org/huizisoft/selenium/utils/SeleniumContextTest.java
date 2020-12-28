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
        given().the_selenium_context_is_created(seleniumContext);
        then().the_selenium_context_is_not_$(null);
    }

    @Test
    void getCurrentInstance() {
        when().the_current_instance_is_created_with_new_object_or_not(false);
        then().the_selenium_context_is_not_$(seleniumContext);
    }

    @Test
    void setCurrentInstance() {
        given().the_selenium_context_is_created(seleniumContext);
        when().the_current_instance_is_set_to_current_selenium_context();
        then().the_selenium_context_is_not_$(null);
    }

    @Test
    void closeWebDriver() {
        given().the_selenium_context_is_created(seleniumContext);
        when().the_web_driver_is_closed();
        then().the_selenium_context_is_not_$(null)
                .and().the_web_driver_is_running(false);
    }

    @Test
    void isWebDriverRunning() {
        given().the_selenium_context_is_created(seleniumContext);
        then().the_web_driver_is_running(true);
    }

    @Test
    void setSeleniumServerBaseUrlAndGetTheNewBaseUrl() {
        given().the_selenium_context_is_created(seleniumContext);
        when().the_selenium_server_base_url_is_set_to("http://fake.base.url");
        then().the_base_url_is_$("http://fake.base.url");
    }

    @AfterEach
    public void cleanUp() {
        try {
            LOGGER.info("Context not null, closing webdriver.");
            seleniumContext.closeWebDriver();
            SeleniumServerBaseUrl.setSeleniumServerBaseUrl(SeleniumServerBaseUrl.getDefaultSeleniumServerBaseUrlValue());
        } catch (Exception e) {
            LOGGER.error("Could not close webdriver");
        }
    }
}