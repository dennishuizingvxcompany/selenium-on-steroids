package org.huizisoft.selenium.utils.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.huizisoft.selenium.utils.configuration.SeleniumBaseUrl;
import org.huizisoft.selenium.utils.SeleniumContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ThenStage<SELF extends Stage<?>> extends Stage<SELF> {
    @ExpectedScenarioState
    protected SeleniumContext seleniumContext;
    @ExpectedScenarioState
    protected Exception exception;

    public SELF verify_the_selenium_context_is_empty_$(boolean emptyOrNot) {
        if (emptyOrNot) {
            assertNull(SeleniumContext.getCurrentInstance());
        } else {
            assertNotNull(SeleniumContext.getCurrentInstance());
        }
        return self();
    }

    public SELF verify_the_base_url_is_$(String url) {
        assertSame(SeleniumBaseUrl.getUrl(), url);
        return self();
    }

    public SELF verify_the_web_driver_is_running_$(boolean running) {
        assertEquals(running, SeleniumContext.isWebDriverRunning());
        return self();
    }

    public SELF no_exception_is_expected() {
        assertNull(exception);
        return self();
    }
}