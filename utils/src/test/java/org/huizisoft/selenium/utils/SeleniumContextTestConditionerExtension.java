package org.huizisoft.selenium.utils;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeleniumContextTestConditionerExtension implements
        BeforeEachCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumContextTestConditionerExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        System.setProperty("seleniumServerBaseUrl", "");
        LOGGER.info("Found system property value of base url in test: {}", System.getProperty("seleniumServerBaseUrl"));
        SeleniumBaseUrl.setSeleniumServerBaseUrl("");
    }
}
