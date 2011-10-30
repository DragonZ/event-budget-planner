package budgeteventplanner.shared.dto.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;

//		BudgetAttendee Organizer = (new BudgetAttendee).Builder(......).setId(id).build();
@Entity
public class Organizer
{

	private UUID organizerID;
	private List<String> eventList;
	private List<String> contactList;
	private String name;

	
	
	
	
	/**************************************************************/
	public static class Builder
	{
		private Organizer organizer;
		/*------------------------------------------------------*/
		public Builder(Organizer orgainzer) {
			this.organizer = (Organizer) orgainzer.clone();
		}
		/*------------------------------------------------------*/
		
		@Override
		public Builder(String firstName, String lastName)
		{
			this.organizer.organizerID= UUID.randomUUID();
			this.organizer.contactList.eventList = Lists.newArrayList(); 
			this.organizer.contactList.contactList = Lists.newArrayList();
		}
		/*---------------------------------------------------*/
		public Builder addEvent(String event)
		{
			this.organizer.eventList.add(event);
			return this;
		}

		public Builder addContact(String contact)
		{
			this.organizer.contacttList.add(contact);
			return this;
		}
		
		public Builder setName (String name)
		{
			this.organizer.name = name;
			return this;
		}
		/*---------------------------------------------------*/
		public Organizer build() 
		{
			return this.organizer;
		}
	}
	/**************************************************************/
	
	
		// Somewhere else we may call organizer = new Organizer.Builder(orgainzer).addEvent("aaa").build();
//		new Organizer.Builder(orgainzer)
//			.addEvent("aaa")
//			.addEvent("aaa")
//			.addEvent("aaa")
//			.addEvent("aaa")
//			.addEvent("aaa")
//			.addEvent("aaa")
//			.addEvent("aaa")
//			.build();

	public List<String> getContactList()
	{
		return contactList;
		
		
	}
	
	
	
}
