package com.fj.graetzelradbooker;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class GraetzelSteps {

    private static final Logger log = LoggerFactory.getLogger(GraetzelSteps.class);
    private static final String BASE_URL = "https://www.graetzlrad.wien/sofort-buchen?bikeId=17205&date=";
    private WebDriver driver;
    private Scenario scenario;

    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("^the user opens Graetzels booking website handing over (.+)$")
    public void the_user_opens(String date) {
        String url = BASE_URL + date;
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get(url);
        takeScreenshot("page_loaded");
    }

    @When("^the suer can see Modal Window \"([^\"]+)\", close it via \"([^\"]+)\"$")
    public void the_suer_closes_modal(String modalText, String buttonText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), '" + modalText + "')]")
        ));

        WebElement button = driver.findElement(
                By.xpath("//*[contains(text(), '" + buttonText + "')]")
        );
        button.click();
        takeScreenshot("privacy_modal_closed");
    }

    @When("^the user can see \"([^\"]+)\" in the title$")
    public void the_user_sees_title(String visibleText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleContains(visibleText));
        takeScreenshot("title_visible");
    }

    @When("the user fills the following details:")
    public void the_user_fills_details(DataTable dataTable) {
        var rows = dataTable.asMaps(String.class, String.class);
        for (var row : rows) {
            var field = row.get("Field");
            var value = row.get("Value");
            WebElement element = driver.findElement(
                    By.xpath("//*[@id=//label[contains(text(),'" + field + "')]/@for and (self::input or self::select)]")
            );
            if ("select".equals(element.getTagName())) {
                new Select(element).selectByVisibleText(value);
            } else {
                element.clear();
                element.sendKeys(value);
            }
        }
        takeScreenshot("details_filled");
    }

    @When("^the user fills DropDown \"([^\"]+)\" with \"([^\"]+)\"$")
    public void the_user_fills_dropdown(String fieldLabel, String value) {
        WebElement selectElement = driver.findElement(
                By.xpath("//select[@id=//label[contains(text(),'" + fieldLabel + "')]/@for]")
        );
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true); arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change'));",
                selectElement, value
        );
        takeScreenshot("dropdown_filled");
    }

    @When("the user clicks {string}")
    public void the_user_clicks(String buttonText) {
        WebElement button = driver.findElement(
                By.xpath("//*[contains(text(), '" + buttonText + "')]")
        );
        button.click();
        takeScreenshot("clicked_senden");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void takeScreenshot(String name) {
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path targetPath = Path.of("target/" + name + ".png");
            Files.copy(screenshotFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            if (scenario != null) {
                byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshotBytes, "image/png", name);
            }

            log.info("Screenshot saved: {}", targetPath);
        } catch (IOException e) {
            log.error("Failed to save screenshot: {}", e.getMessage());
        }
    }

}
