package com.wangyin.autotest.utils;

import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * IO操作工具类.
 *
 * @author chenyun313@gmail.com, 2015-03-24.
 * @version 1.0
 * @since 1.0
 */
public class IOUtils {

    private static String CHARSET_CODE = "UTF-8";

    public static String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        InputStream input = null;
        BufferedReader reader = null;

        try {
            input = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(input, CHARSET_CODE);
            reader = new BufferedReader(streamReader);
            content.append(reader.readLine());

            String str = null;
            while ((str = reader.readLine()) != null) {
                content.append("\n");
                content.append(str);
            }
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(input);
            org.apache.commons.io.IOUtils.closeQuietly(reader);
        }

        return content.toString();
    }

    public static Properties readProperties(File file) throws IOException {
        Properties properties = new Properties();
        InputStream input = null;
        BufferedReader reader = null;

        try {
            input = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(input, CHARSET_CODE);

            properties.load(streamReader);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(input);
            org.apache.commons.io.IOUtils.closeQuietly(reader);
        }

        return properties;
    }

    public static void storeProperties(Map<String, String> data, File file) throws IOException {

        Properties properties = new Properties();
        properties.putAll(data);

        OutputStream output = null;

        try {
            output = new FileOutputStream(file);
            properties.store(output, new Date().toString());
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(output);
        }
    }

    public static void writeFile(String content, File file, boolean append) throws IOException {
        File folder = new File(file.getParent());

        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        OutputStream output = null;
        try {
            byte[] bytes = content.getBytes(CHARSET_CODE);

            output = new FileOutputStream(file, append);
            output.write(bytes);
            output.flush();

        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(output);
        }
    }
}
