package org.huizisoft.selenium.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Super class for all page objects. Provides methods for safely retrieval of/interacting with WebElements without running into timing issues.
 */
public class BasePage {
    private static final Logger LOGGER = LogManager.getLogger(BasePage.class);
    private static String currentWindowHandle;

    public BasePage() {
        init();
    }

    public static boolean isPresentAndDisplayed(By by) {
        try {
            return getWebDriver().findElement(by).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public static boolean isPresentAndDisplayed(WebElement element) {
        return isPresentAndDisplayed(LocatorExtractor.extractByFromWebElement(element));
    }

    public static void waitForElementPresentAndDisplayed(WebElement element) {
        WebElement refreshedElement = refreshPossibleStaleReferenceFor(element);// due to debuggingReasons, cut this out of the waitUntil.
        SeleniumContext.getWebDriverWait().until(ExpectedConditions.visibilityOf(refreshedElement));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(StringUtils.join("Wait for element present and displayed (element): {}", refreshedElement));
        }
    }

    public static boolean waitForInvisibilityOfElement(WebElement element) {
        try {
            return SeleniumContext.getWebDriverWait().until(ExpectedConditions.invisibilityOf(element));
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public static boolean isEnabled(WebElement elm) {
        try {
            return refreshPossibleStaleReferenceFor(elm).isEnabled();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            LOGGER.warn("isEnabled(WebElement elm) failed due to NoSuchElementException or StaleElementReferenceException.");
            return false;
        }
    }

    public static boolean isTextInElementPresent(WebElement element, String expected) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(StringUtils.join("Waiting on text to be present: expecting text -> {}", expected));
        }
        try {
            if (element.isDisplayed()) {
                String actual = getTextFromWebElement(element);
                return actual.toUpperCase().contains(expected.toUpperCase());
            } else {
                return false;
            }
        } catch (NoSuchElementException | StaleElementReferenceException exception) {
            return false;
        }
    }

    private static String getTextFromWebElement(WebElement element) {
        if (element.getTagName().equalsIgnoreCase("input")) {
            return element.getAttribute("value");
        }
        return element.getText();
    }

    public static boolean waitUntilTextInElementPresent(WebElement element, String text) {
        waitForElementPresentAndDisplayed(element);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(StringUtils.join("Done with waiting on text ({}) in element present.", text));
        }
        if (element.getTagName().equalsIgnoreCase("input")) {
            return SeleniumContext.getWebDriverWait().until(ExpectedConditions.attributeContains(element, "value", text));
        }
        return SeleniumContext.getWebDriverWait().until(ExpectedConditions.textToBePresentInElement(refreshPossibleStaleReferenceFor(element), text));
    }

    public static void waitUntilEnabled(WebElement elm) {
        SeleniumContext.getWebDriverWait().until(ExpectedConditions.elementToBeClickable(refreshPossibleStaleReferenceFor(elm)));
    }

    public static void checkOrUncheckCheckbox(WebElement element, boolean checkedOrUnchecked) {
        waitForElementPresentAndDisplayed(element);
        boolean currentState = element.isSelected();
        if (checkedOrUnchecked != currentState) {
            clickElement(element);
        }
        waitForJSAndJQueryToLoad();
    }

    public static void clickCheckBox(WebElement elm, String check) {
        String checked = "is-checked";
        WebElement parentElm = elm.findElement(By.xpath(".."));

        switch (check) {
            case "check":
                if (!isValuePresentInAttribute(parentElm, "class", checked))
                    clickElement(elm);
                break;
            case "uncheck":
                if (isValuePresentInAttribute(parentElm, "class", checked))
                    clickElement(elm);
                break;
            default:
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(StringUtils.join("Could not perform {} with executing clickCheckBox", check));
                }
        }
        waitForJSAndJQueryToLoad();
    }

    public static boolean isValuePresentInAttribute(WebElement elm, String attribute, String value) {
        waitForElementPresentAndDisplayed(elm);
        return elm.getAttribute(attribute).contains(value);
    }

    public static void scrollIntoView(WebElement elm) {
        executeJavascript("arguments[0].scrollIntoView(true);", elm);
    }

    public static void jsClick(WebElement elm) {
        waitUntilEnabled(elm);
        scrollIntoView(elm);
        executeJavascript("arguments[0].click();", elm);
    }

    public static String jsGetText(WebElement element) {
        return String.valueOf(executeJavascript("return arguments[0].innerText;", element));
    }

    public static boolean isAlertPresent() {
        try {
            SeleniumContext.findWebDriver().switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void acceptAlert() {
        try {
            SeleniumContext.findWebDriver().switchTo().alert().accept();
        } catch (NoAlertPresentException e) {
            // No use of throwing, if the popup is there it gets closed.
        }
    }

    public static void waitForJSAndJQueryToLoad() {
        //record start (and end) time of method
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        simpleCheckIfJavascriptQueueIsEmpty();
        waitForDocumentReadyState();
        waitForJQueryToFinish();
        waitForAjaxToFinish();

        //end time
        stopWatch.stop();
        debugComputedTimeOutputter(stopWatch);
    }

    private static void debugComputedTimeOutputter(StopWatch stopWatch) {
        long elapsedTime = stopWatch.getTime(TimeUnit.MILLISECONDS);
        String defaultOutputText = String.format("It took the new method %s ms to compute", elapsedTime);

        if (elapsedTime > 100L && elapsedTime < 500L) {
            LOGGER.debug(defaultOutputText);
        }
        if (elapsedTime > 500L && LOGGER.isDebugEnabled()) {
            LOGGER.debug(StringUtils.join("{} with current url: {}", defaultOutputText, getWebDriver().getCurrentUrl()));
        }
    }

    public static void moveMouseToElement(WebElement element) {
        waitUntilEnabled(element);
        new Actions(getWebDriver()).moveToElement(element).perform();
    }

    public static void clickElement(WebElement element) {
        waitUntilEnabled(element);
        scrollIntoView(element);
        cleanClickElement(SeleniumContext.getWebDriverWait().until(ExpectedConditions.elementToBeClickable(refreshPossibleStaleReferenceFor(element))));
    }

    public static boolean cleanClickElement(WebElement element) {
        try {
            element.click();
            return true;
        } catch (WebDriverException e) {
            if (e.getClass().equals(TimeoutException.class)) {
                throw e;
            }
            if (e.getClass().equals(ElementClickInterceptedException.class) || (e.getMessage().toLowerCase().contains("unknown error") && e.getMessage().contains("is not clickable"))) {
                LOGGER.info("Element not clickable by normal webdriver click(), JavaScript click is executed.");
                jsClick(element);
                return true;
            }
        }
        return false;
    }

    public static void sendKeysElement(WebElement element, String input) {
        waitForElementPresentAndDisplayed(element);
        waitUntilEnabled(element);
        refreshPossibleStaleReferenceFor(element).clear();
        refreshPossibleStaleReferenceFor(element).sendKeys(input);
    }

    public static void selectByVisibleTextFromDropdownList(WebElement element, String value) {
        waitForElementPresentAndDisplayed(element);
        new Select(element).selectByVisibleText(value);
    }

    public static void selectByValueFromDropDownList(WebElement element, String value) {
        waitUntilEnabled(element);
        new Select(element).selectByValue(value);
    }

    public static void selectValueFromDropdownListStartsWithText(WebElement element, String startsWithText) {
        waitUntilEnabled(element);
        clickElement(element);
        Select select = new Select(element);
        List<WebElement> listOfOptions = select.getOptions();
        for (WebElement currentElement : listOfOptions) {
            if (currentElement.getText().startsWith(startsWithText)) {
                clickElement(currentElement);
                break;
            }
        }
    }

    public static void selectValueFromShowList(WebElement element, String elementText) {
        String currentWindow = getWebDriver().getWindowHandle();
        waitUntilEnabled(element);
        clickElement(element);
        Set<String> handles = getWebDriver().getWindowHandles();
        for (String window : handles) {
            if (!window.equalsIgnoreCase(currentWindow)) {
                getWebDriver().switchTo().window(window);
            }
        }
        WebElement showListElement = SeleniumContext.getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[text()='" + elementText + "']")));
        clickElement(showListElement);
        getWebDriver().switchTo().window(currentWindow);
    }

    public static void waitForFrameToBeLoadedAndSwitchToIt(WebElement frameLocator) {
        getWebDriver().switchTo().parentFrame();
        try {
            getWebDriver().switchTo().frame(frameLocator);
        } catch (NoSuchElementException e) {
            //
        }
    }

    public static WebDriver getWebDriver() {
        if (findWebDriver().toString().contains("(null)")) { // driver returns "(null)" instead of session ID when session is closed
            return SeleniumContext.getRemoteWebDriver();
        }
        return findWebDriver();
    }

    public static WebDriver findWebDriver() {
        return SeleniumContext.findWebDriver();
    }

    public static Object executeJavascript(String javaScriptSnippet, WebElement element) {
        JavascriptExecutor js = getExecutor();
        try {
            if (element == null) {
                return js.executeScript(javaScriptSnippet);
            } else {
                return js.executeScript(javaScriptSnippet, element);
            }
        } catch (JavascriptException je) {
            if (je.toString().contains("jQuery is not defined") || je.toString().contains("Ajax is not defined")) {
                // This is produced by the waitForJSAndJQueryToLoad method ... this is not useful information for now!
                // If this errors are raised, no action is needed.
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("");
                }
            } else {
                LOGGER.warn(String.format("Execute javascript exception: %s", je));
            }
        } catch (StaleElementReferenceException e) {
            //Because we handle the StaleReferences from the selfram methods, this javascript error is not so important at this moment in time.
            LOGGER.warn(String.format("StaleElementReferenceException (no action needed!): %s", e.getMessage()));
        }
        return null;
    }

    private static JavascriptExecutor getExecutor() {
        if (getWebDriver() instanceof JavascriptExecutor) {
            return (JavascriptExecutor) getWebDriver();
        }
        LOGGER.warn("There is a possibility that there is a non JavascriptExecutor being returned at this moment.");
        return SeleniumContext.getRemoteWebDriver();
    }

    public static Object executeJavascript(String javaScriptSnippet) {
        return executeJavascript(javaScriptSnippet, null);
    }

    public static WebElement findAndWaitForElementPresentAndDisplayed(By by) {
        WebElement element = SeleniumContext.getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated(by));
        waitForElementPresentAndDisplayed(element);
        return element;
    }

    public static void waitForInvisibilityOfElement(By by) {
        SeleniumContext.getWebDriverWait().until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public static void setCurrentWindowHandle() {
        currentWindowHandle = getWebDriver().getWindowHandle();
    }

    public static String getCurrentWindowHandle() {
        return currentWindowHandle;
    }

    public static void selectMainWindow() {
        getWebDriver().switchTo().window(currentWindowHandle);
    }

    public static void switchToPopUp() {
        for (String handle : getWebDriver().getWindowHandles()) {
            if (!handle.equals(getCurrentWindowHandle()) && !handle.isEmpty())
                getWebDriver().switchTo().window(handle);
        }
    }

    private static void waitForAjaxToFinish() {
        LOGGER.debug("Check Ajax ready state.");
        ExpectedCondition<Boolean> ajaxLoad;

        try {
            ajaxLoad = driver -> ((Long) executeJavascript("return Ajax.activeRequestCount") == 0);
            SeleniumContext.getWebDriverWait().until(ajaxLoad);
        } catch (WebDriverException e) {
            if (!e.toString().contains("Ajax is not defined")) {
                throw e;
            }
        } catch (NullPointerException e) {
            // Nothing implemented, so we do not have to wait!
        }
        LOGGER.debug("Ajax is ready");
    }

    private static void simpleCheckIfJavascriptQueueIsEmpty() {
        executeJavascript("return 1===1");
    }

    private static void waitForDocumentReadyState() {
        ExpectedCondition<Boolean> jsLoad;

        try {
            jsLoad = driver -> executeJavascript("return document.readyState").toString().equals("complete");
            SeleniumContext.getWebDriverWait().until(jsLoad);
        } catch (WebDriverException | NullPointerException e) {
            //Document.readyState is not complete! Who cares!
        }
    }

    private static void waitForJQueryToFinish() {
        LOGGER.debug("Check JQuery state.");
        ExpectedCondition<Boolean> jQueryLoad;

        try {
            jQueryLoad = driver -> ((Long) executeJavascript("return jQuery.active") == 0);
            SeleniumContext.getWebDriverWait().until(jQueryLoad);
        } catch (WebDriverException e) {
            if (!e.toString().contains("unknown error: jQuery is not defined")) {
                throw e;
            }
        } catch (NullPointerException e) {
            // Nothing implemented, so we do not have to wait!
        }
        LOGGER.debug("JQuery is Ready!");
    }

    public static WebElement refreshPossibleStaleReferenceFor(WebElement element) {
        WebElement refreshedWebElement = element;
        if (StaleReferenceUtilities.checkIfElementHasAStaleReference(refreshedWebElement)) {
            LOGGER.info("WebElement needs to be refreshed because it is stale ");
            refreshedWebElement = StaleReferenceUtilities.returnNonStaleWebElement(refreshedWebElement);
        }
        return refreshedWebElement;
    }

    public static void highlightWebElement(WebElement elementToBeHighLighted) {
        executeJavascript("arguments[0].style.backgroundColor='#FDFF47'", elementToBeHighLighted);
    }

    public static void removeHighlightWebElement(WebElement elementToBeHighLighted) {
        executeJavascript("arguments[0].style.backgroundColor=''", elementToBeHighLighted);
    }

    protected void init() {
        PageFactory.initElements(SeleniumContext.getRemoteWebDriver(), this);
        waitForJSAndJQueryToLoad();
    }
}