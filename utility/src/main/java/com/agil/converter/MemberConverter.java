package com.agil.converter;

import com.agil.dto.MemberDTO;
import com.agil.model.Member;

public class MemberConverter extends DTOConverter {

	public MemberConverter() {
		super(Member.class, MemberDTO.class);
	}

	@Override
	public Object convert(Object object) {
		Object convertedObject = super.convert(object);
		if (convertedObject.getClass().equals(getDtoClass())) {
			Member member = (Member) object;
			((MemberDTO) convertedObject).setIsAdmin(member.isAdmin());
		}
		
		return convertedObject;
	}
}
