package org.huizisoft.selenium.utils.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.huizisoft.selenium.utils.SeleniumContext;

public class GivenStage extends Stage<GivenStage> {

    @ExpectedScenarioState
    SeleniumContext seleniumContext;

    public GivenStage the_selenium_context_is_created(SeleniumContext seleniumContext) {
        this.seleniumContext = seleniumContext;
        return self();
    }
}
