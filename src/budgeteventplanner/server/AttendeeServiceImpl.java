package budgeteventplanner.server;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class AttendeeServiceImpl extends RemoteServiceServlet implements AttendeeService {

	public static final Integer EMAIL_REMOVE = -1;
	public static final Integer EMAIL_INVITE = 1;
	public static final int STATUS_NONE = 100;
	static {
		try {
			ObjectifyService.register(Event.class);
			ObjectifyService.register(Attendee.class);
			//ObjectifyService.register(User.class);
		} catch (Exception e) {
		}
	}

	@Override
	public Attendee createAttendee(String eventId, String name, String email) {
		Objectify ofy = ObjectifyService.begin();
		Attendee attendee = new Attendee.Builder(eventId, name, email).build();
		ofy.put(attendee);
		sendEmail(attendee, EMAIL_INVITE);
		return attendee;
	}

	@Override
	public List<Attendee> getAttendeeListByOrganizerId(String organizerId) {
		Objectify ofy = ObjectifyService.begin();
		Query<Event> queryEvent = ofy.query(Event.class).filter("organizerId", organizerId);

		List<Attendee> result = Lists.newArrayList();

		for (Event fetched : queryEvent) {
			if (!fetched.getStatus().equals(Integer.valueOf(0)))
				continue;
			Query<Attendee> queryAttendee = ofy.query(Attendee.class).filter("eventId",
					fetched.getEventId());
			result.addAll(queryAttendee.list());
		}

		return result;
	}

	@Override
	public List<Attendee> getAttendeeListByEventId(String eventId) {
		Objectify ofy = ObjectifyService.begin();
		Query<Attendee> q = ofy.query(Attendee.class).filter("eventId", eventId);
		return q.list();
	}

	public SetMultimap<String, Attendee> getSortedAttendeeList(String organizerId,
			String excludedEventId) {
		SetMultimap<String, Attendee> multimap = HashMultimap.create();
		List<Event> listEvent = new EventServiceImpl().getEventsByOrganizerIdAndStatus(organizerId,
				Event.ACTIVE);
		for (Event event : listEvent) {
			if (!event.getEventId().equals(excludedEventId)) {
				List<Attendee> listAttendee = getAttendeeListByEventId(event.getEventId());
				Collections.sort(listAttendee, new AttendeeComparator());
				multimap.putAll(event.getName(), listAttendee);
			}
		}

		return multimap;
	}

	@Override
	public void updateAttendeeInfo(String attendeeId, String name, String email, String jobTitle,
			String companyName, String address, String phoneNumber, Integer status) {
		Objectify ofy = ObjectifyService.begin();
		if (status.equals(STATUS_NONE)) {
			Attendee attendee = ofy.get(new Key<Attendee>(Attendee.class, attendeeId));
			Attendee updatedAttendee = new Attendee.Builder(attendee).setName(name).setEmail(email)
					.setJobTitle(jobTitle).setCompanyName(companyName).setAddress(address)
					.setPhoneNumber(phoneNumber).build();
			ofy.put(updatedAttendee);
			sendEmail(updatedAttendee);
		} else {
			Attendee attendee = ofy.get(new Key<Attendee>(Attendee.class, attendeeId));
			Attendee updatedAttendee = new Attendee.Builder(attendee).setName(name).setEmail(email)
					.setJobTitle(jobTitle).setCompanyName(companyName).setAddress(address)
					.setPhoneNumber(phoneNumber).setStatus(status).build();
			ofy.put(updatedAttendee);
			sendEmail(updatedAttendee);
		}
	}

	@Override
	public Integer attendeeLogin(String registrationCode) {
		return 1;
	}

	// /This only send accept email
	private void sendEmail(Attendee attendee) {
		Objectify ofy = ObjectifyService.begin();
		Event event = ofy.get(new Key<Event>(Event.class, attendee.getEventId()));
		//User user = ofy.get(new Key<User>(User.class, event.getOrganizerId()));
		Session session = Session.getDefaultInstance(new Properties(), null);
		String msgBody = getConfirmationEmailBody(attendee, event);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("yezhuen@gmail.com", "XYZs"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(attendee.getEmail(),
					attendee.getName()));
			msg.setSubject("[Budget Event Planner] You have successfully updated your information for event: "
					+ event.getName() + " started on " + event.getStartTime());
			msg.setText(msgBody);
			Transport.send(msg);
		} catch (AddressException e) {
		} catch (MessagingException e) {
		} catch (UnsupportedEncodingException e) {

		}
	}

	// this will send invite or remove
	private void sendEmail(Attendee attendee, Integer status) {
		Objectify ofy = ObjectifyService.begin();
		Event event = ofy.get(new Key<Event>(Event.class, attendee.getEventId()));
		//User user = ofy.get(new Key<User>(User.class, event.getOrganizerId()));
		if (status.equals(EMAIL_REMOVE)) // -1 means deleted by organizer
		{
			String subject = "[Budget Event Planner] You've been removed from " + event.getName()
					+ " started on " + event.getStartTime();
			String msgBody = getRemoveEmailBody(attendee, event);
			sendCustomizedEmail(attendee, subject, msgBody);
		}
		if (status.equals(EMAIL_INVITE)) // 1 means send inviting letter
		{
			String subject = "[Budget Event Planner] invitation to " + event.getName()
					+ " started on " + event.getStartTime();
			String msgBody = getInvitationEmailBody(attendee, event);
			sendCustomizedEmail(attendee, subject, msgBody);
		}

	}

	public void sendEmail(String attendeeId, Integer status) {
		Objectify ofy = ObjectifyService.begin();
		Attendee attendee = ofy.get(new Key<Attendee>(Attendee.class, attendeeId));
		sendEmail(attendee, status);
	}

	public void sendEmailBatchByOrganizer(List<String> attendeeIdList, Integer status) {
		Objectify ofy = ObjectifyService.begin();

		for (String attendeeId : attendeeIdList) {
			Attendee attendee = ofy.get(new Key<Attendee>(Attendee.class, attendeeId));
			Event event = ofy.get(new Key<Event>(Event.class, attendee.getEventId()));
			//User user = ofy.get(new Key<User>(User.class, event.getOrganizerId()));

			if (status.equals(EMAIL_REMOVE)) // -1 means deleted by organizer
			{
				String subject = "[Budget Event Planner] You've been removed from "
						+ event.getName() + " started on " + event.getStartTime();
				String msgBody = getRemoveEmailBody(attendee, event);
				sendCustomizedEmail(attendee, subject, msgBody);
			}
			if (status.equals(EMAIL_INVITE)) // 1 means send inviting letter
			{
				String subject = "[Budget Event Planner] invitation to " + event.getName()
						+ " started on " + event.getStartTime();
				String msgBody = getInvitationEmailBody(attendee, event);
				sendCustomizedEmail(attendee, subject, msgBody);
			}
		}

	}

	public void sendEmailBatch(List<String> attendeeIdList, Integer status) {
		Objectify ofy = ObjectifyService.begin();
		for (String attendeeId : attendeeIdList) {
			Attendee attendee = ofy.get(new Key<Attendee>(Attendee.class, attendeeId));
			sendEmail(attendee, status);
		}
	}

	public void sendCustomizedEmail(String attendeeId, String subject, String msgBody) {
		Attendee attendee = getAttendeeByAttendeeId(attendeeId);
		Session session = Session.getDefaultInstance(new Properties(), null);
		msgBody = "Dear " + attendee.getName() + "\n" + msgBody + "\n\n\n Team XYZs";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("yezhuen@gmail.com", "XYZs"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(attendee.getEmail(),
					attendee.getName()));
			msg.setSubject(subject);
			msg.setText(msgBody);
			Transport.send(msg);
		} catch (AddressException e) {
		} catch (MessagingException e) {
		} catch (UnsupportedEncodingException e) {

		}
	}

	public void sendCustomizedEmail(Attendee attendee, String subject, String msgBody) {
		Session session = Session.getDefaultInstance(new Properties(), null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("yezhuen@gmail.com", "XYZs"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(attendee.getEmail(),
					attendee.getName()));
			msg.setSubject(subject);
			msg.setText(msgBody);
			Transport.send(msg);
		} catch (AddressException e) {
		} catch (MessagingException e) {
		} catch (UnsupportedEncodingException e) {

		}
	}

	@Override
	public Attendee getAttendeeByAttendeeId(String attendeeId) {
		Objectify ofy = ObjectifyService.begin();
		Attendee attendee = ofy.get(new Key<Attendee>(Attendee.class, attendeeId));
		return attendee;
	}

	@Override
	public void removeAttendeeByAttendeeList(List<Attendee> attendeeList) {
		List<String> attendeeIdList = Lists.newArrayList();
		for (Attendee a : attendeeList) {
			attendeeIdList.add(a.getAttendeeId());
		}
		sendEmailBatchByOrganizer(attendeeIdList, EMAIL_REMOVE);
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(attendeeList);
	}

	@Override
	public List<Attendee> fillAttendeesInEvent(String eventId, List<Attendee> attendeeList) {
		List<Attendee> newattendeeList = Lists.newArrayList();
		List<String> newattendeeIdList = Lists.newArrayList();
		Objectify ofy = ObjectifyService.begin();
		for (Attendee attendee : attendeeList) {
			Attendee oldAttendee = ofy.get(new Key<Attendee>(Attendee.class, attendee
					.getAttendeeId()));
			Attendee newAttendee = new Attendee.Builder(oldAttendee, eventId).build();
			ofy.put(newAttendee);
			newattendeeList.add(newAttendee);
			newattendeeIdList.add(newAttendee.getAttendeeId());
		}

		sendEmailBatchByOrganizer(newattendeeIdList, EMAIL_INVITE);
		return newattendeeList;
	}

	@Override
	public SetMultimap<Integer, Attendee> getSortedEventAttendeeByStatus(String eventId) {
		SetMultimap<Integer, Attendee> multimap = HashMultimap.create();

		multimap.putAll(Attendee.YES, getEventAttendeeByStatus(eventId, Attendee.YES));
		multimap.putAll(Attendee.NO, getEventAttendeeByStatus(eventId, Attendee.NO));
		multimap.putAll(Attendee.MAYBE, getEventAttendeeByStatus(eventId, Attendee.MAYBE));
		multimap.putAll(Attendee.PENDING, getEventAttendeeByStatus(eventId, Attendee.PENDING));

		return multimap;
	}

	@Override
	public List<Attendee> getEventAttendeeByStatus(String eventId, Integer status) {
		Objectify ofy = ObjectifyService.begin();
		Query<Attendee> q = ofy.query(Attendee.class).filter("eventId", eventId)
				.filter("status", status);
		List<Attendee> sorted = q.list();
		Collections.sort(sorted, new AttendeeComparator());
		return sorted;
	}

	class AttendeeComparator implements Comparator<Attendee> {
		@Override
		public int compare(Attendee o1, Attendee o2) {
			return (o1.getName().compareToIgnoreCase(o2.getName()));
		}
	}

	private String getInvitationEmailBody(Attendee attendee, Event event) {
		String result = "Dear " + attendee.getName() + ":\n\n";
		result += "The event organizer of Event " + event.getName()
				+ " invites you to attendee the event:\n";
		result += "Event Name: " + event.getName() + "\n";
		result += "Event Start Date: " + event.getStartTime() + "\n";
		result += "Event End Date: " + event.getEndTime()  + "\n\n";
		result += "you may use this regustration code " + attendee.getAttendeeId()
				+ " to RSVP this invitation through purduebep.appspot.com.\n";
	//	result += "If you have any question please contact the event organizer: "
	//			+ organizer.getEmail() + "\n";
		result += "Thank you\n\n";
		result += "Team XYZs\n\nPurdue Budget Event Planner\n";
		return result;
	}

	private String getRemoveEmailBody(Attendee attendee, Event event) {
		String result = "Dear " + attendee.getName() + ":\n\n";
		result += "The event organizer of Event " + event.getName()
				+ " had removed you from the event:\n";
		result += "Event Name: " + event.getName() + "\n";
		result += "Event Start Date: " + event.getStartTime() + "\n";
		result += "Event End Date: " + event.getEndTime() + "\n\n";
		//result += "If you have any question please contact the event organizer: "
	//			+ organizer.getEmail() + "\n\n";
		result += "Thank you\n\n";
		result += "Team XYZs\n\nPurdue Budget Event Planner\n";
		return result;
	}

	private String getConfirmationEmailBody(Attendee attendee, Event event) {
		String result = "Dear " + attendee.getName() + ":\n\n";
		result += "You just updated your information for the event:\n";
		result += "Event Name: " + event.getName() + "\n";
		result += "Event Start Date: " +event.getStartTime() + "\n";
		result += "Event End Date: " + event.getEndTime()  + "\n\n";
		result += "This is Your information:\n";
		result += attendee.toString() + "\n";
		result += "you can still use the same regustration code " + attendee.getAttendeeId()
				+ " to renew your information through purduebep.appspot.com.\n\n";
	//	result += "If you have any question please contact the event organizer: "
		//		+ organizer.getEmail() + "\n\n";
		result += "Thank you\n\n";
		result += "Team XYZs\n\nPurdue Budget Event Planner\n";
		return result;
	}
}