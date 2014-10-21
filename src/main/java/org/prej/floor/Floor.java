package org.prej.floor;

import java.util.List;

import org.prej.corridor.MainCorridor;
import org.prej.corridor.SubCorridor;

public class Floor {

	private String name;
	private List<MainCorridor> mainCorridors;
	private List<SubCorridor> subCorridors;

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

	public List<MainCorridor> getMainCorridors() {
		return mainCorridors;
	}

	public void setMainCorridors(List<MainCorridor> mainCorridors) {
		this.mainCorridors = mainCorridors;
	}

	public List<SubCorridor> getSubCorridors() {
		return subCorridors;
	}

	public void setSubCorridors(List<SubCorridor> subCorridors) {
		this.subCorridors = subCorridors;
	}

}
