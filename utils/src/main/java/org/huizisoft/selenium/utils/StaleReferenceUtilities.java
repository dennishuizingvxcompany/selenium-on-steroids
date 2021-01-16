package org.huizisoft.selenium.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;


public class StaleReferenceUtilities {
    private static final Logger LOGGER = LogManager.getLogger(StaleReferenceUtilities.class);
    private static int numberOfRefreshAttempts = 0;
    private static int maxNumberOfRefreshAttempts = 25;

    private StaleReferenceUtilities() {
        //No instantiation needed
    }

    public static Exception getPossibleExceptionOfElement(WebElement element) {
        try {
            element.getTagName();
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Throwing exception: %s", e.getMessage()));
            }
            return e;
        }
        return new NullPointerException("No exception is thrown");
    }

    public static boolean checkIfElementHasAStaleReference(WebElement element) {
        try {
            element.getTagName();
            return false;
        } catch (StaleElementReferenceException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Element is stale!");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static synchronized WebElement returnNonStaleWebElement(WebElement element) {
        WebElement returnedWebElement = null;
        By elementLocator = LocatorExtractor.extractByFromWebElement(element);

        while (returnedWebElement == null || checkIfElementHasAStaleReference(returnedWebElement)) {
            numberOfRefreshAttempts++;
            try {
                returnedWebElement = waitForElementToBeRenderedOnThePageAgain(elementLocator);
            } catch (StaleElementReferenceException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Encountered a stale element reference: %s", numberOfRefreshAttempts));
                }
                returnedWebElement = null;//Setting object back to null to enter the loop again.
            }
            stopRefreshLoop(elementLocator);
            returnedWebElement = finalStaleCheck(returnedWebElement);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(StringUtils.join("Iteration ", numberOfRefreshAttempts));
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(StringUtils.join("Handled element ({}) with stale reference (in iteration: {})", elementLocator, numberOfRefreshAttempts));
        }
        resetCounter();
        return returnedWebElement;
    }

    public static synchronized int getMaxNumberOfRefreshAttempts() {
        return maxNumberOfRefreshAttempts;
    }

    public static synchronized void setMaxNumberOfRefreshAttempts(int attempts) {
        maxNumberOfRefreshAttempts = attempts;
    }

    private static void stopRefreshLoop(By elementLocator) {
        if (numberOfRefreshAttempts == getMaxNumberOfRefreshAttempts()) {
            resetCounter();
            throw new NoSuchElementException(String.format("ReturnNonStaleWebElement: Method was not able to retrieve a WebElement based on locator %s", elementLocator.toString()));
        }
    }

    private static WebElement finalStaleCheck(WebElement returnedWebElement) {
        if (checkIfElementHasAStaleReference(returnedWebElement)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(StringUtils.join("Final check of StaleElementReference failed, we need to refresh again (iteration: {})", numberOfRefreshAttempts));
            }
            return null;
        }
        return returnedWebElement;
    }

    private static synchronized WebElement waitForElementToBeRenderedOnThePageAgain(By by) {
        try {
            BasePage.waitForJSAndJQueryToLoad();
            return BasePage.findAndWaitForElementPresentAndDisplayed(by);
        } catch (TimeoutException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ReturnNonStaleWebElement: TimeoutException occurred");
            }
            resetCounter();
            throw e;
        }
    }

    private static synchronized void resetCounter() {
        numberOfRefreshAttempts = 0;
    }
}
