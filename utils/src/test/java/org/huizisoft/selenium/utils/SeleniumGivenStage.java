package org.huizisoft.selenium.utils;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SeleniumGivenStage extends Stage<SeleniumGivenStage> {
    private static final String DEFAULT_URL = "http://tomcat:8080/testapp/";

    @ProvidedScenarioState
    private WebElement element;

    public SeleniumGivenStage the_selenium_context_is_created() {
        SeleniumContext.getCurrentInstance(true);
        return self();
    }

    public SeleniumGivenStage navigate_to_default_testapp() {
        SeleniumContext.getDefaultWebDriver().get(DEFAULT_URL);
        return self();
    }

    public SeleniumGivenStage a_web_element(By locator) {
        element = SeleniumContext.getDefaultWebDriver().findElement(locator);
        BasePage.waitForElementPresentAndDisplayed(element);
        return self();
    }

    public SeleniumGivenStage a_proxy_web_element(WebElement webElement) {
        element = webElement;
        return self();
    }
}
