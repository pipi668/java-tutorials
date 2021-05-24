/**
 * 
 */
package cn.aposoft.tutorial.fastjson;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;

import cn.aposoft.tutorial.fastjson.FasstJsonCoder.Person;

/**
 * @author Jann Liu
 *
 */
public class FastJsonGetGenericType<T> {
	T data = null;

	String json;

	/**
	 * @param args
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		FastJsonGetGenericType<Person> p = new FastJsonGetGenericType<Person>();
		Person per = new Person();
		per.setAge(35);
		per.setName("jian");
		p.json = JSON.toJSONString(per);
		System.err.println(p.get());
	}

	public T get() throws NoSuchMethodException, SecurityException {
		Method method = this.getClass().getMethod("get", null);
		Type t = method.getGenericReturnType();

		System.out.println("t:" + t);
		Type[] params = ((ParameterizedType) t).getActualTypeArguments();
		Class<T> reponseClass = (Class) params[0];

		return JSON.parseObject(json, reponseClass);
	}

}
