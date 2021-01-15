package org.huizisoft.selenium.utils;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaleReferenceUtilityExtension implements BeforeEachCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaleReferenceUtilityExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Setting default max number of refresh attempts");
        }
        StaleReferenceUtilities.setMaxNumberOfRefreshAttempts(25);
    }
}
