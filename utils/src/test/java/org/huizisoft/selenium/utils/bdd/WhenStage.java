package org.huizisoft.selenium.utils.bdd;

import com.tngtech.jgiven.Stage;
import org.huizisoft.selenium.utils.SeleniumContext;

import java.net.MalformedURLException;

public class WhenStage<SELF extends Stage<?>> extends Stage<SELF> {

    public SELF the_current_selenium_context_instance_is_created_with_new_object_or_not_$(boolean withNewObjectOrNot) {
        try {
            SeleniumContext.getCurrentInstance(withNewObjectOrNot);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return self();
    }

    public SELF the_page_is_refreshed() {
        SeleniumContext.getDefaultWebDriver().navigate().refresh();
        return self();
    }

    public SELF the_web_driver_is_closed() {
        SeleniumContext.closeWebDriver();
        return self();
    }

    public SELF the_current_selenium_contxt_instance_is_set_to_current_selenium_context() {
        SeleniumContext.setCurrentInstance(SeleniumContext.getCurrentInstance());
        return self();
    }
}