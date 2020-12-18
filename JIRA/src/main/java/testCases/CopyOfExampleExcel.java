package testCases;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class CopyOfExampleExcel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getSheet("CR");
	}
	

	public static String[][] getSheet(String dataSheetName) {

		String[][] data = null;

		try {
			FileInputStream fis = new FileInputStream(
					"D:\\Auto\\downloadfile\\" + dataSheetName + ".xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			 XSSFWorkbook wb = new XSSFWorkbook();
	            XSSFSheet sheetnew = wb.createSheet("Sheet1");
	            

			// get the number of rows
			int rowCount = sheet.getLastRowNum();

			XSSFCell myCell;

			// get the number of columns
			int columnCount = sheet.getRow(0).getLastCellNum();
			data = new String[rowCount][columnCount];

			// loop through the rows
			// for(int i=0; i <rowCount+1; i++){
			try {
				XSSFRow row = sheet.getRow(0);
				for (int j = 0; j <= 21; j++) { // loop through the columns
					try {
						String cellValue = "";
						try {

							myCell = row.getCell(j);
							
							

							// if (myCell != null) {

							switch (j) {

							case 0:

								if (myCell != null) {

									if (!row.getCell(j).getStringCellValue()
											.equals("Summary")) {
										row.createCell(j,CellType.STRING);
										
										//ExcelOpener eo=new ExcelOpener("D:\\Auto\\downloadfile\\" + dataSheetName + ".xlsx","D:\\Auto\\downloadfile\\" + dataSheetName + ".xlsx");
										//eo.insertNewColumnBefore(0,j);
										
										
									//	shiftColumns(0,1 ,1);

									}

								}

								else {

									row.createCell(j);

									myCell = row.getCell(j);

									myCell.setCellValue("Summary");

								}
								// "Custom_field__Data_Warehouse_Application_"
								break;

							case 21:

								if (myCell != null) {

									if (!row.getCell(j)
											.getStringCellValue()
											.equals("Custom_field__Data_Warehouse_Application_")) {

										row.createCell(j);
										myCell.setCellValue("Custom_field__Data_Warehouse_Application_");

									}

								}

								else {

									row.createCell(j);

									myCell = row.getCell(j);

									myCell.setCellValue("Custom_field__Data_Warehouse_Application_");

								}
								// "Custom_field__Data_Warehouse_Application_"
								break;

							}

							// }

							/*
							 * if (myCell == null) {
							 * 
							 * row.createCell(j); myCell = row.getCell(j);
							 * 
							 * if (j == 21) { myCell.setCellValue(
							 * "Custom_field__Data_Warehouse_Application_");
							 * 
							 * }
							 * 
							 * }
							 */

							cellValue = row.getCell(j).getStringCellValue();

						} catch (NullPointerException e) {

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			// } //Write the workbook in file system
			try {
				FileOutputStream fileOut = new FileOutputStream(
						"D:\\Auto\\downloadfile\\" + dataSheetName + ".xlsx");
				workbook.write(fileOut);
				fileOut.close();
				workbook.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;

	}

}
