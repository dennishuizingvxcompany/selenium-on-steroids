package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.huizisoft.selenium.utils.bdd.GivenStage;
import org.huizisoft.selenium.utils.bdd.ThenStage;
import org.huizisoft.selenium.utils.bdd.WhenStage;
import org.huizisoft.selenium.utils.junit.extensions.SeleniumContextTestConditionerExtension;
import org.huizisoft.selenium.utils.junit.extensions.StaleReferenceUtilityExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({SeleniumContextTestConditionerExtension.class, StaleReferenceUtilityExtension.class})
class StaleReferenceUtilitiesTest extends ScenarioTest<GivenStage
        , StaleReferenceUtilitiesTest.When
        , StaleReferenceUtilitiesTest.Then> {

    @Test
    void testGetPossibleExceptionOfElementWithANonStaleElement() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(By.id("button1"));
        when().the_element_is_checked_on_a_possible_stale_reference_exception();
        then().an_exception_is_expected(new NullPointerException(), "No exception is thrown");
    }

    @Test
    void testGetPossibleExceptionOfElementWithAStaleElement() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(By.id("button1"));
        when().the_page_is_refreshed()
                .and().the_element_is_checked_on_a_possible_stale_reference_exception();
        then().an_exception_is_expected(new StaleElementReferenceException(""), "element is not attached to the page document");
    }

    @Test
    void testCheckIfElementHasStaleReference() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(By.id("button1"));
        when().the_page_is_refreshed()
                .and().the_stale_state_of_a_web_element_is_checked();
        then().a_non_stale_referenced_web_element_is_returned();
    }

    @Test
    void testReturnNonStaleWebElement() {
        given().the_selenium_context_is_created()
                .and().navigate_to_default_testapp()
                .and().a_web_element(By.id("button1"));
        when().the_page_is_refreshed()
                .and().a_non_stale_element_needs_to_be_returned();
        then().a_non_stale_referenced_web_element_is_returned();
    }

    @Test
    void testGetMaxNumberOfRefreshAttempts() {
        given().the_selenium_context_is_created();
        when().the_get_max_number_of_refresh_attempts_is_called();
        then().the_number_of_max_refresh_attempts_is_$(25);
    }

    @Test
    void testSetMaxNumberOfRefreshAttempts() {
        given().the_selenium_context_is_created();
        when().we_set_the_max_number_of_refresh_attempts(10)
                .and().the_get_max_number_of_refresh_attempts_is_called();
        then().the_number_of_max_refresh_attempts_is_$(10);
    }

    public static class When extends WhenStage<When> {
        @ProvidedScenarioState
        private WebElement webElement;
        @ProvidedScenarioState
        private Exception exception;
        @ProvidedScenarioState
        private int actualValue;

        WhenStage<When> the_stale_state_of_a_web_element_is_checked() {
            StaleReferenceUtilities.checkIfElementHasAStaleReference(webElement);
            return self();
        }

        When the_element_is_checked_on_a_possible_stale_reference_exception() {
            exception = StaleReferenceUtilities.getPossibleExceptionOfElement(webElement);
            return self();
        }

        When a_non_stale_element_needs_to_be_returned() {
            StaleReferenceUtilities.returnNonStaleWebElement(webElement);
            return self();
        }

        When the_get_max_number_of_refresh_attempts_is_called() {
            actualValue = StaleReferenceUtilities.getMaxNumberOfRefreshAttempts();
            return self();
        }

        When we_set_the_max_number_of_refresh_attempts(int numberOfRefreshes) {
            StaleReferenceUtilities.setMaxNumberOfRefreshAttempts(numberOfRefreshes);
            return self();
        }
    }

    public static class Then extends ThenStage<Then> {
        @ProvidedScenarioState
        private WebElement webElement;
        @ExpectedScenarioState
        private Exception exception;
        @ExpectedScenarioState
        private int actualValue;

        Then a_non_stale_referenced_web_element_is_returned() {
            assertTrue(StaleReferenceUtilities.checkIfElementHasAStaleReference(webElement));
            return self();
        }

        Then an_exception_is_expected(Exception e, String containsText) {
            assertEquals(e.getClass(), exception.getClass());
            assertTrue(exception.getMessage().contains(containsText));
            return self();
        }

        Then no_exception_is_expected() {
            assertNull(exception);
            return self();
        }

        Then the_number_of_max_refresh_attempts_is_$(int expectedAmount) {
            assertEquals(expectedAmount, actualValue);
            return self();
        }
    }
}