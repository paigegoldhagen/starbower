package com.paigegoldhagen.astral;

/**
 * For setting and getting checkbox parameters.
 */
public class Parameters {
	private final String Name;
	private final boolean State;
	
	public Parameters(String eventName, boolean notifyState) {
		this.Name = eventName;
		this.State = notifyState;
	}
	
	public String getName() {return Name;}
	public boolean getState() {return State;}
}