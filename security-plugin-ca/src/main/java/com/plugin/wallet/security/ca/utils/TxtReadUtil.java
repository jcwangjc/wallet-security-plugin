package com.plugin.wallet.security.ca.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author : laoa
 * @describe : 读取文件
 * @email : laoa@markcoin.net
 */
public class TxtReadUtil {
    public static String read(String filepath) throws IOException {
        // 通过ClassLoader获取资源文件的输入流
        InputStream inputStream=null;
        BufferedReader reader=null;
        try (InputStream is = TxtReadUtil.class.getClassLoader().getResourceAsStream(filepath)) {
            inputStream=is;
            if (inputStream != null) {
                // 使用BufferedReader读取文本内容
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
            reader.close();
        }
        return null;
    }
}
