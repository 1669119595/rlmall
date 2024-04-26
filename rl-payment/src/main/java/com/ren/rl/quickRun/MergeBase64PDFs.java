package com.ren.rl.quickRun;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MergeBase64PDFs {
    public static void main(String[] args) throws IOException {

        List<String> base64Strings = getBase64s();

        // 创建一个新的PDF文档
        PDDocument mergedDocument = new PDDocument();

        for (String base64String : base64Strings) {
            // 解码Base64字符串为字节数组
            byte[] pdfBytes = Base64.getDecoder().decode(base64String);

            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes)) {
                PDDocument document = convertToPDF(inputStream, "png");
                // 加载PDF字节数据到一个临时的PDDocument对象
                for (int i = 0; i < document.getNumberOfPages(); i++) {
                    mergedDocument.addPage(document.getPage(i));
                }
            }
        }
        // convert merged PDF to base64 encoded String
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mergedDocument.save(out);
        byte[] byteArray = out.toByteArray();
        String base64AfterMerged = Base64.getEncoder().encodeToString(byteArray);
        System.out.println("-----");
        // save the merged document
//        mergedDocument.save("C:\\test_folder\\merged.pdf");
//        mergedDocument.close();
    }

    private static PDDocument convertToPDF(ByteArrayInputStream inputStream, String originalFormat) {
        PDDocument document = new PDDocument();

        try {
            if (originalFormat.equals("jpg") || originalFormat.equals("png")) {
                // Example: Convert image to PDF
                PDPage page = new PDPage();
                document.addPage(page);

                PDImageXObject image = PDImageXObject.createFromByteArray(document, inputStream.readAllBytes(), originalFormat);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.drawImage(image, 0, 0); // Adjust position as needed
                }
            } else {
                // Handle unsupported formats
                System.err.println("Unsupported image format: " + originalFormat);
                return null;
            }

            return document;
        } catch (IOException e) {
            System.err.println("Error converting image to PDF: " + e.getMessage());
            return null;
        }
    }

    private static List<String> getBase64s() {

        List<String> base64s = new ArrayList<>();
        List<String> filePaths = new ArrayList<>();
        filePaths.add("C:\\test_folder\\pic1.png");
        filePaths.add("C:\\test_folder\\pic2.png");
        for (int i = 0; i < filePaths.size(); i++) {
            try {

                // Read the file into a byte array
                File file = new File(filePaths.get(i));
                byte[] fileContent = new byte[(int) file.length()];
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    inputStream.read(fileContent);
                }

                // Encode the byte array to base64
                String base64String = Base64.getEncoder().encodeToString(fileContent);
                base64s.add(base64String);
            } catch (IOException e) {
                System.err.println("Error occurred while reading or encoding the file: " + e.getMessage());
            }
        }
        return base64s;
    }
}
