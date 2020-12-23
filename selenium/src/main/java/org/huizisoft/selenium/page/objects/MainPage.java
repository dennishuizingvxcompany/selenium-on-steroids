package org.huizisoft.selenium.page.objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.selenium.utils.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class MainPage extends BasePage {
    private static final Logger LOGGER = LogManager.getLogger(MainPage.class);

    @FindBy(css = "#button1")
    private WebElement button1;
    @FindBy(css = "#label1")
    private WebElement label1;

    private WebElement button2;

    public WebElement getLabel1() {
        waitForElementPresentAndDisplayed(label1);
        return label1;
    }

    public WebElement getButton1() {
        waitForElementPresentAndDisplayed(button1);
        return button1;
    }

    public WebElement getButton2() {
        waitForElementPresentAndDisplayed(button2);
        return button2;
    }

    public void clickButton1() {
        LOGGER.info("Clicking button 1");
        clickElement(getButton1());
    }

    public String getLabelText() {
        return jsGetText(getLabel1());
    }

    public void clickButton2() {
        clickElement(getButton2());
    }
}
