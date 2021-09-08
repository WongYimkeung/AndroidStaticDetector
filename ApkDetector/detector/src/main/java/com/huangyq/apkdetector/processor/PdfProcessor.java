package com.huangyq.apkdetector.processor;

import com.huangyq.apkdetector.Context;
import com.huangyq.apkdetector.Processor;
import com.huangyq.apkdetector.info.CallInfo;
import com.huangyq.apkdetector.info.MethodInfo;
import com.huangyq.apkdetector.info.PermissionInfo;
import com.huangyq.apkdetector.util.LogUtil;
import com.huangyq.apkdetector.util.Util;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.FileNotFoundException;

import static com.huangyq.apkdetector.Constant.STRING_PDF_NAME;
import static com.huangyq.apkdetector.Constant.STRING_PDF_TITLE;

public class PdfProcessor implements Processor {
    private Document document;
    private Context context;

    @Override
    public boolean process(Context context) {
        this.context = context;
        LogUtil.start("PdfProcessor");
        processResult();
        LogUtil.end("PdfProcessor");
        return true;
    }

    private void processResult() {
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(
                    Util.getPdfName(STRING_PDF_NAME)));
            document = new Document(pdfDocument);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Paragraph title = new Paragraph(STRING_PDF_TITLE);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFontSize(24);
        document.add(title);

        Paragraph methodParagraph = new Paragraph("Method List");
        methodParagraph.setFontSize(18);
        methodParagraph.setMarginTop(50);
        methodParagraph.setMarginBottom(16);
        document.add(methodParagraph);

        Table methodTable = new Table(new UnitValue[]{
                new UnitValue(UnitValue.PERCENT, 25),
                new UnitValue(UnitValue.PERCENT, 5),
                new UnitValue(UnitValue.PERCENT, 70)}, true);
        methodTable.addCell("method");
        methodTable.addCell("count");
        methodTable.addCell("detail");

        for (MethodInfo methodInfo : context.getMethodInfoList()) {
            methodTable.addCell(methodInfo.getName());
            methodTable.addCell(methodInfo.getCallInfoSize() + "");
            LogUtil.d("name: " + methodInfo.getName());
            LogUtil.d("smaliCode: " + methodInfo.getSmaliCode());
            LogUtil.d("smaliClass: " + methodInfo.getSmaliClass());
            LogUtil.d("description: " + methodInfo.getDescription());
            StringBuilder stringBuilder = new StringBuilder();
            for (CallInfo callInfo : methodInfo.getCallInfoList()) {
                stringBuilder.append("fileName: " + callInfo.getFileName() + "\n");
                stringBuilder.append("filePath: " + callInfo.getFilePath() + "\n");
                stringBuilder.append("callPlace: " + callInfo.getCallPlace() + "\n");
                stringBuilder.append("lineNumber: " + callInfo.getLineNumber() + "\n");
                stringBuilder.append("lineContent: " + callInfo.getLineContent() + "\n\n");
                LogUtil.d("\tfileName: " + callInfo.getFileName());
                LogUtil.d("\tfilePath: " + callInfo.getFilePath());
                LogUtil.d("\tcallPlace: " + callInfo.getCallPlace());
                LogUtil.d("\tlineNumber: " + callInfo.getLineNumber());
                LogUtil.d("\tlineContent: " + callInfo.getLineContent());
            }
            methodTable.addCell(stringBuilder.toString());
            LogUtil.d("");
        }
        for (int i = 0; i < 3; i++) {
            methodTable.addCell("");
        }
        document.add(methodTable);

        Paragraph permissionParagraph = new Paragraph("Permission List");
        permissionParagraph.setFontSize(18);
        permissionParagraph.setMarginTop(50);
        permissionParagraph.setMarginBottom(16);
        document.add(permissionParagraph);

        Table permissionTable = new Table(new UnitValue[]{
                new UnitValue(UnitValue.PERCENT, 25),
                new UnitValue(UnitValue.PERCENT, 25),
                new UnitValue(UnitValue.PERCENT, 25),
                new UnitValue(UnitValue.PERCENT, 25)}, true);
        permissionTable.addCell("Name");
        permissionTable.addCell("Description");
        permissionTable.addCell("Level");
        permissionTable.addCell("Declared");

        for (PermissionInfo permissionInfo : context.getPermissionInfoList()) {
            LogUtil.d("name: " + permissionInfo.getName());
            LogUtil.d("description: " + permissionInfo.getDescription());
            LogUtil.d("level: " + permissionInfo.getLevel());
            LogUtil.d("isDeclared: " + (permissionInfo.isDeclared() ? "Y" : "N"));
            permissionTable.addCell(permissionInfo.getName());
            permissionTable.addCell(permissionInfo.getDescription());
            permissionTable.addCell(permissionInfo.getLevel());
            permissionTable.addCell(permissionInfo.isDeclared() ? "Y" : "N");
            LogUtil.d("");
        }
        for (int i = 0; i < 4; i++) {
            permissionTable.addCell("");
        }
        document.add(permissionTable);
        document.close();
    }
}
