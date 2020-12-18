package pages;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import wrappers.ProjectSpecificWrappers;

import com.relevantcodes.extentreports.ExtentTest;

public class HomePage extends ProjectSpecificWrappers {

	public HomePage(RemoteWebDriver driver, ExtentTest test) {
		this.driver = driver;
		this.test = test;

		/*
		 * if (!verifyTitle("MF360 -TEST UNION KBC")) {
		 * reportStep("This Not Home Page", "FAIL"); }
		 */

	}

	public HomePage clickJiraServiceManagement() throws InterruptedException {

		try {
			Thread.sleep(20000);
			/*
			 * WebDriverWait wait = new WebDriverWait(driver, 10);
			 * wait.until(ExpectedConditions
			 * .presenceOfElementLocated(By.xpath(prop
			 * .getProperty("HomePage.JiraServiceManagement.XPATH"))));
			 */
			/*
			 * clickByXpath(prop
			 * .getProperty("HomePage.JiraServiceManagement.XPATH"));
			 */
			WebElement elementJiraServic = driver.findElement(By.xpath(prop
					.getProperty("HomePage.JiraServiceManagement.XPATH")));

			Actions actions = new Actions(driver);
			actions.moveToElement(elementJiraServic);
			actions.click();
			actions.perform();
			reportStep("JiraServiceManagement Clicked Successfully", "STEPPASS");

		} catch (Exception e) {
			reportStep("JiraServiceManagement Not Clicked Successfully",
					"STEPFAIL");
		}

		return this;

	}

	public HomePage clickFiltersWithAdvancedIssueSearch()
			throws InterruptedException {

		Thread.sleep(20000);

		try {
			// clickByXpath(prop.getProperty("HomePage.Filters.XPATH"));
			WebElement elementFilter = driver.findElement(By.xpath(prop
					.getProperty("HomePage.Filters.XPATH")));

			Actions actions = new Actions(driver);
			actions.moveToElement(elementFilter);
			actions.click();
			actions.perform();
			reportStep("Filters Clicked Successfully", "STEPPASS");

		} catch (Exception e) {
			reportStep("Filters Not Clicked Successfully", "STEPFAIL");
		}

		Thread.sleep(2000);
		try {
			clickByXpath(prop.getProperty("HomePage.Advancedissuesearch.XPATH"));
			reportStep("AdvancedIssueSearch Clicked Successfully", "STEPPASS");
		} catch (Exception e) {
			reportStep("AdvancedIssueSearch Not Clicked Successfully",
					"STEPFAIL");
		}

		return this;

	}

	public HomePage clickTypeAsBugs() throws InterruptedException {
		// Thread.sleep(20000);
		System.out.println("Type");

		/*
		 * try { if (driver.findElement(By.linkText("Switch to basic"))
		 * .isDisplayed()) {
		 * 
		 * System.out.println("Element is Visible");
		 * 
		 * //clickByLink("Switch to basic");
		 * driver.findElement(By.linkText("Switch to basic")).click();
		 * 
		 * } } catch (Exception e) { // TODO Auto-generated catch block //
		 * e.printStackTrace(); }
		 */

		try {
			Thread.sleep(20000);

			WebElement element = driver.findElement(By.xpath(prop
					.getProperty("HomePage.Type.XPATH")));

			Actions actions = new Actions(driver);
			actions.moveToElement(element);
			actions.click();
			actions.perform();

			reportStep("Type Clicked Successfully", "STEPPASS");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to click on element");
			reportStep("Type Not Clicked Successfully", "STEPFAIL");
		}

		try {
			Thread.sleep(2000);
			clickByXpath(prop.getProperty("HomePage.Bugs.XPATH"));

			reportStep("Bugs Clicked Successfully", "STEPPASS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reportStep("Bugs Not Clicked Successfully", "STEPFAIL");
		}

		return this;

	}

	public HomePage clickTypeAsCR() throws InterruptedException {
		Thread.sleep(20000);
		System.out.println("Type");

		try {
			if (driver.findElement(By.linkText("Switch to basic"))
					.isDisplayed()) {

				System.out.println("Element is Visible");

				clickByLink("Switch to basic");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			Thread.sleep(20000);

			WebElement element = driver.findElement(By.xpath(prop
					.getProperty("HomePage.Type.XPATH")));

			if (element.isEnabled() && element.isDisplayed()) {
				System.out
						.println("Clicking on element with using java script click");
				Thread.sleep(2000);
				// ((JavascriptExecutor)
				// driver).executeScript("arguments[0].click();", element);

				// WebElement element = driver.findElement(By.id("my-id"));
				Actions actions = new Actions(driver);
				actions.moveToElement(element);
				actions.click();
				actions.perform();

				reportStep("Type Clicked Successfully", "STEPPASS");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to click on element");
			reportStep("Type Not Clicked Successfully", "STEPFAIL");
		}

		try {
			Thread.sleep(2000);

			clickByXpath(prop.getProperty("HomePage.BugCR.XPATH"));
			Thread.sleep(2000);
			clickByXpath(prop.getProperty("HomePage.CER.XPATH"));
			Thread.sleep(2000);
			clickByXpath(prop.getProperty("HomePage.CR.XPATH"));
			Thread.sleep(2000);
			clickByXpath(prop.getProperty("HomePage.DataWarehouse.XPATH"));
			Thread.sleep(2000);
			clickByXpath(prop.getProperty("HomePage.SubTaskCER.XPATH"));
			Thread.sleep(2000);
			clickByXpath(prop.getProperty("HomePage.Sub-TaskCR.XPATH"));
			Thread.sleep(2000);
			clickByXpath(prop.getProperty("HomePage.SubTaskBugCR.XPATH"));
			Thread.sleep(2000);
			reportStep("Bugs Clicked Successfully", "STEPPASS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reportStep("Bugs Not Clicked Successfully", "STEPFAIL");
		}

		return this;

	}

	public HomePage verfiyTotalCount(String intialCount) {
		verifyTextContainsByXpath(prop.getProperty("TotalCountVerify"),
				intialCount);
		return this;
	}

	public HomePage clickExportWithExportExcelCSVCurrentFields(String fType)
			throws InterruptedException {

		try {
			System.out.println("Export");

			clickByXpath(prop.getProperty("HomePage.Export.XPATH"));
			reportStep("Export Clicked Successfully", "STEPPASS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reportStep("Export Not Clicked Successfully", "STEPFAIL");
		}

		Thread.sleep(2000);

		try {
			clickByIdWithoutSnap(prop.getProperty("HomePage.ExportExcelCSV.ID"));

			Thread.sleep(2000);

			downloadFTPFile(fType);
			reportStep("ExportExcelCSV Clicked Successfully", "STEPPASS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reportStep("ExportExcelCSV Not Clicked Successfully", "STEPFAIL");
		}

		return this;

	}

	public HomePage navigateNextURL(String urlFurther, String issueType,
			String downloadedStmtFileType, String tempFolder)
			throws InterruptedException {
		// Thread.sleep(5000);
		// System.out.println("navigateNext"+urlFurther);

		try {

			String[] urlArr = urlFurther.split(",");

			for (String urlSingle : urlArr) {

				System.out.println(urlSingle);

				driver.navigate().to(urlSingle);

			//	Thread.sleep(5000);

				// reportStep("URL navigated Successfully", "STEPPASS");

				renameDownloadedFile(issueType, downloadedStmtFileType,
						tempFolder);

				reportStep("URL navigated and File downloaded Successfully",
						"STEPPASS");
			}
			// driver.navigate().to(urlFurther);
		} catch (Exception e) {

			reportStep("URL not navigated Successfully", "STEPFAIL");
		}

		return this;

	}

	public HomePage renameFileAndSave(String issueType, String mergeFname,
			String fileExtn, String tempFolder) throws IOException {

		try {
			mergeDownloadedFile(issueType, mergeFname, fileExtn, tempFolder);

			reportStep("File merged Successfully", "STEPPASS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reportStep("File not merged Successfully", "STEPFAIL");
		}

		return this;

	}

}
