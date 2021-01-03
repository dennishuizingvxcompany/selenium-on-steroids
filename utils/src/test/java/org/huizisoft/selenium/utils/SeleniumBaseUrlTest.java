package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SeleniumBaseUrlTest extends ScenarioTest<SeleniumBaseUrlTest.Given
        , SeleniumBaseUrlTest.When
        , SeleniumBaseUrlTest.Then> {

    @Test
    void testSetSeleniumServerBaseUrl() {
        given().we_set_the_selenium_server_base_url("http://some.url/path");
        when().we_retrieve_the_set_selenium_server_base_url();
        then().we_can_verify_the_value("http://some.url/path");
    }

    @Test
    void testIfNoDefaultSystyemPropertyIsSet() {
        given().there_is_no_system_property("seleniumServerBaseUrl")
                .and().we_set_the_selenium_server_base_url("");
        when().we_retrieve_the_set_selenium_server_base_url();
        then().we_can_verify_the_value(SeleniumBaseUrl.getDefaultSeleniumServerBaseUrlValue())
                .and().we_bring_back_property_in_old_state("seleniumServerBaseUrl");
    }

    static class Given extends Stage<Given> {
        @ProvidedScenarioState
        private String oldValueOfSystemProperty;

        Given we_set_the_selenium_server_base_url(String url) {
            SeleniumBaseUrl.setSeleniumServerBaseUrl(url);
            return self();
        }

        Given there_is_no_system_property(String seleniumServerBaseUrl) {
            oldValueOfSystemProperty = System.getProperty(seleniumServerBaseUrl);
            System.clearProperty(seleniumServerBaseUrl);
            return self();
        }
    }

    static class When extends Stage<When> {
        @ProvidedScenarioState
        private String retrievedUrl;

        When we_retrieve_the_set_selenium_server_base_url() {
            retrievedUrl = SeleniumBaseUrl.getUrl();
            return self();
        }
    }

    static class Then extends Stage<Then> {
        @ExpectedScenarioState
        private String retrievedUrl;

        @ExpectedScenarioState
        private String oldValueOfSystemProperty;

        Then we_can_verify_the_value(String expectedUrl) {
            assertEquals(expectedUrl, retrievedUrl);
            return self();
        }

        Then we_bring_back_property_in_old_state(String seleniumServerBaseUrl) {
            if (oldValueOfSystemProperty != null) {
                System.setProperty(seleniumServerBaseUrl, oldValueOfSystemProperty);
            }
            return self();
        }
    }
}