package cn.aposoft.tutorial.changliang;

import java.io.IOException;
import java.io.InputStream;

import org.apache.cxf.helpers.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ChangliangClientTest {
    private final static String URL = "http://10.143.108.43:9999";

    public static void main(String[] args) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault();) {
            doPost(client);
        }

    }

    private static void doPost(CloseableHttpClient client) throws IOException {
        String json = getJson();
        System.out.println(json);
        HttpPost post = new HttpPost(URL);
        StringEntity entity = new StringEntity(json);
        post.setEntity(entity);
        post.setHeader("Content-Type", "application/json;charset=utf-8");

        try (CloseableHttpResponse resp = client.execute(post);) {
            HttpEntity respentity = resp.getEntity();
            String s = EntityUtils.toString(respentity);
            System.out.println(s);

        }
    }

    private static String getJson() throws IOException {
        InputStream stream = ChangliangClientTest.class.getClassLoader().getResourceAsStream("changliang/request.txt");

        return IOUtils.toString(stream, "GBK");

    }

}
