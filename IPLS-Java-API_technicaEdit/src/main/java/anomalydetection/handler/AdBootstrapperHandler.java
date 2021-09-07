package anomalydetection.handler;

import java.util.List;
import originalDefault.IPLS;

public class AdBootstrapperHandler implements Runnable {
	private IPLS ipls;
	
	public AdBootstrapperHandler (String modelPath, String ipfsAddress, List<String> Bootstrappers, boolean iplsIsBootstrapper, int iplsPort) {

		// 	Init IPLS
		try {
			ipls = new IPLS();
			ipls.init(ipfsAddress, modelPath, Bootstrappers, iplsIsBootstrapper, iplsPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void run() {	
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("AdBootstrapperHandler - FINALLY");
		}
	}
	
}
