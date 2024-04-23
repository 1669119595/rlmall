package com.ren.rl.quickRun;

import com.twelvemonkeys.imageio.stream.ByteArrayImageInputStream;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

public class MergePicBase64 {
    public static void main(String[] args) throws IOException {

        List<String> base64Strings = getBase64s();

        // 创建一个新的PDF文档
        PDFMergerUtility merger = new PDFMergerUtility();
        PDDocument document1 = newPdfDocument();

        // https://www.appblog.cn/index.php/2024/01/21/java-itextpdf-merge-images-pdf/
        for (String base64String : base64Strings) {
            // 解码Base64字符串为字节数组
            byte[] pdfBytes = Base64.getDecoder().decode(base64String);

            try {
                byte[] pdfs = image2Pdf(pdfBytes, false, 0.0F, 0, 0);
//                PDDocument document = convertToPDF(inputStream, "png");
                ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfs);
                PDDocument document = PDDocument.load(inputStream);
                // 加载PDF字节数据到一个临时的PDDocument对象
                merger.appendDocument(document1, document);
                merger.setDestinationFileName("merged.pdf");
                merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        // convert merged PDF to base64 encoded String
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        mergedDocument.save(out);
//        byte[] byteArray = out.toByteArray();
//        String base64AfterMerged = Base64.getEncoder().encodeToString(byteArray);

        // save the merged document
        document1.save("C:\\test_folder\\merged.pdf");
        document1.close();
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

    private static float jpegQuality = 0.75F;

    private static int pdfOutputBufferSize = 1048576;

    private static PDDocument newPdfDocument() {
        return new PDDocument();
    }

    public static byte[] image2Pdf(byte[] bytes, boolean jpegOnly, float jpegQuality, int minW, int minH) {
        if (jpegQuality <= 0.0F) {
            jpegQuality = 0.75F;
        }

        try {
            PDDocument doc = newPdfDocument();

            byte[] var38;
            try {
                PDPage page = new PDPage(PDRectangle.A4);
                doc.addPage(page);
                PDRectangle cropBox = page.getCropBox();
                ImageInputStream imgIs = new ByteArrayImageInputStream(bytes);
                Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imgIs);


                ImageReader reader = (ImageReader)imageReaders.next();
                ImageReadParam param = reader.getDefaultReadParam();
                reader.setInput(imgIs, true, true);

                BufferedImage bufferedImage;
                try {
                    bufferedImage = reader.read(0, param);
                } finally {
                    reader.dispose();
                    imgIs.close();
                }

                bufferedImage = resize(bufferedImage, minW, minH);
                PDImageXObject pdImage;
                if (jpegOnly) {
                    if (bufferedImage.getTransparency() == 2) {
                        pdImage = LosslessFactory.createFromImage(doc, bufferedImage);
                    } else {
                        pdImage = JPEGFactory.createFromImage(doc, bufferedImage, jpegQuality);
                    }
                } else if (reader.getFormatName().toLowerCase().contains("jpeg")) {
                    pdImage = JPEGFactory.createFromImage(doc, bufferedImage, jpegQuality);
                } else {
                    pdImage = LosslessFactory.createFromImage(doc, bufferedImage);
                }

                PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.OVERWRITE, true, true);

                try {
                    float pageW = cropBox.getWidth();
                    float pageH = cropBox.getHeight();
                    float w = (float)pdImage.getWidth();
                    float h = (float)pdImage.getHeight();
                    float sw = w < pageW ? 1.0F : pageW / w;
                    float sh = h < pageH ? 1.0F : pageH / h;
                    float scale = Math.min(sw, sh);
                    w *= scale;
                    h *= scale;
                    AffineTransform transform = new AffineTransform(w, 0.0F, 0.0F, h, 0.0F, pageH - h);
                    Matrix matrix = new Matrix(transform);
                    contentStream.drawImage(pdImage, matrix);
                } catch (Throwable var33) {
                    try {
                        contentStream.close();
                    } catch (Throwable var32) {
                        var33.addSuppressed(var32);
                    }

                    throw var33;
                }

                contentStream.close();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1048576);
                doc.save(outputStream);
                var38 = outputStream.toByteArray();
            } catch (Throwable var35) {
                if (doc != null) {
                    try {
                        doc.close();
                    } catch (Throwable var31) {
                        var35.addSuppressed(var31);
                    }
                }

                throw var35;
            }

            if (doc != null) {
                doc.close();
            }

            return var38;
        } catch (IOException var36) {
            throw new RuntimeException();
        }
    }

    public static BufferedImage resize(BufferedImage inputImage, int minW, int minH) {
        if (minW > 0 && minH > 0) {
            float w = (float)inputImage.getWidth();
            float h = (float)inputImage.getHeight();
            float sw = w < (float)minW ? 1.0F : (float)minW / w;
            float sh = h < (float)minH ? 1.0F : (float)minH / h;
            float scale = Math.max(sw, sh);
            if (scale < 1.0F) {
                int newW = Math.round(w * scale);
                int newH = Math.round(h * scale);
                int newType = inputImage.getType() == 0 ? 6 : inputImage.getType();
                BufferedImage outputImage = new BufferedImage(newW, newH, newType);
                Graphics2D g2d = outputImage.createGraphics();
                g2d.drawImage(inputImage, 0, 0, newW, newH, (ImageObserver)null);
                g2d.dispose();
                return outputImage;
            } else {
                return inputImage;
            }
        } else {
            return inputImage;
        }
    }
}

