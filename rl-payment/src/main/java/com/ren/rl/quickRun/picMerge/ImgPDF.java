package com.ren.rl.quickRun.picMerge;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;


/**
 * @Description TODO
 * @Author rp
 * @Date 2024/4/23 22:38
 */
/**
 * 图片转换PDF类
 */
public class ImgPDF {
    /**
     * 将图片转换成PDF
     * @param source        文件路径的集合 可以调用 FileUtil.getFileList() 方法
     */
    public static String ImgChangePDF(String[] source) {
        //创建一个文档对象
        Document doc = new Document();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            //定义输出文件的位置
            PdfWriter.getInstance(doc, out);
            //开启文档
            doc.open();
            // 循环获取图片文件夹内的图片
            for (int i = 0; i < source.length; i++) {
                if(source[i] == null){      //前面的方法默认了数组长度是1024，所以这里就让它提前退出循环
                    break;
                }
                byte[] picBytes = Base64.getDecoder().decode(source[i]);
                //路径
                Image img = Image.getInstance(picBytes);
                //获得宽高
                Float h = img.getHeight();
                Float w = img.getWidth();
                //统一压缩
                float percent = getPercent(h, w);
                System.out.println("h:" + h + ",w:" + w + ",percent: " + percent);
                //图片居中
                img.setAlignment(Image.MIDDLE);
                //百分比显示图
                img.scalePercent(percent);
                //设置高和宽的比例
                doc.add(img);
                // 设置间距
                Paragraph paragraph = new Paragraph();
                paragraph.setSpacingBefore(5);
                doc.add(paragraph);
            }
            // 关闭文档
            if(doc != null){
                doc.close();
            }
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 压缩
     * @param
     */
    public static float getPercent(Float h,Float w)
    {
        float g;
        Float g2;
        g2=480f/w*100 * 1000;
        g=Math.round(g2) / 1000f;
        return g;
    }
}
