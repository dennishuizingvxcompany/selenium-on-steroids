package org.huizisoft.selenium.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.jupiter.api.extension.TestInstancePreDestroyCallback;


public class GlobalHooks implements TestInstancePostProcessor, TestInstancePreDestroyCallback {
    private static final Logger LOGGER = LogManager.getLogger(GlobalHooks.class);

    public void cleanUp() {
        if (SeleniumContext.getCurrentInstance() != null) {
            LOGGER.info("Context not null, closing webdriver.");
            SeleniumContext.getCurrentInstance().closeWebDriver();
        }
    }

    @Override
    public void postProcessTestInstance(Object o, ExtensionContext extensionContext) {
        cleanUp();
    }

    @Override
    public void preDestroyTestInstance(ExtensionContext extensionContext) {
        cleanUp();
    }
}
