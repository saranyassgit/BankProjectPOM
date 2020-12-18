package wrappers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import utils.DataInputProvider;

public class ProjectSpecificWrappers extends GenericWrappers {

	public String browserName;
	public String dataSheetName;

	public String sysDate;

	@BeforeSuite
	public void beforeSuite() {
		startResult();
	}

	@BeforeTest
	public void beforeTest() {
		loadObjects();

	}

	@BeforeMethod
	public void beforeMethod() {
		test = startTestCase(testCaseName, testDescription);
		test.assignCategory(category);
		test.assignAuthor(authors);

	}

	@AfterSuite
	public void afterSuite() {
		endResult();
		 quitBrowser();
	}

	@AfterTest
	public void afterTest() {
		unloadObjects();
		quitBrowser();
	}

	@AfterMethod
	public void afterMethod() {
		endTestcase();
		quitBrowser();

	}

	@DataProvider(name = "fetchData")
	public Object[][] getData() {
		return DataInputProvider.getSheet(dataSheetName);
	}

	public String getAppRunningDate() {

		String DATE_NOW = "dd-MMM-YYYY";

		// String DATE_NOW = "MM/DD/YYYY";
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_NOW);
		sysDate = sdf.format(currentDate.getTime());
		System.out.println("sysDate" + sysDate);
		return sysDate;
	}

	public void downloadFile() {

		verifyTitle("Atlassian | Start page");

	}

}
