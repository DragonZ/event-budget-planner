package budgeteventplanner.server;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import budgeteventplanner.client.AttendeeService;
import budgeteventplanner.client.entity.Attendee;
import budgeteventplanner.client.entity.Event;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class AttendeeServiceImpl extends RemoteServiceServlet implements
		AttendeeService {

	public AttendeeServiceImpl() {
		super();
		ObjectifyService.register(Attendee.class);
	}

	@Override
	public String createAttendee(String eventId, String name, String email) {
		Objectify ofy = ObjectifyService.begin();
		Attendee attendee = new Attendee.Builder(eventId, name, email).build();
		ofy.put(attendee);

		return attendee.getAttendeeId();
	}

	@Override
	public ArrayList<Attendee> getAttendeeListByOrganizerId(String organizerId) {
		Objectify ofy = ObjectifyService.begin();
		Query<Event> queryEvent = ofy.query(Event.class).filter("organizerId",
				organizerId);
		ArrayList<Attendee> result = new ArrayList<Attendee>();

		for (Event fetched : queryEvent) {
			Query<Attendee> queryAttendee = ofy.query(Attendee.class).filter(
					"eventId", fetched.getEventId());
			result.addAll(queryAttendee.list());
		}

		return result;
	}

	@Override
	public ArrayList<Attendee> getAttendeeListByEventId(String eventId) {
		Objectify ofy = ObjectifyService.begin();
		Query<Attendee> q = ofy.query(Attendee.class)
				.filter("eventId", eventId);
		return new ArrayList<Attendee>(q.list());
	}

	@Override
	public void updateAttendeeInfo(String attendeeId, String name,
			String email, String jobTitle, String companyName, String address,
			String phoneNumber) {
		Objectify ofy = ObjectifyService.begin();
		Attendee attendee = ofy.query(Attendee.class)
				.filter("attendeeId", attendeeId).get();
		Attendee updatedAttendee = new Attendee.Builder(attendee).setName(name)
				.setEmail(email).setJobTitle(jobTitle)
				.setCompanyName(companyName).setAddress(address)
				.setPhoneNumber(phoneNumber).build();
		ofy.put(updatedAttendee);
		sendEmail(updatedAttendee);
	}

	@Override
	public Integer attendeeLogin(String registrationCode) {
		return 1;
	}

	private void sendEmail(Attendee attendee) {
		Session session = Session.getDefaultInstance(new Properties(), null);
		String msgBody = "Dear " + attendee.getName()
				+ ":\n\nYour submission is accepted!" + attendee.toString()
				+ "\n\n\n Team XYZs";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("yezhuen@gmail.com", "XYZs"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					attendee.getEmail(), attendee.getName()));
			msg.setSubject("Your submission on Budget Event Planner");
			msg.setText(msgBody);
			Transport.send(msg);
		} catch (AddressException e) {
		} catch (MessagingException e) {
		} catch (UnsupportedEncodingException e) {

		}
	}
}