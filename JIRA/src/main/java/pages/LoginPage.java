package pages;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.relevantcodes.extentreports.ExtentTest;

import wrappers.GenericWrappers;
import wrappers.ProjectSpecificWrappers;

public class LoginPage extends ProjectSpecificWrappers {

	public LoginPage(RemoteWebDriver driver, ExtentTest test) {

		System.out.println("start from login page");
		this.driver = driver;
		this.test = test;
		/*
		 * if (!verifyTitle("MF360-Login")) { reportStep("This Not LogIn Page",
		 * "FAIL"); }
		 */
	}

	public LoginPage enterUserName() throws InterruptedException {

		try {
			Thread.sleep(2000);

			String unme = GenericWrappers.uName;
			System.out.println(unme);
			enterById(prop.getProperty("LoginPage.UserName.ID"),
					GenericWrappers.uName);
			reportStep("Username Entered Successfully", "STEPPASS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reportStep("Username Not Entered Successfully", "STEPFAIL");
		}
		return this;
	}

	public LoginPage clickContinue() throws InterruptedException {

		try {

			clickByXpath(prop.getProperty("LoginPage.Continue.XPATH"));
			reportStep("Continue Clicked Successfully", "STEPPASS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reportStep("Continue Not Clicked Successfully", "STEPFAIL");
		}

		return this;

	}

	public LoginPage enterPassword() {
		try {
			enterById(prop.getProperty("LoginPage.Password.ID"),
					GenericWrappers.pwd);
			reportStep("Password Entered Successfully", "STEPPASS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reportStep("Password Not Entered Successfully", "STEPFAIL");
		}
		return this;
	}

	public HomePage clickLogin() throws InterruptedException {

		try {
			clickByXpath(prop.getProperty("LoginPage.Login.XPATH"));

			reportStep("Login Clicked Successfully", "STEPPASS");
			Thread.sleep(20000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reportStep("Login Not Clicked Successfully", "STEPFAIL");
		}

		return new HomePage(driver, test);

	}
}