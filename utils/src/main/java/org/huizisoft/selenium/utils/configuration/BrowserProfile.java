package org.huizisoft.selenium.utils.configuration;

public enum BrowserProfile {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    INTERNET_EXPLORER("internet explorer"),
    SAFARI("safari"),
    OPERA("opera");

    private final String profile;

    BrowserProfile(String browserProfile) {
        profile = browserProfile;
    }

    public static BrowserProfile fromString(String enumValue) {
        for (BrowserProfile current : BrowserProfile.values()) {
            if (current.getProfile().replace("_", " ").equalsIgnoreCase(enumValue)) {
                return current;
            }
        }
        return null;
    }

    public String getProfile() {
        return profile;
    }
}