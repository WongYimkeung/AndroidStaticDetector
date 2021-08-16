package com.huangyq.sdkdetector.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IOUtil {
    public static List<String> read(String path) throws FileNotFoundException {
        if (null == path || path.length() == 0) {
            LogUtil.r("read path is nul");
            return Collections.emptyList();
        }

        return read(new File(path));
    }

    public static List<String> read(File file) throws FileNotFoundException {
        if (null == file || !file.exists()) {
            LogUtil.r("read file is not exists");
            return Collections.emptyList();
        }

        return read(new FileReader(file));
    }

    public static List<String> read(InputStream inputStream) {
        if (null == inputStream) {
            return Collections.emptyList();
        }

        return read(new InputStreamReader(inputStream));
    }

    public static List<String> read(Reader reader) {
        if (null == reader) {
            return Collections.emptyList();
        }

        List<String> stringList = new ArrayList<>();
        try {
            String result;
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((result = bufferedReader.readLine()) != null) {
                stringList.add(result.trim()); // 去除前后空格，避免无法正确处理
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }
}
