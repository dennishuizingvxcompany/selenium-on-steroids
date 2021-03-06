package org.huizisoft.selenium.pom;

import org.huizisoft.selenium.SeleniumContext;
import org.huizisoft.selenium.utils.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class TestPage extends BasePage {
    private WebElement toggleButton;
    private WebElement hiddenButton;

    public TestPage() {
        PageFactory.initElements(SeleniumContext.getRemoteWebDriver(), this);
    }

    public void toggleVisibilityOfHiddenButton() {
        waitForElementPresentAndDisplayed(toggleButton);
        clickElement(toggleButton);
    }

    public WebElement getHiddenButton() {
        return hiddenButton;
    }
}