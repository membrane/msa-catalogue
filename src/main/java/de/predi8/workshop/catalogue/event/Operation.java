package de.predi8.workshop.catalogue.event;

import com.fasterxml.jackson.databind.JsonNode;

public class Operation {
	private final String type;
	private final String action;
	private final JsonNode object;

	public Operation(String type, String action, JsonNode object) {
		this.type = type;
		this.action = action;
		this.object = object;
	}

	public String getType() {
		return this.type;
	}

	public String getAction() {
		return this.action;
	}

	public JsonNode getObject() {
		return this.object;
	}

	public String toString() {
		return "Operation(type=" + type + ", action=" + action + ", object=" + object + ")";
	}
}