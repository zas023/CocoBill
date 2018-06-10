package com.copasso.cocobill.utils;

import com.copasso.cocobill.model.bean.local.BBill;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExportExcel<T> {


    public void exportExcel(Collection<T> dataset, OutputStream out) {

        exportExcel("测试POI导出EXCEL文档", null, dataset, out, "yyyy-MM-dd");

    }


    public void exportExcel(String[] headers, Collection<T> dataset,

                            OutputStream out) {

        exportExcel("测试POI导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd");

    }


    public void exportExcel(String[] headers, Collection<T> dataset,

                            OutputStream out, String pattern) {

        exportExcel("测试POI导出EXCEL文档", headers, dataset, out, pattern);

    }


    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param title   表格标题名
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                <p>
     *                javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */

    @SuppressWarnings("unchecked")

    public void exportExcel(String title, String[] headers,

                            Collection<T> dataset, OutputStream out, String pattern) {

        // 声明一个工作薄

        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个表格

        HSSFSheet sheet = workbook.createSheet(title);

        // 设置表格默认列宽度为15个字节

        sheet.setDefaultColumnWidth((short) 15);


        // 声明一个画图的顶级管理器

        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        // 定义注释的大小和位置,详见文档

        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));

        // 设置注释内容

        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));

        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.

        comment.setAuthor("leno");


        //产生表格标题行

        HSSFRow row = sheet.createRow(0);

        for (short i = 0; i < headers.length; i++) {

            HSSFCell cell = row.createCell(i);

            HSSFRichTextString text = new HSSFRichTextString(headers[i]);

            cell.setCellValue(text);

        }


        //遍历集合数据，产生数据行

        Iterator<T> it = dataset.iterator();

        int index = 0;

        while (it.hasNext()) {

            index++;

            row = sheet.createRow(index);

            T t = (T) it.next();

            //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值

            Field[] fields = t.getClass().getDeclaredFields();

            for (short i = 0; i < fields.length; i++) {

                HSSFCell cell = row.createCell(i);

                Field field = fields[i];

                String fieldName = field.getName();

                String getMethodName = "get"

                        + fieldName.substring(0, 1).toUpperCase()

                        + fieldName.substring(1);

                try {

                    Class tCls = t.getClass();

                    Method getMethod = tCls.getMethod(getMethodName,

                            new Class[]{});

                    Object value = getMethod.invoke(t, new Object[]{});

                    //判断值的类型后进行强制类型转换

                    String textValue = null;

                    if (value instanceof Boolean) {

                        boolean bValue = (Boolean) value;

                        textValue = "男";

                        if (!bValue) {

                            textValue = "女";

                        }

                    } else if (value instanceof Date) {

                        Date date = (Date) value;

                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

                        textValue = sdf.format(date);

                    } else if (value instanceof byte[]) {

                        // 有图片时，设置行高为60px;

                        row.setHeightInPoints(60);

                        // 设置图片所在列宽度为80px,注意这里单位的一个换算

                        sheet.setColumnWidth(i, (short) (35.7 * 80));

                        // sheet.autoSizeColumn(i);

                        byte[] bsValue = (byte[]) value;

                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,

                                1023, 255, (short) 6, index, (short) 6, index);

                        //anchor.setAnchorType(2);

                        patriarch.createPicture(anchor, workbook.addPicture(

                                bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));

                    } else {

                        //其它数据类型都当作字符串简单处理

                        textValue = value.toString();

                    }

                    //如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成

                    if (textValue != null) {

                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");

                        Matcher matcher = p.matcher(textValue);

                        if (matcher.matches()) {

                            //是数字当作double处理

                            cell.setCellValue(Double.parseDouble(textValue));

                        } else {

                            HSSFRichTextString richString = new HSSFRichTextString(textValue);

                            HSSFFont font3 = workbook.createFont();

                            font3.setColor(HSSFColor.BLUE.index);

                            richString.applyFont(font3);

                            cell.setCellValue(richString);

                        }

                    }

                } catch (SecurityException e) {

                    // TODO Auto-generated catch block

                    e.printStackTrace();

                } catch (NoSuchMethodException e) {

                    // TODO Auto-generated catch block

                    e.printStackTrace();

                } catch (IllegalArgumentException e) {

                    // TODO Auto-generated catch block

                    e.printStackTrace();

                } catch (IllegalAccessException e) {

                    // TODO Auto-generated catch block

                    e.printStackTrace();

                } catch (InvocationTargetException e) {

                    // TODO Auto-generated catch block

                    e.printStackTrace();

                } finally {

                    //清理资源

                }

            }


        }

        try {

            workbook.write(out);

        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

    }

}
