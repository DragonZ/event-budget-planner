package budgeteventplanner.shared.dto.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;

//		BudgetAttendee Event = (new BudgetAttendee).Builder(......).setId(id).build();
@Entity
public class Event
{

	private UUID eventID;
	private String name;
	private Organizer organizer;
	private Date startTime;
	private Date endTime;
	private Address location;
	private int visibility;
	private List<Item> itemList;
	private List<Attendee> attendeeList;

	
	
	
	
	/**************************************************************/
	public static class Builder
	{
		private Event event;
		/*------------------------------------------------------*/
		public Builder(Event event) {
			try {
				this.event = (Event) event.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*------------------------------------------------------*/

		public Builder(String name, Organizer organizer, int visibility)
		{
			this.event.name = name;
			this.event.organizer = organizer;
			this.event.visibility = visibility;
			this.event.eventID = UUID.randomUUID();
			this.event.itemList = Lists.newArrayList();
			this.event.attendeeList = Lists.newArrayList();
		}
		/*---------------------------------------------------*/

		
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
		
		/*---------------------------------------------------*/
		public Event build() 
		{
			return this.event;
		}
		/*---------------------------------------------------*/
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
	/**************************************************************/
	
	public List<Item> getItemList()
	{
		return itemList;
	}
	
	public List<Attendee> getAttendeeList()
	{
		return attendeeList;
	}
	
	public UUID getEventID()
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
	
	public Date getStartTime()
	{
		return startTime;
	}
	
	public Date getEndTime()
	{
		return endTime;
	}
	
	public Address location()
	{
		return location;
	}
	
	public int getVisibility()
	{
		return visibility;
	}
	
}
