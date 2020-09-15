package org.huizisoft.selenium.bdd;

import com.tngtech.jgiven.Stage;
import org.huizisoft.selenium.utils.SeleniumContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GivenAnAction extends Stage<GivenAnAction> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GivenAnAction.class);

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
