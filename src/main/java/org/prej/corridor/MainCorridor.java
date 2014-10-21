/**
 * 
 */
package org.prej.corridor;

import java.util.List;

import org.prej.equipment.Equipment;

/**
 * @author prejula
 * 
 */
public class MainCorridor { 

	private String name;

	private List<Equipment> equipments;

	public MainCorridor()
	{
	}

	/**
	 * @return the equipments
	 */
	public List<Equipment> getEquipments() {
		return equipments;
	}

	/**
	 * @param equipments
	 *            the equipments to set
	 */
	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
