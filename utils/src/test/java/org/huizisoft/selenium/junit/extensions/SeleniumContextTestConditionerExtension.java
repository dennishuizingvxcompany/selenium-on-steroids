package org.huizisoft.selenium.junit.extensions;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.configuration.SeleniumBaseUrl;
import org.huizisoft.selenium.SeleniumContext;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class SeleniumContextTestConditionerExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private static final Logger LOGGER = LogManager.getLogger(SeleniumContextTestConditionerExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        String value = System.setProperty("seleniumServerBaseUrl", "");
        SeleniumBaseUrl.setSeleniumServerBaseUrl("");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SeleniumServerBaseUrl system property: {}", StringUtils.isEmpty(value) ? "no value present" : value);
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
