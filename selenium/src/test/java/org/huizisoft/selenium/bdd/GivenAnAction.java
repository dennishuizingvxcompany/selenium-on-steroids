package org.huizisoft.selenium.bdd;

import com.tngtech.jgiven.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.utils.SeleniumContext;

public class GivenAnAction extends Stage<GivenAnAction> {

    private static final Logger LOGGER = LogManager.getLogger(GivenAnAction.class);

    public GivenAnAction doSomething() {
        LOGGER.info("doSomething works");
        return self();
    }

    public GivenAnAction navigateToUrl(String url) {
        SeleniumContext context = SeleniumContext.getCurrentInstance(true);
        context.getWebDriver().get(url);
        LOGGER.info(context.getWebDriver().getPageSource());
        return self();
    }
}
