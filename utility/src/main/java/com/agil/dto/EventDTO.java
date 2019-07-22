package com.agil.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.agil.model.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventDTO {

	private long id;

	@Size(min = 6, max = 16, message = "{event.name.notempty}")
	@NotNull(message = "{event.name.notempty}")
	private String name;

	@NotNull(message = "{event.date.badformat}")
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	private Date startDate;

	private Date rememberDate;

	private String creatorName;

	private boolean finished;

	private boolean remembered;

}
