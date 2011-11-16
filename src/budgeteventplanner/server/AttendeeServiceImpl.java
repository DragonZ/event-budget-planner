package budgeteventplanner.server;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import budgeteventplanner.client.AttendeeService;
import budgeteventplanner.client.entity.Attendee;
import budgeteventplanner.client.entity.TestEntity;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class AttendeeServiceImpl extends RemoteServiceServlet implements AttendeeService 
{
	public AttendeeServiceImpl() 
	{
		ObjectifyService.register(Attendee.class);
	}
	
	@Override
	public void createAttendee(String firstName, String lastName, String email)
			throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		Attendee attendee= new Attendee.Builder(firstName, lastName)
		.build();
		ofy.put(attendee);
		
	}
	
	public ArrayList<Attendee> getAttendeeList(String eventOrganizerID)
	{
		
		Objectify ofy = ObjectifyService.begin();
		Query<Attendee> q = ofy.query(Attendee.class).filter("id", eventOrganizerID);
		ArrayList<Attendee> attendees = new ArrayList<Attendee>();

		//Loop the query results and add to the array
		for (Attendee fetched : q) {
			attendees.add(fetched);
		}
		return attendees;
		
		
		
	}
	
	
	
}