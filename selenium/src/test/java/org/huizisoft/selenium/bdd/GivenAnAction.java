package org.huizisoft.selenium.bdd;

import com.tngtech.jgiven.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.SeleniumContext;

import java.net.MalformedURLException;

public class GivenAnAction extends Stage<GivenAnAction> {

    private static final Logger LOGGER = LogManager.getLogger(GivenAnAction.class);

    public GivenAnAction navigateToUrl(String url) throws MalformedURLException {
        SeleniumContext.getCurrentInstance(true);
        SeleniumContext.getDefaultWebDriver().get(url);
        LOGGER.info(SeleniumContext.getDefaultWebDriver().getPageSource());
        return self();
    }
}
