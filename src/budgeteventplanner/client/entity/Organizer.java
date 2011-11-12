package budgeteventplanner.client.entity;

import java.util.List;
import java.util.UUID;

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;

/**
 * Somewhere else we may call organizer = new
 * (Organizer.Builder)(orgainzer).addEvent("aaa").build(); new
 * Organizer.Builder(orgainzer) .addEvent("aaa") .addEvent("aaa")
 * .addEvent("aaa") .addEvent("aaa") .addEvent("aaa") .addEvent("aaa")
 * .addEvent("aaa") .build();
 */

// BudgetAttendee Organizer = (new
// BudgetAttendee).Builder(......).setId(id).build();
@Entity
public class Organizer {

	private UUID organizerID;
	private List<String> eventList;
	private List<String> contactList;
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
			this.organizer.eventList = Lists.newArrayList();
			this.organizer.contactList = Lists.newArrayList();
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

	public UUID getOrganizerID() {
		return organizerID;
	}

	public List<String> getEventList() {
		return eventList;
	}

	public List<String> getContactList() {
		return contactList;
	}

	public String getName() {
		return name;
	}
}
