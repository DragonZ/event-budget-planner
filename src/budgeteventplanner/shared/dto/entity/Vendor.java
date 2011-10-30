package budgeteventplanner.shared.dto.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;

//		BudgetAttendee Vendor = (new BudgetAttendee).Builder(......).setId(id).build();
@Entity
public class Vendor
{

	private UUID vendorID;
	private String name;
	private List<Category> categoryList;
	private Address location;
	private List<Item> itemList;
	private List<Service> serviceList;

	
	
	
	
	/**************************************************************/
	public static class Builder
	{
		private Vendor vendor;
		/*------------------------------------------------------*/
		public Builder(Vendor event) 
		{
			this.vendor = (Vendor) event.clone();
			this.organizer.contactList.itemList = Lists.newArrayList(); 
			this.organizer.contactList.serviceList = Lists.newArrayList();
		}
		/*------------------------------------------------------*/
		
		@Override
		public Builder(String name)
		{
			this.vendor.name = name;
			this.vendor.vendorID= UUID.randomUUID();
		}
		/*---------------------------------------------------*/
		
		public Builder setLocation (Address location)
		{
			this.vendor.location= location;
			return this;
		}
		
		public Builder setName (String name)
		{
			this.vendor.name = name;
			return this;
		}
		
		/*---------------------------------------------------*/
		public Vendor build() 
		{
			return this.vendor;
		}
		
		/*------------------------------------------------------*/
		public Builder addService(Service service)
		{
			this.vendor.serviceList.add(service);
			return this;
		}

	}
	/**************************************************************/
	
	public List<Item> getItemList()
	{
		return itemList;
	}
	
	public List<Item> getServiceList()
	{
		return serviceList;
	}
	
	public String getName()
	{
		return name;
	}
	
	public List<Attendee> getAttendeeList()
	{
		return attendeeList;
	}
	
	
}
