/**
 * 
 */
package cn.aposoft.tutorial.fastjson;

import com.alibaba.fastjson.JSON;

/**
 * @author Jann Liu
 *
 */
public class FasstJsonCoder<T> {
	String dataString;
	T data;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FasstJsonCoder<Person> fp = new FasstJsonCoder<>();
		fp.data = new Person().setAge(35).setName("abc");
		fp.dataString = JSON.toJSONString(fp.data);

		Person p = fp.toObj();
		System.out.println(":" + p.getAge() + ":" + p.getName());
	}

	private T toObj() {
		return data; // JSON.parseObject(this.dataString, T.class); T.class不成立
	}

	static class Person {
		String name;
		int age;

		public String getName() {
			return name;
		}

		public Person setName(String name) {
			this.name = name;
			return this;
		}

		public int getAge() {
			return age;
		}

		public Person setAge(int age) {
			this.age = age;
			return this;
		}

	}
}
