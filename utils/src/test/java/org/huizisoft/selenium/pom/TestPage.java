package org.huizisoft.selenium.pom;

import org.huizisoft.selenium.SeleniumContext;
import org.huizisoft.selenium.utils.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class TestPage extends BasePage {
    private WebElement toggleButton;
    private WebElement hiddenButton;
    private WebElement radioButton1;
    private WebElement radioButton2;

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

    public WebElement getRadioButton1() {
        waitForElementPresentAndDisplayed(radioButton1);
        return radioButton1;
    }

    public WebElement getRadioButton2() {
        waitForElementPresentAndDisplayed(radioButton2);
        return radioButton2;
    }
}