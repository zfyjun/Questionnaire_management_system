package com.example.ssk.common;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class Cmap {
    /**
     * 根据城市名称查询所在经纬度
     * @param addr
     * 查询的地址
     * @return
     * @throws IOException
     */
    public String[] getCoordinate(String addr) throws IOException {
        String lng = null;//经度
        String lat = null;//纬度
        String address = null;
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        }catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String url = "https://api.map.baidu.com/geocoding/v3/?address="+address+"&output=json&ak=Pho8mQgAlwOTdbZGFOuIdW9k2ouZMeyZ";
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
            if (httpsConn != null) {
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                int count = 1;
                data= br.readLine();
                JSONObject jsonObject=JSONObject.parseObject(data);
                //
                String result=jsonObject.getString("result");
                JSONObject jsonObject2=JSONObject.parseObject(result);
                //
                if(jsonObject2!=null){
                    String location=jsonObject2.getString("location");
                    JSONObject jsonObject3=JSONObject.parseObject(location);
                    lng=jsonObject3.getString("lng");
                    lat=jsonObject3.getString("lat");
                }
                //
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(insr!=null){
                insr.close();
            }
            if(br!=null){
                br.close();
            }
        }
        return new String[]{lng,lat};
    }

}