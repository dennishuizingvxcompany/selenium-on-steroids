package org.huizisoft.selenium.utils;

import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public final class SeleniumContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumContext.class);
    private static SeleniumContext currentInstance;
    private static DesiredCapabilities desiredCapabilities;
    private static int restartWebDriverAfterScenarios = 1;
    private static String seleniumServerBrowserProfile;
    private static WebDriverWait webDriverWait;
    private static long defaultTimeOutInSeconds = 30;
    private RemoteWebDriver webDriver;
    private int scenariosWithCurrentWebDriver = 0;
    private String seleniumServerBaseUrl;

    private SeleniumContext() {
        Properties properties = System.getProperties();
        setSeleniumServerBaseUrl(properties.getProperty("seleniumServerBaseUrl"));
        setSeleniumServerBrowserProfile(properties.getProperty("seleniumServerBrowserProfile"));
        createWait();
    }

    public static WebDriverWait getWebDriverWait() {
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
        if (SeleniumContext.currentInstance != null) {
            SeleniumContext.currentInstance.closeWebDriver();
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
        SeleniumContext.seleniumServerBrowserProfile = seleniumServerBrowserProfile;
    }

    private static void createWait() {
        webDriverWait = new WebDriverWait(currentInstance.getWebDriver(), defaultTimeOutInSeconds);
    }

    private static void resetDesiredCapabilities() {
        desiredCapabilities = null;
    }

    public void after() {
        scenariosWithCurrentWebDriver++;
        if (getRestartWebDriverAfterScenarios() > 0 && scenariosWithCurrentWebDriver >= getRestartWebDriverAfterScenarios()) {
            LOGGER.info("Have run {} scenario's, next scenario will have a new web driver", scenariosWithCurrentWebDriver);
            scenariosWithCurrentWebDriver = 0;
            closeWebDriver();
        }
    }

    /**
     * Closes the current webdriver so that the next call to {@code getWebDriver()} will return a new one.
     */
    public void closeWebDriver() {
        if (webDriver != null) {
            String lowercaseBrowsername = webDriver.getCapabilities().getBrowserName().toLowerCase();
            try {
                LOGGER.info("Closing down current driver instance");
                webDriver.quit();
            } catch (final WebDriverException e) {
                LOGGER.error("Closing exception within driver", e);
            } catch (Exception e) {
                LOGGER.error("Unexpected exception when closing WebDriver {}", e.getMessage());
            }
            if (System.getProperty("os.name").toLowerCase().contains("windows") && lowercaseBrowsername.contains("firefox")) {
                closeFirefoxPopup();
            }
            webDriver = null;
        }
        resetDesiredCapabilities();
    }

    private void closeFirefoxPopup() {
        try {
            Thread.sleep(2000);
            Runtime.getRuntime().exec("taskkill /f /im WerFault.exe");
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Firefox WerFault exception: \n{}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOGGER.error("Unexpected exception when closing Firefox popup {}", e.getMessage());
        }
    }

    public RemoteWebDriver getWebDriver() {
        return getWebDriver(null);
    }

    public void setWebDriver(RemoteWebDriver webDriver) {
        setWebDriver(webDriver, createSeleniumContextWait(webDriver));
    }

    public RemoteWebDriver getWebDriver(DesiredCapabilities desiredCapabilities) {
        if (desiredCapabilities != null) {
            setDesiredCapabilities(desiredCapabilities);
        }
        if (webDriver != null) {
            try {
                webDriver.getPageSource();
            } catch (NullPointerException | NoSuchSessionException | JavascriptException exception) {
                webDriver = null;
            }
        }

        if (webDriver == null) {
            webDriver = createWebDriver();
        }
        return webDriver;
    }

    public RemoteWebDriver findWebDriver() {
        return webDriver;
    }

    private WebDriverWait createSeleniumContextWait(final RemoteWebDriver driver) {
        return new WebDriverWait(driver, defaultTimeOutInSeconds);
    }

    public void setWebDriver(final RemoteWebDriver webDriver, WebDriverWait webDriverWait) {
        if (webDriver == null) {
            throw new IllegalArgumentException("webDriver cannot be null, shouldn't you be calling closeWebDriver()?");
        }
        if (webDriverWait == null) {
            throw new IllegalArgumentException("WebDriverWait cannot be null, shouldn't you be calling closeWebDriver()?");
        }
        closeWebDriver();
        this.webDriver = webDriver;
    }

    private RemoteWebDriver createWebDriver() {
        try {
            return createRemoteWebDriver(getSeleniumServerBaseUrl(), getSeleniumServerBrowserProfile(), getDesiredCapabilities());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    private RemoteWebDriver createRemoteWebDriver(String url, String browser, DesiredCapabilities capabilities) throws MalformedURLException {
        LOGGER.info("creating new driver instance");

        if (capabilities == null) {
            capabilities = getPredefinedCapabilities();
            setDesiredCapabilities(capabilities);//we need to set the instance variable as well, so we stay in sync!
        }
        RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(url), capabilities);

        settingRemoteWebDriverTimeouts(browser, remoteWebDriver);

        maximizeWindow(remoteWebDriver);

        return remoteWebDriver;
    }

    private void settingRemoteWebDriverTimeouts(String browser, RemoteWebDriver remoteWebDriver) {
        if (!browser.equalsIgnoreCase(BrowserType.IE)) {
            // IE gives org.openqa.selenium.InvalidArgumentException: Invalid timeout type specified: page load
            remoteWebDriver.manage().timeouts().pageLoadTimeout(defaultTimeOutInSeconds, TimeUnit.SECONDS);
        }
        remoteWebDriver.manage().timeouts().setScriptTimeout(defaultTimeOutInSeconds, TimeUnit.SECONDS);
    }

    private void maximizeWindow(RemoteWebDriver remoteWebDriver) {
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

    private DesiredCapabilities getPredefinedCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setAcceptInsecureCerts(true);
        capabilities.setJavascriptEnabled(true);
        return capabilities;
    }

    public DesiredCapabilities getDesiredCapabilities() {
        return SeleniumContext.desiredCapabilities;
    }

    public static void setDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
        if (SeleniumContext.desiredCapabilities == null) {
            SeleniumContext.desiredCapabilities = desiredCapabilities;
        } else {
            SeleniumContext.desiredCapabilities = SeleniumContext.desiredCapabilities.merge(desiredCapabilities);
        }
    }

    public boolean isWebDriverRunning() {
        try {
            getWebDriver().getPageSource();
        } catch (NullPointerException | NoSuchSessionException | JavascriptException exception) {
            return false;
        }
        return true;
    }

    public String getSeleniumServerBaseUrl() {
        return seleniumServerBaseUrl;
    }

    public void setSeleniumServerBaseUrl(String seleniumServerBaseUrl) {
        this.seleniumServerBaseUrl = seleniumServerBaseUrl;
    }
}
