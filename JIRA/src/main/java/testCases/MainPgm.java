package testCases;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Calendar;


import org.testng.TestNG;

public class MainPgm {


	public static String initialStartRunTimeSession = null;

	public static void main(String[] args) throws IOException {

		try {

			TestNG testng = new TestNG();
			Class[] classes = new Class[] { TC001_BugCRFileDownlaod.class};
			testng.setTestClasses(classes);
			testng.run();

		} catch (Exception e) {
			// TODO Auto-generated catch block

		}

	}

	public static String getAppRunningDateTime() {

		Calendar currentDate = Calendar.getInstance();
		String TIME_NOW = "MM-dd-yyyy HH-mm-ss";
		SimpleDateFormat stf1 = new SimpleDateFormat(TIME_NOW);
		String reportTime = stf1.format(currentDate.getTime());

		return reportTime;

	}

}