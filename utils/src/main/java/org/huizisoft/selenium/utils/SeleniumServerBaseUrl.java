package org.huizisoft.selenium.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeleniumServerBaseUrl {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumServerBaseUrl.class);
    private static final String DEFAULT_SELENIUM_SERVER_BASE_URL = "http://localhost:4444/wd/hub";
    private static String baseUrl;

    private SeleniumServerBaseUrl() {
        //No need for an instance
    }

    public static void setSeleniumServerBaseUrl(String seleniumServerBaseUrl) {
        setBaseUrl(seleniumServerBaseUrl);
    }

    public static String getUrl() {
        if (StringUtils.isEmpty(SeleniumServerBaseUrl.baseUrl)) {
            baseUrl = System.getProperty("seleniumServerBaseUrl");
            if (StringUtils.isEmpty(SeleniumServerBaseUrl.baseUrl)) {
                setDefaultSeleniumServerBaseUrl();
            }
        }
        return baseUrl;
    }

    private static void setBaseUrl(String url) {
        SeleniumServerBaseUrl.baseUrl = url;
    }

    private static void setDefaultSeleniumServerBaseUrl() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.error("Switching to default url!!! add seleniumServerBaseUrl as environment variable" +
                    "\r\n eg. {}", DEFAULT_SELENIUM_SERVER_BASE_URL);
        }
        setBaseUrl(DEFAULT_SELENIUM_SERVER_BASE_URL);
    }

    public static String getDefaultSeleniumServerBaseUrlValue() {
        return DEFAULT_SELENIUM_SERVER_BASE_URL;
    }
}