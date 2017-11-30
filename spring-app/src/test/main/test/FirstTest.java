package test;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * 第一个爬虫测试
 * Created by DuFei on 2017/7/27.
 */
public class FirstTest {

  public static void main(String[] args) {

    //建立一个新的请求客户端
    CloseableHttpClient httpClient = HttpClients.createDefault();

    //使用HttpGet方式请求网址
    HttpGet httpGet = new HttpGet("http://www.datalearner.com/blog");

    //获取网址的返回结果
    CloseableHttpResponse response = null;
    try {
      response = httpClient.execute(httpGet);
    } catch (IOException e) {
      e.printStackTrace();
    }

    //获取返回结果中的实体
    HttpEntity entity = response.getEntity();

    //将返回的实体输出
    try {
//      System.out.println(EntityUtils.toString(entity));
//      EntityUtils.consume(entity);
    	String html = EntityUtils.toString(entity);
    	Document doc = Jsoup.parse(html);
    	Elements blogList = doc.select("div[class=card-block]");
    	for( Element element : blogList ){

    	      String title = element.select("h5[class=card-title]").text();
    	      String introduction = element.select("p[class=card-text]").text();
    	      String author = element.select("span[class=fa fa-user]").text();

    	      System.out.println("Title:\t"+title);
    	      System.out.println("introduction:\t"+introduction);
    	      System.out.println("Author:\t"+author);
    	      System.out.println("--------------------------");

    	    }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}