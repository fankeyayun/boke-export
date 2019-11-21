package com.example.export.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    public static void Excel(){
        HSSFWorkbook workbook = new HSSFWorkbook();
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("姓名");
        arrayList.add("性别");
        arrayList.add("身份证号");
        arrayList.add("联系方式");
        arrayList.add("出生年月日");
        String[] array = arrayList.toArray(new String[arrayList.size()]);


        HSSFSheet sheet = workbook.createSheet(); // 创建一个Sheet页
        workbook.setSheetName(0, "信息表");
        // 设置表格默认列宽度为20个字节
        CellRangeAddress region1 = new CellRangeAddress(0, 0, (short) 0, (short) array.length-1);
        sheet.addMergedRegion(region1);
        sheet.setDefaultColumnWidth((short) 36);
        sheet.setColumnWidth(0,20*255);
        sheet.setColumnWidth(1,10*255);
        sheet.setColumnWidth(2,15*255);
        sheet.setColumnWidth(3,40*155);
        sheet.setColumnWidth(4,20*255);
        // 生成标题样式
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFDataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("@"));
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 13);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);

        //说明行的样式
        CellStyle ExplainStyle = workbook.createCellStyle();
        ExplainStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //底部边框
        ExplainStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        ExplainStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        ExplainStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        ExplainStyle.setFillForegroundColor(HSSFColor.WHITE.index);

        // 说明表格
        HSSFRow row1 = sheet.createRow(0); //第一行
        row1.setHeight((short)400);
        Cell cell1 = row1.createCell((short) 0);  //说明单元格
        cell1.setCellValue("详细说明 ");
        cell1.setCellType(Cell.CELL_TYPE_STRING);
        cell1.setCellStyle(ExplainStyle);
        for(int j=1;j<=5;j++){
            cell1=row1.createCell(j);
            cell1.setCellStyle(ExplainStyle);
        }


        //第一行合并单元
        CellRangeAddress regin2 = new CellRangeAddress(1, 1, 0, 5);
        sheet.addMergedRegion(regin2);

        // 标题行
        HSSFRow row2 = sheet.createRow(1); //第二行
        row2.setHeight((short)500);
        HSSFCell cell2 = row2.createCell((short) 0);//创建单元格
        cell2.setCellStyle(style);
        cell2.setCellValue("个人基础信息");
        cell2.setCellType(Cell.CELL_TYPE_STRING);
        for(int f=2;f<=5;f++){
            cell1=row2.createCell(f);
            cell1.setCellStyle(style);
        }

        // 表头行
        HSSFRow row = sheet.createRow(2);  //第三行
        row.setHeight((short)500);
        for (int i = 0; i < array.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            cell.setCellStyle(style);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            HSSFRichTextString text = new HSSFRichTextString(array[i]);
            cell.setCellValue(text.toString());
        }
        String [] list={"男","女"};
        CellRangeAddressList regions = new CellRangeAddressList(3,20,1,1);
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(list);
        HSSFDataValidation data_validation = new HSSFDataValidation(regions,constraint);
        sheet.addValidationData(data_validation);
        List<List<String>> result = null;
        // 遍历集合数据，产生数据行
        if (result != null) {
            int index = 3;
            for (List<String> m : result) {
                row = sheet.createRow(index);
                int cellIndex = 0;
                for (String str : m) {
                    HSSFCell cell = row.createCell((short) cellIndex);
                    cell.setCellValue(str.toString());
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cellIndex++;
                }
                index++;
            }
            // 设置单元格格式为文本格式
            HSSFCellStyle textStyle = workbook.createCellStyle();
            HSSFDataFormat format1 = workbook.createDataFormat();
            textStyle.setDataFormat(format1.getFormat("@"));
            //日期格式
            HSSFCellStyle dateStyle = workbook.createCellStyle();
            HSSFDataFormat dataFormat= workbook.createDataFormat();
            dateStyle.setDataFormat(dataFormat.getFormat("yyyy/mm/dd"));
            //设置单元格格式为"文本"
            sheet.setDefaultColumnStyle(3, textStyle);//第三列
            sheet.setDefaultColumnStyle(4,textStyle);//第四列
            sheet.setDefaultColumnStyle(5,dateStyle);//第五列
            sheet.setDefaultRowHeight((short) ((short)18*20));
        }
    }
}
