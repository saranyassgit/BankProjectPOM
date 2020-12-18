package testCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pages.HomePage;
import pages.LoginPage;
import wrappers.ProjectSpecificWrappers;

public class TC001_BugCRFileDownlaod extends ProjectSpecificWrappers {

	public String CRType;
	public String mergeFileName;
	public String tempFolder ;
	public String downloadedStmtFileType = "XLSX";

	@BeforeClass
	public void setData() {
		testCaseName = "TC001_BugFileDownlaod";
		testDescription = "BugFileDownlaod";
		authors = "Saranya SS";
		category = "Regression";
		browserName = "chrome";
		dataSheetName = "TC001";
	}

	@Test(dataProvider = "fetchData")
	public void fileDownlaodIssueType(String multipleURL, String multipleURLCR)
			throws Exception {

		try {

			// Start bug download and merge
			if (multipleURL != null) {

				CRType = "BugCRCSV";
				mergeFileName = "Bugs";

				tempFolder = mergeFileName + downloadedStmtFileType;


				invokeApp(browserName, CRType, tempFolder);

				new LoginPage(driver, test)

				.enterUserName()

				.clickContinue()

				.enterPassword()

				.clickLogin();
				if (verifyTitle("Atlassian | Start page")) {
					HomePage hp = new HomePage(driver, test);
					hp.navigateNextURL(multipleURL, CRType,
							downloadedStmtFileType, tempFolder);
					hp.renameFileAndSave(CRType, mergeFileName,
							downloadedStmtFileType, tempFolder);

				} else {

					reportStep("URL is not Logged In successfully", "STEPFAIL");
				}
			}

			// Next CR download and merge

			if (multipleURLCR != null) {

				HomePage hp = new HomePage(driver, test);
				CRType = "BugCRCSV";
				mergeFileName = "CR";

				tempFolder = mergeFileName + downloadedStmtFileType;

				createFolder(CRType, tempFolder);


				if (verifyTitle("Atlassian | Start page")) {

					hp.navigateNextURL(multipleURLCR, CRType,
							downloadedStmtFileType, tempFolder);
					hp.renameFileAndSave(CRType, mergeFileName,
							downloadedStmtFileType, tempFolder);

				}

				else {

					reportStep("URL is not Logged In successfully", "STEPFAIL");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
	}

}
