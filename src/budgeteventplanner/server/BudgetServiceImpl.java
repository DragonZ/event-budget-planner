package budgeteventplanner.server;

import java.security.NoSuchAlgorithmException;

import budgeteventplanner.client.BudgetService;
import budgeteventplanner.client.entity.Attendee;
import budgeteventplanner.client.entity.Budget;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class BudgetServiceImpl extends RemoteServiceServlet implements BudgetService 
{
	public BudgetServiceImpl() 
	{
		ObjectifyService.register(Attendee.class);
	}
	
	@Override
	public void createBudget(String budegetName)
			throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		Budget attendee= new Budget.Builder(budegetName)
		.build();
		ofy.put(attendee);
		
	}
}