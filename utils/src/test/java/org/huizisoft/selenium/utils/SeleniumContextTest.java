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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SeleniumContextTestConditionerExtension.class)
class SeleniumContextTest extends ScenarioTest<SeleniumContextTest.Given, SeleniumContextTest.When, SeleniumContextTest.Then> {

    @Test
    void verifyCreateInstanceTest() {
        given().the_selenium_context_is_created();
        then().verify_the_selenium_context_is_empty_$(false);
    }

    @Test
    void getCurrentInstance() {
        when().the_web_driver_is_closed()
                .and().the_current_selenium_context_instance_is_created_with_new_object_or_not_$(false);
        then().verify_the_selenium_context_is_empty_$(true);
    }

    @Test
    void getCurrentInstanceWithNewObject() {
        when().the_current_selenium_context_instance_is_created_with_new_object_or_not_$(true);
        then().verify_the_selenium_context_is_empty_$(false);
    }

    @Test
    void setCurrentInstance() {
        given().the_selenium_context_is_created();
        when().the_current_selenium_contxt_instance_is_set_to_current_selenium_context();
        then().verify_the_selenium_context_is_empty_$(false);
    }

    @Test
    void closeWebDriver() {
        given().the_selenium_context_is_created();
        when().the_web_driver_is_closed();
        then().verify_the_selenium_context_is_empty_$(true)
                .and().verify_the_web_driver_is_running_$(false);
    }

    @Test
    void isWebDriverRunning() {
        given().the_selenium_context_is_created();
        then().verify_the_web_driver_is_running_$(true);
    }

    @Test
    void setSeleniumServerBaseUrlAndGetTheNewBaseUrl() {
        given().the_selenium_context_is_created();
        when().the_selenium_server_base_url_is_set_to_$("http://fake.base.url");
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
        then().the_expected_capabilities_are_$(desiredCapabilities);
    }

    @Test
    void testClearCurrentContext() {
        given().the_selenium_context_is_created();
        when().the_current_selenium_context_is_cleared();
        then().verify_the_web_driver_is_running_$(false);
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void testRestartOfWebDriverAfterScenario(int input) {
        given().the_selenium_context_is_created();
        when().the_restart_of_scenarios_is_set_to_value_$(input);
        then().then_the_value_of_restart_after_scenarios_should_be_$(input);
    }

    @Test
    void testRestartOfWebDriverAfterScenarioMayNotBeZero() {
        given().the_selenium_context_is_created();
        when().the_restart_of_scenarios_is_set_to_value_$(-1);
        then().we_should_expect_exception_$(new IllegalArgumentException("restartWebDriverAfterScenarios must be >= 0"));
    }

    static class When extends WhenStage<When> {
        @ProvidedScenarioState
        private Capabilities capabilities;
        @ProvidedScenarioState
        private Exception exception;

        When the_default_capabilities_are_being_retrieved() {
            capabilities = SeleniumContext.getDefaultWebDriver().getCapabilities();
            return self();
        }

        When the_current_selenium_context_is_cleared() {
            SeleniumContext.clearCurrentInstance();
            return self();
        }

        When the_restart_of_scenarios_is_set_to_value_$(int numberOfScenariosBeforeRestart) {
            try {
                SeleniumContext.setRestartWebDriverAfterScenarios(numberOfScenariosBeforeRestart);
            } catch (IllegalArgumentException e) {
                exception = e;
            }
            return self();
        }
    }

    static class Then extends ThenStage<Then> {
        @ExpectedScenarioState
        private Capabilities capabilities;
        @ProvidedScenarioState
        private Exception exception;

        Then the_expected_capabilities_are_$(Capabilities expected) {
            assertEquals(expected.getBrowserName(), capabilities.getBrowserName());
            assertEquals(expected.getCapability("javascriptEnabled"), capabilities.getCapability("javascriptEnabled"));
            assertEquals(expected.getCapability("acceptInsecureCerts"), capabilities.getCapability("acceptInsecureCerts"));
            return self();
        }

        Then then_the_value_of_restart_after_scenarios_should_be_$(int expectedValue) {
            assertEquals(expectedValue, SeleniumContext.getRestartWebDriverAfterScenarios());
            return self();
        }

        Then we_should_expect_exception_$(IllegalArgumentException e) {
            assertEquals(e.getClass(), exception.getClass());
            assertEquals(e.getMessage(), exception.getMessage());
            return self();
        }
    }

    static class Given extends SeleniumGivenStage<Given> {

    }
}