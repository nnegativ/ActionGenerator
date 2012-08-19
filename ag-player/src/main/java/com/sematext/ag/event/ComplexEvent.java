package com.sematext.ag.event;

import pl.solr.dm.types.ObjectDataType;

public class ComplexEvent extends Event {
	private ObjectDataType object;
	
	public ComplexEvent(final ObjectDataType object) {
		this.object = object;
	}

	public ObjectDataType getObject() {
		return object;
	}

	public String getIdentifier() {
		return object.getIdentifier();
	}

}
