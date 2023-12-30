import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Runner {

    WebDriver driver;
    WebDriverWait wait;
    String url = "https://www.naukri.com/nlogin/login";

    private void openBrowser() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void navigateToNaukri() {
        openBrowser();
        driver.get(url);
    }

    private void login(String username,String password) {

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//input[@id='usernameField']")))).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//input[@id='passwordField']")))).sendKeys(password);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//button[@type='submit' and text()='Login']")))).click();
    }

    private void updateProfile() {
        try {
            List<WebElement> list = driver.findElements(By.xpath("//div[@class='chatbot_Nav']/div"));
            if (!list.isEmpty())
                list.get(0).click();

            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='nI-gNb-drawer__icon']")))).click();
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='nI-gNb-drawer__icon']")))).click();
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[@href='/mnjuser/profile?id=&altresid' and text()='View & Update Profile']")))).click();
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[@class='fullname']//following-sibling::em")))).click();

            WebElement name = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//input[@id='name']"))));
            String getName = name.getAttribute("value");

            String updateName = getName.equals("Rohan Waghmare") ? "Rohan R Waghmare" : "Rohan Waghmare";
            name.click();
            Thread.sleep(5000);
            name.clear();
            name.sendKeys(updateName);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//button[@type='button' and text()='Save']")))).click();
            System.out.println("Profile Updated at "+ LocalDate.now() +" "+ LocalTime.now());
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            driver.quit();
        }
    }

    @Test
    public void run() {
        Runner runner = new Runner();
        String username = System.getenv("username");
        String password = System.getenv("password");
        runner.navigateToNaukri();
        runner.login(username,password);
        runner.updateProfile();
    }
}
