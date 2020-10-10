package org.huizisoft.selenium.utils.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.huizisoft.selenium.utils.SeleniumContext;

public class WhenStage extends Stage<WhenStage> {
    @ExpectedScenarioState
    private SeleniumContext seleniumContext;

    public WhenStage the_selenium_context_is_created() {
        seleniumContext = SeleniumContext.createInstance();
        return self();
    }

    public WhenStage the_current_instance_is_created_with_create_with_null(boolean withNullOrNot) {
        seleniumContext = SeleniumContext.getCurrentInstance(withNullOrNot);
        return self();
    }
}
