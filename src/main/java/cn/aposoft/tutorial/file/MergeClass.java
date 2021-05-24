/**
 * 
 */
package cn.aposoft.tutorial.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * @author Jann Liu
 *
 */
public class MergeClass {

	/**
	 * 964,304=1268
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
//		InputStream input1 = new FileInputStream("d:/1.txt");
//		InputStream input2 = new FileInputStream("d:/1-name.txt");
//		List<String> lines1 = IOUtils.readLines(input1, "UTF-8");
//		List<String> lines2 = IOUtils.readLines(input2, "UTF-8");
//		System.out.println(""+lines1.size()+","+lines2.size()+"="+(lines1.size()+lines2.size()));
//		lines1.addAll(lines2);
//		Collections.sort(lines1);
//		for (String s : lines1) {
//			System.out.println(s);
//		}
		InputStream input3 = new FileInputStream("d:/1-dis.txt");
		Writer writer = new OutputStreamWriter(new FileOutputStream("d:/1-dis.csv"), "GBK");
		List<String> lines3 = IOUtils.readLines(input3, "UTF-8");
		System.out.println(lines3.size());
		for (int i = 0; i < lines3.size(); i++) {
			String s = lines3.get(i).replace("	", ",");
			writer.write(s);
			writer.write("\n");
		}
		input3.close();
		writer.close();
	}

}
