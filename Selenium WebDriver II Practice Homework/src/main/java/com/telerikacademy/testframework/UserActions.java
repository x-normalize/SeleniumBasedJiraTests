package com.telerikacademy.testframework;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static com.telerikacademy.testframework.Utils.LOGGER;
import static com.telerikacademy.testframework.Utils.getConfigPropertyByKey;
import static com.telerikacademy.testframework.Utils.getUIMappingByKey;
import static com.telerikacademy.testframework.Utils.getWebDriver;
import static com.telerikacademy.testframework.Utils.tearDownWebDriver;
import static java.lang.String.format;

public class UserActions {

    final WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    public UserActions() {
        this.driver = getWebDriver();
    }

    public static void loadBrowser(String baseUrlKey) {
        getWebDriver().get(getConfigPropertyByKey(baseUrlKey));
    }

    public static void quitDriver() {
        tearDownWebDriver();
    }

    public void clickElement(String key, Object... arguments) {
        String locator = getLocatorValueByKey(key, arguments);

        LOGGER.info("Clicking on element " + key);
        WebElement element = driver.findElement(By.xpath(locator));
        element.click();
    }

    public void typeValueInField(String value, String field, Object... fieldArguments) {
        String locator = getLocatorValueByKey(field, fieldArguments);
        WebElement element = driver.findElement(By.xpath(locator));
        element.sendKeys(value);
    }

    public void dragAndDropElement(String fromElementLocator, String toElementLocator) {

        String fromLocator = getLocatorValueByKey(fromElementLocator);
        WebElement fromElement = driver.findElement(By.xpath(fromLocator));

        String toLocator = getLocatorValueByKey(toElementLocator);
        WebElement toElement = driver.findElement(By.xpath(toLocator));

        Actions actions = new Actions(driver);

        Action dragAndDrop = actions.clickAndHold(fromElement)
                .moveToElement(toElement)
                .release(toElement)
                .build();
        dragAndDrop.perform();
    }

    //############# WAITS #########
    public void waitForElementVisible(String locatorKey, Object... arguments) {
        int defaultTimeout = Integer.parseInt(getConfigPropertyByKey("config.defaultTimeoutSeconds"));

        waitForElementVisibleUntilTimeout(locatorKey, defaultTimeout, arguments);
    }

    public void waitForElementClickable(String locatorKey, Object... arguments) {
        int defaultTimeout = Integer.parseInt(getConfigPropertyByKey("config.defaultTimeoutSeconds"));

        waitForElementToBeClickableUntilTimeout(locatorKey, defaultTimeout, arguments);
    }

    //############# WAITS #########
    public void waitForElementPresent(String locator, Object... arguments) {
        // TODO: Implement the method
        // 1. Initialize Wait utility with default timeout from properties
        int defaultTimeout = Integer.parseInt(getConfigPropertyByKey("config.defaultTimeoutSeconds"));
        // 2. Use the method that checks for Element present
        // 3. Fail the test with meaningful error message in case the element is not present
        waitForElementPresenceUntilTimeout(locator, defaultTimeout, arguments);
    }

    public void assertElementPresent(String locator) {
        Assert.assertNotNull(format("Element with %s doesn't present.", locator),
            driver.findElement(By.xpath(getUIMappingByKey(locator))));
    }

    public void assertElementAttribute(String locator, String attributeName, String attributeValue) {
        // TODO: Implement the method
        // 1. Find Element using the locator value from Properties
        String xpath = getLocatorValueByKey(locator);
        WebElement element = driver.findElement(By.xpath(xpath));
        // 2. Get the element attribute
        String value = element.getAttribute(attributeName);
        // 3. Assert equality with expected value
        Assert.assertEquals(format("Element with locator %s doesn't match", attributeName), getLocatorValueByKey(attributeValue), value);
    }

    private String getLocatorValueByKey(String locator) {
        return format(getUIMappingByKey(locator));
    }

    private String getLocatorValueByKey(String locator, Object[] arguments) {
        return format(getUIMappingByKey(locator), arguments);
    }

    private void waitForElementVisibleUntilTimeout(String locator, int seconds, Object... locatorArguments) {
        Duration timeout = Duration.ofSeconds(seconds);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        String xpath = getLocatorValueByKey(locator, locatorArguments);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        } catch (Exception exception) {
            Assert.fail("Element with locator: '" + xpath + "' was not found.");
        }
    }

    private void waitForElementToBeClickableUntilTimeout(String locator, int seconds, Object... locatorArguments) {
        Duration timeout = Duration.ofSeconds(seconds);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        String xpath = getLocatorValueByKey(locator, locatorArguments);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        } catch (Exception exception) {
            Assert.fail("Element with locator: '" + xpath + "' was not found.");
        }
    }

    private void waitForElementPresenceUntilTimeout(String locator, int seconds, Object... locatorArguments) {
        Duration timeout = Duration.ofSeconds(seconds);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        String xpath = getLocatorValueByKey(locator, locatorArguments);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        } catch (Exception exception) {
            Assert.fail("Element with locator: '" + xpath + "' was not found.");
        }
    }

    public void pressEnterKeyOnElement(String locator) {
        WebElement element = driver.findElement(By.xpath(locator));
        Actions actions = new Actions(driver);
        actions.moveToElement(element)
                .sendKeys(Keys.ENTER)
                .perform();
    }

    public void typeValueInFieldWithActions(String value, String field, Object... fieldArguments) {
        String locator = getLocatorValueByKey(field, fieldArguments);
        WebElement element = driver.findElement(By.xpath(locator));

        Actions actions = new Actions(driver);
        actions.moveToElement(element)
                .click()
                .sendKeys(value)
                .build()
                .perform();
    }

    public void hoverOverElement(String locator, Object... locatorArguments) {
        String xpath = getLocatorValueByKey(locator, locatorArguments);
        WebElement element = driver.findElement(By.xpath(xpath));

        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    public boolean isElementPresent(String locator, Object... arguments) {
        String xpath = getLocatorValueByKey(locator, arguments);
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static void dropdownMenuSelectBug() {
        final WebDriver driver = getWebDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement dropdownMenuSelectBug = wait.until(ExpectedConditions.elementToBeClickable
                (By.xpath("//*[@id='issue-create.ui.modal.create-form.type-picker.issue-type-select']/div")));
        dropdownMenuSelectBug.click();

        WebElement bugOption = wait.until(ExpectedConditions.elementToBeClickable
                (By.xpath("//div[text()='Bug']")));
        bugOption.click();
    }
}
