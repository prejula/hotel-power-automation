/**
 * 
 */
package org.prej.main;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.prej.controller.PowerController;
import org.prej.corridor.MainCorridor;
import org.prej.corridor.SubCorridor;
import org.prej.equipment.Equipment;
import org.prej.equipment.ac.AC;
import org.prej.equipment.light.Light;
import org.prej.floor.Floor;
import org.prej.ouput.EquipmentStateWriter;

/**
 * @author prejula
 * 
 */
public class AutomationControlSystem {

	private PowerController powerController = null;
	
	private List<Floor> floorPlan;

	/**
	 * Initialize floor plan
	 * @throws IOException
	 */
	public void init() throws IOException {
		
		Properties properties = loadPropertiesFile();
		
		floorPlan = createFloorPlan(properties);

		powerController = new PowerController();
		powerController.init(floorPlan, Integer.parseInt((String) properties
				.get("night_slot_starttime")), Integer
				.parseInt((String) properties.get("night_slot_endtime")), Integer.parseInt((String) properties
						.get("sleep_time")));
	}

	/**
	 * write state of floor equipments in output stream
	 * @param outputStream
	 */
	public void writeEquipmentState(OutputStream outputStream) {
		try {

			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					outputStream);
			EquipmentStateWriter equipmentStateWriter = new EquipmentStateWriter(floorPlan);
			String result = equipmentStateWriter.write();

			bufferedOutputStream.write(result.getBytes());
			bufferedOutputStream.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param floorName
	 * @param subCorridorName
	 */
	public void movementOccured(String floorName, String subCorridorName) {
		powerController.execute(floorName, subCorridorName);
	}
		
	/**
	 * @return the floorPlan
	 */
	public List<Floor> getFloorPlan() {
		return floorPlan;
	}
	
	private List<Floor> createFloorPlan(Properties properties) {
		
		List<Floor> floors = null;
		
		int noOfFloors = Integer.parseInt((String) properties
				.get("no_floors"));
		
		
		for (int i = 1; i <= noOfFloors; i++) {
			
			floors = null == floors ? new ArrayList<Floor>() : floors;

			Floor floor = new Floor();
			floor.setName("Floor" + i);
			
			createMainCorridor(properties, floor);

			createSubCorridors(properties, floor);
			
			floors.add(floor);
		}
		return floors;
	}

	private void createSubCorridors(Properties properties, Floor floor) {
		
		List<SubCorridor> subCorridors = null;

		int noOfSubCorridorPerFloor = Integer.parseInt((String) properties
				.get("no_subcorridors_per_floor"));
		int noLightsPerSubCorridor = Integer.parseInt((String) properties
				.get("no_lights_per_subcorridor"));;
	    int noAcPerSubCorridor = Integer.parseInt((String) properties
				.get("no_ac_per_subcorridor"));
		
		for (int j = 1; j <= noOfSubCorridorPerFloor; j++) {
			
			subCorridors = null == subCorridors ? new ArrayList<SubCorridor>()
					: subCorridors;

			SubCorridor subCorridor = new SubCorridor();
			subCorridor.setName("Sub" + j);

			List<Equipment> equipments = createEquipments(noLightsPerSubCorridor, noAcPerSubCorridor, properties);
			subCorridor.setEquipments(equipments);
			
			subCorridors.add(subCorridor);
		}

		floor.setSubCorridors(subCorridors);
	}

	private void createMainCorridor(Properties properties, Floor floor) {
		
		List<MainCorridor> mainCorridors = null;
		
		int noMainCorridors = Integer.parseInt((String) properties
				.get("no_maincorridors_per_floor"));
		int noLightsPerMainCorridor = Integer.parseInt((String) properties
				.get("no_lights_per_maincorridor"));
		int noACPerMainCorridor = Integer.parseInt((String) properties
				.get("no_ac_per_maincorridor"));
		
		for (int j = 1; j <= noMainCorridors; j++) {
			
			mainCorridors = null == mainCorridors ? new ArrayList<MainCorridor>()
					: mainCorridors;

			MainCorridor mainCorridor = new MainCorridor();
			mainCorridor.setName("Main" + j);

			List<Equipment> equipments = createEquipments(noLightsPerMainCorridor, noACPerMainCorridor, properties);
			mainCorridor.setEquipments(equipments);
			
			mainCorridors.add(mainCorridor);
		}

		floor.setMainCorridors(mainCorridors);
	}

	private List<Equipment> createEquipments(int noLightsPerMainCorridor,
			int noACPerMainCorridor, Properties properties) {
	
		
		List<Equipment> equipments = new ArrayList<Equipment>();

		int powerUnitPerLight = Integer.parseInt((String) properties
				.get("power_unit_light"));
		int powerUnitPerAC = Integer.parseInt((String) properties
				.get("power_unit_ac"));
		
		for (int k = 1; k <= noLightsPerMainCorridor; k++) {
			
			equipments = null == equipments ? new ArrayList<Equipment>()
					: equipments;

			Light light = new Light();
			light.setName("Light" + k);
			light.setState("OFF");
			light.setPowerUnits(powerUnitPerLight);

			equipments.add(light);

		}
		for (int l = 1; l <= noACPerMainCorridor; l++) {
			
			equipments = null == equipments ? new ArrayList<Equipment>()
					: equipments;

			AC ac = new AC();
			ac.setName("AC" + l);
			ac.setState("ON");
			ac.setPowerUnits(powerUnitPerAC);

			equipments.add(ac);
		}
		return equipments;
	}

	private Properties loadPropertiesFile() throws IOException {
		Properties properties = new Properties();

		InputStream inputStream = this.getClass()
				.getResourceAsStream("/init.properties");

		properties.load(inputStream);
		return properties;
	}
}
