package org.huizisoft.selenium.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocatorExtractor {
    private static final Logger LOGGER = LogManager.getLogger(LocatorExtractor.class);

    private LocatorExtractor() {
        //no instantiation needed.
    }

    public static By extractByFromWebElement(@Nonnull WebElement webElement) {
        if (checkIfElementIsInteractive(webElement)) {
            return returnByClause(extractLocatorAndSelectorFromWebElementString(webElement));
        }
        if (elementThrowsException(webElement)) {
            return returnByClause(enforceExceptionToExtractByFromProxyElement(webElement));
        }
        throw new NoSuchElementException("Could not find or use element for extracting the By clause.");
    }

    private static By returnByClause(Map<String, String> selectorAndLocator) {
        String key = selectorAndLocator.keySet().iterator().next();
        String value = selectorAndLocator.get(key);
        switch (key) {
            case "css selector":
                return new By.ByCssSelector(value);
            case "class name":
                return new By.ByClassName(value);
            case "id":
                return new By.ById(value);
            case "link text":
                return new By.ByLinkText(value);
            case "xpath":
                return new By.ByXPath(value);
            case "name":
                return new By.ByName(value);
            case "partial link text":
                return new By.ByPartialLinkText(value);
            case "tag name":
                return new By.ByTagName(value);
            default:
                throw new NullPointerException("No suitable by clause found");
        }
    }

    private static Map<String, String> enforceExceptionToExtractByFromProxyElement(@Nonnull WebElement webElement) {
        try {
            if (webElement.toString().toLowerCase().contains("proxy") && LOGGER.isDebugEnabled()) {
                //this could raise a NoSuchElementException
                LOGGER.debug("We probly triggered an exception");
            }
        } catch (NoSuchElementException e) {
            return extractLocatorAndSelectorFromException(e);
        } catch (NullPointerException npe) {
            return Collections.emptyMap();
        }
        return extractLocatorAndSelectorFromWebElementString(webElement);
    }

    private static Map<String, String> extractLocatorAndSelectorFromException(NoSuchElementException e) {
        String neededPartOfException = e.getMessage().substring(e.getMessage().indexOf("***"));

        Pattern pattern = Pattern.compile("(.*Using=)(.+?)(, value=)(.+)(})");
        Matcher patternMatcher = pattern.matcher(neededPartOfException);
        String locator = "";
        String selector = "";
        if (patternMatcher.find()) {
            locator = patternMatcher.group(2);
            selector = patternMatcher.group(4);
        }
        return getEntrySetFromValues(locator, selector);
    }

    private static Map<String, String> extractLocatorAndSelectorFromWebElementString(@Nonnull WebElement webElement) {
        Pattern pattern = Pattern.compile("(.*-> )(.+?)(: )(.+)(])");
        String stringToRegEx = webElement.toString();

        Matcher matcher = pattern.matcher(stringToRegEx);

        String locator = "";
        String selector = "";
        if (matcher.matches()) {
            locator = matcher.group(2);
            selector = matcher.group(4);
        }

        if (locator.isEmpty() && selector.isEmpty()) {
            return Collections.emptyMap();
        }
        return getEntrySetFromValues(locator, selector);
    }

    private static Map<String, String> getEntrySetFromValues(String locator, String selector) {
        Map<String, String> map = new HashMap<>();
        map.put(locator, selector);
        return map;
    }

    private static boolean checkIfElementIsInteractive(WebElement webElement) {
        if (webElement != null && !elementThrowsException(webElement)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(StringUtils.join("Verify if element is interactable"));
            }
            return true;
        }
        return false;
    }

    private static boolean elementThrowsException(@Nonnull WebElement webElement) {
        try {
            webElement.getText();
            return false;
        } catch (StaleElementReferenceException | org.openqa.selenium.NoSuchElementException e) {
            return true;
        }
    }
}
