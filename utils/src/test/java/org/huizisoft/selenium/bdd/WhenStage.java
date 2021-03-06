package org.huizisoft.selenium.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.huizisoft.selenium.SeleniumContext;
import org.huizisoft.selenium.pom.TestPage;
import org.huizisoft.selenium.utils.BasePage;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;

public class WhenStage<SELF extends Stage<?>> extends Stage<SELF> {

    @ProvidedScenarioState
    private Boolean currentVisibilityStateOfWebElement;
    @ProvidedScenarioState
    private boolean enabledState;

    public SELF the_current_selenium_context_instance_is_created_with_new_object_or_not_$(boolean withNewObjectOrNot) {
        try {
            SeleniumContext.getCurrentInstance(withNewObjectOrNot);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return self();
    }

    public SELF the_page_is_refreshed() {
        SeleniumContext.getRemoteWebDriver().navigate().refresh();
        return self();
    }

    public SELF the_web_driver_is_closed() {
        SeleniumContext.closeWebDriver();
        return self();
    }

    public SELF the_current_selenium_contxt_instance_is_set_to_current_selenium_context() {
        SeleniumContext.setCurrentInstance(SeleniumContext.getCurrentInstance());
        return self();
    }

    public SELF the_invisibilityOfElement_is_checked() {
        currentVisibilityStateOfWebElement = BasePage.waitForInvisibilityOfElement(new TestPage().getHiddenButton());
        return self();
    }

    public SELF the_is_enabled_is_checked_for_element(WebElement radioButton1) {
        enabledState = TestPage.isEnabled(radioButton1);
        return self();
    }
}