package TestLoginPage;

import org.testng.asserts.SoftAssert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class TestLogin {
	
	WebDriver driver;
	
	@BeforeMethod
	public void browserSetup() {
		
		String chromePath = "C:\\\\Program Files\\\\chromedriver_win32\\\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", chromePath);
		driver = new ChromeDriver();
		String loginURL = "https://bank.paysera.com/en/login";
		driver.get(loginURL);
		driver.manage().window().maximize();
			
	}
	
	/* TC001: Verify Login Functionality
	 * WHEN Email is unregistered
	 * EXPECTED user remains in the login page
	 *  correct error message displayed
	 */
	@Test(description="Verify user login when email is unregistered")
	public void TC001_VerifyUnregisteredEmail()
	{
		SoftAssert softAssert = new SoftAssert();
		LoginPage login = new LoginPage(driver);
		String expectedErrorMsg = "The specified user could not be found";
		
		/*Enter user identifier*/
		Boolean isSuccess = login.bankLogin("NonUser001@gmail.com");
		
		if(!isSuccess) {
			/*Verify error message is displayed correctly*/
			String actualErrorMsg = driver.findElement(By.xpath("//div[@role='alert']//div")).getText();
			softAssert.assertEquals(actualErrorMsg, expectedErrorMsg, "TC001 - FAILED - Incorrect error message displayed");
		} else {
			WebElement userHeader = driver.findElement(By.xpath("//div[@class='row user-identifier-row']"));
			softAssert.assertFalse(userHeader.isDisplayed(), "TC001 - FAILED - Unregistered user allowed to login");
		}
		
		softAssert.assertAll();
	}
	
	/* TC002: Verify Login Functionality
	 * WHEN Phone No. is missing country code
	 * EXPECTED user remains in the login page
	 *  correct error message displayed
	 */
	@Test(description="Verify user login when valid phone number with missing country code")
	public void TC002_VerifyNoCountryCodePhoneNo()
	{
		SoftAssert softAssert = new SoftAssert();
		LoginPage login = new LoginPage(driver);
		String expectedErrorMsg = "The specified user could not be found";
		
		/*Enter user identifier*/
		Boolean isSuccess = login.bankLogin("9985759348");
		
		if(!isSuccess) {
			/*Verify error message is displayed correctly*/
			String actualErrorMsg = driver.findElement(By.xpath("//div[@role='alert']//div")).getText();
			softAssert.assertEquals(actualErrorMsg, expectedErrorMsg, "TC002 - FAILED - Incorrect error message displayed");
		} else {
			WebElement userHeader = driver.findElement(By.xpath("//div[@class='row user-identifier-row']"));
			softAssert.assertFalse(userHeader.isDisplayed(), "TC002 - FAILED - Phone number with missing country code allowed to login");
		}
		
		softAssert.assertAll();
	}

	/* TC003: Verify Login Functionality
	 * WHEN Phone No. is missing international dialing prefix
	 * EXPECTED user remains in the login page
	 *  correct error message displayed
	 */
	@Test(description="Verify user login when phone number is missing international dialling prefix")
	public void TC003_VerifyNoPrefixPhoneNo()
	{
		SoftAssert softAssert = new SoftAssert();
		LoginPage login = new LoginPage(driver);
		String expectedErrorMsg = "The specified user could not be found";
		
		/*Enter user identifier*/
		Boolean isSuccess = login.bankLogin("63985759348");
		
		if(!isSuccess) {
			/*Verify error message is displayed correctly*/
			String actualErrorMsg = driver.findElement(By.xpath("//div[@role='alert']//div")).getText();
			softAssert.assertEquals(actualErrorMsg, expectedErrorMsg, "TC003 - FAILED - Incorrect error message displayed");
		} else {
			WebElement userHeader = driver.findElement(By.xpath("//div[@class='row user-identifier-row']"));
			softAssert.assertFalse(userHeader.isDisplayed(), "TC003 - FAILED - Users with incomplete phone number allowed to login");
		}
		
		softAssert.assertAll();
	}
	
	/* TC004: Verify Login Functionality
	 * WHEN Email address is registered to an account
	 * EXPECTED user is redirected to authentication page
	 *  user identifier is set to email address
	 *  authentication methods types are displayed: Mobile, Password
	 *  App authentication is set by default (tab expanded)
	 *  App Code is generated
	 *  Password Authentication tab is not expanded
	 */
	@Test(description="Verify user login when email is registered to an account")
	public void TC004_VerifyValidEmailAndAuthPage()
	{
		SoftAssert softAssert = new SoftAssert();
		LoginPage login = new LoginPage(driver);
		String expectedUserIdentifier = "lanzcompala@gmail.com";
		
		/*Enter user identifier*/
		Boolean isSuccess = login.bankLogin("lanzcompala@gmail.com");
		
		if(isSuccess) {
			/*Verify logged account identifier*/
			String actualUserIdentifier = driver.findElement(By.xpath("//div[@class='row user-identifier-row']//strong")).getText();
			softAssert.assertEquals(actualUserIdentifier,expectedUserIdentifier,"TC004 - FAILED - Incorrect User Identifier displayed");
			
			/*Verify authentications methods*/
			/*Important: For some reason, mobile authentication is NOT
			 * available when executing the script, therefore the assertions 
			 * related to Mobile App Code authentication won't be executed.
			 * Similar case to TC007 and TC008
			 * */
			WebElement tabPassword = driver.findElement(By.xpath("//*[@id='login-methods-heading-user_credentials']/.."));
			if(tabPassword.getAttribute("aria-expanded") == "true") {
				driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				WebElement authApp = driver.findElement(By.id("login-methods-heading-app_credentials"));
				WebElement authPassword = driver.findElement(By.id("login-methods-heading-user_credentials"));
				softAssert.assertTrue(authApp.isDisplayed(),"TC004 - FAILED - App authentication is not displayed");
				softAssert.assertTrue(authPassword.isDisplayed(),"TC004 - FAILED - Password authentication is not displayed");
				
				/*Verify that App authentication is set by default (tab expanded) and App Code is displayed*/
				WebElement tabAuthApp = driver.findElement(By.xpath("//*[@id='login-methods-heading-app_credentials']/.."));
				softAssert.assertEquals("true",tabAuthApp.getAttribute("aria-expanded"), "TC004 - FAILED - App authentication tab is not expanded by default");
				WebElement appCode = driver.findElement(By.xpath("//*[@id='login-methods-heading-app_credentials']/../following-sibling::div//h1"));
				softAssert.assertTrue(appCode.isDisplayed(), "TC004 - FAILED - App Code is not displayed under Mobil App authentication tab");
				
				/*Verify that Password authentication tab is not expanded by default*/
				softAssert.assertEquals("false",tabPassword.getAttribute("aria-expanded"), "TC004 - FAILED - App authentication tab is not expanded by default");
			} else {
				softAssert.assertTrue(false,"TC004 - FAILED - Mobile App Authentication NOT displayed, unable to verify");
			}
			
		} else {
			softAssert.assertTrue(false,"TC004 - FAILED - User with registered email is unable to login");
		}
		
		softAssert.assertAll();
	}
	
	/* TC005: Verify Login Functionality
	 * WHEN Phone number is a registered account
	 * AND password tab is expanded
	 * AND incorrect password entered
	 * EXPECTED user is redirected to authentication page
	 * AND Mobile App tab is hidden
	 *  Password field is displayed
	 *  LOG IN button is disabled by default (Password field empty)
	 *  Forgot Password link is displayed
	 * AND correct error message displayed
	 */
	@Test(description="Verify user login when phone is registered to an account and wrong password is used")
	public void TC005_VerifyValidPhoneWithWrongPassword()
	{
		SoftAssert softAssert = new SoftAssert();
		LoginPage login = new LoginPage(driver);
		String expectedErrorMsg = "Incorrect password. Please try again.";
		String expectedUserIdentifier = "+639985759348";
		
		/*Enter user identifier*/
		Boolean isSuccess = login.bankLogin("+639985759348");
		
		if(isSuccess) {
			/*Verify logged account identifier*/
			String actualUserIdentifier = driver.findElement(By.xpath("//div[@class='row user-identifier-row']//strong")).getText();
			softAssert.assertEquals(actualUserIdentifier,expectedUserIdentifier,"TC005 - FAILED - Incorrect User Identifier displayed");
			
			/*Click Password tab */
			WebElement tabPassword = driver.findElement(By.xpath("//*[@id='login-methods-heading-user_credentials']/.."));
			//tabPassword.click();
			softAssert.assertEquals("true",tabPassword.getAttribute("aria-expanded"), "TC005 - FAILED - Password authentication tab is not expanded when clicked");
			
			/*Verify Password field is displayed*/
			WebElement txtPassword = driver.findElement(By.id("password"));
			softAssert.assertTrue(txtPassword.isDisplayed(), "TC005 - FAILED - Password field is not displayed");
			
			/*Verify LOG IN button is disabled when Password field is empty*/
			WebElement btnLogin = driver.findElement(By.xpath("//*[@id='login-methods-body-user_credentials']//button[@type='submit']"));
			softAssert.assertFalse(btnLogin.isEnabled(), "TC005 - FAILED - LOG IN button is not enabled when Password field is empty");
			
			/*Verify Forgot Password link is displayed*/
			WebElement lnkForgotPassword = driver.findElement(By.xpath("//*[@id='login-methods-body-user_credentials']//a[contains(text(),'Forgot password')]"));
			softAssert.assertTrue(lnkForgotPassword.isDisplayed(),"TC005 - FAILED - Forgot Password link is not displayed");
			
			/*Verify LOG IN button is disabled when Password field has value*/
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			txtPassword.sendKeys("testPassword123");
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			softAssert.assertTrue(btnLogin.isEnabled(), "TC005 - FAILED - LOG IN button is still disabled when Password field has value");
			txtPassword.clear();
			
			/*Verify correct error message is displayed when wrong password is entered*/
			Boolean isAuthSuccess = login.bankAuthPassword("wrongPassword@123");
			if(!isAuthSuccess) {
				String actualErrorMsg = driver.findElement(By.xpath("//div[@role='alert']//div")).getText();
				softAssert.assertEquals(actualErrorMsg, expectedErrorMsg, "TC005 - FAILED - Incorrect error message displayed");
			} else {
				softAssert.assertTrue(false,"TC005 - FAILED - Error message is not displayed when wrong password is entered");
			}
			
		} else {
			softAssert.assertTrue(false,"TC005 - FAILED - User with registered phone number is unable to login");
		}
		
		softAssert.assertAll();
	}
	
	/* TC006: Verify Login Functionality
	 * WHEN Email is a registered account
	 * AND password tab is expanded
	 * AND correct password entered
	 * EXPECTED user is redirected to authentication page
	 * AND Password field is displayed
	 * AND user is redirected to Account Overview page
	 */
	@Test(description="Verify user login when correct email and password is used")
	public void TC006_VerifyEmailWithCorrectPassword()
	{
		SoftAssert softAssert = new SoftAssert();
		LoginPage login = new LoginPage(driver);
		
		/*Enter user identifier*/
		Boolean isSuccess = login.bankLogin("lanzcompala@gmail.com");
		if(isSuccess) {
			/*Enter correct password*/
			Boolean isAuthSuccess = login.bankAuthPassword("LanzTesting@123");
			if(isAuthSuccess) {
				WebElement lblAccountOverview = driver.findElement(By.xpath("//h1[text()='Account Overview']"));
				softAssert.assertTrue(lblAccountOverview.isDisplayed(),"TC006 - FAILED - User is not redirected to Account Overview page");
			} else {
				softAssert.assertTrue(false,"TC006 - FAILED - User unable to login");
			}
		} else {
			softAssert.assertTrue(false,"TC006 - FAILED - User with registered email is unable to login");
		}
		
		softAssert.assertAll();	
	}
	
	/* TC007: Verify Login Functionality
	 * WHEN Email is a registered account
	 * AND Mobile App code is canceled
	 * AND Repeat button is clicked
	 * EXPECTED user is redirected to authentication page
	 * AND Mobile App Code is generate
	 * AND new Mobile App code is generated
	 */
	@Test(description="Verify Mobile App Code will generate new value when cancelled and repeat")
	public void TC007_VerifyMobileAppCodeCancelAndRepeat()
	{
		SoftAssert softAssert = new SoftAssert();
		LoginPage login = new LoginPage(driver);
		WebDriverWait wait = new WebDriverWait(driver, 15);
		
		/*Enter user identifier*/
		Boolean isSuccess = login.bankLogin("lanzcompala@gmail.com");
		if(isSuccess) {
			WebElement tabPassword = driver.findElement(By.xpath("//*[@id='login-methods-heading-user_credentials']/.."));
			if(tabPassword.getAttribute("aria-expanded") == "true") {
				WebElement appCode = driver.findElement(By.xpath("//*[@id='login-methods-heading-app_credentials']/../following-sibling::div//h1"));
				String initialAppCode = appCode.getText();
				
				/*Verify Cancel button in Mobile App tab*/
				WebElement btnCancel = driver.findElement(By.xpath("//*[@id='login-methods-body-app_credentials']//button[text()='Cancel']"));
				softAssert.assertTrue(btnCancel.isDisplayed(),"TC007 - FAILED - Cancel button is not displayed in Mobile App authentication tab");
				
				/*Verify Repeat button once Cancel button is clicked*/
				btnCancel.click();
				WebElement btnRepeat = driver.findElement(By.xpath("//*[@id='login-methods-body-app_credentials']//button[text()='Repeat']"));
				softAssert.assertTrue(btnRepeat.isDisplayed(),"TC007 - FAILED - Repeat button is not displayed");
				
				/*Verify New App Code generated after Repeat button is clicked*/
				btnRepeat.click();
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				WebElement iconLoading = driver.findElement(By.xpath("//div[contains(@class,'spinner')]"));
	        	wait.until(ExpectedConditions.invisibilityOf(iconLoading));
	        	softAssert.assertTrue(appCode.isDisplayed(),"TC007 - FAILED - App Code not generated after repeat button is clicked");
	        	String newAppCode = appCode.getText();
	        	softAssert.assertNotEquals(initialAppCode,newAppCode,"TC007 - FAILED - App Code generated is the same as the initial App Code");
			} else {
				softAssert.assertTrue(false,"TC007 - FAILED - Mobile App Authentication NOT displayed, unable to verify");
			}

		} else {
			softAssert.assertTrue(false,"TC007 - FAILED - User with registered email unable to login");
		}
		
		softAssert.assertAll();	
		
	}
	/* TC008: Verify Login Functionality
	 * WHEN Phone No is a registered account
	 * AND Mobile App code expired
	 * AND Repeat button is clicked
	 * EXPECTED user is redirected to authentication page
	 * AND Mobile App Code is generate
	 * AND new Mobile App code is generated
	 */
	@Test(description="Verify Mobile App Code will generate new value when expired and repeat")
	public void TC008_VerifyMobileAppCodeExpiredAndRepeat()
	{
		SoftAssert softAssert = new SoftAssert();
		LoginPage login = new LoginPage(driver);
		WebDriverWait wait = new WebDriverWait(driver, 15);
		String expectedErrorMsg = "Verification time expired";
		
		/*Enter user identifier*/
		Boolean isSuccess = login.bankLogin("+639985759348");
		if(isSuccess) {
			
			WebElement tabPassword = driver.findElement(By.xpath("//*[@id='login-methods-heading-user_credentials']/.."));
			if(tabPassword.getAttribute("aria-expanded") == "true") {
				WebElement appCode = driver.findElement(By.xpath("//*[@id='login-methods-heading-app_credentials']/../following-sibling::div//h1"));
				String initialAppCode = appCode.getText();
				
				/*Verify Countdown when no response from App*/
				driver.manage().timeouts().implicitlyWait(65, TimeUnit.SECONDS);
				WebElement lblCountDown = driver.findElement(By.xpath("//*//span[@class='countdown']"));
				softAssert.assertTrue(lblCountDown.isDisplayed(),"TC008 - FAILED - Countdown did not display after no response from app");
				
				/*Verify App Code expired after countdown and error message and Repeat button is displayed*/
				wait.until(ExpectedConditions.invisibilityOf(lblCountDown));
				WebElement alertInvalidInput = driver.findElement(By.xpath("//div[@role='alert']"));
				String actualErrorMsg = driver.findElement(By.xpath("//div[@role='alert']//div")).getText();
				softAssert.assertTrue(alertInvalidInput.isDisplayed(),"TC008 - FAILED - Error message is not displayed after App Code expired");
				softAssert.assertEquals(actualErrorMsg, expectedErrorMsg, "TC008 - FAILED - Incorrect error message displayed");
				WebElement btnRepeat = driver.findElement(By.xpath("//*[@id='login-methods-body-app_credentials']//button[text()='Repeat']"));
				softAssert.assertTrue(btnRepeat.isDisplayed(),"TC008 - FAILED - Repeat button is not displayed");
				
				/*Verify New App Code generated after Code expired and repeat is clicked*/
				btnRepeat.click();
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				WebElement iconLoading = driver.findElement(By.xpath("//div[contains(@class,'spinner')]"));
	        	wait.until(ExpectedConditions.invisibilityOf(iconLoading));
	        	softAssert.assertTrue(appCode.isDisplayed(),"TC008 - FAILED - App Code not generated after expiring and repeat button is clicked");
	        	String newAppCode = appCode.getText();
	        	softAssert.assertNotEquals(initialAppCode,newAppCode,"TC008 - FAILED - App Code generated is the same as the initial App Code");
			} else {
				softAssert.assertTrue(false,"TC008 - FAILED - Mobile App Authentication NOT displayed, unable to verify");
			}
			
		} else {
			softAssert.assertTrue(false,"TC008 - FAILED - User with registered phone number is unable to login");
		}
		
		softAssert.assertAll();	
		
	}
	
	@AfterMethod
	public void testResult() {
		
		driver.quit();
	}

}