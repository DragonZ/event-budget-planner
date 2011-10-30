package budgeteventplanner.shared.dto.entity;

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
			try {
				this.vendor = (Vendor) event.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.vendor.itemList = Lists.newArrayList(); 
			this.vendor.serviceList = Lists.newArrayList();
		}
		/*------------------------------------------------------*/
		

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
	
	/************** Getters ********************************/
	public UUID vendorID()
	{
		return vendorID;
	}
	
	public Address location()
	{
		return location;
	}
	
	public List<Item> itemList()
	{
		return itemList;
	}
	
	public List<Category> categoryList()
	{
		return categoryList;
	}
	
	public String name()
	{
		return name;
	}

	public List<Service> serviceList()
	{
		return serviceList;
	}
	
	
}
