package testCases;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


import java.util.HashMap;
import java.util.Map;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public  FileInputStream fis;
	public  FileOutputStream fileOut;
	public  XSSFWorkbook wb;
	public  XSSFSheet sh;
	public  XSSFCell cell;
	public  XSSFRow row;
	public  XSSFCellStyle cellstyle;
	public  XSSFColor mycolor;
	        
	      /*  public static void main(String []args) throws Exception {
	            ExcelUtil excel = new ExcelUtil();
	            excel.setExcelFile("./testData.xlsx", "Sheet1");
	      
	        }
  
*/
    public  void setExcelFile(String ExcelPath,String SheetName) throws Exception
    {  
       try{
          File f = new File(ExcelPath);
          if(!f.exists())
          {
             f.createNewFile();
             System.out.println("File doesn't exist, so created!");
           }  
           fis=new FileInputStream(ExcelPath);
           wb=new XSSFWorkbook(fis);
           sh = wb.getSheet(SheetName);
           //sh = wb.getSheetAt(0); //0 - index of 1st sheet
           if (sh == null)
           {
               sh = wb.createSheet(SheetName);
           }  
        }catch (Exception e){System.out.println(e.getMessage());}
    }
    
    public  void setCellData(String text, int rownum, int colnum,String ExcelPath ) throws Exception
    {
     try{   
        row  = sh.getRow(rownum);
        if(row ==null)
        {
           row = sh.createRow(rownum);
        }
        cell = row.getCell(colnum);
       if (cell != null) 
        {
            cell.setCellValue(text);
        } 
        else 
        {
             cell = row.createCell(colnum);
             cell.setCellValue(text);  
        }
        fileOut = new FileOutputStream(ExcelPath);
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
     }catch(Exception e){throw (e);} }
    public String getCellData(int rownum, int colnum) throws Exception{
        try{
            cell = sh.getRow(rownum).getCell(colnum);
            String CellData = null;
           // switch (cell.getCellType())
            FormulaEvaluator formulaEvaluator=wb.getCreationHelper().createFormulaEvaluator();  
             switch (formulaEvaluator.evaluateInCell(cell).getCellType())  
            {
                case Cell.CELL_TYPE_STRING:
                    CellData = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC: 
                    if (DateUtil.isCellDateFormatted(cell))
                    {
                        CellData = String.valueOf(cell.getDateCellValue());
                    }
                    else
                    {
                        CellData = String.valueOf((long)cell.getNumericCellValue());
                    }
                    break;
             /*   case BOOLEAN:
                    CellData = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    CellData = "";
                    break;*/
            }
            return CellData;
        }catch (Exception e){
            return"";
        }
    }

 

}

