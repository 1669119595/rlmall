package com.ren.rl.quickRun.picMerge;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @Description TODO
 * @Author rp
 * @Date 2024/4/23 22:40
 */
public class RunTest {

    public static void main(String[] args) {
        String byteStr = ImgPDF.ImgChangePDF(getBase64s());
        byte[] decode = Base64.getDecoder().decode(byteStr);
        // 和pdf再合并
        try{
            File pdfFile = new File("C:\\test_folder\\MFA Registration Guide.pdf");
            FileInputStream in = new FileInputStream(pdfFile);
            byte[] fileContent = new byte[(int) pdfFile.length()];
            in.read(fileContent);
            String pdfBaseStr = Base64.getEncoder().encodeToString(fileContent);
            List<String> bytesList = new ArrayList<>();
            bytesList.add(byteStr);
            bytesList.add(pdfBaseStr);
            mergeDocument2PdfString(bytesList);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
//        try{
//            File file = new File("C:\\test_folder\\mergeV3.pdf");
//            FileOutputStream out = new FileOutputStream(file);
//            out.write(decode);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
    }

    private static String[] getBase64s() {
        String[] fileList = FileUtil.getFileList("C:\\test_folder");
        String[] strList = new String[10];
        for (int i = 0; i < fileList.length; i++) {
            try {
                if(fileList[i] == null) {
                    break;
                }
                // Read the file into a byte array
                File file = new File(fileList[i]);
                byte[] fileContent = new byte[(int) file.length()];
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    inputStream.read(fileContent);
                }
                // Encode the byte array to base64
                String base64String = Base64.getEncoder().encodeToString(fileContent);
                strList[i] = base64String;
            } catch (IOException e) {
                System.err.println("Error occurred while reading or encoding the file: " + e.getMessage());
            }
        }
        return strList;
    }

    private static String mergeDocument2PdfString(List<String> base64List) {
        if (base64List.isEmpty()) {
            return null;
        }
        try (PDDocument mergedDocument = new PDDocument()){
            for (String base64String : base64List) {
                byte[] pdfBytes = Base64.getDecoder().decode(base64String);
                try (ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes)) {
                    PDDocument document = PDDocument.load(inputStream);
                    for (int i = 0; i < document.getNumberOfPages(); i++) {
                        mergedDocument.addPage(document.getPage(i));
                    }
                }
            }
            // Convert merged PDF to base64 encoded String
//			ByteArrayOutputStream out = new ByteArrayOutputStream();

            mergedDocument.save("C:\\test_folder\\mergedV4_includePdf.pdf");
            mergedDocument.close();
//            return Base64.getEncoder().encodeToString(out.toByteArray());
            return "good";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
