package com.tigercard.farecalc.model;

public class Fare {
	private FareType fareType;
	private int fareAmount;
	
	public Fare(FareType fareType, int fareAmount) {
		this.fareType = fareType;
		this.fareAmount = fareAmount;
	}

	public FareType getFareType() {
		return fareType;
	}

	public int getFareAmount() {
		return fareAmount;
	}
}
