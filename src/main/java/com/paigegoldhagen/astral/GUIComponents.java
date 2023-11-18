package com.paigegoldhagen.astral;

import java.awt.*;
import java.util.List;

/**
 * For setting and getting GUI components.
 */
public class GUIComponents {
	private final Label Title, Subtitle, LeftText, RightText;
	private final Choice Dropdown;
	private final ScrollPane ScrollWindow;
	private final List<Checkbox> Checkboxes;
	
	public GUIComponents(Label title, Label subtitle, Label leftText, Label rightText, Choice dropdown, ScrollPane scrollWindow, List<Checkbox> checkboxes) {
		this.Title = title;
		this.Subtitle = subtitle;
		this.LeftText = leftText;
		this.RightText = rightText;
		this.Dropdown = dropdown;
		this.ScrollWindow = scrollWindow;
		this.Checkboxes = checkboxes;
	}
	
	public Component[] getComponentsList() {return new Component[]{Title, Subtitle, LeftText, RightText, Dropdown, ScrollWindow};}
	public Choice getDropdown() {return Dropdown;}
	public List<Checkbox> getCheckboxes() {return Checkboxes;}
	
}