package org.huizisoft.selenium.junit.extensions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.utils.StaleReferenceUtilities;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class StaleReferenceUtilityExtension implements AfterEachCallback {
    private static final Logger LOGGER = LogManager.getLogger(StaleReferenceUtilityExtension.class);

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Setting default max number of refresh attempts");
        }
        StaleReferenceUtilities.setMaxNumberOfRefreshAttempts(25);
    }
}
