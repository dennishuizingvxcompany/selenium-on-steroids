package org.huizisoft.selenium.tests;

import com.tngtech.jgiven.junit5.ScenarioTest;
import org.huizisoft.selenium.SeleniumContext;
import org.huizisoft.selenium.bdd.GivenAnAction;
import org.huizisoft.selenium.bdd.ThenSomethingIsExpected;
import org.huizisoft.selenium.bdd.WhenSomethingOccurs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainPageTest extends ScenarioTest<GivenAnAction, WhenSomethingOccurs, ThenSomethingIsExpected> {

    @Test
    void testMainPage() throws MalformedURLException {
        given().navigateToUrl("http://tomcat:8080/testapp/");
        when().the_user_clicks_a_button_by_id()
                .and().the_user_clicks_button_2();
        then().checkLabel1Text("button clicked");
    }

    @Test
    void testWithoutJgiven() throws MalformedURLException {
        SeleniumContext.getCurrentInstance(true);
        SeleniumContext.getRemoteWebDriver().get("http://tomcat:8080/testapp/");
        assertTrue(SeleniumContext.getRemoteWebDriver().getCurrentUrl().endsWith("testapp/"));
        SeleniumContext.closeWebDriver();
    }

    @AfterEach
    public void tearDown() {
        SeleniumContext.closeWebDriver();
    }
}