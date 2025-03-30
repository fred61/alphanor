package org.swissmail.fred.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*

Spike to experiment with SoftReference

Soft references stay alive after last reference. Duration is determined by 
	XX:SoftRefLRUPolicyMSPerMB
Default value is 1000.

Observations:
	- weak ref is always gone when soft ref is gone
	- point in time when soft ref is cleared is unpredictable
    - attaching JMC or running jmap can cause soft ref to be cleared.

Useful VM option to log GC: -Xlog:gc*=debug:D:\\tmp\\gc.${current_date}.log:tm,l,tg

 */

public class SoftReferenceSpike {

	public static void main(String[] args) throws Exception {
		log("Yo");
		new SoftReferenceSpike().run();
	}
	
	private static void log(String msg) {
		System.out.println(String.format("[%dms] - %s", System.currentTimeMillis(), msg));
	}
	
	private static class Payload {
		private byte[] load;
		
		public Payload(int sizeK) {
			load= new byte[sizeK * 1024];
		}
	}
	

	private ReferenceQueue<Object> theSoftQueue = new ReferenceQueue<>();
	private ReferenceQueue<Object> theWeakQueue = new ReferenceQueue<>();

	private SoftReference<Object> theSoftRef;
	private WeakReference<Object> theWeakRef;
	
	private long lastReferenced= 0;

	private int garbageDelay = 1;
	private int garbageSize = 256;	
	private AtomicInteger garbageRetention = new AtomicInteger(16384);		//retain 4M initially

	private void run() throws Exception {
		var preload= new byte[16 * 1024][1024];		// waste 16M
		
		var theObject = new Payload(128);		// make ref reasonable size

		theSoftRef = new SoftReference<>(theObject, theSoftQueue);

		log("soft ref: " + theSoftRef);

		Thread gcGenerator = new Thread(this::generateGarbage);
		gcGenerator.setName("gcGenerator");
		gcGenerator.setDaemon(true);

		Thread referenceObserver = new Thread(this::observeRefs);
		referenceObserver.setName("referenceObserver");

		Thread queueObserver = new Thread(this::observeRefQueue);
		queueObserver.setName("queueObserver");
		queueObserver.setDaemon(true);

		queueObserver.start();
		gcGenerator.start();
		referenceObserver.start();

		theObject = null;
	}

	private void generateGarbage() {
		try {
			Thread.sleep(garbageDelay);
			for (;;) {
				int retain = garbageRetention.get();
				byte[][] garbage = new byte[retain][];
				for (int i = 0; i < retain; i++) {
					garbage[i] = new byte[garbageSize];
					Thread.sleep(0, 250_000);
				}
			}
		} catch (OutOfMemoryError | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void moreGarbage() {
		long retainedK= garbageRetention.addAndGet(4096);
		retainedK= retainedK * garbageSize / 1024;
		log("retain more garbage: " + retainedK);
	}

	private void observeRefQueue() {
		log("observing ref queues");
		try {
			var theRef = theSoftQueue.remove();
			if (theRef != null) {
				log("removed from softQueue: " + theRef);
				log("referrent: " + theRef.get());
				double  lastRefAgo= (System.nanoTime() - lastReferenced) / 1_000_000D;
				log("last referenced " +lastRefAgo + " ms ago");

				var weakQueueRef = theWeakQueue.poll();
				log("removed from weakQueue: " + weakQueueRef);
				if (weakQueueRef != null) {
					log("referrent: " + weakQueueRef.get());
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void observeRefs() {
		log("observing refs");
		long sleepTime = 1000;
		try {
			int i = 0;
			while (!theSoftRef.refersTo(null)) {
				lastReferenced= System.nanoTime();
				log("soft ref still present: " + theSoftRef.get());
				i += 1;
//				if (i % 16 == 0) {
//					moreGarbage();
//				}
				if (theWeakRef == null && i == 48) {
					log("create weak ref");
					theWeakRef= new WeakReference<Object>(theSoftRef.get(), theWeakQueue);
				}
				Thread.sleep(sleepTime);
			}
			if (theWeakRef == null) {
				log("soft ref gone before weak ref created");
			} else {
				if (theWeakRef.get() == null) {
					log("soft ref and weak ref both gone");
				} else {
					log("soft ref gone, weak ref alive");
				}
			}
			var softQueuedReference = theSoftQueue.poll();
			log("object in soft queue: " + softQueuedReference);
			if (softQueuedReference != null) {
				var softReferrent = softQueuedReference.get();
				log("softQueue referrent: " + softReferrent);
			}

			var weakQueuedReference = theWeakQueue.poll();
			log("object in weak queue: " + weakQueuedReference);
			if (weakQueuedReference != null) {
				var weakReferrent = weakQueuedReference.get();
				log("weakQueue referrent: " + weakReferrent);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
