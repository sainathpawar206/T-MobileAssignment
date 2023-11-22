import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

public class Filters {

    private static WebDriver driver;

    /*

        waitForElementToBeClickable() wait for maximum 10 seconds for element to become clickable
        accepts By type param so that it would work for all locator strategies

     */
    private static WebElement waitForElementToBeClickable(By by){

        WebDriverWait wait =  new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    /*

        scrollToFilters() scrolls till 'Filters' element
        Added with the purpose to see the group options selected visually

     */
    private static void scrollToFilters() throws InterruptedException {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor)driver;
        WebElement element = driver.findElement(By.xpath("//h2[contains(text(),'Filters')]"));
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);",element);
        Thread.sleep(5000);
    }

    /*

        SelectFilter() waits until group option Element becomes Clickable
        then, select the options based on values passed to options parameter
        Pass "all" as value to options params, if you want to select all options under group
        E.g., selectFilter("Brands","all");
     */
    private static void selectFilter(String group, String... options){

        By filterGroupXpath = By.xpath("//legend[@data-testid = 'desktop-filter-group-name' and normalize-space()='" + group + "']");
        WebElement filterGroupElement = waitForElementToBeClickable(filterGroupXpath);
        filterGroupElement.click();

        WebElement groupElement = driver.findElement(By.xpath("//div[@aria-label='"+group+"']"));
        if(options[0].equalsIgnoreCase("all")){
            List<WebElement> optionElements = groupElement.findElements(By.xpath("//span[@class='filter-display-name']"));
            IntStream.range(0, optionElements.size()).forEach(i->optionElements.get(i).click());

        }else {
            for (String option : options) {
                WebElement filterOptionElement = groupElement.findElement(By.xpath("//span[@class='filter-display-name' and contains(text(),'" + option + "')]"));
                filterOptionElement.click();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.t-mobile.com/tablets");

//        selectFilter("Brands","Apple","Samsung","TCL");
//        selectFilter("Deals","New", "Special offer");
//        selectFilter("Operating System","iPadOS");
        selectFilter("Brands","all");

        scrollToFilters();//Added scroll just to see options selected visually
        driver.quit();
    }
}
