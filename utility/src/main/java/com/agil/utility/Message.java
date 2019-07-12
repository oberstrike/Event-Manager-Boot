package com.agil.utility;

public class Message {

	private MessageType type;

	private String content;

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	protected Message(MessageType type, String content) {
		this();
		this.type = type;
		this.content = content;
	}
	
	protected Message() {
		super();
	}
	
	public String getTypeLowered() {
		return this.type.toString().toLowerCase();
	}

	public static class MessageBuilder{
		
		public static Message create(MessageType type, String content) {
			return new Message(type, content);
		}
		
	}
	
}
