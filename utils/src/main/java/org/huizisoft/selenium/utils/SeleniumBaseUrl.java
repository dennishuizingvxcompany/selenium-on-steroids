package org.huizisoft.selenium.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeleniumBaseUrl {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumBaseUrl.class);
    private static final String DEFAULT_SELENIUM_SERVER_BASE_URL = "http://localhost:8080/wd/hub";
    private static String baseUrl;

    private SeleniumBaseUrl() {
        //No need for an instance
    }

    public static void setSeleniumServerBaseUrl(String seleniumServerBaseUrl) {
        setBaseUrl(seleniumServerBaseUrl);
    }

    public static String getUrl() {
        if (StringUtils.isEmpty(SeleniumBaseUrl.baseUrl)) {
            baseUrl = System.getProperty("seleniumServerBaseUrl");
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Found system property seleniumServerBaseUrl value: {}", baseUrl);
            }
            if (StringUtils.isEmpty(SeleniumBaseUrl.baseUrl)) {
                setDefaultSeleniumServerBaseUrl();
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Current seleniumServerBaseUrl: {}", baseUrl);
        }
        return baseUrl;
    }

    private static void setBaseUrl(String url) {
        SeleniumBaseUrl.baseUrl = url;
    }

    private static void setDefaultSeleniumServerBaseUrl() {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Switching to default url!!! add seleniumServerBaseUrl as environment variable" +
                    "\r\n eg. seleniumServerBaseUrl={}", DEFAULT_SELENIUM_SERVER_BASE_URL);
        }
        setBaseUrl(DEFAULT_SELENIUM_SERVER_BASE_URL);
    }

    public static String getDefaultSeleniumServerBaseUrlValue() {
        return DEFAULT_SELENIUM_SERVER_BASE_URL;
    }
}