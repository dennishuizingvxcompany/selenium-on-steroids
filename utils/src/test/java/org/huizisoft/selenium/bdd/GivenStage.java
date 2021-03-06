package org.huizisoft.selenium.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.huizisoft.selenium.SeleniumContext;
import org.huizisoft.selenium.configuration.SeleniumBaseUrl;
import org.huizisoft.selenium.pom.TestPage;
import org.huizisoft.selenium.utils.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

public class GivenStage extends Stage<GivenStage> {
    private static final String DEFAULT_URL = "http://tomcat:8080/testapp/";
    @ProvidedScenarioState
    protected Exception exception;
    @ProvidedScenarioState
    protected WebElement element;

    @FindBy(css = "button8")
    private WebElement proxyWebElement;

    private TestPage testPage;

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

    public GivenStage a_proxy_web_element() {
        element = proxyWebElement;
        return self();
    }

    public GivenStage the_selenium_server_base_url_is_set_to_$(String url) {
        SeleniumBaseUrl.setSeleniumServerBaseUrl(url);
        return self();
    }

    public GivenStage capability_is_being_set_$(DesiredCapabilities desiredCapabilities) {
        SeleniumContext.setDesiredCapabilitiesOnRunningWebDriver(desiredCapabilities);
        return self();
    }

    public GivenStage initializePageFactory() {
        PageFactory.initElements(SeleniumContext.getDefaultWebDriver(), this);
        return self();
    }

    public GivenStage the_button_is_set_to_hidden() {
        testPage.toggleVisibilityOfHiddenButton();
        return self();
    }

    public GivenStage the_test_page_object_is_being_initialized() {
        testPage = new TestPage();
        return self();
    }

    public GivenStage the_selenium_context_time_out_is_set_to() {
        SeleniumContext.getRemoteWebDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        SeleniumContext.getRemoteWebDriver().manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        return self();
    }
}