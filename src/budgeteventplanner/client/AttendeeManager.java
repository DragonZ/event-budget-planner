/*
 * 1 needs to find the attendeetList from server though event name
 */

package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import budgeteventplanner.client.entity.Attendee;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.ibm.icu.util.StringTokenizer;

public class AttendeeManager implements EntryPoint {

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	@SuppressWarnings("unused")
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	@SuppressWarnings("unused")
	private final static AttendeeServiceAsync attendeeService = GWT
			.create(GreetingService.class);

	final static TextArea infoBox = new TextArea();
	static MyLabel[] attendees;// = new Label[5];
	static HorizontalPanel hPanel = new HorizontalPanel();
	static TreeItem root = new TreeItem("Attendees");
	List<Attendee> attendeeList;
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub

		hPanel.setSpacing(50);
		RootPanel.get("attendees").add(hPanel);
		hPanel.setSize("100%", "100%");
		infoBox.setSize("500px", "500px");
		infoBox.setReadOnly(true);
		infoBox.setText("here");
		showAttendees("ddd");
		// TreeItem hTree =new TreeItem();

	}

	class MyHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			infoBox.setText(((MyLabel) event.getSource()).getAttendeeInfo());
			/*
			 * greetingService.getAttendeeInfo(((Label)
			 * event.getSource()).getText(), new AsyncCallback<String>(){ public
			 * void onFailure(Throwable caught) { }
			 * 
			 * public void onSuccess(String result) {
			 * 
			 * 
			 * infoBox.setText(result); }
			 * 
			 * }
			 * 
			 * );
			 */
		}
	}

	class MyLabel extends Label {
		public MyLabel(String m) {
			super(m);
		}
		
		String attendeeInfo = null;

		void setAttendeeInfo(String info) {
			attendeeInfo = info;
		}

		String getAttendeeInfo() {
			return attendeeInfo;
		}
	}

	public static void showAttendees(String eventID) {
		
			attendeeService.getAttendeeList(eventID, new AsyncCallback<ArrayList<Attendee>>(){
				public void onFailure(Throwable caught){}
				public	void onSuccess(ArrayList<Attendee> attendeeList){
				attendees = new MyLabel[attendeeList.size()];

				for (int i = 0; i < attendeeList.size(); i++) {
					attendees[i] = new MyLabel(attendeeList.get(i).getName());
					attendees[i].setAttendeeInfo(attendeeList.get(i).toString());
					root.addItem(attendees[i]);
					attendees[i].addClickHandler(new MyHandler());
				}
				} 
			});


		Tree hTree = new Tree();
		hTree.addItem(root);

		hPanel.add(hTree);
		hPanel.add(infoBox);
	}

}
