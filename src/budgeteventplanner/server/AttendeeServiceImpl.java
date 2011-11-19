package budgeteventplanner.server;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import budgeteventplanner.client.AttendeeService;
import budgeteventplanner.client.entity.Attendee;
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
	public String createAttendee(String firstName, String lastName, String email)
			throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		Attendee attendee= new Attendee.Builder(firstName, lastName, email).build();
		ofy.put(attendee);
		return attendee.getAttendeeId();
		
	}
	
	@Override
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
	
	
	// This function updates the info of a given attendee
	public void saveAttendeeInfo(String attendeeID,  String email, 
			String firstName, String midName, String lastName, 
			String jobTitle, String companyName, String address, String phoneNum)
	{
		Objectify ofy = ObjectifyService.begin();
		Attendee attendee = ofy.query(Attendee.class).filter("id", attendeeID).get();

		new Attendee.Builder(attendee).setName(lastName).setCompanyName(companyName).setJobTitle(jobTitle).build();
		
		ofy.put(attendee);
	}
	
	@Override
	public Integer attendeeLogin(String registrationCode)
			throws NoSuchAlgorithmException {
		return 1;
	}
	
	
}