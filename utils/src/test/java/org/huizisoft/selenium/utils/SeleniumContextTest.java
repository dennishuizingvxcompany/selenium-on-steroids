package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.huizisoft.selenium.utils.bdd.GivenStage;
import org.huizisoft.selenium.utils.bdd.ThenStage;
import org.huizisoft.selenium.utils.bdd.WhenStage;
import org.huizisoft.selenium.utils.junit.extensions.SeleniumContextTestConditionerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SeleniumContextTestConditionerExtension.class)
class SeleniumContextTest extends ScenarioTest<GivenStage, SeleniumContextTest.When, SeleniumContextTest.Then> {

    @Test
    void testIfAMallFormedUrlCanBeUsedInTheBaseUrl() {
        given().the_selenium_server_base_url_is_set_to_$("ftttp://wwwwwww.wwwwww.wwwww");
        given().the_selenium_context_is_created();
        when().there_is_an_exception();
        then().we_should_expect_exception_$(new MalformedURLException("unknown protocol: ftttp"));
    }

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
        given().the_selenium_context_is_created()
                .and().the_selenium_server_base_url_is_set_to_$("http://fake.base.url");
        then().verify_the_base_url_is_$("http://fake.base.url");
    }

    @Test
    void testDefaultCapabilities() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp();
        when().the_capabilities_are_being_retrieved();
        then().desired_capability_browser_name("chrome")
                .and().desired_capability_accept_insecure_certificates(true)
                .and().desired_capability_javascript_enabled(true);
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

    @Test
    void testTheDefaultWebDriverWait() {
        given().the_selenium_context_is_created();
        when().we_get_the_wait_of_current_driver();
        then().the_default_web_driver_wait_should_be(new WebDriverWait(SeleniumContext.getDefaultWebDriver(), 1));
    }

    @Test
    void testSetDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities(DesiredCapabilities.chrome());
        desiredCapabilities.setJavascriptEnabled(false);
        desiredCapabilities.setAcceptInsecureCerts(false);

        given().capability_is_being_set_$(desiredCapabilities)
                .and().the_selenium_context_is_created();
        when().the_capabilities_are_being_retrieved();
        then().desired_capability_browser_name("chrome")
                .and().desired_capability_javascript_enabled(true)//apparently this cant be set!
                .and().desired_capability_accept_insecure_certificates(false);
    }

    @Test
    void testGetRemoteWebDriverWithDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName("chrome");
        desiredCapabilities.setJavascriptEnabled(true);
        desiredCapabilities.setAcceptInsecureCerts(false);

        given().the_selenium_context_is_created();
        when().we_invoke_get_remote_webdriver(desiredCapabilities);
        then().we_have_a_remote_webdriver(desiredCapabilities)
                .and().there_is_no_exception_thrown();
    }

    @Test
    void testGetRemoteWebDriverWithoutCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName("chrome");
        desiredCapabilities.setJavascriptEnabled(true);
        desiredCapabilities.setAcceptInsecureCerts(true);

        given().the_selenium_context_is_created();
        when().we_invoke_get_remote_webdriver(null);
        then().we_have_a_remote_webdriver(desiredCapabilities)
                .and().there_is_no_exception_thrown();
    }

    static class When extends WhenStage<When> {
        @ProvidedScenarioState
        private Capabilities capabilities;
        @ProvidedScenarioState
        private Exception exception;
        @ProvidedScenarioState
        private WebDriverWait webDriverWait;
        @ProvidedScenarioState
        private RemoteWebDriver remoteWebDriver;

        When the_capabilities_are_being_retrieved() {
            capabilities = SeleniumContext.getRemoteWebDriver().getCapabilities();
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

        When we_get_the_wait_of_current_driver() {
            webDriverWait = SeleniumContext.getWebDriverWait();
            return self();
        }

        When there_is_an_exception() {
            assertNotNull(exception);
            return self();
        }

        When we_invoke_get_remote_webdriver(DesiredCapabilities desiredCapabilities) {
            try {
                remoteWebDriver = SeleniumContext.getRemoteWebDriver(desiredCapabilities);
            } catch (MalformedURLException e) {
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
        @ProvidedScenarioState
        private WebDriverWait webDriverWait;
        @ExpectedScenarioState
        private RemoteWebDriver remoteWebDriver;

        Then desired_capability_browser_name(String expected) {
            assertEquals(expected, capabilities.getBrowserName());
            return self();
        }

        Then desired_capability_javascript_enabled(boolean expected) {
            assertEquals(expected, capabilities.is("javascriptEnabled"));
            return self();
        }

        Then desired_capability_accept_insecure_certificates(boolean expected) {
            assertEquals(expected, capabilities.is("acceptInsecureCerts"));
            return self();
        }

        Then then_the_value_of_restart_after_scenarios_should_be_$(int expectedValue) {
            assertEquals(expectedValue, SeleniumContext.getRestartWebDriverAfterScenarios());
            return self();
        }

        Then we_should_expect_exception_$(Exception e) {
            assertEquals(e.getClass(), exception.getClass());
            assertEquals(e.getMessage(), exception.getMessage());
            return self();
        }

        Then there_is_no_exception_thrown() {
            assertNull(exception);
            return self();
        }

        Then the_default_web_driver_wait_should_be(WebDriverWait expected) {
            assertEquals(expected.getClass(), webDriverWait.getClass());
            return self();
        }

        Then we_have_a_remote_webdriver(DesiredCapabilities desiredCapabilities) {
            assertNotNull(remoteWebDriver);
            assertEquals(desiredCapabilities.is("javascriptEnabled"), remoteWebDriver.getCapabilities().is("javascriptEnabled"));
            assertEquals(desiredCapabilities.is("acceptInsecureCerts"), remoteWebDriver.getCapabilities().is("acceptInsecureCerts"));
            return self();
        }
    }
}