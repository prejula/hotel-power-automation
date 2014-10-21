/**
 * 
 */
package org.prej.main;

import java.io.IOException;


/**
 * @author prejula
 * 
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		AutomationControlSystem automationController = new AutomationControlSystem();
		try {
			automationController.init();

		} catch (IOException e) {
			e.printStackTrace();
		} 

		automationController.writeEquipmentState(System.out);

		automationController.movementOccured("Floor1", "Sub1");
		
		automationController.movementOccured("Floor2", "Sub1");

		automationController.writeEquipmentState(System.out);
	}

}
