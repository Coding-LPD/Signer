package com.scnu.zhou.signer.elss.util.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kelvin on 2016/9/3.
 */
public class HttpHelper {

    public static String doGet(String url, Map<String, String> params) throws IOException{

        StringBuilder builder = new StringBuilder(url);
        Set<Map.Entry<String, String>> entrys = null;
        // 如果是GET请求，将参数放在URL中
        if (params != null && !params.isEmpty()) {
            builder.append("?");
            entrys = params.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                builder.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");

            }
            builder.deleteCharAt(builder.length() - 1);
        }

        URL httpUrl = new URL(builder.toString());
        HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(5000);

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String str;
        StringBuffer buffer = new StringBuffer();
        while ((str = reader.readLine()) != null){
            buffer.append(str);
        }

        return buffer.toString();
    }


    public static String doPost(String url, Map<String, String> params) throws IOException{

        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String, String>> entrys = null;
        // 如果存在参数，则放在HTTP请求体中
        Log.e("params", params.toString());
        if (params != null && !params.isEmpty()) {
            entrys = params.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                builder.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }
            builder.deleteCharAt(builder.length() - 1);
        }

        URL httpUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
        conn.setRequestMethod("POST");
        conn.setReadTimeout(5000);
        OutputStream out = conn.getOutputStream();
        out.write(builder.toString().getBytes("UTF-8"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String str;
        StringBuffer buffer = new StringBuffer();
        while ((str = reader.readLine()) != null){
            buffer.append(str);
        }

        return buffer.toString();
    }


    public static String doPut(String url, Map<String, String> params) throws IOException{

        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String, String>> entrys = null;
        // 如果存在参数，则放在HTTP请求体中
        Log.e("params", params.toString());
        if (params != null && !params.isEmpty()) {
            entrys = params.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                builder.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }
            builder.deleteCharAt(builder.length() - 1);
        }

        URL httpUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
        conn.setRequestMethod("PUT");
        conn.setReadTimeout(5000);
        OutputStream out = conn.getOutputStream();
        out.write(builder.toString().getBytes("UTF-8"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String str;
        StringBuffer buffer = new StringBuffer();
        while ((str = reader.readLine()) != null){
            buffer.append(str);
        }

        return buffer.toString();
    }


    public static String uploadFile(String actionUrl, String fileName, String filePath) throws IOException{

        String end ="\r\n";
        String twoHyphens ="--";
        String boundary ="*****";

        URL url =new URL(actionUrl);
        HttpURLConnection con=(HttpURLConnection)url.openConnection();
          /* 允许Input、Output，不使用Cache */
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
          /* 设置传送的method=POST */
        con.setRequestMethod("POST");
          /* setRequestProperty */
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        con.setRequestProperty("Content-Type",
                "multipart/form-data;boundary="+boundary);
          /* 设置DataOutputStream */
        DataOutputStream ds =
                new DataOutputStream(con.getOutputStream());
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; "+
                "name=\"file1\";filename=\""+
                fileName +"\""+ end);
        ds.writeBytes(end);
          /* 取得文件的FileInputStream */
        FileInputStream fStream =new FileInputStream(filePath);
          /* 设置每次写入1024bytes */
        int bufferSize =1024;
        byte[] buffer = new byte[bufferSize];
        int length =-1;
          /* 从文件读取数据至缓冲区 */
        while((length = fStream.read(buffer)) !=-1) {
            /* 将资料写入DataOutputStream中 */
            ds.write(buffer, 0, length);
        }
        ds.writeBytes(end);
        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
          /* close streams */
        fStream.close();
        ds.flush();
          /* 取得Response内容 */
        InputStream is = con.getInputStream();
        int ch;
        StringBuffer b =new StringBuffer();
        while( ( ch = is.read() ) !=-1 ) {
            b.append( (char)ch );
        }
          /* 将Response显示于Dialog */
        //showDialog("上传成功"+b.toString().trim());
          /* 关闭DataOutputStream */
        ds.close();

        return b.toString();
    }

}
