package org.huizisoft.selenium.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.huizisoft.selenium.utils.BasePage;
import org.huizisoft.selenium.configuration.SeleniumBaseUrl;
import org.huizisoft.selenium.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;

public class GivenStage extends Stage<GivenStage> {
    private static final String DEFAULT_URL = "http://tomcat:8080/testapp/";
    @ProvidedScenarioState
    protected Exception exception;
    @ProvidedScenarioState
    private WebElement element;

    public GivenStage the_selenium_context_is_created() {
        try {
            SeleniumContext.getCurrentInstance(true);
        } catch (Exception e) {
            exception = e;
        }
        return self();
    }

    public GivenStage navigate_to_default_testapp() {
        SeleniumContext.getDefaultWebDriver().get(DEFAULT_URL);
        return self();
    }

    public GivenStage a_web_element(By locator) {
        element = SeleniumContext.getDefaultWebDriver().findElement(locator);
        BasePage.waitForElementPresentAndDisplayed(element);
        return self();
    }

    public GivenStage a_proxy_web_element(WebElement webElement) {
        element = webElement;
        return self();
    }

    public GivenStage the_selenium_server_base_url_is_set_to_$(String url) {
        SeleniumBaseUrl.setSeleniumServerBaseUrl(url);
        return self();
    }

    public GivenStage capability_is_being_set_$(DesiredCapabilities desiredCapabilities) {
        try {
            SeleniumContext.setDesiredCapabilities(desiredCapabilities);
        } catch (MalformedURLException e) {
            exception = e;
        }
        return self();
    }
}