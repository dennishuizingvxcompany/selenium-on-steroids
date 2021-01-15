package org.huizisoft.selenium.utils;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeleniumContextTestConditionerExtension implements
        BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumContextTestConditionerExtension.class);

    @Override
    public void afterAll(ExtensionContext extensionContext) {

    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        SeleniumContext.closeWebDriver();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {

    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        System.setProperty("seleniumServerBaseUrl", "");
        LOGGER.info("Found system property value of base url in test: {}", System.getProperty("seleniumServerBaseUrl"));
        SeleniumBaseUrl.setSeleniumServerBaseUrl("");
    }
}
