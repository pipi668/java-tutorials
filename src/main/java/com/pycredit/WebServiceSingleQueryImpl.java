/**
 * 
 */
package com.pycredit;

/**
 * 实现鹏元个人银行账户核查 主机对主机方式单个查询接口
 * 
 * @author LiuJian
 * @see WebServiceSingleQuery
 */
public class WebServiceSingleQueryImpl implements WebServiceSingleQuery {
    /**
     * @see WebServiceSingleQuery#queryReport(String, String, String)
     */
    @Override
    public String queryReport(String userID, String password, String queryCondition) {
        return "<result></result>";
    }

}
