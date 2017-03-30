/**
 * 
 */
package com.pycredit;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 
 * 个人银行账户核查 主机对主机方式单个查询接口
 * 
 * @author LiuJian
 *
 */
@WebService
public interface WebServiceSingleQuery {
    /**
     * 根据查询条件，查询单笔信用报告信息
     * 
     * @param userID
     *            用户登录名
     * @param password
     *            登录密码密文
     * @param queryCondition
     *            查询申请条件
     * @return 返回结果为xml格式，如下所示：
     * 
     *         <pre>
     *         <result> <status>1：正常；2：异常</status>
     *         <errorCode>status节点值为异常时的错误代码，常见的返回代码有： 8001：查询用户不存在或登录密码错。
     *         1003：查询申请内容为空。 2004：查询申请条件校验错。 9002：系统错误，比如数据库错误等。 </errorCode>
     *         <errorMessage>errorCode节点错误代码对应的错误描述</errorMessage>
     *         <returnValue>查询正常时该节点有值，若调用压缩并Base64编码方式，返回的是压缩Base64编码的字符串，
     *         需把returnValue值用Base64解码，把字符串转换字节流，把字节流解压后，获取到报告的详细内容，进而对数据进行解析处理，
     *         若调用非压缩非Base64编码方式，返回的值为明文数据，具体内容格式请参照文件<<个人银行账户查询结果xml规范>>。
     *         </returnValue> 
     *         </result>
     *         </pre>
     */
    @WebMethod
    public String queryReport(@WebParam(name = "userID") String userID, @WebParam(name = "password") String password,
            @WebParam(name = "queryCondition") String queryCondition);
}
