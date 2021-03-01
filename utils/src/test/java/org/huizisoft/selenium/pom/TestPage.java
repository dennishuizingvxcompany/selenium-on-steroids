package org.huizisoft.selenium.pom;

import org.huizisoft.selenium.utils.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TestPage extends BasePage {
    @FindBy
    private WebElement toggleButton;

    public void toggleVisibilityOfHiddenButton() {
        waitForElementPresentAndDisplayed(toggleButton);
        clickElement(toggleButton);
    }

    public WebElement getToggleButton() {
        return toggleButton;
    }
}