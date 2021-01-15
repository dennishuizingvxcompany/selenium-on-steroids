package org.huizisoft.selenium.utils.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.huizisoft.selenium.utils.SeleniumBaseUrl;
import org.huizisoft.selenium.utils.SeleniumContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ThenStage<SELF extends Stage<?>> extends Stage<SELF> {
    @ExpectedScenarioState
    SeleniumContext seleniumContext;

    public SELF the_selenium_context_is_empty$(boolean emptyOrNot) {
        if (emptyOrNot) {
            assertNull(SeleniumContext.getCurrentInstance());
        } else {
            assertNotNull(SeleniumContext.getCurrentInstance());
        }
        return self();
    }

    public SELF the_base_url_is_$(String url) {
        assertSame(SeleniumBaseUrl.getUrl(), url);
        return self();
    }

    public SELF the_web_driver_is_running(boolean running) {
        assertEquals(running, SeleniumContext.isWebDriverRunning());
        return self();
    }
}