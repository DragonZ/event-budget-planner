/*
 * Xuan Xu created public Integer sendEmail(String name, String email);
 * Xuan Xu created public String getAttendeeInfo(String AttendeeName);
 */
package budgeteventplanner.server;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import budgeteventplanner.client.GreetingService;
import budgeteventplanner.shared.FieldVerifier;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
			"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
		+ ".<br><br>It looks like you are using:<br>" + userAgent + "Xuan";
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
		.replaceAll(">", "&gt;");
	}

	@Override
	public Integer sendEmail(String name, String email)
	throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "Dear "+name+":\n\nYour submittion is accepted!\n\nName: "+name+"\nEmail: "+email+"\nplease verify!\n\nbest.\nTeam XYZs";

		try {Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("yezhuen@gmail.com", "XYZs"));
		msg.addRecipient(Message.RecipientType.TO,
				new InternetAddress(email, name));	
		msg.setSubject("Your submittion is accepted!");
		msg.setText(msgBody);
		Transport.send(msg);

		} catch (AddressException e) {
			// ...
		} catch (MessagingException e) {
			// ...
		} catch (UnsupportedEncodingException e){}
		return 1;
	}
	
	@Override
	public String getAttendeeInfo(String AttendeeName)
	throws IllegalArgumentException {
		return "Infomation for "+AttendeeName;
	}
}
