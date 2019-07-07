package com.agil.model;

import java.sql.Date;
import java.util.Calendar;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


@javax.persistence.Entity
public class VerificationToken {

	private static final int EXPIRATION = 60 * 24;

	public VerificationToken() {

	}

	public VerificationToken(String token2, Member member) {
		this.token = token2;
		this.member = member;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String token;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member user) {
		this.member = user;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public static int getExpiration() {
		return EXPIRATION;
	}

	@OneToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private Member member;

	private Date expiryDate;

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}

}
