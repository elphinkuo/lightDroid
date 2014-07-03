package com.elphin.framework.util;

import android.text.TextUtils;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-7-16
 * Time: 下午2:01
 */
public final class IOUitls {

    private IOUitls() {
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        if (!(inputStream instanceof BufferedInputStream)) {
            inputStream = new BufferedInputStream(inputStream);
        }

        if (!(outputStream instanceof BufferedOutputStream)) {
            outputStream = new BufferedOutputStream(outputStream);
        }

        final byte[] buf = new byte[512];
        int count = -1;
        while ((count = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, count);
        }
        outputStream.flush();
    }

    public static void copyQuietly(InputStream inputStream, OutputStream outputStream) {
        try {
            copy(inputStream, outputStream);
        } catch (IOException e) {
        } finally {
            closeQuietly(inputStream);
            closeQuietly(outputStream);
        }
    }

    public static byte[] readFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException("filePath is empty");
        }
        final File file = new File(filePath);
        if (!file.isFile()) {
            throw new IllegalArgumentException(filePath + " is not a File");
        }
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            copyQuietly(new FileInputStream(file), outputStream);
            return outputStream.toByteArray();
        } catch (FileNotFoundException e) {
        }

        return new byte[0];
    }

    public static byte[] readFile(File file) {
        if (!file.isFile()) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " is not a File");
        }
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            copyQuietly(new FileInputStream(file), outputStream);
            return outputStream.toByteArray();
        } catch (FileNotFoundException e) {
        }

        return new byte[0];
    }

    public static String readFile(String filePath, String charset) {
        if (TextUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException("filePath is empty");
        }
        final File file = new File(filePath);
        if (!file.isFile()) {
            throw new IllegalArgumentException(filePath + " is not a File");
        }
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            copyQuietly(new FileInputStream(file), outputStream);
            return outputStream.toString(charset);
        } catch (FileNotFoundException e) {
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    public static String readFile(File file, String charset) {
        if (!file.isFile()) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " is not a File");
        }
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            copyQuietly(new FileInputStream(file), outputStream);
            return outputStream.toString(charset);
        } catch (FileNotFoundException e) {
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    public static void writeToFile(String filePath, InputStream inputStream) {
        if (TextUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException("filePath is empty");
        }
        final File file = new File(filePath);
        final File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (!parent.exists()) {
            throw new IllegalStateException("Can't create dir " + parent.getAbsolutePath());
        }

        try {
            copyQuietly(inputStream, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
        }
    }

    public static void writeToFile(File file, InputStream inputStream) {
        final File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (!parent.exists()) {
            throw new IllegalStateException("Can't create dir " + parent.getAbsolutePath());
        }

        try {
            copyQuietly(inputStream, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
        }
    }

    public static void writeToFile(String filePath, byte[] data) {
        writeToFile(filePath, new ByteArrayInputStream(data));
    }

    public static void writeToFile(File file, byte[] data) {
        writeToFile(file.getAbsolutePath(), data);
    }

    public static void writeToFile(String filePath, String data, String charset) {
        try {
            writeToFile(filePath, data.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void writeToFile(File file, String data, String charset) {
        try {
            writeToFile(file, data.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
