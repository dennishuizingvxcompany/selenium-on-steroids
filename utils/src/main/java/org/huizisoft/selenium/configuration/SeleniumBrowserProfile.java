package org.huizisoft.selenium.configuration;

import org.apache.commons.lang3.StringUtils;

public class SeleniumBrowserProfile {
    private static final BrowserProfile DEFAULT_PROFILE = BrowserProfile.CHROME;
    private static final String SYSTEM_PROPERTY = "seleniumServerBrowserProfile";
    private static BrowserProfile current;

    private SeleniumBrowserProfile() {
        //no instance needed
    }

    public static BrowserProfile getSeleniumBrowserProfile() {
        String profile = System.getProperty(SYSTEM_PROPERTY);
        BrowserProfile possibleNewProfile = BrowserProfile.fromString(profile);
        if (current == null && StringUtils.isEmpty(profile)) {
            setSeleniumBrowserProfile(DEFAULT_PROFILE);
        } else {
            if (current != possibleNewProfile && possibleNewProfile != null) {
                current = possibleNewProfile;
            }
        }
        return current;
    }

    public static void setSeleniumBrowserProfile(BrowserProfile browserProfile) {
        current = browserProfile;
        System.setProperty(SYSTEM_PROPERTY, current.getProfile());
    }
}