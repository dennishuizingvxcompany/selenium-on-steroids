package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.huizisoft.selenium.utils.bdd.SeleniumGivenStage;
import org.huizisoft.selenium.utils.bdd.ThenStage;
import org.huizisoft.selenium.utils.bdd.WhenStage;
import org.huizisoft.selenium.utils.junit.extensions.SeleniumContextTestConditionerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SeleniumContextTestConditionerExtension.class)
class SeleniumContextTest extends ScenarioTest<SeleniumContextTest.Given, SeleniumContextTest.When, SeleniumContextTest.Then> {

    @Test
    void verifyCreateInstanceTest() {
        given().the_selenium_context_is_created();
        then().verify_the_selenium_context_is_empty$(false);
    }

    @Test
    void getCurrentInstance() {
        when().the_web_driver_is_closed()
                .and().the_current_selenium_context_instance_is_created_with_new_object_or_not(false);
        then().verify_the_selenium_context_is_empty$(true);
    }

    @Test
    void getCurrentInstanceWithNewObject() {
        when().the_current_selenium_context_instance_is_created_with_new_object_or_not(true);
        then().verify_the_selenium_context_is_empty$(false);
    }

    @Test
    void setCurrentInstance() {
        given().the_selenium_context_is_created();
        when().the_current_selenium_contxt_instance_is_set_to_current_selenium_context();
        then().verify_the_selenium_context_is_empty$(false);
    }

    @Test
    void closeWebDriver() {
        given().the_selenium_context_is_created();
        when().the_web_driver_is_closed();
        then().verify_the_selenium_context_is_empty$(true)
                .and().verify_the_web_driver_is_running(false);
    }

    @Test
    void isWebDriverRunning() {
        given().the_selenium_context_is_created();
        then().verify_the_web_driver_is_running(true);
    }

    @Test
    void setSeleniumServerBaseUrlAndGetTheNewBaseUrl() {
        given().the_selenium_context_is_created();
        when().the_selenium_server_base_url_is_set_to("http://fake.base.url");
        then().verify_the_base_url_is_$("http://fake.base.url");
    }

    @Test
    void testDefaultCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setAcceptInsecureCerts(true);
        desiredCapabilities.setJavascriptEnabled(true);
        desiredCapabilities.setBrowserName("chrome");

        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp();
        when().the_default_capabilities_are_being_retrieved();
        then().the_expected_capabilities_are(desiredCapabilities);
    }

    @Test
    void testClearCurrentContext() {
        given().the_selenium_context_is_created();
        when().the_current_selenium_context_is_cleared();
        then().verify_the_web_driver_is_running(false);
    }


    @Test
    void testRestartOfWebDriverAfterScenario() {
        given().the_selenium_context_is_created();
        when().the_restart_of_scenarios_is_set_to_value(1);
        then().then_the_value_of_restart_after_scenarios_should_be_$(1);
    }


    static class When extends WhenStage<When> {
        @ProvidedScenarioState
        private Capabilities capabilities;

        When the_default_capabilities_are_being_retrieved() {
            capabilities = SeleniumContext.getDefaultWebDriver().getCapabilities();
            return self();
        }

        When the_current_selenium_context_is_cleared() {
            SeleniumContext.clearCurrentInstance();
            return self();
        }

        When the_restart_of_scenarios_is_set_to_value(int numberOfScenariosBeforeRestart) {
            SeleniumContext.setRestartWebDriverAfterScenarios(numberOfScenariosBeforeRestart);
            return self();
        }
    }

    static class Then extends ThenStage<Then> {
        @ExpectedScenarioState
        private Capabilities capabilities;

        Then the_expected_capabilities_are(Capabilities expected) {
            assertEquals(expected.getBrowserName(), capabilities.getBrowserName());
            assertEquals(expected.getCapability("javascriptEnabled"), capabilities.getCapability("javascriptEnabled"));
            assertEquals(expected.getCapability("acceptInsecureCerts"), capabilities.getCapability("acceptInsecureCerts"));
            return self();
        }

        Then then_the_value_of_restart_after_scenarios_should_be_$(int expectedValue) {
            assertEquals(expectedValue, SeleniumContext.getRestartWebDriverAfterScenarios());
            return self();
        }
    }

    static class Given extends SeleniumGivenStage<Given> {

    }
}