package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.huizisoft.selenium.utils.bdd.SeleniumGivenStage;
import org.huizisoft.selenium.utils.bdd.ThenStage;
import org.huizisoft.selenium.utils.bdd.WhenStage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StaleReferenceUtilitiesTest extends ScenarioTest<StaleReferenceUtilitiesTest.Given
        , StaleReferenceUtilitiesTest.When
        , StaleReferenceUtilitiesTest.Then> {

    @Test
    void testGetPossibleExceptionOfElement() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(By.id("button1"));
        when().the_page_is_refreshed()
                .and().the_stale_state_of_a_web_element_is_checked();
        then().a_non_stale_referenced_web_element_is_returned();
    }

    @Test
    void testCheckIfElementHasStaleReference() {
        //TODO: Implement test
    }

    @Test
    void testReturnNonStaleWebElement() {
        //TODO: Implement test
    }

    @Test
    void testGetMaxNumberOfRefreshAttempts() {
        //TODO: implement test
    }

    @Test
    void testSetMaxNumberOfRefreshAttempts() {
        //TODO: implement test
    }

    static class Given extends SeleniumGivenStage<Given> {

    }

    public static class When extends WhenStage<When> {
        @ProvidedScenarioState
        private WebElement webElement;

        public WhenStage<When> the_stale_state_of_a_web_element_is_checked() {
            StaleReferenceUtilities.returnNonStaleWebElement(webElement);
            return self();
        }
    }

    public static class Then extends ThenStage<Then> {
        @ProvidedScenarioState
        private WebElement webElement;

        public Then a_non_stale_referenced_web_element_is_returned() {
            assertTrue(StaleReferenceUtilities.checkIfElementHasAStaleReference(webElement));
            return self();
        }
    }
}