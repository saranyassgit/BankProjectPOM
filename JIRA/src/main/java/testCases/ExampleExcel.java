package testCases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExampleExcel {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		getSheet("CR");
	}

	public static void getSheet(String dataSheetName) throws Exception {

		// String[][] data = null;
		FileOutputStream fileOut;

		FileInputStream fis = new FileInputStream("D:\\Auto\\downloadfile\\"
				+ dataSheetName + ".xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);

		// get the number of rows
		int rowCount = sheet.getLastRowNum();

		XSSFCell myCell;
		XSSFWorkbook wb;
		XSSFSheet sh;
		XSSFCell cell;
		XSSFRow row;
		// get the number of columns
		int columnCount = sheet.getRow(0).getLastCellNum();
		// data = new String[rowCount][columnCount];

		for (int i = 0; i < rowCount + 1; i++) {

			XSSFRow row1 = sheet.getRow(i);

			for (int j = 1; j < columnCount; j++) { // loop through the columns

				String cellValue = "";

				cellValue = row1.getCell(j).getStringCellValue();

				String ExcelPath = "D:\\Auto\\downloadfile\\" + "BugsNew"
						+ ".xlsx";
				String SheetName = "sheet1";

				File f = new File(ExcelPath);
				if (!f.exists()) {
					f.createNewFile();
					System.out.println("File doesn't exist, so created!");
				}
				FileInputStream fis_write = new FileInputStream(ExcelPath);
				wb = new XSSFWorkbook(fis_write);
				sh = wb.getSheet(SheetName);
				// sh = wb.getSheetAt(0); //0 - index of 1st sheet
				if (sh == null) {
					sh = wb.createSheet(SheetName);
				}

				row = sh.getRow(i);
				if (row == null) {
					row = sh.createRow(i);
				}
				cell = row.getCell(j);
				if (cell != null) {
					cell.setCellValue(cellValue);
				} else {
					cell = row.createCell(j);
					cell.setCellValue(cellValue);
				}
				fileOut = new FileOutputStream(ExcelPath);
				wb.write(fileOut);
				fileOut.flush();
				fileOut.close();

			}
		}

	}

}
