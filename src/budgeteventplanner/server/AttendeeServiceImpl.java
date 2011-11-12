package budgeteventplanner.server;

import java.security.NoSuchAlgorithmException;

import budgeteventplanner.client.AttendeeService;
import budgeteventplanner.client.entity.Attendee;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

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
		Attendee attendee= new Attendee.Builder(firstName, lastName, email)
		.build();
		ofy.put(attendee);
		
	}
}