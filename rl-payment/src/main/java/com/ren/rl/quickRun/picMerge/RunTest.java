package com.ren.rl.quickRun.picMerge;

/**
 * @Description TODO
 * @Author rp
 * @Date 2024/4/23 22:40
 */
public class RunTest {
    public static void main(String[] args) {
        String[] fileList = FileUtil.getFileList("C:\\test_folder");
        String target = "C:\\test_folder\\merge.pdf";
        ImgPDF.ImgChangePDF(fileList,target);
    }
}
