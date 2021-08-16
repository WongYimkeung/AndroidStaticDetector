package com.huangyq.sdkdetector.processor;

import com.huangyq.sdkdetector.Context;
import com.huangyq.sdkdetector.Processor;
import com.huangyq.sdkdetector.info.CallInfo;
import com.huangyq.sdkdetector.info.MethodInfo;
import com.huangyq.sdkdetector.util.LogUtil;
import com.huangyq.sdkdetector.util.Util;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.FileNotFoundException;
import java.util.List;

import static com.huangyq.sdkdetector.Constant.STRING_PDF_NAME;

public class PdfProcessor implements Processor {
    private Document document;

    @Override
    public boolean process(Context context) {
        processResult(context.getMethodInfoList());
        return true;
    }

    private void processResult(List<MethodInfo> methodInfoList) {
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(
                    Util.getPdfName(STRING_PDF_NAME)));
            document = new Document(pdfDocument);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Paragraph title = new Paragraph("");
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFontSize(24);
        document.add(title);

        Table table = new Table(new UnitValue[]{
                new UnitValue(UnitValue.PERCENT, 25),
                new UnitValue(UnitValue.PERCENT, 5),
                new UnitValue(UnitValue.PERCENT, 70)}, true);
        table.addCell("method");
        table.addCell("count");
        table.addCell("detail");

        LogUtil.start("PdfProcessor");
        for (MethodInfo methodInfo : methodInfoList) {
            table.addCell(methodInfo.getName());
            table.addCell(methodInfo.getCallInfoSize() + "");
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
            table.addCell(stringBuilder.toString());
            LogUtil.d("");
        }
        document.add(table);
        document.close();
        LogUtil.end("PdfProcessor");
    }
}
