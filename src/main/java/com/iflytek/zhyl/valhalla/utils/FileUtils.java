package com.iflytek.zhyl.valhalla.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author quwang2
 */
public class FileUtils {

    public static byte[] getFile(final InputStream inputStream ) throws IOException {
        return cloneInputStream(inputStream).toByteArray();
    }

    public static ByteArrayOutputStream  cloneInputStream(final InputStream inputStream ) throws IOException {
        //这个是重点
        ByteArrayOutputStream  resultByte = new ByteArrayOutputStream();
        byte[] read_buf = new byte[1024 * 1024];
        int read_len = 0;
        while ((read_len = inputStream .read(read_buf)) > 0) {
            resultByte.write(read_buf, 0, read_len);
        }
        return resultByte;
    }

    public static void writeFile(InputStream inputStream, String filePath) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        byte[] bytes = new byte[1024 * 8];
        while (true){
            int read = 0;
            if(inputStream != null){
                read = inputStream.read(bytes);
            }
            if(read == -1){
                break;
            }
            fileOutputStream.write(bytes,0,read);
        }
        fileOutputStream.flush();
    }

}
