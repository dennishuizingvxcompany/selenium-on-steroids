package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.junit5.ScenarioTest;
import org.huizisoft.selenium.utils.bdd.SeleniumGivenStage;
import org.huizisoft.selenium.utils.bdd.ThenStage;
import org.huizisoft.selenium.utils.bdd.WhenStage;
import org.huizisoft.selenium.utils.junit.extensions.SeleniumContextTestConditionerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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

    static class When extends WhenStage<When> {
    }

    static class Then extends ThenStage<Then> {
    }

    static class Given extends SeleniumGivenStage<Given> {
    }
}