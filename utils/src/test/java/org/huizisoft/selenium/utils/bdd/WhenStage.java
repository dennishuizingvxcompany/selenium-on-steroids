package org.huizisoft.selenium.utils.bdd;

import com.tngtech.jgiven.Stage;
import org.huizisoft.selenium.utils.SeleniumBaseUrl;
import org.huizisoft.selenium.utils.SeleniumContext;

public class WhenStage<When extends Stage<?>> extends Stage<When> {

    public When the_current_selenium_context_instance_is_created_with_new_object_or_not(boolean withNewObjectOrNot) {
        SeleniumContext.getCurrentInstance(withNewObjectOrNot);
        return self();
    }

    public When the_page_is_refreshed() {
        SeleniumContext.getDefaultWebDriver().navigate().refresh();
        return self();
    }

    public When the_web_driver_is_closed() {
        SeleniumContext.closeWebDriver();
        return self();
    }

    public When the_current_selenium_contxt_instance_is_set_to_current_selenium_context() {
        SeleniumContext.setCurrentInstance(SeleniumContext.getCurrentInstance());
        return self();
    }

    public When the_selenium_server_base_url_is_set_to(String url) {
        SeleniumBaseUrl.setSeleniumServerBaseUrl(url);
        return self();
    }
}