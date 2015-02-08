package pl.edu.agh.crawler.action;

import org.openqa.selenium.WebDriver;

public abstract class RecordedAction {
    protected WebDriver driver;

    public void perform() {
        try {
            setUp();
            runAction();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void setUp() throws Exception {
    }

    protected abstract void runAction() throws Exception;

    public RecordedAction withDriver(WebDriver driver) {
        this.driver = driver;
        return this;
    }
}
