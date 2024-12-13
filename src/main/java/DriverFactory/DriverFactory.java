package DriverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Locale;

public class DriverFactory
{
//For Parallelization

    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static void setupDriver(String browser)
    {
        switch (browser.toLowerCase())
        {
            case "edge":
                driverThreadLocal.set(new EdgeDriver());
                break;
            case "firefox":
                driverThreadLocal.set(new FirefoxDriver());
                break;
            default:
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                driverThreadLocal.set(new ChromeDriver(options));

        }

    }

    public static WebDriver getDriver()
    {
        return driverThreadLocal.get();
    }

    public static void quitDriver()
    {
        getDriver().quit();
        driverThreadLocal.remove();
    }
}
