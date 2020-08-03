package org.huizisoft.selenium.tests;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.huizisoft.selenium.bdd.GivenAnAction;
import org.huizisoft.selenium.bdd.ThenSomethingIsExpected;
import org.huizisoft.selenium.bdd.WhenSomethingOccurs;
import org.huizisoft.selenium.utils.SeleniumContext;
import org.junit.Test;

public class MainPageTest extends ScenarioTest<GivenAnAction, WhenSomethingOccurs, ThenSomethingIsExpected> {
    @Test
    public void testMainPage() {
        given().navigateToUrl("http://tomcat:8080/testapp/index.jsp");
        when().the_user_clicks_a_button_by_id();
        given().doSomething();
    }

    @Test
    public void testWithoutJgiven() {
        SeleniumContext driver = SeleniumContext.getCurrentInstance();
        driver.getWebDriver().get("http://tomcat:8080/testapp/index.jsp");
    }
}