package TestLoginPage;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;


public class LoginPage {
	
	private WebDriver driver;
	
    public LoginPage(WebDriver driver) {
        
        this.driver = driver;
              
    }
	
    /*	Method to login user either by Email or Phone Number
     * 	@param String userIdentifier - user email/phone no.
     * 	returns boolean true if login successful
     */
	public boolean bankLogin(String userIdentifier)
	{
		WebDriverWait wait = new WebDriverWait(driver, 15);
		String loginURL = "https://bank.paysera.com/en/login";
		WebElement txtEmailPhone = driver.findElement(By.id("userIdentifier"));
        WebElement btnLogin = driver.findElement(By.xpath("//*//button[@type='submit']"));
        
        Boolean isSuccess = null;
        
        wait.until(ExpectedConditions.visibilityOf(txtEmailPhone));
        txtEmailPhone.sendKeys(userIdentifier);
        btnLogin.click();
        
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebElement iconLoading = driver.findElement(By.xpath("//div[contains(@class,'spinner')]"));
        wait.until(ExpectedConditions.invisibilityOf(iconLoading));
        
        //WebElement alertInvalidInput = driver.findElement(By.xpath("//div[@role='alert']"));
        if(loginURL.equals(driver.getCurrentUrl())) {
        	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        	wait.until(ExpectedConditions.invisibilityOf(iconLoading));
        	isSuccess = false;
        } else {
        	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        	wait.until(ExpectedConditions.invisibilityOf(iconLoading));
        	isSuccess = true;
        }
    
        return isSuccess;
	}
	
	/*	Method to login user either by Email or Phone Number
     * 	@param String userIdentifier - user email/phone no.
     * 	returns boolean true if login successful
     */
	public boolean bankAuthPassword(String password)
	{
		WebDriverWait wait = new WebDriverWait(driver, 15);
		WebElement btnLogin = driver.findElement(By.xpath("//*//button[@type='submit']"));
		String authURL = driver.getCurrentUrl();
		
		
		Boolean isSuccess = null;
		
		WebElement tabPassword = driver.findElement(By.xpath("//*[@id='login-methods-heading-user_credentials']/.."));
		if(tabPassword.getAttribute("aria-expanded") == "false") {
			tabPassword.click();
		}
		WebElement txtPassword = driver.findElement(By.xpath("//*[contains(@id,'password')]"));
		wait.until(ExpectedConditions.visibilityOf(txtPassword));
		txtPassword.sendKeys(password);
        btnLogin.click();
        
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebElement iconLoading = driver.findElement(By.xpath("//div[contains(@class,'spinner')]"));
        wait.until(ExpectedConditions.invisibilityOf(iconLoading));
        
        if(authURL.equals(driver.getCurrentUrl())) {
        	isSuccess = false;
        } else {
        	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        	wait.until(ExpectedConditions.invisibilityOf(iconLoading));
        	isSuccess = true;
        }
		
		return isSuccess;
	}

}