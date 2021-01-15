package org.huizisoft.selenium.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class StaleReferenceUtilityExtension implements BeforeEachCallback {
    private static final Logger LOGGER = LogManager.getLogger(StaleReferenceUtilityExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Setting default max number of refresh attempts");
        }
        StaleReferenceUtilities.setMaxNumberOfRefreshAttempts(25);
    }
}
