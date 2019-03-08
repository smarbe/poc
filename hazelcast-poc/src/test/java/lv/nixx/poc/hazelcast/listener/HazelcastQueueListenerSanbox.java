package lv.nixx.poc.hazelcast.listener;

import org.junit.Test;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;

import com.hazelcast.test.TestHazelcastInstanceFactory;

public class HazelcastQueueListenerSanbox {

	private static final String QUEUE_NAME = "simpleQueue";
	private HazelcastInstance hazelcastInstance = new TestHazelcastInstanceFactory().newHazelcastInstance();
	
	@Test
	public void queueListenerTest() throws InterruptedException {
		IQueue<Message> producerQueue = hazelcastInstance.getQueue(QUEUE_NAME);

		producerQueue.addItemListener(new MyItemListener(), true);
	    
	    producerQueue.add(new Message(1L, "Message1"));
	    producerQueue.add(new Message(2L, "Message2"));
	    producerQueue.add(new Message(3L, "Message3"));

	}

	class MyItemListener implements ItemListener<Message> {
		IQueue<Message> consumerQueue = hazelcastInstance.getQueue(QUEUE_NAME);
		
		@Override
		public void itemAdded(ItemEvent<Message> item) {
			// ItemAdded not remove data from queue, we should call consumerQueue.remove...
			System.out.println("Tread:" + Thread.currentThread() + ":" + item + ":" + consumerQueue.size() + ":" + consumerQueue.remove(item.getItem()));
		}

		@Override
		public void itemRemoved(ItemEvent<Message> item) {
			System.out.println("Tread:" + Thread.currentThread() + ":" + item);
		}
	}
	

}
