package com.juxinli.tools;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/2.
 */
@Component
public class ReadExcel {

    //判断excel版本
    private Workbook openWorkbook(InputStream in, String fileFileName) throws IOException {
        Workbook wb = null;
        if (fileFileName.contains(".xlsx")) {
            wb = new XSSFWorkbook(in);//Excel 2007
        } else {
            wb = (Workbook) new HSSFWorkbook(in);//Excel 2003
        }
        return wb;
    }

    public List<String[]> getExcelData(InputStream in, String fileFileName) throws Exception {
//        InputStream in = new FileInputStream(fileName);    //创建输入流
        Workbook wb = openWorkbook(in, fileFileName);// 获取Excel文件对象
        Sheet sheet = wb.getSheetAt(0);// 获取文件的指定工作表m 默认的第一个
        List<String[]> list = new ArrayList<String[]>();
        Row row = null;
        Cell cell = null;
        int totalRows = sheet.getPhysicalNumberOfRows();    // 总行数
        int totalCells = sheet.getRow(0).getPhysicalNumberOfCells();//总列数

        for (int r = 0; r < totalRows; r++) {
            row = sheet.getRow(r);
            String[] arr = new String[totalCells];
            for (int c = 0; c < totalCells; c++) {
                cell = row.getCell(c);
                String cellValue = "";
                if (null != cell) {
                    // 以下是判断数据的类型
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                            cellValue = cell.getNumericCellValue() + "";
//	时间格式
//                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
//                                Date dd = cell.getDateCellValue();
//                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                cellValue = df.format(dd);
//                            }
                            break;
                        case HSSFCell.CELL_TYPE_STRING: // 字符串
                            cellValue = cell.getStringCellValue();
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                            cellValue = cell.getBooleanCellValue() + "";
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA: // 公式
                            cellValue = cell.getCellFormula() + "";
                            break;
                        case HSSFCell.CELL_TYPE_BLANK: // 空值
                            cellValue = "";
                            break;
                        case HSSFCell.CELL_TYPE_ERROR: // 故障
                            cellValue = "非法字符";
                            break;
                        default:
                            cellValue = "未知类型";
                            break;
                    }
                    arr[c] = cellValue;
                }
            }
            list.add(arr);
        }
        // 返回值集合
        return list;
    }

}
