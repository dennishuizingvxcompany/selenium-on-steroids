package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.huizisoft.selenium.bdd.GivenStage;
import org.huizisoft.selenium.bdd.ThenStage;
import org.huizisoft.selenium.bdd.WhenStage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasePageTest extends ScenarioTest<GivenStage, BasePageTest.When, BasePageTest.Then> {
    @Test
    void testIsPresentAndDisplayed() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp();
        when().base_page_is_present_and_displayed_is_called_with_element(By.id("button1"));
        then().the_element_is_found_on_the_page(true);
    }

    static class When extends WhenStage<When> {
        @ProvidedScenarioState
        private boolean isPresentAndDisplayed;

        public When base_page_is_present_and_displayed_is_called_with_element(By button1) {
            isPresentAndDisplayed = BasePage.isPresentAndDisplayed(button1);
            return this;
        }
    }

    static class Then extends ThenStage<Then> {
        @ProvidedScenarioState
        private boolean isPresentAndDisplayed;

        public Then the_element_is_found_on_the_page(boolean expected) {
            assertEquals(isPresentAndDisplayed, expected);
            return this;
        }
    }
}