package cn.aposoft.tutorial.jackson.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "condition")
public class Condition {
    @JacksonXmlProperty(isAttribute = true, localName = "queryType")
    private String queryType = "25173";
    @JacksonXmlProperty(namespace = "", localName = "item")
    private Item name = new Item();
    @JacksonXmlProperty(namespace = "", localName = "item")
    private Item accountNo;

    /**
     * @return the queryType
     */
    public String getQueryType() {
        return queryType;
    }

    /**
     * @param queryType
     *            the queryType to set
     */
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public void setName(Item name) {
        this.name = name;
    }

    public Item getName() {
        return name;
    }

    /**
     * @return the accountNo
     */
    public Item getAccountNo() {
        return accountNo;
    }

    /**
     * @param accountNo
     *            the accountNo to set
     */
    public void setAccountNo(Item accountNo) {
        this.accountNo = accountNo;
    }
}
