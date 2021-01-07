package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.junit5.ScenarioTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.utils.bdd.ThenStage;
import org.huizisoft.selenium.utils.bdd.WhenStage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class SeleniumContextTest extends ScenarioTest<SeleniumGivenStage, WhenStage, ThenStage> {
    private static final Logger LOGGER = LogManager.getLogger(SeleniumContextTest.class);

    @Test
    @SuppressWarnings("java:S2699")
    void verifyCreateInstanceTest() {
        given().the_selenium_context_is_created();
        then().the_selenium_context_is_empty$(false);
    }

    @Test
    @SuppressWarnings("java:S2699")
    void getCurrentInstance() {
        when().the_current_instance_is_created_with_new_object_or_not(false);
        then().the_selenium_context_is_empty$(true);
    }

    @Test
    @SuppressWarnings("java:S2699")
    void getCurrentInstanceWithNewObject() {
        when().the_current_instance_is_created_with_new_object_or_not(true);
        then().the_selenium_context_is_empty$(false);
    }

    @Test
    @SuppressWarnings("java:S2699")
    void setCurrentInstance() {
        given().the_selenium_context_is_created();
        when().the_current_instance_is_set_to_current_selenium_context();
        then().the_selenium_context_is_empty$(false);
    }

    @Test
    @SuppressWarnings("java:S2699")
    void closeWebDriver() {
        given().the_selenium_context_is_created();
        when().the_web_driver_is_closed();
        then().the_selenium_context_is_empty$(true)
                .and().the_web_driver_is_running(false);
    }

    @Test
    @SuppressWarnings("java:S2699")
    void isWebDriverRunning() {
        given().the_selenium_context_is_created();
        then().the_web_driver_is_running(true);
    }

    @Test
    @SuppressWarnings("java:S2699")
    void setSeleniumServerBaseUrlAndGetTheNewBaseUrl() {
        given().the_selenium_context_is_created();
        when().the_selenium_server_base_url_is_set_to("http://fake.base.url");
        then().the_base_url_is_$("http://fake.base.url");
    }

    @AfterEach
    public void cleanUp() {
        try {
            LOGGER.info("Context not null, closing webdriver.");
            SeleniumContext.closeWebDriver();
            SeleniumBaseUrl.setSeleniumServerBaseUrl(SeleniumBaseUrl.getDefaultSeleniumServerBaseUrlValue());
        } catch (Exception e) {
            LOGGER.error("Could not close webdriver");
        }
    }
}