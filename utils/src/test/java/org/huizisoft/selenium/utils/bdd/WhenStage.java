package org.huizisoft.selenium.utils.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.huizisoft.selenium.utils.SeleniumContext;

public class WhenStage extends Stage<WhenStage> {
    @ExpectedScenarioState
    private SeleniumContext seleniumContext;

    public WhenStage the_selenium_context_is_created(SeleniumContext seleniumContext) {
        this.seleniumContext = seleniumContext;
        return self();
    }

    public WhenStage the_current_instance_is_created_with_new_object_or_not(boolean withNewObjectOrNot) {
        seleniumContext = SeleniumContext.getCurrentInstance(withNewObjectOrNot);
        return self();
    }

    public WhenStage the_current_instance_is_set_to_current_selenium_context() {
        SeleniumContext.setCurrentInstance(seleniumContext);
        return self();
    }
}