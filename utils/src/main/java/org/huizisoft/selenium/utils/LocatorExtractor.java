package org.huizisoft.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocatorExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocatorExtractor.class);

    public static By extractByFromWebElement(WebElement webElement) {
        Map.Entry<String, String> selectorAndLocator;

        verifyNullableObject(webElement);

        selectorAndLocator = enforceExceptionToExtractByFrom(webElement);
        if (selectorAndLocator == null) {
            return extractByFromWebElement(webElement);
        }
        return returnByClause(selectorAndLocator);
    }

    private static By returnByClause(Map.Entry<String, String> selectorAndLocator) {
        String value = selectorAndLocator.getValue();
        switch (selectorAndLocator.getKey()) {
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

    private static Map.Entry<String, String> enforceExceptionToExtractByFrom(WebElement webElement) {
        try {
            if (webElement.toString().toLowerCase().contains("proxy")) {
                webElement.getText(); //Invoke WebElement to enforce exceptions.
            }
            return extractLocatorAndSelectorFrom(webElement);
        } catch (NoSuchElementException e) {
            return extractByClauseFromException(e);
        }
    }

    private static Map.Entry<String, String> extractByClauseFromException(NoSuchElementException e) {
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

    private static Map.Entry<String, String> extractLocatorAndSelectorFrom(WebElement webElement) {
        Pattern pattern = Pattern.compile("(.*-> )(.+?)(: )(.+)(])");
        String stringToRegEx = webElement.toString();

        Matcher matcher = pattern.matcher(stringToRegEx);

        String locator = "";
        String selector = "";
        if (matcher.matches()) {
            locator = matcher.group(2);
            selector = matcher.group(4);
        }
        return getEntrySetFromValues(locator, selector);
    }

    private static Map.Entry<String, String> getEntrySetFromValues(String locator, String selector) {
        HashMap<String, String> returnedMap = new HashMap<>();
        returnedMap.put(locator, selector);

        return returnedMap.entrySet().iterator().next();
    }

    private static void verifyNullableObject(WebElement webElement) {
        if (webElement != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("String representation of the element: ", webElement);
            }
        }
        throw new NoSuchElementException("No element present");
    }
}
