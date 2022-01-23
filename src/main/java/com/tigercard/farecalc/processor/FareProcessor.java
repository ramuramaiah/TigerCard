package com.tigercard.farecalc.processor;

import com.tigercard.farecalc.model.LineItemEntry;

/**
 * A simple interface contract for various {@link FareProcessor}.</br>
 * The {@link FareProcessor#rollUp()} can be called on-demand after processing all the
 * {@link LineItemEntry}. 
 */
public interface FareProcessor {
	public void initContext(SharedState userContext);
	public void setChildProcessor(FareProcessor fareCapProcessor);
	public void processLineItemEntry(LineItemEntry lineItemEntry);
	public void rollUp();
}
