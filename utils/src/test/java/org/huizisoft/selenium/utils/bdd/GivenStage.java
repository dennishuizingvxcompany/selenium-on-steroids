package org.huizisoft.selenium.utils.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.huizisoft.selenium.utils.BasePage;
import org.huizisoft.selenium.utils.SeleniumBaseUrl;
import org.huizisoft.selenium.utils.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class GivenStage<SELF extends Stage<?>> extends Stage<SELF> {
    private static final String DEFAULT_URL = "http://tomcat:8080/testapp/";

    @ProvidedScenarioState
    private WebElement element;

    public SELF the_selenium_context_is_created() {
        SeleniumContext.getCurrentInstance(true);
        return self();
    }

    public SELF navigate_to_default_testapp() {
        SeleniumContext.getDefaultWebDriver().get(DEFAULT_URL);
        return self();
    }

    public SELF a_web_element(By locator) {
        element = SeleniumContext.getDefaultWebDriver().findElement(locator);
        BasePage.waitForElementPresentAndDisplayed(element);
        return self();
    }

    public SELF a_proxy_web_element(WebElement webElement) {
        element = webElement;
        return self();
    }

    public SELF the_selenium_server_base_url_is_set_to_$(String url) {
        SeleniumBaseUrl.setSeleniumServerBaseUrl(url);
        return self();
    }
}
