package com.huangyq.apkdetector.processor;

import com.huangyq.apkdetector.Context;
import com.huangyq.apkdetector.Processor;
import com.huangyq.apkdetector.info.PermissionInfo;
import com.huangyq.apkdetector.util.LogUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;

import static com.huangyq.apkdetector.Constant.*;

public class ManifestProcessor implements Processor {
    private final Processor processor;
    private Context context;

    public ManifestProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public boolean process(Context context) {
        this.context = context;
        processManifest(context.getAndroidManifestPath());
        return processor.process(context);
    }

    private void processManifest(String manifestPath) {
        LogUtil.start("ManifestProcessor");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new File(manifestPath));
            Element root = document.getRootElement();

            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();

                if (element.getName().equals(STRING_MANIFEST_USES_PERMISSION)) {
                    processPermission(element.attributeValue("name"));
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        LogUtil.end("ManifestProcessor");
    }

    private void processPermission(String permission) {
        boolean isPermissionContain = false;

        for (PermissionInfo permissionInfo : context.getPermissionInfoList()) {
            if (permissionInfo.getName().equals(permission)) {
                permissionInfo.setDeclared(true);
                isPermissionContain = true;
                break;
            }
        }

        if (!isPermissionContain) {
            context.getPermissionInfoList().add(new PermissionInfo(
                    permission, "", "", true));
        }
    }
}
