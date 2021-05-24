package cn.aposoft.springcloud.alibaba.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.Executor;

public class NacosClientDemo {
    /**
     * 使用说明：
     * 1. 读取不使用后台鉴权功能。
     * 2. namespace =
     * @param args
     */
    public static void main(String[] args) {
        try {
            String serverAddr = "10.207.22.202:8848";
            String dataId = "zx00001";
            String group = "BIDEN";
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            properties.put("namespace","ae897d41-8766-4d3e-8f42-9dd1a8ae63bb"); // 需要使用tenant_id（mysql: config_info表）
            ConfigService configService = NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, group, 5000);
            System.out.println(content);
            Properties propertiesContent = new Properties();
            propertiesContent.load(new StringReader(content));
            System.out.println(propertiesContent.getProperty("admin","nacos"));
            final Thread t = Thread.currentThread();
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    System.out.println("recieve1:" + configInfo);
                    System.out.println(Thread.currentThread().getName());
                    t.interrupt();
                }
                @Override
                public Executor getExecutor() {
                    System.out.println("get executor:" );
                    return null;
                }
            });

            Thread.sleep(1000*1000*1000);


        } catch (NacosException | IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            if(e instanceof InterruptedException){
                System.out.println("interrupt");
            }
            e.printStackTrace();
        }
    }

}
