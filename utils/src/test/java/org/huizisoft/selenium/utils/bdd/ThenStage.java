package org.huizisoft.selenium.utils.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.huizisoft.selenium.utils.SeleniumContext;

import static org.junit.jupiter.api.Assertions.assertNotSame;

public class ThenStage extends Stage<ThenStage> {
    @ExpectedScenarioState
    SeleniumContext seleniumContext;

    public ThenStage the_selenium_context_is_not_$(Object expectedObject) {
        assertNotSame(seleniumContext, expectedObject);
        return self();
    }
}