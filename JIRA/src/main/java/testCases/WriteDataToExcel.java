package testCases;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
public class WriteDataToExcel {
	public static void main(String args[]) throws Exception
    {
        FileInputStream fis = new FileInputStream("D:\\Auto\\downloadfile\\"+ "CR" + ".xlsx");
        FileOutputStream fos = null;
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = null;
        XSSFCell cell = null;
        XSSFFont font = workbook.createFont();
        XSSFCellStyle style = workbook.createCellStyle();
 
        row = sheet.getRow(0);
        if(row == null)
            row = sheet.createRow(1);
 
        cell = row.getCell(4);
        if(cell == null)
 
        	cell = row.createCell(1);
        
        fos = new FileOutputStream("D:\\Auto\\downloadfile\\"+ "CRNew" + ".xlsx");
        workbook.write(fos);
        fos.close();
    }
}
