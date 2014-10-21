/**
 * 
 */
package org.prej.controller;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.prej.corridor.MainCorridor;
import org.prej.corridor.SubCorridor;
import org.prej.equipment.Equipment;
import org.prej.equipment.light.Light;
import org.prej.floor.Floor;
import org.prej.main.AutomationControlSystem;

/**
 * @author prejula
 * 
 */
public class AutomationControllerTest extends TestCase {

	private AutomationControlSystem automationController = null;

	@Override
	public void setUp() {
		automationController = new AutomationControlSystem();
		try {
			automationController.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testDefaultEquipmentState() {
		
		automationController.writeEquipmentState(System.out);

		List<Floor> floors = automationController.getFloorPlan();
		assertEquals(2, floors.size());

		for (Floor floor : floors) {
			for (MainCorridor mainCorridor : floor.getMainCorridors()) {
				for (Equipment equipment : mainCorridor.getEquipments()) {
					assertEquals("ON", equipment.getState());
				}
			}

			for (SubCorridor subCorridor : floor.getSubCorridors()) {
				for (Equipment equipment : subCorridor.getEquipments()) {
					if (equipment instanceof Light)
						assertEquals("OFF", equipment.getState());
					else
						assertEquals("ON", equipment.getState());
				}
			}
		}

	}

	public void testMovementOnFloor1Sub2() throws InterruptedException {
		automationController.movementOccured("Floor1", "Sub2");

		automationController.writeEquipmentState(System.out);

		List<Floor> floors = automationController.getFloorPlan();
		assertEquals(2, floors.size());

		for (Floor floor : floors) {
			for (MainCorridor mainCorridor : floor.getMainCorridors()) {
				for (Equipment equipment : mainCorridor.getEquipments()) {
					assertEquals("ON", equipment.getState());
				}
			}

			for (SubCorridor subCorridor : floor.getSubCorridors()) {

				for (Equipment equipment : subCorridor.getEquipments()) {
					
					if ("Floor1".equals(floor.getName())) {
						if ("Sub2".equals(subCorridor.getName())) {
							if (equipment instanceof Light)
								assertEquals("ON", equipment.getState());
							else
								assertEquals("ON", equipment.getState());
						} else {
							if (equipment instanceof Light)
								assertEquals("OFF", equipment.getState());
							else
								assertEquals("OFF", equipment.getState());
						}
					}
				}

			}
		}

		Thread.sleep(1001);

		automationController.writeEquipmentState(System.out);
		
		for (Floor floor : floors) {
			for (MainCorridor mainCorridor : floor.getMainCorridors()) {
				for (Equipment equipment : mainCorridor.getEquipments()) {
					assertEquals("ON", equipment.getState());
				}
			}

			for (SubCorridor subCorridor : floor.getSubCorridors()) {
				for (Equipment equipment : subCorridor.getEquipments()) {
					if (equipment instanceof Light)
						assertEquals("OFF", equipment.getState());
					else
						assertEquals("ON", equipment.getState());
				}
			}
		}

	}

	public void testMovementOnFloor1Sub1Floor2Sub2()
			throws InterruptedException {
		automationController.movementOccured("Floor1", "Sub1");
		automationController.movementOccured("Floor2", "Sub2");
		automationController.writeEquipmentState(System.out);

		List<Floor> floors = automationController.getFloorPlan();
		assertEquals(2, floors.size());
		
		for (Floor floor : floors) {
			for (MainCorridor mainCorridor : floor.getMainCorridors()) {
				for (Equipment equipment : mainCorridor.getEquipments()) {
					assertEquals("ON", equipment.getState());
				}
			}

			for (SubCorridor subCorridor : floor.getSubCorridors()) {

				for (Equipment equipment : subCorridor.getEquipments()) {
					
					if ("Floor1".equals(floor.getName())) {
						if ("Sub1".equals(subCorridor.getName())) {
							if (equipment instanceof Light)
								assertEquals("ON", equipment.getState());
							else
								assertEquals("ON", equipment.getState());
						} else {
							if (equipment instanceof Light)
								assertEquals("OFF", equipment.getState());
							else
								assertEquals("OFF", equipment.getState());
						}
					}
					
					if ("Floor2".equals(floor.getName())) {
						if ("Sub2".equals(subCorridor.getName())) {
							if (equipment instanceof Light)
								assertEquals("ON", equipment.getState());
							else
								assertEquals("ON", equipment.getState());
						} else {
							if (equipment instanceof Light)
								assertEquals("OFF", equipment.getState());
							else
								assertEquals("OFF", equipment.getState());
						}
					}
				}

			}
		}
		
		Thread.sleep(1001);

		automationController.writeEquipmentState(System.out);

		for (Floor floor : floors) {
			for (MainCorridor mainCorridor : floor.getMainCorridors()) {
				for (Equipment equipment : mainCorridor.getEquipments()) {
					assertEquals("ON", equipment.getState());
				}
			}

			for (SubCorridor subCorridor : floor.getSubCorridors()) {
				for (Equipment equipment : subCorridor.getEquipments()) {
					if (equipment instanceof Light)
						assertEquals("OFF", equipment.getState());
					else
						assertEquals("ON", equipment.getState());
				}
			}
		}
	}

	public void tearDown() {
		automationController = null;
	}
}
