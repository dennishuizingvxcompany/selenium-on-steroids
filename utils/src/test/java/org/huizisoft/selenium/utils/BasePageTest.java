package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.huizisoft.selenium.bdd.GivenStage;
import org.huizisoft.selenium.bdd.ThenStage;
import org.huizisoft.selenium.bdd.WhenStage;
import org.huizisoft.selenium.junit.extensions.SeleniumContextTestConditionerExtension;
import org.huizisoft.selenium.pom.TestPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SeleniumContextTestConditionerExtension.class)
class BasePageTest extends ScenarioTest<GivenStage, BasePageTest.When, BasePageTest.Then> {
    @Test
    void testIsPresentAndDisplayed() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp();
        when().base_page_is_present_and_displayed_is_called_with_element(By.id("button1"));
        then().the_element_is_found_on_the_page(true);
    }

    @Test
    void testIsPresentAndDisplayedWithErrorFindingElement() {
        given().the_selenium_context_is_created()
                .and().a_proxy_web_element();
        when().base_page_is_present_and_displayed_is_called_with_element(By.id("button1"))
                .and().the_page_is_refreshed();
        then().the_element_is_found_on_the_page(false);
    }

    @Test
    void testIsPresentAndDisplayedWithWebElement() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(By.id("button1"));
        when().base_page_is_present_and_displayed_is_called_with_element();
        then().the_element_is_found_on_the_page(true);
    }

    @Test
    void testIsPresentAndDisplayedWithWebElementErrorFindingElement() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(By.id("button1"));
        when().the_page_is_refreshed()
                .and().the_page_is_refreshed()
                .and().base_page_is_present_and_displayed_is_called_with_element();
        then().the_element_is_found_on_the_page(true);//TODO: Not getting stale element reference!!
    }

    @Test
    void testWaitForInvisibilityOfElement() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().the_test_page_object_is_being_initialized()
                .and().the_button_is_set_to_hidden();
        when().the_invisibilityOfElement_is_checked();
        then().verify_if_the_visibility_of_current_web_element_is_$(true);
    }

    @Test
    void testWaitForInvisibilityOfElementWhileElementIsPresent() {
        given().the_selenium_context_is_created()
                .and().the_selenium_context_time_out_is_set_to()
                .and().navigate_to_default_testapp()
                .and().the_test_page_object_is_being_initialized();
        when().the_invisibilityOfElement_is_checked();
        then().verify_if_the_visibility_of_current_web_element_is_$(false);
    }

    @Test
    void testIsEnabled() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp();
        when().the_is_enabled_is_checked_for_element(new TestPage().getRadioButton1());
        then().verify_the_enabled_state_is(true);
    }

    @Test
    void testIsNotEnabled() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp();
        when().the_is_enabled_is_checked_for_element(new TestPage().getRadioButton2());
        then().verify_the_enabled_state_is(false);
    }

    @Test
    void testIsEnabledForNonExistingElement() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp();
        when().the_is_enabled_is_checked_for_element(new TestPage().getNonExistingElement());
        then().verify_the_enabled_state_is(false);
    }

    @Test
    void testIsTextInElementPresent() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(By.id("getTextOfElement"));
        when().the_user_calls_is_text_in_element_present("text within an element");
        then().verify_the_value_of_the_element(true);
    }

    @Test
    void testWaitUntilTextInElementPresent() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(By.id("getTextOfElement"));
        when().the_user_calls_wait_until_text_in_element_present("Updated text of element");
        then().verify_the_value_of_the_element(true);
    }

    static class When extends WhenStage<When> {
        @ProvidedScenarioState
        private boolean isPresentAndDisplayed;
        @ExpectedScenarioState
        private WebElement element;
        @ProvidedScenarioState
        private boolean expectedStateOfTextPresent;

        public When base_page_is_present_and_displayed_is_called_with_element(By button1) {
            isPresentAndDisplayed = BasePage.isPresentAndDisplayed(button1);
            return self();
        }

        public When base_page_is_present_and_displayed_is_called_with_element() {
            isPresentAndDisplayed = BasePage.isPresentAndDisplayed(element);
            return self();
        }

        public When the_user_calls_is_text_in_element_present(String expectedText) {
            expectedStateOfTextPresent = BasePage.isTextInElementPresent(element, expectedText);
            return self();
        }

        public When the_user_calls_wait_until_text_in_element_present(String text_within_an_element) {
            expectedStateOfTextPresent = BasePage.waitUntilTextInElementPresent(element, text_within_an_element);
            return self();
        }
    }

    static class Then extends ThenStage<Then> {
        @ProvidedScenarioState
        private boolean isPresentAndDisplayed;
        @ProvidedScenarioState
        private boolean expectedStateOfTextPresent;

        public Then the_element_is_found_on_the_page(boolean expected) {
            assertEquals(expected, isPresentAndDisplayed);
            return self();
        }

        public Then verify_the_value_of_the_element(boolean expectedValue) {
            assertEquals(expectedValue, expectedStateOfTextPresent);
            return self();
        }
    }
}