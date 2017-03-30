/**
 * 
 */
package cn.aposoft.tutorial.jackson.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author LiuJian
 *
 */
@JacksonXmlRootElement(namespace = "", localName = "person")
public class Person {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
