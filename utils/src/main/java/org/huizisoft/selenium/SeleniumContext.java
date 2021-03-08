package org.huizisoft.selenium;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.configuration.SeleniumBaseUrl;
import org.huizisoft.selenium.configuration.SeleniumBrowserProfile;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public final class SeleniumContext {
    private static final Logger LOGGER = LogManager.getLogger(SeleniumContext.class);
    private static final long DEFAULT_TIME_OUT_IN_SECONDS = 30;
    private static SeleniumContext currentInstance;
    private static DesiredCapabilities desiredCapabilities;
    private static int restartWebDriverAfterScenarios = 1;
    private static WebDriverWait webDriverWait;
    private static RemoteWebDriver remoteWebDriver;

    private SeleniumContext() throws MalformedURLException {
        if (getRemoteWebDriver() != null) {
            closeWebDriver();
        }
        createRemoteWebDriver(SeleniumBaseUrl.getUrl(), SeleniumBrowserProfile.getSeleniumBrowserProfile().getProfile(), getDesiredCapabilities());
    }

    public static WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }

    public static void setWebDriverWait(WebDriverWait webDriverWait) {
        SeleniumContext.webDriverWait = webDriverWait;
    }

    /**
     * Creates the currentInstance aka initializes Properties:
     * -> seleniumServerBaseUrl
     * -> seleniumServerBrowserProfile
     * -> headlessBrowsing
     *
     * @return {@link SeleniumContext}
     */
    private static SeleniumContext createInstance() throws MalformedURLException {
        return new SeleniumContext();
    }

    public static SeleniumContext getCurrentInstance() {
        return currentInstance;
    }

    public static void setCurrentInstance(SeleniumContext context) {
        currentInstance = context;
    }

    public static SeleniumContext getCurrentInstance(boolean createIfNull) throws MalformedURLException {
        if (currentInstance == null && createIfNull) {
            currentInstance = createInstance();
        }
        return currentInstance;
    }

    /**
     * Clears:
     * - SeleniumContext (currentInstance)
     * - DesiredCapabilities
     */
    public static void clearCurrentInstance() {
        desiredCapabilities = null;
        if (getCurrentInstance() != null) {
            closeWebDriver();
            setCurrentInstance(null);
        }
    }

    /**
     * Firefox has its own way of being ready it seems. This should mitigate the TypeError: document.body is null
     */
    private static void firefoxWaitForDocumentReady() {
        boolean complete;
        do {
            complete = false;
            try {
                final Object testValue = getRemoteWebDriver().executeScript("return document.readyState");
                complete = testValue.toString().equalsIgnoreCase("complete");
            } catch (final RuntimeException e) {
                //if exception, just try it again
            }
            //if not complete, try again.
        } while (!complete);
    }

    public static int getRestartWebDriverAfterScenarios() {
        return restartWebDriverAfterScenarios;
    }

    public static void setRestartWebDriverAfterScenarios(int restartWebDriverAfterNumberOfScenarios) {
        if (restartWebDriverAfterNumberOfScenarios < 0) {
            throw new IllegalArgumentException("restartWebDriverAfterScenarios must be >= 0");
        }
        restartWebDriverAfterScenarios = restartWebDriverAfterNumberOfScenarios;
    }

    private static DesiredCapabilities getDesiredCapabilities() {
        return SeleniumContext.desiredCapabilities;
    }

    private static void setDesiredCapabilities(DesiredCapabilities capabilities) {
        if (SeleniumContext.desiredCapabilities != null) {
            LOGGER.debug("Merging capabilities");
            SeleniumContext.desiredCapabilities.merge(capabilities);
        } else {
            SeleniumContext.desiredCapabilities = capabilities;
        }
    }

    private static DesiredCapabilities getPredefinedCapabilities() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Adding predefined capabilities");
        }
        setDesiredCapabilities(new DesiredCapabilities());
        getDesiredCapabilities().setBrowserName(SeleniumBrowserProfile.getSeleniumBrowserProfile().getProfile());
        getDesiredCapabilities().setAcceptInsecureCerts(true);
        getDesiredCapabilities().setJavascriptEnabled(true);
        return getDesiredCapabilities();
    }

    public static RemoteWebDriver findWebDriver() {
        return getRemoteWebDriver();
    }

    public static void closeWebDriver() {
        if (getRemoteWebDriver() != null) {
            String lowercaseBrowserName = getRemoteWebDriver().getCapabilities().getBrowserName().toLowerCase();
            try {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Closing down current driver instance");
                }
                getRemoteWebDriver().close();
                getRemoteWebDriver().quit();
            } catch (final WebDriverException e) {
                LOGGER.error("Closing exception within driver", e);
            } catch (Exception e) {
                LOGGER.error(StringUtils.join("Unexpected exception when closing WebDriver {}", e.getMessage()));
            }
            if (System.getProperty("os.name").toLowerCase().contains("windows") && lowercaseBrowserName.contains("firefox")) {
                closeFirefoxPopup();
            }
            setRemoteWebDriver(null);
            currentInstance = null;
        }
    }

    private static void closeFirefoxPopup() {
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
            Runtime.getRuntime().exec("taskkill /f /im WerFault.exe");
        } catch (IOException | InterruptedException e) {
            LOGGER.error(StringUtils.join("Firefox WerFault exception: \n{}", e.getMessage()));
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOGGER.error(StringUtils.join("Unexpected exception when closing Firefox popup {}", e.getMessage()));
        }
    }

    private static RemoteWebDriver createRemoteWebDriver(String url, String browser, DesiredCapabilities capabilities) throws MalformedURLException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Creating new remote WebDriver instance with url {}", url);
        }

        if (capabilities == null) {
            capabilities = SeleniumContext.getPredefinedCapabilities();
            setDesiredCapabilities(capabilities);
        } else {
            setDesiredCapabilities(capabilities);
        }
        if (getRemoteWebDriver() != null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Closing current webdriver to start a new one.");
            }
            closeWebDriver();
        }
        setRemoteWebDriver(new RemoteWebDriver(new URL(url), getDesiredCapabilities()));

        settingRemoteWebDriverTimeouts(browser);

        maximizeWindow();

        return getRemoteWebDriver();
    }

    private static void settingRemoteWebDriverTimeouts(String browser) {
        if (!browser.equalsIgnoreCase(BrowserType.IE)) {
            // IE gives org.openqa.selenium.InvalidArgumentException: Invalid timeout type specified: page load
            getRemoteWebDriver().manage().timeouts().pageLoadTimeout(DEFAULT_TIME_OUT_IN_SECONDS, TimeUnit.SECONDS);
        }
        getRemoteWebDriver().manage().timeouts().setScriptTimeout(DEFAULT_TIME_OUT_IN_SECONDS, TimeUnit.SECONDS);
        webDriverWait = new WebDriverWait(getRemoteWebDriver(), DEFAULT_TIME_OUT_IN_SECONDS);
    }

    private static void maximizeWindow() {
        String lowercaseBrowsername = getRemoteWebDriver().getCapabilities().getBrowserName().toLowerCase();
        //Except for Chrome we can use the maximize of manage().window. For Chrome this is done by setting the capabilities.
        if (!lowercaseBrowsername.contains("chrome")) {
            try {
                getRemoteWebDriver().manage().window().maximize();
            } catch (Exception e) {
                //Only notify something went wrong, no action needed!
                LOGGER.error(e.getMessage());
            }
        }

        if (lowercaseBrowsername.contains("firefox")) {
            firefoxWaitForDocumentReady();
        }
    }

    private static void createWebDriver() {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Creating RemoteWebDriver with SeleniumBaseUrl: {}", SeleniumBaseUrl.getUrl());
            }
            createRemoteWebDriver(SeleniumBaseUrl.getUrl(), SeleniumBrowserProfile.getSeleniumBrowserProfile().getProfile(), getDesiredCapabilities());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static RemoteWebDriver getRemoteWebDriver() {
        return remoteWebDriver;
    }

    private static void setRemoteWebDriver(RemoteWebDriver remoteWebDriver) {
        SeleniumContext.remoteWebDriver = remoteWebDriver;
    }

    public static boolean isWebDriverRunning() {
        if (currentInstance == null) {
            return false;
        }
        try {
            getRemoteWebDriver().getPageSource();
        } catch (NullPointerException | NoSuchSessionException | JavascriptException exception) {
            return false;
        }
        return true;
    }

    public static RemoteWebDriver setDesiredCapabilitiesOnRunningWebDriver(DesiredCapabilities desiredCapabilities) {
        setDesiredCapabilities(desiredCapabilities);
        createWebDriver();
        return getRemoteWebDriver();
    }

}