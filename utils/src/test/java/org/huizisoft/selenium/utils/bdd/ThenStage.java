package org.huizisoft.selenium.utils.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.huizisoft.selenium.utils.SeleniumContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ThenStage extends Stage<ThenStage> {
    @ExpectedScenarioState
    SeleniumContext seleniumContext;

    public ThenStage the_selenium_context_is_not_$(Object expectedObject) {
        assertNotSame(seleniumContext, expectedObject);
        return self();
    }

    public ThenStage the_base_url_is_$(String url) {
        assertSame(seleniumContext.getSeleniumServerBaseUrl(), url);
        return self();
    }

    public ThenStage the_web_driver_is_running(boolean running) {
        assertEquals(running, seleniumContext.isWebDriverRunning());
        return self();
    }
}