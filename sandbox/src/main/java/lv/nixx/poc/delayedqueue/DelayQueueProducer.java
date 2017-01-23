package lv.nixx.poc.delayedqueue;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class DelayQueueProducer {

	private BlockingQueue<Email> queue;
	private final Random random = new Random();

	private static final String emailBody = "Email body text with delay :: ";

	public DelayQueueProducer(BlockingQueue<Email> queue) {
		this.queue = queue;
	}

	private Thread producerThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					// Put Random delay for each email to send.
					int delay = random.nextInt(10000);
					String receipient = UUID.randomUUID().toString() + "@gmail.com";
					Email email = new Email(receipient, emailBody + delay, delay);

					System.out.printf("Put email in a DelayQueue = %s%n", email);
					queue.put(email);
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}, "Producer Thread");

	public void start() {
		this.producerThread.start();
	}
	
}