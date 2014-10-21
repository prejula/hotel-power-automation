/**
 * 
 */
package org.prej.equipment.ac;

import org.prej.equipment.Equipment;

/**
 * @author prejula
 * 
 */
public class AC implements Equipment {

	private String state;
	private int powerUnits;
	private String name; 

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

	public String getState() {
		return null == state ? "ON" : state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getPowerUnits() {
		return powerUnits;
	}

	public void setPowerUnits(int powerUnits) {
		this.powerUnits = powerUnits;
	}

}
