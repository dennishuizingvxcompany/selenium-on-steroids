package org.huizisoft.selenium.page.objects;

import org.huizisoft.selenium.utils.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainPage extends BasePage {

    @FindBy
    private WebElement button1;

    @FindBy
    private WebElement label1;

    public boolean button1Present() {
        return isPresentAndDisplayed(button1);
    }

}
