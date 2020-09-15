package org.huizisoft.selenium.bdd;

import com.tngtech.jgiven.Stage;
import org.huizisoft.selenium.page.objects.MainPage;

import static org.junit.Assert.assertTrue;

public class ThenSomethingIsExpected extends Stage<ThenSomethingIsExpected> {
    public ThenSomethingIsExpected checkLabel1Text(String expectedText) {
        assertTrue(new MainPage().getLabelText().equalsIgnoreCase(expectedText));
        return self();
    }
}
