/**
 * 
 */
package org.prej.ouput;

import java.util.List;

import org.prej.corridor.MainCorridor;
import org.prej.corridor.SubCorridor;
import org.prej.equipment.Equipment;
import org.prej.floor.Floor;

/**
 * @author prejula
 * 
 */
public class EquipmentStateWriter {

	private List<Floor> floors;

	/** 
	 * set floors
	 * @param floors
	 */
	public EquipmentStateWriter(List<Floor> floors) {
		this.floors = floors;
	}

	/**
	 * create equipment state string
	 * @return
	 */
	public String write() {

		StringBuilder builder = new StringBuilder();
		for (Floor floor : floors) {
			builder.append(floor.getName());
			builder.append("\n");

			for (MainCorridor mainCorridor : floor.getMainCorridors()) {
				builder.append(mainCorridor.getName());
				builder.append("\n");
				for (Equipment equipment : mainCorridor.getEquipments()) {
					builder.append(equipment.getName() + "  "
							+ equipment.getState());
					builder.append("\n");
				}
			}
			for (SubCorridor subCorridor : floor.getSubCorridors()) {
				builder.append(subCorridor.getName());
				builder.append("\n");
				for (Equipment equipment : subCorridor.getEquipments()) {
					builder.append(equipment.getName() + "  "
							+ equipment.getState());
					builder.append("\n");
				}
			}

		}
		return builder.toString();
	}

}
