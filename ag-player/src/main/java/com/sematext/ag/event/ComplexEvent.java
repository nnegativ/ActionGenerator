package com.sematext.ag.event;

import pl.solr.dm.types.ObjectDataType;

/**
 * Implementation of <code>Event<code> which allows use more sophisticated record declaration.
 *  
 * @author negativ
 *
 */
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
