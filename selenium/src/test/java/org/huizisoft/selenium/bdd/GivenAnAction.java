package org.huizisoft.selenium.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.utils.SeleniumContext;

public class GivenAnAction extends Stage<GivenAnAction> {

    private static final Logger LOGGER = LogManager.getLogger(GivenAnAction.class);
    @ExpectedScenarioState
    private SeleniumContext seleniumContext;

    public GivenAnAction doSomething() {
        LOGGER.info("doSomething works");
        return self();
    }

    public GivenAnAction navigateToUrl(String url) {
        seleniumContext.getWebDriver().get(url);
        LOGGER.info(seleniumContext.getWebDriver().getPageSource());
        return self();
    }
}
