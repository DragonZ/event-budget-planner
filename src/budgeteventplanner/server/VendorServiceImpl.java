package budgeteventplanner.server;

import java.security.NoSuchAlgorithmException;

import budgeteventplanner.client.BudgetService;
import budgeteventplanner.client.VendorService;
import budgeteventplanner.client.entity.Attendee;
import budgeteventplanner.client.entity.Budget;
import budgeteventplanner.client.entity.Vendor;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class VendorServiceImpl extends RemoteServiceServlet implements VendorService 
{
	public VendorServiceImpl() 
	{
		ObjectifyService.register(Vendor.class);
	}
	
	@Override
	public void createVendor(String vendorName, String location)
			throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		
		Vendor vendor = new Vendor.Builder().setName(vendorName).setLocation(location).build();
		
		ofy.put(vendor);
		
	}

}