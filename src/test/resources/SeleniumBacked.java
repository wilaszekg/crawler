package com.example.tests;

import com.thoughtworks.selenium.Selenium;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebDriver;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;
import static org.apache.commons.lang3.StringUtils.join;

public class Behaviour {
    private Selenium selenium;
    private String manualAddedUrl;

    @Before
    public void setUp() throws Exception {
        WebDriver driver = new FirefoxDriver();
        String baseUrl = "http://www.site.com/";
        manualAddedUrl = "http://www.site.com/";
        selenium = new WebDriverBackedSelenium(driver, baseUrl);
    }

    @Test
    public void testYto() throws Exception {
        WebDriverBackedSelenium wdbs = (WebDriverBackedSelenium)selenium;
        wdbs.getWrappedDriver().get(manualAddedUrl);
    }

    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }
}
