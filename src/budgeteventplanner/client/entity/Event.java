package budgeteventplanner.client.entity;

import java.util.Date;
import java.util.List;

import budgeteventplanner.shared.UUID;

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;

//		BudgetAttendee Event = (new BudgetAttendee).Builder(......).setId(id).build();
@Entity
public class Event
{
	private String eventID;
	private String name;
	private Organizer organizer;
	private Date startTime;
	private Date endTime;
	private Address location;
	private int visibility;
	private List<Item> itemList;
	private List<Attendee> attendeeList;

	public static class Builder
	{
		private Event event = new Event();
		
		public Builder(Event event) {
			this.event = event;
		}

		public Builder(String name, Organizer organizer, int visibility)
		{
			this.event.name = name;
			this.event.organizer = organizer;
			this.event.visibility = visibility;
			this.event.eventID = UUID.randomUUID();
			this.event.itemList = Lists.newArrayList();
			this.event.attendeeList = Lists.newArrayList();
		}
		
		public Builder setStartTime (Date startTime)
		{
			this.event.startTime = startTime;
			return this;
		}
		public Builder setEndTime (Date endTime)
		{
			this.event.endTime= endTime;
			return this;
		}
		public Builder setLocation (Address location)
		{
			this.event.location= location;
			return this;
		}
		
		public Event build() 
		{
			return this.event;
		}
		
		public Builder addItem(Item event)
		{
			this.event.itemList.add(event);
			return this;
		}

		public Builder addAttendee(Attendee attendee)
		{
			this.event.attendeeList.add(attendee);
			return this;
		}
	}
	
	public List<Item> itemList()
	{
		return itemList;
	}
	
	public List<Attendee> attendeeList()
	{
		return attendeeList;
	}
	
	public String eventID()
	{
		return eventID;
	}
	
	public String name()
	{
		return name;
	}
	
	public Organizer organizer()
	{
		return organizer;
	}
	
	public Date startTime()
	{
		return startTime;
	}
	
	public Date endTime()
	{
		return endTime;
	}
	
	public Address location()
	{
		return location;
	}
	
	public int visibility()
	{
		return visibility;
	}
	
}