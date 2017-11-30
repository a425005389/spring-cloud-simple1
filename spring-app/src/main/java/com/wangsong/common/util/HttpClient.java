package com.wangsong.common.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class HttpClient {
	
	// 获取src路径的正则  
	static final String IMGURL_REG = "http:\"?(.*?)(\"|>|\\s+)";  
	
	@Test
    public void testGet() throws Exception {
		
		
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://image.baidu.com/search/index?tn=baiduimage&ct=201326592&lm=-1&cl=2&ie=gbk&word=gif&fr=ala&ala=1&alatpl=adress&pos=0&hs=2&xthttps=111111");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            int l;
            String data = null;
            byte[] tmp = new byte[2048];
            while ((l = instream.read(tmp)) != -1) {
                data = new String(tmp, 0, l, "utf-8");
                List<String> list = getImageUrl(data);
                for(String a : list){
                	System.out.println(a.replace("\\", "").toString());
                }
            }
        }
    }
	
	/*** 
     * 获取ImageUrl地址 
     *  
     * @param HTML 
     * @return 
     */  
    private static List<String> getImageUrl(String HTML) {  
        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);  
        List<String> listImgUrl = new ArrayList<String>();  
        while (matcher.find()) {  
            listImgUrl.add(matcher.group());  
        }  
        return listImgUrl;  
    }  
}
