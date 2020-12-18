package wrappers;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Reporter;

import com.opencsv.CSVReader;
import com.relevantcodes.extentreports.ExtentTest;

public class GenericWrappers extends Reporter implements Wrappers {

	public GenericWrappers(RemoteWebDriver driver, ExtentTest test) {
		this.driver = driver;
		this.test = test;
	}

	public RemoteWebDriver driver;
	protected static Properties prop;

	public String sUrl, primaryWindowHandle, sHubUrl, sHubPort, dbDriver,
			dbServer, dbPort, dbName, dbUser, dbPwd;
	public static String uName, pwd, downloadPath,reportTime, ftpFlag, ftpPath,
			ftpUsername, ftpPassword;
	
	public static String tempdPath;

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	public static final char FILE_DELIMITER = ',';
	//public static final String FILE_EXTN = ".xlsx";
	//public static final String FILE_NAME = "EXCEL_DATA";

	
	public GenericWrappers() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File(
					"./drivers/configresources/config.properties")));
			sHubUrl = prop.getProperty("HUB");
			sHubPort = prop.getProperty("PORT");
			sUrl = prop.getProperty("URL");
			dbDriver = prop.getProperty("DBDRIVER");
			dbServer = prop.getProperty("DBSERVER");
			dbPort = prop.getProperty("DBPORT");
			dbName = prop.getProperty("SID");
			dbUser = prop.getProperty("DBUSERNAME");
			dbPwd = prop.getProperty("DBPASSWORD");
			uName = prop.getProperty("USERNAME");
			pwd = prop.getProperty("PASSWORD");
			downloadPath = prop.getProperty("DOWNLOADPATH");
			ftpFlag = prop.getProperty("FTPFLAG");
			ftpPath = prop.getProperty("FTPPATH");
			ftpUsername = prop.getProperty("FTPUSRENAME");
			ftpPassword = prop.getProperty("FTPPASSWORD");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadObjects() {
		prop = new Properties();
		try {
			prop.load(new FileInputStream(new File(
					"./drivers/configresources/object.properties")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void unloadObjects() {
		prop = null;
	}

	/**
	 * This method will launch the browser in local machine and maximise the
	 * browser and set the wait for 30 seconds and load the url
	 * 
	 * @author Babu - TestLeaf
	 * @param url
	 *            - The url with http or https
	 * @return
	 * 
	 */
	public RemoteWebDriver invokeApp(String browser, String issueType,String tempFolder ) {
		return invokeApp(browser, false, issueType,tempFolder );
	}
	public RemoteWebDriver invokeApp(String browser) {
		return invokeApp(browser, false);
	}

	/**
	 * This method will launch the browser in grid node (if remote) and maximise
	 * the browser and set the wait for 30 seconds and load the url
	 * 
	 * @author Babu - TestLeaf
	 * @param url
	 *            - The url with http or https
	 * @return
	 * 
	 */

	public static boolean isDirectoryExists(String directoryPath)

	{
		if (!Paths.get(directoryPath).toFile().isDirectory()) {
			return false;
		}
		return true;
	}

	public boolean createDirectory(String dirPath) {

		boolean isDir = false;
		System.out.println("dirPath" + dirPath);
		// Check If Directory Already Exists Or Not?
		Path dirPathObj = Paths.get(dirPath);
		boolean dirExists = Files.exists(dirPathObj);
		if (dirExists) {
			System.out.println("! Directory Already Exists !");

			isDir = true;
		} else {
			try {
				// Creating The New Directory Structure
				Files.createDirectories(dirPathObj);

				System.out.println("dirPathObj" + dirPathObj);
				System.out.println("! New Directory Successfully Created !");
			} catch (IOException ioExceptionObj) {
				System.out
						.println("Problem Occured While Creating The Directory Structure= "
								+ ioExceptionObj.getMessage());
			}
		}
		return isDir;

	}

	public String syspathValidation(String sysPath) {

		String npath = null;
		try {
			if (sysPath.endsWith("\\"))

			{
				npath = sysPath;
				// npath=sysPath.substring(0,sysPath.length()-1);
				System.out.println("systempath" + npath);
			} else {

				npath = sysPath + "\\";
				System.out.println("systempath" + npath);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

		return npath;

	}
	
	public static String syspathValidation1(String sysPath) {

		String npath = null;
		try {
			if (sysPath.endsWith("\\"))

			{
				npath = sysPath;
				// npath=sysPath.substring(0,sysPath.length()-1);
				System.out.println("systempath" + npath);
			} else {

				npath = sysPath + "\\";
				System.out.println("systempath" + npath);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

		return npath;

	}
	public boolean fileRenameAndMove(String downloadedfileName,
			String permanentPathFileRename, String fileExtn) {

		boolean isFileRename = false;
		System.out.println("Enter in to file rename process");

		try {

			// File oldFile = new File(downloadedfileName);
			String newFilepath = permanentPathFileRename + "." + fileExtn;
			// File newFile = new File(newFilepath);

			System.out.println("downloadedfileName" + downloadedfileName);
			System.out.println("newFilepath" + newFilepath);

			Path temp = Files.move(Paths.get(downloadedfileName),
					Paths.get(newFilepath));

			if (temp != null) {
				System.out.println("File renamed and moved successfully");
				isFileRename = true;
			} else {
				System.out.println("Failed to move the file");
			}
			/*
			 * if (oldFile.renameTo(newFile)) {
			 * 
			 * 
			 * System.out.println(
			 * "The file was moved successfully to the new folder");
			 * 
			 * } else {
			 * 
			 * System.out.println("The File was not moved.");
			 * 
			 * }
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isFileRename;

	}
	public String getAppRunningDateAndTime() {

		Calendar currentDate = Calendar.getInstance();
		String TIME_NOW = "ddMMMyyyyHHmmss";
		SimpleDateFormat stf1 = new SimpleDateFormat(TIME_NOW);
		reportTime = stf1.format(currentDate.getTime());

		return reportTime;

	}
	
	
	public void changeToExcelType(String inPutfName,String outPutfname,String fileExtn ) throws IOException {
		// TODO Auto-generated method stub
	
	
	    try {
			String csvLoc =inPutfName;
			String xlsFileLocation = outPutfname + "." + fileExtn;
			
  
			/*String xlsLoc = "D:\\Auto\\downloadfile\\Bug\\BugsXLSX\\Jira Service Desk.xlsx"
					, csvLoc = "D:\\Auto\\downloadfile\\Bug\\Jira Service Desk.csv", fileLoc = "";*/



			
			convertCsvToXls(xlsFileLocation, csvLoc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	
	
	
	
	} 
	   
	public static String convertCsvToXls(String xlsFileLocation, String csvFilePath) {
		SXSSFSheet sheet = null;
		CSVReader reader = null;
		Workbook workBook = null;
		String generatedXlsFilePath = "";
		FileOutputStream fileOutputStream = null;

		try {

			/**** Get the CSVReader Instance & Specify The Delimiter To Be Used ****/
			String[] nextLine;
			reader = new CSVReader(new FileReader(csvFilePath), FILE_DELIMITER);

			workBook = new SXSSFWorkbook();
			sheet = (SXSSFSheet) workBook.createSheet("Sheet");

			int rowNum = 0;
			System.out.println("Creating New .Xls File From The Already Generated .Csv File");
			while((nextLine = reader.readNext()) != null) {
				Row currentRow = sheet.createRow(rowNum++);
				for(int i=0; i < nextLine.length; i++) {
					if(NumberUtils.isDigits(nextLine[i])) {
						currentRow.createCell(i).setCellValue(Integer.parseInt(nextLine[i]));
					} else if (NumberUtils.isNumber(nextLine[i])) {
						currentRow.createCell(i).setCellValue(Double.parseDouble(nextLine[i]));
					} else {
						currentRow.createCell(i).setCellValue(nextLine[i]);
					}
				}
			}

			//generatedXlsFilePath = xlsFileLocation + FILE_NAME + FILE_EXTN;
			generatedXlsFilePath = xlsFileLocation;
			System.out.println("The File Is Generated At The Following Location?= " + generatedXlsFilePath);

			fileOutputStream = new FileOutputStream(generatedXlsFilePath.trim());
			workBook.write(fileOutputStream);
		} catch(Exception exObj) {
			System.out.println("Exception In convertCsvToXls() Method?=  " + exObj);
		} finally {			
			try {

				/**** Closing The Excel Workbook Object ****/
				workBook.close();

				/**** Closing The File-Writer Object ****/
				fileOutputStream.close();

				/**** Closing The CSV File-ReaderObject ****/
				reader.close();
			} catch (IOException ioExObj) {
				System.out.println("Exception While Closing I/O Objects In convertCsvToXls() Method?=  " + ioExObj);			
			}
		}

		return generatedXlsFilePath;
	}	    
	
	public void renameDownloadedFile(String issueType,String downloadedStmtFileType,String tempFolder )
	{
		

		try {

			String dateAndTime = getAppRunningDateAndTime();

			String outputFilePath = syspathValidation(downloadPath);

		
			String tempFileDownloadedPath = outputFilePath + issueType;

			String getFile = getFileNameFromdirectory(tempFileDownloadedPath);

			System.out.println("Get file from temporavary path" + getFile);
			
			
			

			if (getFile != null) {
				
				
				String outputFilePath1 = syspathValidation(tempFileDownloadedPath);
				
				String oPath=outputFilePath1+tempFolder;
				boolean temporarydirExists1 = isDirectoryExists(oPath);

				if (!temporarydirExists1) {
					
					createDirectory(oPath);

				}
				
			
				String newpath = oPath+"\\"+issueType+dateAndTime;
				
				System.out.println("newpath"+newpath);
				
				
				
				String downloadedfileName = tempFileDownloadedPath + "\\"
						+ getFile;
				
				changeToExcelType(downloadedfileName,newpath,downloadedStmtFileType);
				
				
				deleteFileFromDirectory(tempFileDownloadedPath);
			}
		}

			
		
			
		catch (Exception e) {
			// TODO Auto-generated catch block

			
			//e.printStackTrace();
		}
	
		
	}
	public String getFileNameFromdirectory(String dirPath)
			throws InterruptedException {
		String filename = null;
		try {

			File dir = new File(dirPath);

			Thread.sleep(2000);
			File[] files = dir.listFiles();
			if (files.length == 0) {
				System.out.println("The directory is empty");
			} else {
				for (File aFile : files) {
					System.out
							.println(aFile.getName() + " - " + aFile.length());

					filename = aFile.getName();
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
		}
		return filename;
	}

	
	public void deleteFileFromDirectory(String dirPath )
	{
		
		try {
			File directory = new File(dirPath);
			// get all the files from a directory
			File[] fList = directory.listFiles();
			if (fList.length == 0) {
				
				
				System.out.println("The directory is empty");
			}
			else
			{
			for (File file1 : fList) {
				
				String fname=file1.getName();
				
				File file = new File(dirPath+"\\"+fname);
				boolean result = file.delete();
				
				   if(result) {
				        System.out.println("File Deleted");
				      }
				      else {
				        System.out.println("File not Found");
				      }
			
}
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	
	
	public void temporavaryDirectoyCreateAndFileDelete(String fileType,String dpath)
	
	
	{
		
		boolean temporarydirExists = isDirectoryExists(dpath);

		if (!temporarydirExists) {
			// Temporavary folder is created
			createDirectory(dpath);

		}
		
		else
			
			
		{
			deleteFileFromDirectory(dpath);
		}
		
		
		
	}
	
	public void createFolder(String fileType,String tempFolder)
	{
		

		String outputFilePath = syspathValidation(downloadPath);
		String dirPath = outputFilePath + fileType;
		// String dirPath =sysPath+"\\"+bankName;
		System.out.println("Directory path" + dirPath);
		
		temporavaryDirectoyCreateAndFileDelete(fileType,dirPath);
		
		String outputFilePath1 = syspathValidation(dirPath);
		
		String oPath=outputFilePath1+tempFolder;
		
		temporavaryDirectoyCreateAndFileDelete(fileType,oPath);
		
	}
	
	public static String downlaodFilePathAssign(String fileType)
	{
		
		String outputFilePath = syspathValidation1(downloadPath);
		String dirPath = outputFilePath + fileType;
		
		
		tempdPath=dirPath;
		
		return tempdPath;
		
		
	}
	
	public RemoteWebDriver invokeApp(String browser, boolean bRemote,
			String fileType,String tempFolder ) {
		try {

			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setBrowserName(browser);
			dc.setPlatform(Platform.WINDOWS);

			String outputFilePath = syspathValidation(downloadPath);
			String dirPath = outputFilePath + fileType;
			// String dirPath =sysPath+"\\"+bankName;
			System.out.println("Directory path" + dirPath);
			
			temporavaryDirectoyCreateAndFileDelete(fileType,dirPath);
			
			String outputFilePath1 = syspathValidation(dirPath);
			
			String oPath=outputFilePath1+tempFolder;
			
			temporavaryDirectoyCreateAndFileDelete(fileType,oPath);

			// this is for grid run
			if (bRemote)
				driver = new RemoteWebDriver(new URL("http://" + sHubUrl + ":"
						+ sHubPort + "/wd/hub"), dc);
			else { // this is for local run
				if (browser.equalsIgnoreCase("chrome")) {
					System.setProperty("webdriver.chrome.driver",
							"./drivers/chromedriver.exe");

					Map<String, Object> preferences = new Hashtable<String, Object>();
					preferences.put("profile.default_content_settings.popups",
							0);
					preferences.put("download.prompt_for_download", "false");
					preferences.put("download.default_directory", dirPath);

					preferences.put("plugins.plugins_disabled", new String[] {
							"Adobe Flash Player", "Chrome PDF Viewer" });

					ChromeOptions options = new ChromeOptions();
					options.setExperimentalOption("prefs", preferences);

					options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

					driver = new ChromeDriver(options);

				}

				else if (browser.equalsIgnoreCase("ie")) {

					System.setProperty("webdriver.ie.driver",
							"./drivers/IEDriverServer.exe");
					driver = new InternetExplorerDriver();
				}

				else {
					System.setProperty("webdriver.gecko.driver",
							"./drivers/geckodriver.exe");
					driver = new FirefoxDriver();
				}
			}

			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.get(sUrl);

			primaryWindowHandle = driver.getWindowHandle();
			/*
			 * reportStep("The browser:" + browser + " launched successfully",
			 * "PASS");
			 */

		} catch (Exception e) {
			// e.printStackTrace();
			/*
			 * reportStep("The browser:" + browser + " could not be launched",
			 * "FAIL");
			 */
			// reportStep("The browser:" + browser + " could not be launched");
		}

		return driver;
	}
	public RemoteWebDriver invokeApp(String browser, boolean bRemote ) {
		try {

			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setBrowserName(browser);
			dc.setPlatform(Platform.WINDOWS);
/*
			String outputFilePath = syspathValidation(downloadPath);
			String dirPath = outputFilePath + fileType;
			// String dirPath =sysPath+"\\"+bankName;
			System.out.println("Directory path" + dirPath);
			
			temporavaryDirectoyCreateAndFileDelete(fileType,dirPath);
			
			String outputFilePath1 = syspathValidation(dirPath);
			
			String oPath=outputFilePath1+tempFolder;
			
			temporavaryDirectoyCreateAndFileDelete(fileType,oPath);
*/
			// this is for grid run
			if (bRemote)
				driver = new RemoteWebDriver(new URL("http://" + sHubUrl + ":"
						+ sHubPort + "/wd/hub"), dc);
			else { // this is for local run
				if (browser.equalsIgnoreCase("chrome")) {
					System.setProperty("webdriver.chrome.driver",
							"./drivers/chromedriver.exe");

					Map<String, Object> preferences = new Hashtable<String, Object>();
					preferences.put("profile.default_content_settings.popups",
							0);
					preferences.put("download.prompt_for_download", "false");
					preferences.put("download.default_directory", downloadPath);

					preferences.put("plugins.plugins_disabled", new String[] {
							"Adobe Flash Player", "Chrome PDF Viewer" });

					ChromeOptions options = new ChromeOptions();
					options.setExperimentalOption("prefs", preferences);

					options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

					driver = new ChromeDriver(options);

				}

				else if (browser.equalsIgnoreCase("ie")) {

					System.setProperty("webdriver.ie.driver",
							"./drivers/IEDriverServer.exe");
					driver = new InternetExplorerDriver();
				}

				else {
					System.setProperty("webdriver.gecko.driver",
							"./drivers/geckodriver.exe");
					driver = new FirefoxDriver();
				}
			}

			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.get(sUrl);

			primaryWindowHandle = driver.getWindowHandle();
			/*
			 * reportStep("The browser:" + browser + " launched successfully",
			 * "PASS");
			 */

		} catch (Exception e) {
			// e.printStackTrace();
			/*
			 * reportStep("The browser:" + browser + " could not be launched",
			 * "FAIL");
			 */
			// reportStep("The browser:" + browser + " could not be launched");
		}

		return driver;
	}

	/**
	 * This method will enter the value to the text field using id attribute to
	 * locate
	 * 
	 * @param idValue
	 *            - id of the webelement
	 * @param data
	 *            - The data to be sent to the webelement
	 * @author Babu - TestLeaf
	 * @throws IOException
	 * @throws COSVisitorException
	 */
	//

	public void downloadFTPFile(String fType) {
		
		try {
			System.out.println("ftpFlag" + ftpFlag);
			System.out.println("ftpPath" + ftpPath);
			System.out.println("ftpUsername" + ftpUsername);
			System.out.println("ftpPassword" + ftpPassword);
			
			
			

			if (ftpFlag != null) {

				if (ftpFlag.equalsIgnoreCase("Y")) {

					if ((ftpPath != null) && (ftpUsername != null)
							&& (ftpPassword != null))

					{

						String outputftpPath = syspathValidation(ftpPath);
						System.out.println("outputftpPath" + outputftpPath);
					//	String outputftpdownloadpPath = syspathValidation(downloadPath);
						//System.out.println("outputftpdownloadpPath" + outputftpdownloadpPath);
						String outputFilePath = syspathValidation(downloadPath);
						String dirPathFTP = outputFilePath + fType;
						String newpath = outputftpPath+fType;
						System.out.println("newpath" + newpath);

						boolean temporarydirExists = isDirectoryExists(newpath);

						if (!temporarydirExists) {
							// Temporavary folder is created
							createDirectory(newpath);

						}

						System.out.println("newpath" + newpath);
						try {
							// new ProcessBuilder(
							// "./drivers/Sterling_Sftp_Utility.exe",ftpUsername,
							// ftpPassword, newpath+ "."+
							// downloadedStmtFileType,outputftpPath +
							// outputFileName+ "_" + dateAndTime + "."+
							// downloadedStmtFileType)

							new ProcessBuilder(
									"./drivers/Sterling_Sftp_Utility.exe",
									ftpUsername, ftpPassword,dirPathFTP,newpath).start();
							System.out.println("FTP FILE SUCCESSFULL");
							/*
							 * System.out.println("moved to ftp" + ftpUsername + "-"
							 * + ftpPassword + "-" + newpath + "-" + outputftpPath +
							 * outputFileName + "_" + dateAndTime);
							 */

						} catch (Exception e) {

							System.out.println("FTP FILE ERROR");
						}
					}
				} else {

					System.out
							.println("FTP CREDENTIALS ARE REQUIRED IF FTPFlag IS Y");

				}

			} else {

				System.out.println("FTP CREDENTIALS ARE REQUIRED IF FTPFlag IS Y");

				System.out.println("FTP flag should be Y ");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void enterById(String idValue, String data) {
		try {
			driver.findElement(By.id(idValue)).clear();
			driver.findElement(By.id(idValue)).sendKeys(data);
			reportStep("The data: " + data + " entered successfully in field :"
					+ idValue, "PASS");
		} catch (NoSuchElementException e) {
			reportStep("The data: " + data
					+ " could not be entered in the field :" + idValue, "FAIL");
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while entering " + data
					+ " in the field :" + idValue, "FAIL");
		} catch (Exception e) {
			reportStep("Unknown exception occured while entering " + data
					+ " in the field :" + idValue, "FAIL");
		}
	}

	public void enterByIdwithTab(String idValue) {
		try {
			// driver.findElement(By.id(idValue)).clear();
			WebElement ele = driver.findElement(By.id(idValue));

			ele.sendKeys(Keys.TAB);
			// reportStep("The data: "+data+" entered successfully in field :"+idValue,
			// "PASS");
		} catch (NoSuchElementException e) {
			// reportStep("The data: "+data+" could not be entered in the field :"+idValue,
			// "FAIL");
		} catch (WebDriverException e) {
			// reportStep("Unknown exception occured while entering "+data+" in the field :"+idValue,
			// "FAIL");
		} catch (Exception e) {
			// reportStep("Unknown exception occured while entering "+data+" in the field :"+idValue,
			// "FAIL");
		}
	}

	/**
	 * This method will enter the value to the text field using name attribute
	 * to locate
	 * 
	 * @param nameValue
	 *            - name of the webelement
	 * @param data
	 *            - The data to be sent to the webelement
	 * @author Babu - TestLeaf
	 * @throws IOException
	 * @throws COSVisitorException
	 */

	// Enter the values using Name Locator
	public void enterByName(String nameValue, String data) {
		try {
			driver.findElement(By.name(nameValue)).clear();
			driver.findElement(By.name(nameValue)).sendKeys(data);
			reportStep("The data: " + data + " entered successfully in field :"
					+ nameValue, "PASS");

		} catch (NoSuchElementException e) {
			reportStep("The data: " + data
					+ " could not be entered in the field :" + nameValue,
					"FAIL");
		} catch (Exception e) {
			reportStep("Unknown exception occured while entering " + data
					+ " in the field :" + nameValue, "FAIL");
		}

	}

	/**
	 * This method will enter the value to the text field using name attribute
	 * to locate
	 * 
	 * @param xpathValue
	 *            - xpathValue of the webelement
	 * @param data
	 *            - The data to be sent to the webelement
	 * @author Babu - TestLeaf
	 * @throws IOException
	 * @throws COSVisitorException
	 */
	public void enterByXpath(String xpathValue, String data) {

		System.out.println("entrer");
		try {
			driver.findElement(By.xpath(xpathValue)).clear();
			driver.findElement(By.xpath(xpathValue)).sendKeys(data);
			reportStep("The data: " + data + " entered successfully in field :"
					+ xpathValue, "PASS");

		} catch (NoSuchElementException e) {
			reportStep("The data: " + data
					+ " could not be entered in the field :" + xpathValue,
					"FAIL");
		} catch (Exception e) {
			reportStep("Unknown exception occured while entering " + data
					+ " in the field :" + xpathValue, "FAIL");
		}

	}

	/**
	 * This method will verify the title of the browser
	 * 
	 * @param title
	 *            - The expected title of the browser
	 * @author Babu - TestLeaf
	 */
	public boolean verifyTitle(String title) {
		boolean bReturn = false;
		try {
			if (driver.getTitle().equalsIgnoreCase(title)) {
			/*	reportStep("The title of the page matches with the value :"
						+ title, "PASS");*/
				bReturn = true;
			} else {
				// reportStep("The title of the page:"+driver.getTitle()+" did not match with the value :"+title,
				// "SUCCESS");
			}

		} catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title",
					"FAIL");
		}
		return bReturn;
	}

	/**
	 * This method will verify the given text matches in the element text
	 * 
	 * @param xpath
	 *            - The locator of the object in xpath
	 * @param text
	 *            - The text to be verified
	 * @author Babu - TestLeaf
	 */
	public void verifyTextByXpath(String xpath, String text) {
		try {
			String sText = driver.findElementByXPath(xpath).getText();

			System.out.println("Displayed text" + sText);
			if (sText.equalsIgnoreCase(text)) {
				reportStep("The text: " + sText + " matches with the value :"
						+ text, "PASS");
			} else {
				reportStep("The text: " + sText
						+ " did not match with the value :" + text, "FAIL");
			}
		} catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title",
					"FAIL");
		}
	}
	
	
	

	/**
	 * This method will verify the given text is available in the element text
	 * 
	 * @param xpath
	 *            - The locator of the object in xpath
	 * @param text
	 *            - The text to be verified
	 * @author Babu - TestLeaf
	 */
	public boolean verifyTextContainsByXpath(String xpath, String text) {
		boolean res = false;
		try {

			String sText = driver.findElementByXPath(xpath).getText();
			if (sText.contains(text)) {
				res = true;
				// reportStep("The text: "+sText+" contains the value :"+text,
				// "PASS");
			} else {
				res = false;
				reportStep("The text: " + sText
						+ " did not contain the value :" + text, "FAIL");

			}
		} catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title",
					"FAIL");
		}

		return res;
	}

	public boolean verifyTextContainsByName(String name, String text) {
		boolean res = false;
		try {

			String sText = driver.findElementByName(name).getText();
			if (sText.contains(text)) {
				res = true;
				reportStep("The text: " + sText + " contains the value :"
						+ text, "PASS");
			} else {
				res = false;
				/*
				 * reportStep("The text: " + sText +
				 * " did not contain the value :" + text, "FAIL");
				 */
			}
		} catch (Exception e) {
			/*
			 * reportStep("Unknown exception occured while verifying the title",
			 * "FAIL");
			 */}

		return res;
	}

	/**
	 * This method will verify the given text is available in the element text
	 * 
	 * @param id
	 *            - The locator of the object in id
	 * @param text
	 *            - The text to be verified
	 * @author Babu - TestLeaf
	 */
	public void verifyTextById(String id, String text) {
		try {
			String sText = driver.findElementById(id).getText();
			if (sText.equalsIgnoreCase(text)) {
				reportStep("The text: " + sText + " matches with the value :"
						+ text, "PASS");
			} else {
				reportStep("The text: " + sText
						+ " did not match with the value :" + text, "FAIL");
			}
		} catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title",
					"FAIL");
		}
	}

	/**
	 * This method will verify the given text is available in the element text
	 * 
	 * @param id
	 *            - The locator of the object in id
	 * @param text
	 *            - The text to be verified
	 * @author Babu - TestLeaf
	 */
	public void verifyTextContainsById(String id, String text) {
		try {
			String sText = driver.findElementById(id).getText();
			if (sText.contains(text)) {
				reportStep("The text: " + sText + " contains the value :"
						+ text, "PASS");
			} else {
				reportStep("The text: " + sText
						+ " did not contain the value :" + text, "FAIL");
			}
		} catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title",
					"FAIL");
		}
	}

	/**
	 * This method will close all the browsers
	 * 
	 * @author Babu - TestLeaf
	 */
	public void quitBrowser() {
		try {
			driver.quit();
		} catch (Exception e) {
			/*
			 * reportStep("The browser:" +
			 * driver.getCapabilities().getBrowserName() +
			 * " could not be closed.", "FAIL");
			 */
		}

	}

	/**
	 * This method will click the element using id as locator
	 * 
	 * @param id
	 *            The id (locator) of the element to be clicked
	 * @author Babu - TestLeaf
	 */
	public void clickById(String id) {
		try {

			Thread.sleep(3000);

			driver.findElement(By.id(id)).click();

			reportStep("The element with id: " + id + " is clicked.", "PASS");

		} catch (NoSuchElementException e) {
			reportStep("The element with id: " + id + " could not be clicked.",
					"FAIL");
		}

		catch (Exception e) {
			reportStep("The element with id: " + id + " could not be clicked.",
					"FAIL");
		}
	}

	public void clickByIdWithoutSnap(String id) {
		try {

			Thread.sleep(3000);

			driver.findElement(By.id(id)).click();

			// reportStep("The element with id: " + id + " is clicked.",
			// "PASS");

		} catch (NoSuchElementException e) {
			// reportStep("The element with id: " + id +
			// " could not be clicked.","FAIL");
		}

		catch (Exception e) {
			// reportStep("The element with id: " + id +
			// " could not be clicked.","FAIL");
		}
	}

	public void scrollAndClickById(String id) {
		try {

			Thread.sleep(10000);

			/*
			 * String text= driver.findElement(By.id(id)).getAttribute("id");
			 * System.out.println("output......."+text);
			 */
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(By.id(id));
			js.executeScript("arguments[0].click();", element);

			Thread.sleep(5000);

			// btnStartScan

			reportStep("The element with id: " + id + " is clicked.", "PASS");

		} catch (NoSuchElementException e) {
			reportStep("The element with id: " + id + " could not be clicked.",
					"FAIL");
		}

		catch (Exception e) {
			reportStep("The element with id: " + id + " could not be clicked.",
					"FAIL");
		}
	}

	public void ClickByIdAlert(String id) {
		try {

			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(By.id(id));
			js.executeScript("arguments[0].click();", element);

		} catch (NoSuchElementException e) {
		}

		catch (Exception e) {

		}
	}

	public void frames() {

		// Assume driver is initialized properly.
		List<WebElement> ele = driver.findElements(By.tagName("frame"));
		System.out.println("Number of frames in a page :" + ele.size());
		for (WebElement el : ele) {
			// Returns the Id of a frame.
			System.out.println("Frame Id :" + el.getAttribute("id"));
			// Returns the Name of a frame.
			System.out.println("Frame name :" + el.getAttribute("name"));
		}

	}

	public void clickLoginById(String id) {
		try {

			Thread.sleep(3000);

			driver.findElement(By.id(id)).click();

			Thread.sleep(4000);

			WebDriverWait wait = new WebDriverWait(driver, 2);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			System.out.println(alert.getText());
			alert.accept();
			Thread.sleep(3000);

			WebDriverWait wait1 = new WebDriverWait(driver, 2);
			wait1.until(ExpectedConditions.alertIsPresent());
			Alert alert1 = driver.switchTo().alert();
			System.out.println(alert1.getText());
			alert1.accept();

			// Thread.sleep(5000);
			System.out.println("Login button is clicked");

		} catch (NoSuchElementException e) {

			// System.out.println("Login button is not clicked");

		}

		catch (Exception e) {

			// System.out.println("Login button is not clicked");

		}
	}

	public void displayReport(String text) {
		try {

			reportStep("Error message : " + text + " is shown.", "PASS");

		} catch (NoSuchElementException e) {
			// reportStep("The element with id: "+id+" could not be clicked.",
			// "FAIL");
		}

		catch (Exception e) {
			// reportStep("The element with id: "+id+" could not be clicked.",
			// "FAIL");
		}
	}

	public void clickByIdAfterCloseAlert(String id) {
		try {

			driver.findElement(By.id(id)).click();
			acceptAlert();
			reportStep("The element with id: " + id + " is clicked.", "PASS");

		} catch (NoSuchElementException e) {
			reportStep("The element with id: " + id + " could not be clicked.",
					"FAIL");
		}

		catch (Exception e) {
			reportStep("The element with id: " + id + " could not be clicked.",
					"FAIL");
		}
	}

	/**
	 * This method will click the element using id as locator
	 * 
	 * @param id
	 *            The id (locator) of the element to be clicked
	 * @author Babu - TestLeaf
	 */
	public void clickByClassName(String classVal) {
		try {
			driver.findElement(By.className(classVal)).click();
			reportStep("The button with class Name: " + classVal
					+ " is clicked.", "PASS");
		} catch (NoSuchElementException e) {
			reportStep("The button with class Name: " + classVal
					+ " could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The button with class Name: " + classVal
					+ " could not be clicked.", "FAIL");
		}
	}

	/**
	 * This method will click the element using name as locator
	 * 
	 * @param name
	 *            The name (locator) of the element to be clicked
	 * @author Babu - TestLeaf
	 */
	public void clickByName(String name) {
		try {
			driver.findElement(By.name(name)).click();
			reportStep("The element with name: " + name + " is clicked.",
					"PASS");
		} catch (Exception e) {
			reportStep("The element with name: " + name
					+ " could not be clicked.", "FAIL");
		}
	}

	/**
	 * This method will click the element using link name as locator
	 * 
	 * @param name
	 *            The link name (locator) of the element to be clicked
	 * @author Babu - TestLeaf
	 */
	public void clickByLink(String name) {
		// driver.findElementByLinkText(name).click();
		try {
			driver.findElementByLinkText(name).click();
			reportStep("The element with link name: " + name + " is clicked.",
					"PASS");
		} catch (WebDriverException e) {
			reportStep("The element with link name: " + name
					+ " could not be clicked.", "FAIL");
		}
	}

	public void clickByLinkNoSnap(String name) {
		try {
			driver.findElement(By.linkText(name)).click();
			// reportStep("The element with link name: "+name+" is clicked.",
			// "PASS");
		} catch (Exception e) {
			reportStep("The element with link name: " + name
					+ " could not be clicked.", "FAIL");
		}
	}

	/**
	 * This method will click the element using xpath as locator
	 * 
	 * @param xpathVal
	 *            The xpath (locator) of the element to be clicked
	 * @author Babu - TestLeaf
	 */
	public void clickByXpath(String xpathVal) {
		try {
			driver.findElement(By.xpath(xpathVal)).click();
			// reportStep("The element : "+xpathVal+" is clicked.", "PASS");
		} catch (Exception e) {
			// reportStep("The element with xpath: "+xpathVal+" could not be clicked.",
			// "FAIL");
		}
	}

	public void clickByXpathNoSnap(String xpathVal) {
		try {
			driver.findElement(By.xpath(xpathVal)).click();
			System.out.println("1st Lead clicked");
			// reportStep("The element : "+xpathVal+" is clicked.", "PASS");
		} catch (WebDriverException e) {
			reportStep("The element with xpath: " + xpathVal
					+ " could not be clicked.", "FAIL");
		}
	}

	/**
	 * This method will mouse over on the element using xpath as locator
	 * 
	 * @param xpathVal
	 *            The xpath (locator) of the element to be moused over
	 * @author Babu - TestLeaf
	 */
	public void mouseOverByXpath(String xpathVal) {
		try {
			new Actions(driver)
					.moveToElement(driver.findElement(By.xpath(xpathVal)))
					.build().perform();
			reportStep("The mouse over by xpath : " + xpathVal
					+ " is performed.", "PASS");
		} catch (Exception e) {
			reportStep("The mouse over by xpath : " + xpathVal
					+ " could not be performed.", "FAIL");
		}
	}

	/**
	 * This method will mouse over on the element using link name as locator
	 * 
	 * @param xpathVal
	 *            The link name (locator) of the element to be moused over
	 * @author Babu - TestLeaf
	 */
	public void mouseOverByLinkText(String linkName) {
		try {
			new Actions(driver)
					.moveToElement(driver.findElement(By.linkText(linkName)))
					.build().perform();
			reportStep("The mouse over by link : " + linkName
					+ " is performed.", "PASS");
		} catch (Exception e) {
			reportStep("The mouse over by link : " + linkName
					+ " could not be performed.", "FAIL");
		}
	}

	/**
	 * This method will return the text of the element using xpath as locator
	 * 
	 * @param xpathVal
	 *            The xpath (locator) of the element
	 * @author Babu - TestLeaf
	 */
	public String getTextByXpath(String xpathVal) {
		String bReturn = "";
		try {
			return driver.findElement(By.xpath(xpathVal)).getText();
		} catch (Exception e) {
			reportStep("The element with xpath: " + xpathVal
					+ " could not be found.", "FAIL");
		}
		return bReturn;
	}

	/**
	 * This method will return the text of the element using id as locator
	 * 
	 * @param xpathVal
	 *            The id (locator) of the element
	 * @author Babu - TestLeaf
	 */
	public String getTextById(String idVal) {
		String bReturn = "";
		try {
			return driver.findElementById(idVal).getText();
		} catch (Exception e) {
			reportStep(
					"The element with id: " + idVal + " could not be found.",
					"FAIL");
		}
		return bReturn;
	}

	public String getTextByName(String idVal) {
		String bReturn = "";
		try {
			return driver.findElementByName(idVal).getText();
		} catch (Exception e) {
			/*
			 * reportStep( "The element with id: " + idVal +
			 * " could not be found.", "FAIL");
			 */
		}
		return bReturn;
	}

	/**
	 * This method will select the drop down value using id as locator
	 * 
	 * @param id
	 *            The id (locator) of the drop down element
	 * @param value
	 *            The value to be selected (visibletext) from the dropdown
	 * @author Babu - TestLeaf
	 */
	public void selectVisibileTextById(String id, String value) {
		try {
			new Select(driver.findElement(By.id(id)))
					.selectByVisibleText(value);
			;
			reportStep("The element with id: " + id
					+ " is selected with value :" + value, "PASS");
		} catch (Exception e) {
			reportStep("The value: " + value + " could not be selected.",
					"FAIL");
		}
	}

	public void selectVisibileTextByName(String name, String value) {
		try {
			new Select(driver.findElement(By.name(name)))
					.selectByVisibleText(value);
			;
			reportStep("The element with id: " + name
					+ " is selected with value :" + value, "PASS");
		} catch (Exception e) {
			reportStep("The value: " + value + " could not be selected.",
					"FAIL");
		}
	}

	public void selectVisibileTextByXPath(String xpath, String value) {
		try {
			new Select(driver.findElement(By.xpath(xpath)))
					.selectByVisibleText(value);
			;
			reportStep("The element with xpath: " + xpath
					+ " is selected with value :" + value, "PASS");
		} catch (Exception e) {
			reportStep("The value: " + value + " could not be selected.",
					"FAIL");
		}
	}

	public void selectIndexById(String id, String value) {
		try {
			new Select(driver.findElement(By.id(id))).selectByIndex(Integer
					.parseInt(value));
			;
			reportStep("The element with id: " + id
					+ " is selected with index :" + value, "PASS");
		} catch (Exception e) {
			reportStep("The index: " + value + " could not be selected.",
					"FAIL");
		}
	}

	public void switchToParentWindow() {
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String wHandle : winHandles) {
				driver.switchTo().window(wHandle);
				break;
			}
		} catch (Exception e) {
			reportStep("The window could not be switched to the first window.",
					"FAIL");
		}
	}

	public void switchToLastWindow() {
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String wHandle : winHandles) {
				driver.switchTo().window(wHandle);
			}
		} catch (Exception e) {
			reportStep("The window could not be switched to the last window.",
					"FAIL");
		}
	}

	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert could not be accepted.", "FAIL");
		}

	}

	public String getAlertText() {
		String text = null;
		try {
			driver.switchTo().alert().dismiss();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert could not be accepted.", "FAIL");
		}
		return text;

	}

	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert could not be accepted.", "FAIL");
		}

	}

	public long takeSnap() {
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;
		try {
			FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE),
					new File("./reports/images/" + number + ".jpg"));
		} catch (WebDriverException e) {
			// reportStep("The browser has been closed.", "FAIL");
		} catch (IOException e) {
			// reportStep("The snapshot could not be taken", "WARN");
		}
		return number;
	}

	public void clickMenu(String mainMenu, String menuOption) {
		// TODO Auto-generated method stub

		// locate the menu to hover over using its xpath
		try {
			WebElement menu = driver.findElement(By.id(mainMenu));

			Actions builder = new Actions(driver);

			builder.moveToElement(menu).build().perform();

			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.id(menuOption)));

			WebElement menuOp = driver.findElement(By.id(menuOption));

			menuOp.click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void clickMenu(String mainMenu, String subMenu, String subMenuOption) {
		// TODO Auto-generated method stub
		try {
			WebElement menu1 = driver.findElement(By.id(mainMenu));

			Actions builder1 = new Actions(driver);

			builder1.moveToElement(menu1).build().perform();

			WebElement subMenuele = driver.findElement(By.id(subMenu));

			Actions builder2 = new Actions(driver);

			builder2.moveToElement(subMenuele).build().perform();

			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.id(subMenuOption)));

			WebElement menuOp = driver.findElement(By.id(subMenuOption));

			menuOp.click();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean verifyTextPresentErrMsg(String text) {
		// TODO Auto-generated method stub
		boolean res = false;
		try {

			String sText = driver.findElement(By.cssSelector("body")).getText();

			if (sText.contains(text)) {
				res = true;
				// reportStep("The text: "+sText+" contains the value :"+text,
				// "PASS");
				reportStep("The text: contains the value :" + text, "PASS");
			}
		} catch (Exception e) {
			reportStep("Unknown exception occured while verifying the content",
					"FAIL");
		}

		return res;
	}

	public boolean verifyPartialTextPresentErrMsg(String text) {
		// TODO Auto-generated method stub
		boolean res = false;
		try {

			String sText = driver.findElement(By.cssSelector("body")).getText();

			if (sText.contains(text)) {
				res = true;
				reportStep("The text: contains the value :" + text, "PASS");
			}
		} catch (Exception e) {
			reportStep("Unknown exception occured while verifying the content",
					"FAIL");
		}

		return res;
	}

	public boolean verifyDatAvailable(String actualText, String expectedText) {
		// TODO Auto-generated method stub
		boolean res = false;
		try {

			// String sText =
			// driver.findElement(By.cssSelector("body")).getText();

			if (actualText.equals(expectedText)) {
				res = true;
				reportStep("The text: " + actualText + " contains the value :"
						+ expectedText, "PASS");
			}
		} catch (Exception e) {
			reportStep("Unknown exception occured while verifying the content",
					"FAIL");
		}

		return res;
	}

	public void switchFrame(int frameIndex) {
		// TODO Auto-generated method stub

		try {
			driver.switchTo().frame(frameIndex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void switchFrame1(String frameName) {
		// TODO Auto-generated method stub

		try {
			driver.switchTo().frame(frameName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<String> readBackendCentralInwardTrades(String tablename,
			String barcode, String packageNo) throws Exception {

		// Calendar
		// password : 2019$icentral

		// store all the values to list
		List<String> lst = new ArrayList<String>();

		int r = 0;
		int RowCount;
		try {

			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriver);

			// Setup the connection with the DB
			connect = DriverManager.getConnection("jdbc:oracle:thin:@"
					+ dbServer + ":" + dbPort + ":" + dbName, dbUser, dbPwd);

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();

			// Result set get the result of the SQL query

			// While Loop to iterate through all data and print results

			resultSet = statement
					.executeQuery("select BARCODE,PACKAGE_NO from " + tablename
							+ " where " + tablename + ".BARCODE='" + barcode
							+ "' AND " + tablename + ".PACKAGE_NO='"
							+ packageNo + "'");
			// + "'");
			// + "' AND "+ tablename + ".PACKAGE_NO='" +packageNo+"'");

			ResultSetMetaData metadata = resultSet.getMetaData();
			int numberOfColumns = metadata.getColumnCount();
			while (resultSet.next()) {
				int i = 1;
				while (i <= numberOfColumns) {

					// System.out.println("Column value"+resultSet.getString(i));
					lst.add(resultSet.getString(i++));
					/*
					 * for (int j = 0; j <=numberOfColumns-1; j++){
					 * System.out.println("USERID" + resultSet.getString(j));
					 * 
					 * };
					 */
				}
			}
			// closing DB Connection
			connect.close();

		} catch (Exception e) {
			// cMethods.justReport("The sql query has not been executed due to exception :",
			// "FAILED");
			e.printStackTrace();
			throw e;
		} finally {
			// close();
		}

		return lst;

	}

	/*public RemoteWebDriver invokeApp(String browser) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	public void mergeDownloadedFile(String issueType,String mergeFname,String fileExtn,String tempFolder ) throws IOException
	{
		String outputFPath = syspathValidation(downloadPath);
		String directoryName = outputFPath + issueType+ "\\" + tempFolder;
		String mergeFileName=outputFPath + mergeFname + "." + fileExtn;
			
	
	mergeExcelFiles(new File(mergeFileName),directoryName);
	
	}
	
	public static void mergeExcelFiles(File file,String directoryName ) throws IOException {
		XSSFWorkbook book = new XSSFWorkbook();
		XSSFSheet sheet = book.createSheet(file.getName());
	//	String directoryName = "D:\\Auto\\downloadfile\\Bug";
		//D:\Auto\downloadfile\Bug
		File directory = new File(directoryName);
		// get all the files from a directory
		File[] fList = directory.listFiles();

		for (File file1 : fList) {
			if (file1.isFile()) {
				String ParticularFile = file1.getName();
				FileInputStream fin = new FileInputStream(new File(
						directoryName + "\\" + ParticularFile));
				XSSFWorkbook b = new XSSFWorkbook(fin);
				for (int i = 0; i < b.getNumberOfSheets(); i++) {
					copySheets(book, sheet, b.getSheetAt(i));
				}
			}

			try {
				writeFile(book, file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

		}
	}

	protected static void writeFile(XSSFWorkbook book, File file)
			throws Exception {
		FileOutputStream out = new FileOutputStream(file);
		book.write(out);
		out.close();
	}

	private static void copySheets(XSSFWorkbook newWorkbook,
			XSSFSheet newSheet, XSSFSheet sheet) {
		copySheets(newWorkbook, newSheet, sheet, true);
	}

	private static void copySheets(XSSFWorkbook newWorkbook,
			XSSFSheet newSheet, XSSFSheet sheet, boolean copyStyle) {
		  int newRownumber = newSheet.getLastRowNum();
		  int getFirstRowNum=0;
		System.out.println("newRownumber"+newRownumber);
		int maxColumnNum = 0;
		Map<Integer, XSSFCellStyle> styleMap = (copyStyle) ? new HashMap<Integer, XSSFCellStyle>()
				: null;
		
		if (newRownumber==0)
			
		{
			getFirstRowNum=sheet.getFirstRowNum();
		}
		else
		{
			
			getFirstRowNum=sheet.getFirstRowNum()+1;
		}

		for (int i = getFirstRowNum;i <= sheet.getLastRowNum(); i++) {
			
			  int sheetgetFirstRowNum = sheet.getFirstRowNum();
			//	System.out.println("sheetgetFirstRowNum"+sheetgetFirstRowNum);
			XSSFRow srcRow = sheet.getRow(i);
			XSSFRow destRow = newSheet.createRow(i + newRownumber);
			if (srcRow != null) {
				copyRow(newWorkbook, sheet, newSheet, srcRow, destRow, styleMap);
				if (srcRow.getLastCellNum() > maxColumnNum) {
					maxColumnNum = srcRow.getLastCellNum();
				}
			}
		}
		for (int i = 0; i <= maxColumnNum; i++) {
			newSheet.setColumnWidth(i, sheet.getColumnWidth(i));
		}
	}

	public static void copyRow(XSSFWorkbook newWorkbook, XSSFSheet srcSheet,
			XSSFSheet destSheet, XSSFRow srcRow, XSSFRow destRow,
			Map<Integer, XSSFCellStyle> styleMap) {
		destRow.setHeight(srcRow.getHeight());
		for (int j = srcRow.getFirstCellNum(); j <= srcRow.getLastCellNum(); j++) {
			XSSFCell oldCell = srcRow.getCell(j);
			XSSFCell newCell = destRow.getCell(j);
			if (oldCell != null) {
				if (newCell == null) {
					newCell = destRow.createCell(j);
				}
				copyCell(newWorkbook, oldCell, newCell, styleMap);
			}
		}
	}

	public static void copyCell(XSSFWorkbook newWorkbook, XSSFCell oldCell,
			XSSFCell newCell, Map<Integer, XSSFCellStyle> styleMap) {
		if (styleMap != null) {
			int stHashCode = oldCell.getCellStyle().hashCode();
			XSSFCellStyle newCellStyle = styleMap.get(stHashCode);
			if (newCellStyle == null) {
				newCellStyle = newWorkbook.createCellStyle();
				newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
				styleMap.put(stHashCode, newCellStyle);
			}
			newCell.setCellStyle(newCellStyle);
		}
		switch (oldCell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			newCell.setCellValue(oldCell.getRichStringCellValue());
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			newCell.setCellValue(oldCell.getNumericCellValue());
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			newCell.setCellType(XSSFCell.CELL_TYPE_BLANK);
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			newCell.setCellValue(oldCell.getBooleanCellValue());
			break;
		case XSSFCell.CELL_TYPE_ERROR:
			newCell.setCellErrorValue(oldCell.getErrorCellValue());
			break;
		case XSSFCell.CELL_TYPE_FORMULA:
			newCell.setCellFormula(oldCell.getCellFormula());
			break;
		default:
			break;
		}
	}
}
