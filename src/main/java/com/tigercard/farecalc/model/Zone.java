package com.tigercard.farecalc.model;

import java.util.Objects;

public class Zone {

	private int zoneId;
	
	public Zone(int zoneId) {
		this.zoneId = zoneId;
	}

	public int getZoneId() {
		return zoneId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(zoneId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Zone other = (Zone) obj;
		return zoneId == other.zoneId;
	}
	
	@Override
	public String toString() {
		return String.valueOf(zoneId);
	}
}
