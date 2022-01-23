package com.tigercard.farecalc.processor;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import com.tigercard.farecalc.model.DayEntry;
import com.tigercard.farecalc.model.LineItemEntry;
import com.tigercard.farecalc.model.WeekEntry;
import com.tigercard.farecalc.model.Zone;

/**
 * Shared state between various {@link FareProcessor}'s.
 */
public class SharedState {
	protected List<DayEntry> dayEntries = new ArrayList<DayEntry>();
	protected List<WeekEntry> weekEntries = new ArrayList<WeekEntry>();
	protected LineItemEntry lastProcessedEntry;
	protected AbstractMap.SimpleEntry<Zone, Zone> farthestTripInADay;
	protected AbstractMap.SimpleEntry<Zone, Zone> farthestTripInAWeek;
	protected int totalAmount;
}
