package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.huizisoft.selenium.bdd.GivenStage;
import org.huizisoft.selenium.bdd.ThenStage;
import org.huizisoft.selenium.bdd.WhenStage;
import org.huizisoft.selenium.junit.extensions.SeleniumContextTestConditionerExtension;
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

    static class When extends WhenStage<When> {
        @ProvidedScenarioState
        private boolean isPresentAndDisplayed;
        @ExpectedScenarioState
        private WebElement element;

        public When base_page_is_present_and_displayed_is_called_with_element(By button1) {
            isPresentAndDisplayed = BasePage.isPresentAndDisplayed(button1);
            return this;
        }

        public When base_page_is_present_and_displayed_is_called_with_element() {
            isPresentAndDisplayed = BasePage.isPresentAndDisplayed(element);
            return this;
        }
    }

    static class Then extends ThenStage<Then> {
        @ProvidedScenarioState
        private boolean isPresentAndDisplayed;

        public Then the_element_is_found_on_the_page(boolean expected) {
            assertEquals(expected, isPresentAndDisplayed);
            return this;
        }
    }
}