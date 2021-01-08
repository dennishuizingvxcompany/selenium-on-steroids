package org.huizisoft.selenium.utils.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.utils.SeleniumBaseUrl;
import org.huizisoft.selenium.utils.SeleniumContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ThenStage<SELF extends Stage<?>> extends Stage<SELF> {
    private static final Logger LOGGER = LogManager.getLogger(ThenStage.class);
    @ExpectedScenarioState
    SeleniumContext seleniumContext;

    static void tearDown() {
        int counter = 0;
        while (SeleniumContext.getDefaultWebDriver() != null) {
            SeleniumContext.closeWebDriver();
            LOGGER.info("Number of time i tried to close the WebDriver {}", counter);
        }
    }

    public SELF the_selenium_context_is_empty$(boolean emptyOrNot) {
        if (emptyOrNot) {
            assertNull(SeleniumContext.getCurrentInstance());
        } else {
            assertNotNull(SeleniumContext.getCurrentInstance());
        }
        return then();
    }

    public SELF the_base_url_is_$(String url) {
        assertSame(SeleniumBaseUrl.getUrl(), url);
        return then();
    }

    public SELF the_web_driver_is_running(boolean running) {
        assertEquals(running, SeleniumContext.isWebDriverRunning());
        return then();
    }
}