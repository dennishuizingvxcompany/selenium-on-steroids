package org.huizisoft.selenium.bdd;

import com.tngtech.jgiven.Stage;
import org.huizisoft.selenium.utils.SeleniumContext;

public class GivenAnAction extends Stage<GivenAnAction> {

    public GivenAnAction doSomething() {
        System.out.println("doSomething works");
        return self();
    }

    public GivenAnAction navigateToUrl(String url) {
        SeleniumContext context = SeleniumContext.getCurrentInstance(true);
        context.getWebDriver().get(url);
        System.out.println(context.getWebDriver().getPageSource().toString());
        return self();
    }
}
