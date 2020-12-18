package testCases;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelApiTest {
	 public FileInputStream fis = null;
	    public FileOutputStream fos = null;
	    public XSSFWorkbook workbook = null;
	    public XSSFSheet sheet = null;
	    public XSSFRow row = null;
	    public XSSFCell cell = null;
	    String xlFilePath;
	 
	    public ExcelApiTest(String xlFilePath) throws Exception
	    {
	        this.xlFilePath = xlFilePath;
	        fis = new FileInputStream(xlFilePath);
	        workbook = new XSSFWorkbook(fis);
	        fis.close();
	    }
	 
	    public boolean setCellData(String sheetName, int colNumber, int rowNum, String value)
	    {
	        try
	        {
	            sheet = workbook.getSheet(sheetName);
	            row = sheet.getRow(rowNum);
	            if(row==null)
	                row = sheet.createRow(rowNum);
	 
	            cell = row.getCell(colNumber);
	            if(cell == null)
	                cell = row.createCell(colNumber);
	 
	            cell.setCellValue(value);
	 
	            fos = new FileOutputStream(xlFilePath);
	            workbook.write(fos);
	            fos.close();
	        }
	        catch (Exception ex)
	        {
	            ex.printStackTrace();
	            return  false;
	        }
	        return true;
	    }
}
