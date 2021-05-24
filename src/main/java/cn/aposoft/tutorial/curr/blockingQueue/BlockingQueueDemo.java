/**
 * 
 */
package cn.aposoft.tutorial.curr.blockingQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Jann Liu
 *
 */
public class BlockingQueueDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		BlockingQueue<String> queue = new LinkedBlockingQueue<>();
		queue.size();
	}

}
