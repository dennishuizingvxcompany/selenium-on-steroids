package org.huizisoft.selenium.utils.junit.extensions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.utils.SeleniumBaseUrl;
import org.huizisoft.selenium.utils.SeleniumContext;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class SeleniumContextTestConditionerExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private static final Logger LOGGER = LogManager.getLogger(SeleniumContextTestConditionerExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        System.setProperty("seleniumServerBaseUrl", "");
        SeleniumBaseUrl.setSeleniumServerBaseUrl("");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SeleniumServerBaseUrl system property: {}", System.getProperty("seleniumServerBaseUrl"));
            LOGGER.debug("SeleniumServerBaseUrl object value: {}", SeleniumBaseUrl.getUrl());
        }
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        SeleniumContext.closeWebDriver();
        SeleniumContext.clearCurrentInstance();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {

    }
}
