package budgeteventplanner.server;

import java.security.NoSuchAlgorithmException;
import budgeteventplanner.client.EventService;
import budgeteventplanner.client.entity.Event;
import budgeteventplanner.client.entity.Organizer;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class EventServiceImpl extends RemoteServiceServlet implements EventService 
{
	public EventServiceImpl() 
	{
		ObjectifyService.register(Event.class);
	}

	@Override
	public void createEvent(String eventName, String organizer, Integer visibility)
			throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		Event event= new Event.Builder(eventName, organizer, visibility)
		.build();
		ofy.put(event);
		
	}
}