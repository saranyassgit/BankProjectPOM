package testCases;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteDataToExcelUsingUtilClass {
	public static void main(String args[]) throws Exception {
		FileInputStream fis = new FileInputStream("D:\\Auto\\downloadfile\\"
				+ "CR" + ".xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheetnew = wb.createSheet("Sheet1");
		ExcelApiTest edata = new ExcelApiTest("D:\\Auto\\downloadfile\\"
				+ "CRNew" + ".xlsx");
		// get the number of rows
		int rowCount = sheet.getLastRowNum();

		XSSFCell myCell;

		// get the number of columns
	//	int columnCount = sheet.getRow(0).getLastCellNum();

		for (int i = 0; i < rowCount + 1; i++) {

			XSSFRow row = sheet.getRow(i);
			for (int j = 0; j <= 1; j++) { // loop through the columns

				myCell = row.getCell(j);
				String cellValue = "";

				cellValue = row.getCell(j).getStringCellValue();

				System.out.println("1"
						+ sheet.getRow(0).getCell(0).getStringCellValue());
				
				System.out.println(i +j);
				
                 if(i==0 && j==0)
                 {
				if (!sheet.getRow(0).getCell(0).getStringCellValue()
						.equals("Summary")) {
					

					cellValue = "Summary";
					edata.setCellData("CR.xlsx", j, i, cellValue);

					cellValue = row.getCell(j).getStringCellValue();
					j = j + 1;
				}

                 }
                 
               
                
               

				edata.setCellData("CR.xlsx", j, i, cellValue);
			}

		}

	}
}
