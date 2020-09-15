package org.huizisoft.selenium.bdd;

import com.tngtech.jgiven.Stage;
import org.huizisoft.selenium.page.objects.MainPage;

public class WhenSomethingOccurs extends Stage<WhenSomethingOccurs> {
    public WhenSomethingOccurs the_user_clicks_a_button_by_id() {
        new MainPage().clickButton1();
        return self();
    }
}
