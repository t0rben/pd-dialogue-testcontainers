package com.prodyna.dialogue.testcontainers.staticcontent;

import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.BrowserWebDriverContainer;

import org.openqa.selenium.remote.DesiredCapabilities;

public class StaticContentTest {

    @Rule
    public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome());

    @Test
    public void simpleTest()  {
        //doSimpleWebdriverTest(chrome);
    }

    @Test
    public void simpleExploreTest() {
       // doSimpleExplore(chrome);
    }


}
