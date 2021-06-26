package br.com.kualit.tablecodegenerator.model;

import java.io.Serializable;

public class Column implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4194358131045529739L;
	
	private String name;
	private String editable;
	private String showCheckBox;

	public Column(String name, String editable, String showCheckBox) {
		this.name = name;
		this.editable = editable;
		this.showCheckBox = showCheckBox;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEditable() {
		return editable;
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}

	public String getShowCheckBox() {
		return showCheckBox;
	}

	public void setShowCheckBox(String showCheckBox) {
		this.showCheckBox = showCheckBox;
	}

}
