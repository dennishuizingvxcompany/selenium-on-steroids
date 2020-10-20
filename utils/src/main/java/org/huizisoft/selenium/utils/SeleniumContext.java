package org.huizisoft.selenium.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public final class SeleniumContext {
    private static final Logger LOGGER = LogManager.getLogger(SeleniumContext.class);
    private static final long DEFAULT_TIME_OUT_IN_SECONDS = 30;
    private static SeleniumContext currentInstance;
    private static DesiredCapabilities desiredCapabilities;
    private static int restartWebDriverAfterScenarios = 1;
    private static String seleniumServerBrowserProfile;
    private static WebDriverWait webDriverWait;
    private static RemoteWebDriver remoteWebDriver;
    private static String seleniumServerBaseUrl;
    private int scenariosWithCurrentWebDriver = 0;

    private SeleniumContext() {
        Properties properties = System.getProperties();
        setSeleniumServerBaseUrl(properties.getProperty("seleniumServerBaseUrl"));
        setSeleniumServerBrowserProfile(properties.getProperty("seleniumServerBrowserProfile"));
        if (remoteWebDriver == null) {
            try {
                createRemoteWebDriver(seleniumServerBaseUrl, seleniumServerBrowserProfile, desiredCapabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public static WebDriverWait getWebDriverWait() {
        if (webDriverWait == null) {
            createWait();
        }
        return webDriverWait;
    }

    /**
     * Creates the currentInstance aka initializes Properties:
     * -> seleniumServerBaseUrl
     * -> seleniumServerBrowserProfile
     * -> headlessBrowsing
     *
     * @return {@link SeleniumContext}
     */
    public static SeleniumContext createInstance() {
        return new SeleniumContext();
    }

    public static SeleniumContext getCurrentInstance() {
        return currentInstance;
    }

    public static void setCurrentInstance(SeleniumContext context) {
        currentInstance = context;
    }

    public static SeleniumContext getCurrentInstance(boolean createIfNull) {
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
            SeleniumContext.closeWebDriver();
            SeleniumContext.setCurrentInstance(null);
        }
    }

    /**
     * Firefox has its own way of being ready it seems. This should mitigate the TypeError: document.body is null
     */
    private static void firefoxWaitForDocumentReady(RemoteWebDriver remoteWebDriver) {
        boolean complete;
        do {
            complete = false;
            try {
                final Object testValue = remoteWebDriver.executeScript("return document.readyState");
                complete = testValue.toString().equalsIgnoreCase("complete");
            } catch (final RuntimeException e) {
                //if exception, just try it again
            }
            //if not complete, try again.
        } while (!complete);
    }

    /**
     * {@code restartWebDriverAfterScenarios} holds the number of scenario's that will be played before the web driver
     * (the 'browser') is re-initialized. Set this value to {@code 0} to keep using the same WebDriver over and over.
     *
     * @return number of scenario's during which a WebDriver will be reused
     */
    public static int getRestartWebDriverAfterScenarios() {
        return SeleniumContext.restartWebDriverAfterScenarios;
    }

    /**
     * {@code restartWebDriverAfterScenarios} holds the number of scenario's that will be played before the WebDriver
     * (the 'browser') is re-initialized. Set this value to {@code 0} to keep using the same WebDriver over and over.
     *
     * @param restartWebDriverAfterScenarios number of scenario's during which a WebDriver will be reused
     */
    public static void setRestartWebDriverAfterScenarios(int restartWebDriverAfterScenarios) {
        if (restartWebDriverAfterScenarios < 0) {
            throw new IllegalArgumentException("restartWebDriverAfterScenarios must be >= 0");
        }
        SeleniumContext.restartWebDriverAfterScenarios = restartWebDriverAfterScenarios;
    }

    public static String getSeleniumServerBrowserProfile() {
        return seleniumServerBrowserProfile;
    }

    public static void setSeleniumServerBrowserProfile(String seleniumServerBrowserProfile) {
        try {
            if (seleniumServerBrowserProfile.isEmpty()) {
                setDefaultBrowserProfile();
            } else {
                SeleniumContext.seleniumServerBrowserProfile = seleniumServerBrowserProfile;
            }
        } catch (NullPointerException e) {
            setDefaultBrowserProfile();
        }
    }

    private static void setDefaultBrowserProfile() {
        SeleniumContext.seleniumServerBrowserProfile = "chrome";
        LOGGER.warn("Default browserprofile is set");
    }

    private static void createWait() {
        webDriverWait = new WebDriverWait(getCurrentInstance().getDefaultWebDriver(), DEFAULT_TIME_OUT_IN_SECONDS);
    }

    private static void resetDesiredCapabilities() {
        desiredCapabilities = null;
    }

    public static void setDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
        if (SeleniumContext.desiredCapabilities == null) {
            SeleniumContext.desiredCapabilities = desiredCapabilities;
        } else {
            SeleniumContext.desiredCapabilities = SeleniumContext.desiredCapabilities.merge(desiredCapabilities);
        }
    }

    public static DesiredCapabilities getPredefinedCapabilities() {
        desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName(seleniumServerBrowserProfile);
        desiredCapabilities.setAcceptInsecureCerts(true);
        desiredCapabilities.setJavascriptEnabled(true);
        desiredCapabilities.acceptInsecureCerts();
        return SeleniumContext.desiredCapabilities;
    }

    public static RemoteWebDriver findWebDriver() {
        return remoteWebDriver;
    }

    public static void closeWebDriver() {
        if (remoteWebDriver != null) {
            String lowercaseBrowserName = remoteWebDriver.getCapabilities().getBrowserName().toLowerCase();
            try {
                LOGGER.info("Closing down current driver instance");
                remoteWebDriver.close();
                remoteWebDriver.quit();
            } catch (final WebDriverException e) {
                LOGGER.error("Closing exception within driver", e);
            } catch (Exception e) {
                LOGGER.error(StringUtils.join("Unexpected exception when closing WebDriver {}", e.getMessage()));
            }
            if (System.getProperty("os.name").toLowerCase().contains("windows") && lowercaseBrowserName.contains("firefox")) {
                closeFirefoxPopup();
            }
            remoteWebDriver = null;
        }
        resetDesiredCapabilities();
    }

    public static void setWebDriver(final RemoteWebDriver webDriver, WebDriverWait webDriverWait) {
        if (webDriver == null) {
            throw new IllegalArgumentException("webDriver cannot be null, shouldn't you be calling closeWebDriver()?");
        }
        if (webDriverWait == null) {
            throw new IllegalArgumentException("WebDriverWait cannot be null, shouldn't you be calling closeWebDriver()?");
        }
        closeWebDriver();
        remoteWebDriver = webDriver;
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
        LOGGER.info("Creating new driver instance");

        if (capabilities == null) {
            capabilities = SeleniumContext.getPredefinedCapabilities();
            setDesiredCapabilities(capabilities);//we need to set the instance variable as well, so we stay in sync!
        }
        remoteWebDriver = new RemoteWebDriver(new URL(url), capabilities);

        settingRemoteWebDriverTimeouts(browser, remoteWebDriver);

        maximizeWindow(remoteWebDriver);

        return remoteWebDriver;
    }

    private static void settingRemoteWebDriverTimeouts(String browser, RemoteWebDriver remoteWebDriver) {
        if (!browser.equalsIgnoreCase(BrowserType.IE)) {
            // IE gives org.openqa.selenium.InvalidArgumentException: Invalid timeout type specified: page load
            remoteWebDriver.manage().timeouts().pageLoadTimeout(DEFAULT_TIME_OUT_IN_SECONDS, TimeUnit.SECONDS);
        }
        remoteWebDriver.manage().timeouts().setScriptTimeout(DEFAULT_TIME_OUT_IN_SECONDS, TimeUnit.SECONDS);
    }

    private static void maximizeWindow(RemoteWebDriver remoteWebDriver) {
        final String lowercaseBrowsername = remoteWebDriver.getCapabilities().getBrowserName().toLowerCase();
        //Except for Chrome we can use the maximize of manage().window. For Chrome this is done by setting the capabilities.
        if (!lowercaseBrowsername.contains("chrome")) {
            try {
                remoteWebDriver.manage().window().maximize();
            } catch (final Exception e) {
                //Only notify something went wrong, no action needed!
                LOGGER.error(e.getMessage());
            }
        }

        if (lowercaseBrowsername.contains("firefox")) {
            firefoxWaitForDocumentReady(remoteWebDriver);
        }
    }

    public static RemoteWebDriver getRemoteWebDriver(DesiredCapabilities desiredCapabilities) {
        if (desiredCapabilities != null) {
            setDesiredCapabilities(desiredCapabilities);
        }
        if (remoteWebDriver != null) {
            try {
                remoteWebDriver.getPageSource();
            } catch (NullPointerException | NoSuchSessionException | JavascriptException exception) {
                remoteWebDriver = null;
            }
        }

        if (remoteWebDriver == null) {
            remoteWebDriver = createWebDriver();
        }
        return remoteWebDriver;
    }

    private static RemoteWebDriver createWebDriver() {
        try {
            return createRemoteWebDriver(getSeleniumServerBaseUrl(), getSeleniumServerBrowserProfile(), SeleniumContext.getPredefinedCapabilities());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String getSeleniumServerBaseUrl() {
        return seleniumServerBaseUrl;
    }

    public static void setSeleniumServerBaseUrl(String url) {
        try {
            if (url.isEmpty()) {
                setDefaultSeleniumBaseUrl();
            } else {
                seleniumServerBaseUrl = url;
            }
        } catch (NullPointerException e) {
            setDefaultSeleniumBaseUrl();
        }
    }

    private static void setDefaultSeleniumBaseUrl() {
        LOGGER.error("Default seleniumServerBaseUrl is set, you probably want to set the equivalent system property or build property");
        seleniumServerBaseUrl = "http://localhost:4444/wd/hub";
    }

    public void after() {
        scenariosWithCurrentWebDriver++;
        if (getRestartWebDriverAfterScenarios() > 0 && scenariosWithCurrentWebDriver >= getRestartWebDriverAfterScenarios()) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(StringUtils.join("Have run {} scenario's, next scenario will have a new web driver", scenariosWithCurrentWebDriver));
            }
            scenariosWithCurrentWebDriver = 0;
            closeWebDriver();
        }
    }

    //TODO: This needs to be refactored.
    public RemoteWebDriver getDefaultWebDriver() {
        return getRemoteWebDriver(null);
    }

    public void setRemoteWebDriver(RemoteWebDriver remoteWebDriver) {
        setWebDriver(remoteWebDriver, createSeleniumContextWait(remoteWebDriver));
    }

    private WebDriverWait createSeleniumContextWait(final RemoteWebDriver driver) {
        return new WebDriverWait(driver, DEFAULT_TIME_OUT_IN_SECONDS);
    }

    public boolean isWebDriverRunning() {
        try {
            remoteWebDriver.getPageSource();
        } catch (NullPointerException | NoSuchSessionException | JavascriptException exception) {
            return false;
        }
        return true;
    }
}
