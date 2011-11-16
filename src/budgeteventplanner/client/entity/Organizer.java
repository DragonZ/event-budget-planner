package budgeteventplanner.client.entity;

import java.io.Serializable;
import java.util.ArrayList;

import budgeteventplanner.shared.UUID;

import com.googlecode.objectify.annotation.Entity;

/**
 * Somewhere else we may call organizer = new
 * (Organizer.Builder)(orgainzer).addEvent("aaa").build(); new
 * Organizer.Builder(orgainzer) .addEvent("aaa") .addEvent("aaa")
 * .addEvent("aaa") .addEvent("aaa") .addEvent("aaa") .addEvent("aaa")
 * .addEvent("aaa") .build();
 */

@SuppressWarnings("serial")
// BudgetAttendee Organizer = (new
// BudgetAttendee).Builder(......).setId(id).build();
@Entity
public class Organizer implements Serializable{

	private String organizerID;
	private ArrayList<String> eventList;
	private ArrayList<String> contactList;
	private String name;

	public Organizer() {
	}

	public static class Builder {
		private Organizer organizer = new Organizer();

		public Builder(Organizer orgainzer) {
			this.organizer = orgainzer;
		}

		public Builder(String firstName, String lastName) {
			this.organizer.organizerID = UUID.randomUUID();
			this.organizer.eventList = new ArrayList<String>();
			this.organizer.contactList = new ArrayList<String>();
		}

		public Builder setName(String name) {
			this.organizer.name = name;
			return this;
		}

		public Organizer build() {
			return this.organizer;
		}

		public Builder addEvent(String event) {
			this.organizer.eventList.add(event);
			return this;
		}

		public Builder addContact(String contact) {
			this.organizer.contactList.add(contact);
			return this;
		}
	}

	public String getOrganizerID() {
		return organizerID;
	}

	public ArrayList<String> getEventList() {
		return eventList;
	}

	public ArrayList<String> getContactList() {
		return contactList;
	}

	public String getName() {
		return name;
	}
}
