package budgeteventplanner.server;

import java.io.UnsupportedEncodingException;
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

	static {
		try {
			ObjectifyService.register(Event.class);
			ObjectifyService.register(Attendee.class);
		} catch (Exception e) {
		}
	}

	@Override
	public Attendee createAttendee(String eventId, String name, String email) {
		Objectify ofy = ObjectifyService.begin();
		Attendee attendee = new Attendee.Builder(eventId, name, email).build();
		ofy.put(attendee);
		sendEmail(attendee, this.EMAIL_INVITE);
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
			List<Attendee> listAttendee = getAttendeeListByEventId(event.getEventId());
			multimap.putAll(event.getName(), listAttendee);
		}

		return multimap;
	}

	@Override
	public void updateAttendeeInfo(String attendeeId, String name, String email, String jobTitle,
			String companyName, String address, String phoneNumber, Integer status) {
		Objectify ofy = ObjectifyService.begin();

		Attendee attendee = ofy.get(new Key<Attendee>(Attendee.class, attendeeId));
		Attendee updatedAttendee = new Attendee.Builder(attendee).setName(name).setEmail(email)
				.setJobTitle(jobTitle).setCompanyName(companyName).setAddress(address)
				.setPhoneNumber(phoneNumber).setStatus(status).build();
		ofy.put(updatedAttendee);
		sendEmail(updatedAttendee, status);
	}

	@Override
	public Integer attendeeLogin(String registrationCode) {
		return 1;
	}

	private void sendEmail(Attendee attendee, Integer status) {
		Session session = Session.getDefaultInstance(new Properties(), null);
		String msgBody = "Dear " + attendee.getName() + ":\n\nYour submission is accepted!"
				+ attendee.toString() + "\n\n\n Team XYZs";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("yezhuen@gmail.com", "XYZs"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(attendee.getEmail(),
					attendee.getName()));
			msg.setSubject("Your submission on Budget Event Planner");
			msg.setText(msgBody);
			Transport.send(msg);
		} catch (AddressException e) {
		} catch (MessagingException e) {
		} catch (UnsupportedEncodingException e) {

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

			if (status.equals(EMAIL_REMOVE)) // -1 means deleted by organizer
			{
				String subject = "You Have been Removed From Event";
				String msgBody = "You Have been Removed From Event.";
				sendCustomizedEmail(attendee, subject, msgBody);
			}
			if (status.equals(EMAIL_INVITE)) // 1 means send inviting letter
			{
				String subject = "You are invited to a new Event";
				String msgBody = "Your Registration code is:" + attendee.getAttendeeId()
						+ "\n Please go to purduebep.appspot.com for registration";
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
}