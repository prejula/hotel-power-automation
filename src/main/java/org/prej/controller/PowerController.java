package org.prej.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.prej.corridor.MainCorridor;
import org.prej.corridor.SubCorridor;
import org.prej.equipment.Equipment;
import org.prej.equipment.ac.AC;
import org.prej.equipment.light.Light;
import org.prej.floor.Floor;

public class PowerController implements Controller{

	private List<Floor> floors; 
	private long startTimeLimit;
	private long endTimeLimit;
	private int sleepTime;

	/**
	 * Initialize
	 * @param floors
	 * @param startT
	 * @param endT
	 * @param sleepTime 
	 */
	public void init(List<Floor> floors, int startT, int endT, int sleepTime) {
		
		this.floors = floors;
		
		this.sleepTime = sleepTime;

		setInitialStateofEquipments();

		setNightTimeSlot(startT, endT);
	}

	/**
	 * Executes controller functionality
	 */
	public void execute(String floorName, String subCorridorName) {
		long currentTime = System.currentTimeMillis();
		if (currentTime >= startTimeLimit && currentTime <= endTimeLimit) {
			for (Floor floor : floors) {
				if (floorName.equals(floor.getName())) {
					for (SubCorridor subCorridor : floor.getSubCorridors()) {
						if (subCorridor.getName().equals(subCorridorName)) {
							for (Equipment equipment : subCorridor
									.getEquipments()) {
								if (equipment instanceof Light) {
									equipment.setState("ON");
								}
							}
							break;
						}
					}

					checkPowerConsumption(floor, subCorridorName);

					createSubCorridorEquipThread(subCorridorName, floor);

					break;
				}
			}
		}
		
	}

	private void setNightTimeSlot(int startT, int endT) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, startT);
		startTimeLimit = calendar.getTimeInMillis();

		calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, endT);
		endTimeLimit = calendar.getTimeInMillis();
	}
	
	private void createSubCorridorEquipThread(String subCorridorName,
			Floor floor) {
		ControlSubCorridorEquipment controlSubCorridorEquipment = new ControlSubCorridorEquipment();
		controlSubCorridorEquipment.setTime(System
				.currentTimeMillis());
		controlSubCorridorEquipment.setFloor(floor);
		controlSubCorridorEquipment.setSubCorridor(subCorridorName);

		Thread t = new Thread(controlSubCorridorEquipment);
		t.start();
	}

	private void checkPowerConsumption(Floor floor, String subCorridorName) {

		int totalPowerLimit = (floor.getMainCorridors().size() * 15)
				+ (floor.getSubCorridors().size() * 10);
		
		while(calulatePowerConsumption(floor) < totalPowerLimit)
		{
			changeStateDuetoLowerLimit(floor, subCorridorName);
		}
		
		while (calulatePowerConsumption(floor) > totalPowerLimit) {
			changeStateDuetoUpperLimit(floor, subCorridorName);
		}
		
		
	}

	private void changeStateDuetoLowerLimit(Floor floor, String subCorridorName) {
		
		for (SubCorridor subCorridor : floor.getSubCorridors()) {
			if (!subCorridorName.equals(subCorridor.getName())) {
				for (Equipment equipment : subCorridor.getEquipments()) {
					if (equipment instanceof AC
							&& equipment.getState().equals("OFF")) {
						equipment.setState("ON");
						break;
					} 
				}
			}
		}
		
		
	}

	private void changeStateDuetoUpperLimit(Floor floor, String subCorridorName) {

		for (SubCorridor subCorridor : floor.getSubCorridors()) {
			if (!subCorridorName.equals(subCorridor.getName())) {
				for (Equipment equipment : subCorridor.getEquipments()) {
					if (equipment instanceof AC
							&& equipment.getState().equals("ON")) {
						equipment.setState("OFF");
						break;
					} else {
						continue;
					}
				}
			}
		}
	}

	private int calulatePowerConsumption(Floor floor) {
		int powerConsumed = 0;
		for (MainCorridor mainCorridor : floor.getMainCorridors()) {
			for (Equipment equipment : mainCorridor.getEquipments()) {

				powerConsumed = "ON".equals(equipment.getState()) ? powerConsumed
						+ equipment.getPowerUnits()
						: powerConsumed + 0;
			}
		}

		for (SubCorridor subCorridor : floor.getSubCorridors()) {
			for (Equipment equipment : subCorridor.getEquipments()) {
				powerConsumed = "ON".equals(equipment.getState()) ? powerConsumed
						+ equipment.getPowerUnits()
						: powerConsumed + 0;
			}
		}
		return powerConsumed;

	}

	private void setInitialStateofEquipments() {

		for (Floor floor : floors) {
			for (MainCorridor mainCorridor : floor.getMainCorridors()) {
				for (Equipment equipment : mainCorridor.getEquipments()) {
					if (equipment instanceof Light) {
						equipment.setState("ON");
					}
				}
			}

			for (SubCorridor subCorridor : floor.getSubCorridors()) {
				for (Equipment equipment : subCorridor.getEquipments()) {
					if (equipment instanceof Light) {
						equipment.setState("OFF");
					}
				}
			}
		}

	}

	private void changeSubCorridorLightState(Floor floor, String subCorridorName) {
		for (SubCorridor subCorridor : floor.getSubCorridors()) {

			if (subCorridorName.equals(subCorridor.getName())) {
				for (Equipment equipment : subCorridor
						.getEquipments()) {
					if (equipment instanceof Light) {
						equipment.setState("OFF");
					}
				}
			} 
		}
	}

	/**
	 * Check movement
	 * @author prejula
	 *
	 */
	private class ControlSubCorridorEquipment implements Runnable {

		long timeOfLightOn = 0;
		long timeOffset = sleepTime;
		Floor floor = null;
		String subCorridorName = null;
		boolean run = true;

		public void run() {
			while (run) {
				long currentTime = System.currentTimeMillis();

				if (currentTime - timeOfLightOn >= timeOffset) {
					
					changeSubCorridorLightState(floor, subCorridorName);
					
					checkPowerConsumption(floor, subCorridorName);

					run = false;
				}
			}
		}

		public void setSubCorridor(String subCorridorName) {
			this.subCorridorName = subCorridorName;
		}

		public void setFloor(Floor floor) {
			this.floor = floor;
		}

		public void setTime(long timeOfLightOn) {
			this.timeOfLightOn = timeOfLightOn;
		}
	}

	
}
