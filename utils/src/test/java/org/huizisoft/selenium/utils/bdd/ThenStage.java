package org.huizisoft.selenium.utils.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.utils.SeleniumContext;

import static org.junit.jupiter.api.Assertions.assertNotSame;

public class ThenStage extends Stage<ThenStage> {
    private static final Logger LOGGER = LogManager.getLogger(ThenStage.class);
    @ExpectedScenarioState
    SeleniumContext seleniumContext;

    public ThenStage the_selenium_context_is_not_$(Object expectedObject) {
        assertNotSame(seleniumContext, expectedObject);
        return self();
    }
}