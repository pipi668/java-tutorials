/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.range;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * HTTP协议,分段读取协议
 * 
 * @author LiuJian
 * @date 2017年4月15日
 * 
 */
public class HttpRangeStub {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        // Range: bytes=0-499
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet("http://www.aposoft.cn");
            get.addHeader("Range", "bytes=0-499");
            try (CloseableHttpResponse resp = client.execute(get);) {
            }
            get.setHeader("Range", "bytes=500-999");
            try (CloseableHttpResponse resp = client.execute(get);) {
            }
        }

    }

}
