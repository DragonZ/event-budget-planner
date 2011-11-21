/*
 * Needs to clean the root h panel ... ... clean everthing for next time use.
 */

package budgeteventplanner.client;

import java.util.ArrayList;

import budgeteventplanner.client.entity.Attendee;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

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
	private final static AttendeeServiceAsync attendeeService = GWT
			.create(AttendeeService.class);
	private final static EventServiceAsync eventService = GWT
	.create(EventService.class);
	final static TextArea infoBox = new TextArea();
	static ArrayList<Label> attendees= new ArrayList<Label>();// = new Label[5];
	static ArrayList<CheckBox> checkableAttendees= new ArrayList<CheckBox>();
	static HorizontalPanel hPanel = new HorizontalPanel();
	static ArrayList<String> AttendeeIDs=new ArrayList<String>();
	static TreeItem root = new TreeItem("Attendees");
	static Button submit=new Button("submit");
	//static TreeItem rootCheckable =new TreeItem(new CheckBox("Attendees"));
//	static VerticalPanel dialogVPanel = new VerticalPanel();
	//final static DialogBox wusuowei = new DialogBox();
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		
//		dialogVPanel.addStyleName("dialogVPanel");
//		dialogVPanel.add(new HTML("<b>Submit Successful!</b>"));
//	
//		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
//		wusuowei.setWidget(dialogVPanel);
	
		
		
		hPanel.setSpacing(50);
		RootPanel.get("attendees").add(hPanel);
		hPanel.setSize("100%", "100%");
		infoBox.setSize("500px", "500px");
		infoBox.setReadOnly(true);
		infoBox.setText("info of attendees");
		//Cookies.setCookie("Event_id", "aw3iryfwerioghoawiguaweigu");
		//Cookies.getCookie("Event_id");
		//edittingAttendees("aaa","vvv");
		// showAttendees("ddd");
		// TreeItem hTree =new TreeItem();
		

	}

//	class MyHandler implements ClickHandler {
//
//		public void onClick(ClickEvent event) {
//			infoBox.setText(((MyLabel) event.getSource()).getAttendeeInfo());
//			/*
//			 * greetingService.getAttendeeInfo(((Label)
//			 * event.getSource()).getText(), new AsyncCallback<String>(){ public
//			 * void onFailure(Throwable caught) { }
//			 * 
//			 * public void onSuccess(String result) {
//			 * 
//			 * 
//			 * infoBox.setText(result); }
//			 * 
//			 * }
//			 * 
//			 * );
//			 */
//		}
//	}

//	class MyLabel extends Label {
//		public MyLabel(String m) {
//			super(m);
//		}
//		
//		String attendeeInfo = null;
//
//		void setAttendeeInfo(String info) {
//			attendeeInfo = info;
//		}
//
//		String getAttendeeInfo() {
//			return attendeeInfo;
//		}
//	}
//used by creating or editing event
	public static void edittingAttendees(final String eventID, String OrganizerID){
		attendeeService.getAttendeeListByOrganizerId(OrganizerID, new AsyncCallback<ArrayList<Attendee>>(){
			public void onFailure(Throwable caught){}
			public	void onSuccess(ArrayList<Attendee> attendeeList){
				//infoBox.setText("number:"+attendeeList.size());
				for (int i = 0; i < attendeeList.size(); i++) {
			    	//System.out.println("Name:"+attendeeList.get(i).getName());
			    	checkableAttendees.add(new CheckBox(attendeeList.get(i).getName()));
				final String info=attendeeList.get(i).toString();
				final String id=attendeeList.get(i).getAttendeeId();
				root.addItem(checkableAttendees.get(i));
				
				//checkableAttendees.get(1).setChecked(false);
				checkableAttendees.get(i).addClickHandler(new ClickHandler(){
					@SuppressWarnings("deprecation")
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						infoBox.setText(info);
						if(((CheckBox) event.getSource()).isChecked())
						AttendeeIDs.add(id);
						else
							AttendeeIDs.remove(id);
					}
				} );
			}
			} 
		});
		submit.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				eventService.fillAttendeesInEvent(eventID, AttendeeIDs, new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						RootPanel.get("XuXuan").setVisible(false);
						RootPanel.get("XiaYuan").setVisible(true);
					}
					
				});
			}
			
		});
		Tree hTree = new Tree();
		hTree.addItem(root);
		hPanel.add(hTree);
		hPanel.add(infoBox);
		hPanel.add(submit);
	}
// Used by only inspect the attendees. 
	public static void showAttendees(String eventID) {
		
			attendeeService.getAttendeeListByEventId(eventID, new AsyncCallback<ArrayList<Attendee>>(){
				public void onFailure(Throwable caught){}
				public	void onSuccess(ArrayList<Attendee> attendeeList){

			
				    for (int i = 0; i < attendeeList.size(); i++) {

				    	System.out.println("Name:"+attendeeList.get(i).getName());
					attendees.add(new Label(attendeeList.get(i).getName()));
					final String info=attendeeList.get(i).toString();
					root.addItem(attendees.get(i));
					attendees.get(i).addClickHandler(new ClickHandler(){

						
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							infoBox.setText(info);
						}
					} );
				}
				} 
			});

		Tree hTree = new Tree();
		hTree.addItem(root);

		hPanel.add(hTree);
		hPanel.add(infoBox);
		
	}

}
