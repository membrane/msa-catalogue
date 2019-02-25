package de.predi8.catalogue.event;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

//import static net.logstash.logback.marker.Markers.appendEntries;

public class Operation {

	private static final Logger log = LoggerFactory.getLogger(Operation.class);

	private String bo;
	private String action;
	private JsonNode object;

	public Operation() {
	}

	public Operation(String bo, String action, JsonNode object) {
		this.bo = bo;
		this.action = action;
		this.object = object;
	}


	public void setBo(String bo) {
		this.bo = bo;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setObject(JsonNode object) {
		this.object = object;
	}

	public String getBo() {
		return this.bo;
	}

	public String getAction() {
		return this.action;
	}

	public JsonNode getObject() {
		return this.object;
	}

	public String toString() {
		return "Operation( bo=" + bo + " action=" + action + " object=" + object + ")";
	}

	public void logSend() {
		log("send");
	}

	public void logReceive() {
		log("receive");
	}

	private void log(String direction) {
		Map<String,Object> entries = new HashMap<>();
		entries.put("bo",bo);
		entries.put("action", action);
		entries.put("object", object);
		entries.put("direction", direction);

//		log.info(appendEntries(entries),"");
	}
}