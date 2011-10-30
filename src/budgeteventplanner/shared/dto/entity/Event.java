package budgeteventplanner.shared.dto.entity;

import java.util.ArrayList;
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
	private Date EndTime;
	private Address location;
	private int visibility;
	private List<Item> itemList;
	private List<Attendee> attendeeList;

	
	
	
	
	/**************************************************************/
	public static class Builder
	{
		private Event item;
		/*------------------------------------------------------*/
		public Builder(Event event) {
			this.item = (Event) event.clone();
		}
		/*------------------------------------------------------*/
		
		@Override
		public Builder(String name, Organizer organizer, int visibility)
		{
			this.item.name = name;
			this.item.organizer = organizer;
			this.item.visibility = visibility;
			this.item.eventID= UUID.randomUUID();
			this.item.itemList = Lists.newArrayList();
			this.item.attendeeList = Lists.newArrayList();

		}
		/*---------------------------------------------------*/
		public Builder addItem(Item item)
		{
			this.item.itemList.add(item);
			return this;
		}

		public Builder addAttendee(Attendee attendee)
		{
			this.item.attendeeList.add(attendee);
			return this;
		}
		
		public Builder setStartTime (Date startTime)
		{
			this.item.startTime= startTime;
			return this;
		}
		public Builder setEndTime (Date endTime)
		{
			this.item.endTime= endTime;
			return this;
		}
		public Builder setLocation (Address location)
		{
			this.item.location= location;
			return this;
		}
		
		/*---------------------------------------------------*/
		public Event build() 
		{
			return this.item;
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
	
	
}
