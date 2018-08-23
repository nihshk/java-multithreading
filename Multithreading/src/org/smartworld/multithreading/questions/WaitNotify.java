package org.smartworld.multithreading.questions;

public class WaitNotify {

	public static void main(String[] args) {

		ResourceCarrier carrier = new ResourceCarrier();

		ThingNeedingResource thingNeedingResource = new ThingNeedingResource(carrier);
		ThingNeedingResource thingNeedingResource2 = new ThingNeedingResource(carrier);
		ThingNeedingResource thingNeedingResource3 = new ThingNeedingResource(carrier);

		ResourceCreator resourceCreator = new ResourceCreator(carrier);

		new Thread(thingNeedingResource).start();
		new Thread(thingNeedingResource2).start();
		new Thread(thingNeedingResource3).start();
		new Thread(resourceCreator).start();
	}

}

class ResourceCarrier {
	private boolean resourceReady;

	public boolean isResourceReady() {
		return resourceReady;
	}

	public void resourceIsReady() {
		resourceReady = true;

	}
}

class ResourceCreator implements Runnable {
	private ResourceCarrier carrier;

	public ResourceCreator(ResourceCarrier carrier) {

		this.carrier = carrier;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (carrier) {
			carrier.resourceIsReady();
			carrier.notifyAll();
		}
	}
}

class ThingNeedingResource implements Runnable {

	private ResourceCarrier carrier;

	public ThingNeedingResource(ResourceCarrier carrier) {

		this.carrier = carrier;
	}

	@Override
	public void run() {
		synchronized (carrier) {
			while (!carrier.isResourceReady()) {
				try {
					System.out.println(Thread.currentThread().getName() + " Waiting for Resource");
					carrier.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(Thread.currentThread().getName() + " haz resource");
		}
	}
}
