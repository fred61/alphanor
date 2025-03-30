package org.swissmail.fred.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

/*

Spike to experiment with WeakReference

Observations:
	- if WeakRef isn't pinned by a strong ref when object is created, it is GCed almost immediately
	- WeakRef will be cleared as soon as the GC sees it. Note with G1, this does not mean Full GC.

*/
public class WeakReferenceSpike {

	public static void main(String[] args) throws Exception {
		log("Yo");
		new WeakReferenceSpike().run();
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
	
	

	private ReferenceQueue<Object> theWeakQueue = new ReferenceQueue<>();
	private WeakReference<Object> theWeakRef;
	
	private int garbageDelay = 1;
	private int garbageSize = 256;	
	private AtomicInteger garbageRetention = new AtomicInteger(16384);		//retain 4M initially

	private void run() throws Exception {
		var preload= new byte[16 * 1024][1024];		// waste 16M
		
		var theObject = new Payload(128);		// make ref reasonable size

		theWeakRef = new WeakReference<>(theObject, theWeakQueue);

		log("weak ref: " + theWeakRef);

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
			var theRef = theWeakQueue.remove();
			if (theRef != null) {
				log("removed from weakQueue: " + theRef);
				log("referrent: " + theRef.get());
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
			var pinRef= theWeakRef.get();
			while (!theWeakRef.refersTo(null)) {
				log("ref still present: " + theWeakRef.get());
				i += 1;
				if (pinRef != null && i == 48) {
					log("unpinning ref");
					pinRef= null;
				}
				Thread.sleep(sleepTime);
			}
			log("ref cleared");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
