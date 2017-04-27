package cn.aposoft.tutorial.http.okclient;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetExample {
    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("javax.net.debug", "all");// ,ssl,handshake,verbose
        GetExample example = new GetExample();
        String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
        // System.out.println(response);
        System.out.println("Another time--------------------");
        example.run("https://raw.github.com/square/okhttp/master/README.md");
        // System.out.println(response);
    }
}
