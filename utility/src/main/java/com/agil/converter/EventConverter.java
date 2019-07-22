package com.agil.converter;

import com.agil.dto.EventDTO;
import com.agil.model.Event;

public class EventConverter extends DTOConverter {

	public EventConverter() {
		super(Event.class, EventDTO.class);
	}

	@Override
	public Object convert(Object object) {
		Object convertedObject = super.convert(object);
		if (convertedObject.getClass().equals(getDtoClass())) {
			Event event = (Event) object;
			((EventDTO) convertedObject).setCreatorName(event.getCreator().getUsername());
		}

		return convertedObject;
	}

}
