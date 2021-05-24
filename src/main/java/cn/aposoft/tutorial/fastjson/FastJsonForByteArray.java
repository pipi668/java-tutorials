/**
 * 
 */
package cn.aposoft.tutorial.fastjson;

import com.alibaba.fastjson.JSON;

/**
 * @author Jann Liu
 *
 */
public class FastJsonForByteArray {

	/**
	 * FAST-JSON使用 Base64对byte数组进行编码
	 * @param args
	 */
	public static void main(String[] args) {
		byte[] bytes = "woshiyigefenshuajiang".getBytes();
		String s = JSON.toJSONString(bytes);
		System.out.println(s);
		byte[] bytesR = JSON.parseObject(s, byte[].class);
		String sR = new String(bytesR);
		System.out.println(sR);
	}

}
