package org.huizisoft.selenium.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SeleniumContextTest {

    private SeleniumContext seleniumContext;

    @Test
    void createInstance() {
        seleniumContext = SeleniumContext.createInstance();
        Assertions.assertEquals(true, seleniumContext != null);
    }

    @Test
    void getCurrentInstance() {
        seleniumContext = SeleniumContext.getCurrentInstance(true);
        Assertions.assertEquals(true, seleniumContext != null);
        seleniumContext.getWebDriver();
    }

    @Test
    void setCurrentInstance() {
    }

    @Test
    void testGetCurrentInstance() {
    }

    @Test
    void clearCurrentInstance() {
    }

    @Test
    void getRestartWebDriverAfterScenarios() {
    }

    @Test
    void setRestartWebDriverAfterScenarios() {
    }

    @Test
    void getSeleniumServerBrowserProfile() {
    }

    @Test
    void setSeleniumServerBrowserProfile() {
    }

    @Test
    void before() {
    }

    @Test
    void after() {
    }

    @Test
    void closeWebDriver() {
    }

    @Test
    void getWebDriver() {
    }

    @Test
    void setWebDriver() {
    }

    @Test
    void testGetWebDriver() {
    }

    @Test
    void findWebDriver() {
    }

    @Test
    void testSetWebDriver() {
    }

    @Test
    void getDesiredCapabilities() {
    }

    @Test
    void setDesiredCapabilities() {
    }

    @Test
    void isWebDriverRunning() {
    }

    @Test
    void getSeleniumServerBaseUrl() {
    }

    @Test
    void setSeleniumServerBaseUrl() {
    }
}