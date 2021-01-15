package org.huizisoft.selenium.utils;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class StaleReferenceUtilityExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        StaleReferenceUtilities.setMaxNumberOfRefreshAttempts(25);
    }
}
