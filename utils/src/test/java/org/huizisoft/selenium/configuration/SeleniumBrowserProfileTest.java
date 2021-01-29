package org.huizisoft.selenium.configuration;

import org.huizisoft.selenium.configuration.BrowserProfile;
import org.huizisoft.selenium.configuration.SeleniumBrowserProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeleniumBrowserProfileTest {
    private static final String PROPERTY_VALUE = "seleniumServerBrowserProfile";
    private String currentValue;

    @BeforeEach
    void setUp() {
        currentValue = System.getProperty(PROPERTY_VALUE);
    }

    @AfterEach
    void tearDown() {
        if (currentValue != null) {
            System.setProperty(PROPERTY_VALUE, currentValue);
        }
    }

    @Test
    void testDefaultProfile() {
        System.setProperty(PROPERTY_VALUE, "");
        BrowserProfile profile = SeleniumBrowserProfile.getSeleniumBrowserProfile();
        assertEquals(BrowserProfile.CHROME, profile);
    }

    @Test
    void testProfileSetInProperty() {
        System.setProperty(PROPERTY_VALUE, "opera");
        assertEquals(BrowserProfile.OPERA, SeleniumBrowserProfile.getSeleniumBrowserProfile());
    }

    @Test
    void testSettingTheProfile() {
        System.setProperty(PROPERTY_VALUE, "internet explorer");
        assertEquals(BrowserProfile.INTERNET_EXPLORER, SeleniumBrowserProfile.getSeleniumBrowserProfile());
        SeleniumBrowserProfile.setSeleniumBrowserProfile(BrowserProfile.EDGE);
        assertEquals(BrowserProfile.EDGE, SeleniumBrowserProfile.getSeleniumBrowserProfile());
    }
}