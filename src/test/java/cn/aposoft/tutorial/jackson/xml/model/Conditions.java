/**
 * 
 */
package cn.aposoft.tutorial.jackson.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author LiuJian
 *
 */
@JacksonXmlRootElement(namespace = "", localName = "conditions")
public class Conditions {

    private Condition condition = new Condition();

    /**
     * @return the condition
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * @param condition
     *            the condition to set
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
