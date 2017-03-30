package cn.aposoft.tutorial.jackson.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import cn.aposoft.tutorial.jackson.xml.model.Conditions;
import cn.aposoft.tutorial.jackson.xml.model.Item;

public class JacksonXmlTool {
    public static void main(String[] args) {
        StringWriter writer = new StringWriter();

        // Important: create XmlMapper; it will use proper factories,
        // workarounds
        ObjectMapper xmlMapper = new XmlMapper();
        Person p = new Person();
        p.setName("liujian");

        try {
            String xml = xmlMapper.writeValueAsString(p);
            System.out.println(xml);
            // or
            xmlMapper.writeValue(writer, p);
            System.out.println(writer.toString());

            Conditions cond = new Conditions();
            List<Item> item = new ArrayList<>();
            Item item1 = new Item();
            item1.setName("name");
            item1.setValue("被查询人姓名,必填");
            cond.getCondition().setName(item1);
            Item accountNo = new Item();
            accountNo.setName("accountNo");
            accountNo.setValue("银行账户号,必填");
            cond.getCondition().setAccountNo(accountNo);
            System.out.println(xmlMapper.writeValueAsString(cond));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
