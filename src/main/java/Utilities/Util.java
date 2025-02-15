package Utilities;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Util {

    private static final String ScreenshotPath = "Test-Outputs/Screenshots/";

    public static void SetData(WebDriver driver , By locator , String s)
    {
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        driver.findElement(locator).sendKeys(s);
    }

    public static void ClickElement(WebDriver driver, By locator)
    {
    new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(locator));
    driver.findElement(locator).click();
    }

    public static void clearText(WebDriver driver , By locator){
        new WebDriverWait(driver,Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        findWebElement(driver,locator).clear();
    }

    public static String getText(WebDriver driver, By locator)
    {
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator).getText();
    }

    public static WebDriverWait generalWait_5Sec(WebDriver driver)
    {
        return new WebDriverWait(driver,Duration.ofSeconds(5));
    }

    public static void scrolling(WebDriver driver, By locator)
    {
        ((JavascriptExecutor)driver).executeScript("arguments[0]._scrollIntoView();",findWebElement(driver, locator));
    }

    public static void scrollToBottom(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public static void scrollToTop(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0);");
    }

    public static WebElement findWebElement(WebDriver driver, By locator)
    {
        return driver.findElement(locator);
    }

    public static Boolean checkVisibilityofElement(WebDriver driver, By locator)
    {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementInViewport(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for the element to be visible
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        // Scroll into view before checking
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

        // Check if the element is within the viewport
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (Boolean) js.executeScript(
                "var rect = arguments[0].getBoundingClientRect();" +
                        "return (" +
                        "rect.top >= 0 && " +
                        "rect.left >= 0 && " +
                        "rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && " +
                        "rect.right <= (window.innerWidth || document.documentElement.clientWidth)" +
                        ");",
                element
        );
    }

    public static void SelectingFromDropDown(WebDriver driver, By locator, String Op)
    {
        new Select(findWebElement(driver, locator)).selectByVisibleText(Op);
    }

    public static void moveToElement(WebDriver driver , By locator){
        new WebDriverWait(driver,Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        Actions actions = new Actions(driver);
        actions.moveToElement(findWebElement(driver,locator)).perform();
    }

    public static String getTimeStamp(){
        return new SimpleDateFormat("yyyy-MM-dd-h-m-ssa").format(new Date());
    }

    public static void takeScreenshot(WebDriver driver , String screenshotName) throws IOException
    {

        try {
            File screenshotSrc = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            File screenshotFile = new File(ScreenshotPath + screenshotName + "-" + getTimeStamp() +".png");
            FileUtils.copyFile(screenshotSrc,screenshotFile);
            Allure.addAttachment(screenshotName, Files.newInputStream(Path.of(screenshotFile.getPath())));
        } catch (Exception e){
            LogsUtils.error(e.getMessage());
        }

    }

    public static void TakeFullScreenshot(WebDriver driver, By locator)
    {
        try {
            Shutterbug.shootPage(driver, Capture.FULL_SCROLL)
                    .highlight(findWebElement(driver, locator))
                    .save(ScreenshotPath);
        } catch (Exception e) {
            LogsUtils.error(e.getMessage());
        }

    }

    public static int generateRandomNum(int upperBound) {
        //the upperBound value is EXCLUSIVE, But zero is inclusive
        return new Random().nextInt(upperBound) + 1;//the +1 includes the upperBound and prevents return of zero
    }

    public static Set<Integer> generateUniqueRandNums(int NumberOfProductsNeeded, int TotalNumOfProducts) {
        Set<Integer> UniqueRands = new HashSet<>();

        while (UniqueRands.size() < NumberOfProductsNeeded) {
            int randNum = generateRandomNum(TotalNumOfProducts);
            UniqueRands.add(randNum);
        }
        return UniqueRands;
    }

    public static boolean VerifyRedirectToPage(WebDriver driver, String expectedURL) {
        try {
            Util.generalWait_5Sec(driver).until(ExpectedConditions.urlToBe(expectedURL));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //function to retrieve all cookies
    public static Set<Cookie> getAllCookies(WebDriver driver)
    {
        return driver.manage().getCookies();
    }

    //function to restore those cookies
    public static void restoreSession(WebDriver driver, Set<Cookie> cookies)
    {
        for (Cookie co : cookies)
        {
            driver.manage().addCookie(co);
        }
    }
}
