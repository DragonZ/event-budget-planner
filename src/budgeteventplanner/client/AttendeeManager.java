/*
 * Needs to clean the eventAttendeeTreeItem h panel ... ... clean everthing for next time use.
 *  add new attendee manully. and send info back
 *  remove attendee and sent info back
 *   1, need organizerAttendeeList and eventAttendeeList, readyToRemoveIdList, readyToCreateList, readyToSendNotifyList
 *   2, while loading, needs to load 2tree. orga and event attendeeList
 *   3,button addToCurrentEvent: add currentorganizerAttendee to eventattendeeList 
 *   	 show out in the tree
 *   	add to readyToCreateListe
 *   4, button rmove from CurrentEvent: rm from the current Tree
 *   									rm from eventAttendeeList
 *   									if already exist in readyToCreateList then rm in that list
 *  									else add in readyToRemoveList
 *  5 create new attendee manuly for this event,
 *  									1, create attendee 
 *  									2, get attendee's info and insert in organizerAttendeeList eventAttendeeList
 *  									3, show out in both Tree
 *  									4, insert in readyToSendNotifyList
 *  6,apply change: 1,rm Removelist,and send email.
 *  				2, create attendee and send email
 *  				3,send email to readyToSendNotify
 *  				4, jump back.
 *  7, organizer send email: 1, create box
 *  						 2, send handler, send email to checked attendee in eventlist
 *   									
 *  manuly add attendee and send email to notify.
 *  after add not manul still need to send notify.
 *  delete the attendee and send email to notify.
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AttendeeManager implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final static AttendeeServiceAsync attendeeService = GWT
			.create(AttendeeService.class);
	private final static EventServiceAsync eventService = GWT
	.create(EventService.class);
	final static TextArea infoBox = new TextArea();
	final static TextArea emailContentBox = new TextArea();
	static Button sendEmail=new Button("Send");//To attendees in This Event
	//eventAttendeeList and organizerattendeeList will only store attendee's name
	static ArrayList<CheckBox> eventAttendeeList= new ArrayList<CheckBox>();// = new Label[5];
	static ArrayList<CheckBox> organizerAttendeeList= new ArrayList<CheckBox>();
	static ArrayList<String> eventAttendeeEmailList =new ArrayList<String>();
	//the attendeeId that is going to be removed from current event
	static ArrayList<String> readyToRemoveIdList=new ArrayList<String>();
	static ArrayList<TreeItem> readyToRemoveCheckBoxList=new ArrayList<TreeItem>();
	//static ArrayList<String> readyToMoveList_info=new ArrayList<String>();
	//store the attendeeId that going to recreate and store in the event
	
	static ArrayList< ArrayList<String>> readyToCreateList=new ArrayList< ArrayList<String>>();
	static ArrayList<ArrayList<String>> readyToMoveToEventList= new ArrayList< ArrayList<String>>();
	//static ArrayList<String> readyToCreateList_info=new ArrayList<String>();
	//the AttendeeId of attendees that going to be sent email
	static ArrayList<String> readyToSendNotifyList=new ArrayList<String>();
	static HorizontalPanel hPanel = new HorizontalPanel();
	static VerticalPanel vPanel=new VerticalPanel();
	static ArrayList<String> AttendeeIDs=new ArrayList<String>();
	static TreeItem eventAttendeeTreeItem = new TreeItem("Attendees in This Event");
	static TreeItem organizerAttendeeTreeItem = new TreeItem("Attendees You had used before");
	static Button submit=new Button("Apply all changes");
	static Button addToEvent=new Button("Add");//checked attendees to current event
	static Button removeFromEvent=new Button("Remove");// checked attendees from current event
	static Tree eventAttendeeTree = new Tree();
	static Tree organizerAttendeeTree=new Tree();
	 Label nameLabel = new Label("*Name:");
	 static TextBox nameBox = new TextBox();
	 Label emailLable = new Label("*Email:");
	 static TextBox emailBox = new TextBox();
	static Button inputAttendee=new Button("Create Attendee");

	//static TreeItem rootCheckable =new TreeItem(new CheckBox("Attendees"));
//	static VerticalPanel dialogVPanel = new VerticalPanel();
	//final static DialogBox wusuowei = new DialogBox();
	@Override
	public void onModuleLoad() {
		
//		dialogVPanel.addStyleName("dialogVPanel");
//		dialogVPanel.add(new HTML("<b>Submit Successful!</b>"));
//	
//		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
//		wusuowei.setWidget(dialogVPanel);
	
		
		 VerticalPanel vPanelm=new VerticalPanel();
		hPanel.setSpacing(50);
		RootPanel.get("attendees").add(vPanelm);
		hPanel.setSize("100%", "100%");
		infoBox.setSize("200px", "300px");
		infoBox.setReadOnly(true);
		infoBox.setText("info of eventAttendeeList will appear here");
		eventAttendeeTree.addItem(eventAttendeeTreeItem);
		organizerAttendeeTree.addItem(organizerAttendeeTreeItem);
		 VerticalPanel vPanelo=new VerticalPanel();
		 vPanelo.add(organizerAttendeeTree);
		 vPanelo.add(addToEvent);
		hPanel.add(vPanelo);
		
		hPanel.add(infoBox);
		 VerticalPanel vPanele=new VerticalPanel();
		 vPanele.add(eventAttendeeTree);
		 vPanele.add(removeFromEvent);
		//hPanel.add(eventAttendeeTree);
		 hPanel.add(vPanele);

		 vPanelm.add(hPanel);

		 HorizontalPanel hPanell = new HorizontalPanel();
		 vPanelm.add(hPanell);
		 
		 VerticalPanel vPanelEmail=new VerticalPanel();
		 hPanell.add(vPanelEmail);
		 vPanelEmail.add(emailContentBox);
		 vPanelEmail.add(sendEmail);
		 
		 VerticalPanel vPanelInputAttendee=new VerticalPanel();
		 hPanell.add(vPanelInputAttendee);
		 vPanelInputAttendee.add(nameLabel);
		 vPanelInputAttendee.add(nameBox);
		 vPanelInputAttendee.add(emailLable);
		 vPanelInputAttendee.add(emailBox);
		 vPanelInputAttendee.add(inputAttendee);
		 
		 hPanell.add(submit);
		//vPanel.add(addToEvent);
		//vPanel.add(removeFromEvent);
		
//		vPanel.add(nameLabel);
//		vPanel.add(nameBox);
//		vPanel.add(emailLable);
//		vPanel.add(emailBox);
//		vPanel.add(inputAttendee);
		//vPanel.add(emailContentBox);
		//vPanel.add(sendEmail);
		//hPanel.add(vPanel);
		//Cookies.setCookie("Event_id", "aw3iryfwerioghoawiguaweigu");
		//Cookies.getCookie("Event_id");
		sendEmail.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				for(String attendeeId:readyToRemoveIdList)
				attendeeService.sendCustomizedEmail(attendeeId, "An Letter About your Event", emailContentBox.getText(), new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						
					}});
			}});
		edittingAttendees("A811938E-93C5-4373-8478-C8FC28E5C509","YuanXia");
		// showAttendees("ddd");
		// TreeItem eventAttendeeTree =new TreeItem();
		
		

	}

//test For new UI
	private static String getEmailFromInfo(String info){
		int start=info.indexOf("Email: ");
		start=start+7;
		int end=info.indexOf("\nAddress:");
		return info.substring(start, end);
	} 
	public static void edittingAttendees(final String eventID, String organizerID){
		attendeeService.getAttendeeListByOrganizerId(organizerID, new AsyncCallback<ArrayList<Attendee>>(){
			public void onFailure(Throwable caught){
				infoBox.setText("failure"+caught);
			}
			public	void onSuccess(ArrayList<Attendee> attendeeList){
				
				//infoBox.setText("number:"+attendeeList.size());
				for (int i = 0; i < attendeeList.size(); i++) {
			    	//System.out.println("Name:"+attendeeList.get(i).getName());
			    	
				 String info=attendeeList.get(i).toString();
				 String id=attendeeList.get(i).getAttendeeId();
				 String name=attendeeList.get(i).getName();
				final ArrayList<String> attendeeInfo =new ArrayList<String> ();
				 attendeeInfo.add(id);
				 attendeeInfo.add(name);
				 attendeeInfo.add(info);
				organizerAttendeeList.add(new CheckBox(name));
				organizerAttendeeTreeItem.addItem(organizerAttendeeList.get(i));
				
				//organizerAttendeeList.get(1).setChecked(false);
				organizerAttendeeList.get(i).addClickHandler(new ClickHandler(){
					@SuppressWarnings("deprecation")
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						infoBox.setText(attendeeInfo.get(2));
						if(((CheckBox) event.getSource()).isChecked())
						{
							
						//	readyToCreateList.add(attendeeInfo);
							readyToMoveToEventList.add(attendeeInfo);
							//readyToCreateList_info.add(info);
							//infoBox.setText(""+readyToCreateList.toString());
						}
						else
						{
							//readyToCreateList.remove(attendeeInfo);
							readyToMoveToEventList.remove(attendeeInfo);
					
							//infoBox.setText(""+readyToCreateList.toString());
						}
					}
				} );
			}
			} 
		});
		
		attendeeService.getAttendeeListByEventId(eventID, new AsyncCallback<ArrayList<Attendee>>(){
			public void onFailure(Throwable caught){}
			public	void onSuccess(ArrayList<Attendee> attendeeList){
				
				//infoBox.setText("number:"+attendeeList.size());
				for (int i = 0; i < attendeeList.size(); i++) {
			    	//System.out.println("Name:"+attendeeList.get(i).getName());
					eventAttendeeEmailList.add(attendeeList.get(i).getEmail());
			    	eventAttendeeList.add(new CheckBox(attendeeList.get(i).getName()));
				final String info=attendeeList.get(i).toString();
				final String id=attendeeList.get(i).getAttendeeId();
				final TreeItem ti=eventAttendeeTreeItem.addItem(eventAttendeeList.get(i));
				
				//organizerAttendeeList.get(1).setChecked(false);
				eventAttendeeList.get(i).addClickHandler(new ClickHandler(){
					@SuppressWarnings("deprecation")
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						infoBox.setText(info);
						if(((CheckBox) event.getSource()).isChecked())
						{
							//eventAttendeeList.contains((CheckBox) event.getSource());
							readyToRemoveIdList.add(id);
							readyToRemoveCheckBoxList.add(ti);
							//infoBox.setText(""+readyToRemoveIdList.toString());
						}
						else
						{
							readyToRemoveIdList.remove(id);
							readyToRemoveCheckBoxList.remove(ti);
						//	infoBox.setText(""+readyToRemoveIdList.toString());
						}
					}
				} );
			}
			} 
		});
		// TODO Auto-generated method stub
		// addToCurrentEvent: add currentorganizerAttendee to eventattendeeList 
		//   	 show out in the tree
		//  	add to readyToCreateListe
		
		removeFromEvent.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				/*button rmove from CurrentEvent: rm from the current Tree
													rm from eventAttendeeList
													if already exist in readyToCreateList then rm in that list
												else add in readyToRemoveList*/
				for(TreeItem ti:readyToRemoveCheckBoxList)
				eventAttendeeTreeItem.removeItem(ti);
				for(String id:readyToRemoveIdList){
					for(ArrayList<String> attInfo:readyToCreateList)
					{
						if(id.compareTo(attInfo.get(0))==0)
							{
							readyToCreateList.remove(attInfo);
							readyToRemoveIdList.remove(id);
							break;
							}
					}
				}
				
			}});
		addToEvent.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
		for(final ArrayList<String> attendeeInfo:readyToMoveToEventList)
		{
			boolean ifExist=false;
			for(String ai : eventAttendeeEmailList)
			{
				if(attendeeInfo.get(2).contains(ai))
					{
					ifExist=true;
					break;
					}
			}
			if(ifExist) continue;
			//final ArrayList<String> attendeeInfo_cpy=(ArrayList<String>) attendeeInfo.clone();
			CheckBox cbox=new CheckBox(attendeeInfo.get(1));
			
			eventAttendeeEmailList.add(getEmailFromInfo(attendeeInfo.get(2)));
			eventAttendeeList.add(cbox);
			final TreeItem ti=eventAttendeeTreeItem.addItem(cbox);
			cbox.addClickHandler(new ClickHandler(){

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					infoBox.setText(getEmailFromInfo(attendeeInfo.get(2)));
					//infoBox.setText(attendeeInfo.get(2));
					if(((CheckBox) event.getSource()).isChecked())
					{
						
						readyToRemoveIdList.add(attendeeInfo.get(0));
						readyToRemoveCheckBoxList.add(ti);
					//	infoBox.setText(""+readyToRemoveIdList.toString());
					}
					else
					{
						readyToRemoveIdList.remove(attendeeInfo.get(0));
						readyToRemoveCheckBoxList.remove(ti);
						//infoBox.setText(""+readyToRemoveIdList.toString());
					}
				}});
			
			
		}
		readyToCreateList.addAll(readyToMoveToEventList);
		readyToMoveToEventList.clear();
			}
			
		});
		inputAttendee.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				attendeeService.createAttendee(eventID, nameBox.getText(), emailBox.getText(),new AsyncCallback<Attendee>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess( final Attendee result) {
						eventAttendeeEmailList.add(emailBox.getText());
						// TODO Auto-generated method stub
						//final ArrayList<String> attendeeInfo_cpy=(ArrayList<String>) attendeeInfo.clone();
						readyToSendNotifyList.add(result.getAttendeeId());
						CheckBox cbox=new CheckBox(nameBox.getText());
						eventAttendeeList.add(cbox);
						final TreeItem ti=eventAttendeeTreeItem.addItem(cbox);
						cbox.addClickHandler(new ClickHandler(){

							@SuppressWarnings("deprecation")
							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								infoBox.setText("attendee's info needed");
								if(((CheckBox) event.getSource()).isChecked())
								{
									
									readyToRemoveIdList.add(result.getAttendeeId());
									readyToRemoveCheckBoxList.add(ti);
								//	infoBox.setText(""+readyToRemoveIdList.toString());
								}
								else
								{
									readyToRemoveIdList.remove(result.getAttendeeId());
									readyToRemoveCheckBoxList.remove(ti);
									//infoBox.setText(""+readyToRemoveIdList.toString());
								}
							}});
						
						 CheckBox cboxOrganizer = new CheckBox(nameBox.getText());
						 final ArrayList<String> attendeeInfo =new ArrayList<String> ();
						 attendeeInfo.add(result.getName());
						 attendeeInfo.add(nameBox.getText());
						 attendeeInfo.add(result.toString());
						organizerAttendeeList.add(cboxOrganizer);
						organizerAttendeeTreeItem.addItem(cboxOrganizer);
						cboxOrganizer.addClickHandler(new ClickHandler(){

							@Override
							public void onClick(ClickEvent event) {
								
								infoBox.setText(result.toString());
								if(((CheckBox) event.getSource()).isChecked())
								{
									
									readyToCreateList.add(attendeeInfo);
									//readyToCreateList_info.add(info);
									//infoBox.setText(""+readyToCreateList.toString());
								}
								else
								{
									readyToCreateList.remove(attendeeInfo);
							
									//infoBox.setText(""+readyToCreateList.toString());
								}
							}});
						 
						
					
					}});
				
			}});
		submit.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				ArrayList<String> attendeeList=new ArrayList<String>();
				for(ArrayList<String> al : readyToCreateList)
				{
					attendeeList.add(al.get(0));
				}
				eventService.fillAttendeesInEvent(eventID, attendeeList, new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
//						infoBox.setText("info of eventAttendeeList will appear here");
//						AttendeeIDs.clear();
//						eventAttendeeTreeItem.removeItems();
//						organizerAttendeeList.clear();
//						RootPanel.get("XuXuan").setVisible(false);
//						RootPanel.get("XiaYuan").setVisible(true);
					
					}
					
				});
				attendeeService.sendEmailBatchByOrganizer(attendeeList,1,new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						
					}});
				//remove readyToRemoveIdList
				attendeeService.deleteAttendeeByAttendeeIdList(readyToRemoveIdList,new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						
					}});
				attendeeService.sendEmailBatchByOrganizer(readyToRemoveIdList,-1,new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						
					}});
				//Start Send email to notify attendees.
				attendeeService.sendEmailBatchByOrganizer(readyToSendNotifyList,1,new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						
					}});
			}
			
		});
	
	
	}
//used by creating or editing event
	public static void edittingAttendees_past(final String eventID, String OrganizerID){
		attendeeService.getAttendeeListByOrganizerId(OrganizerID, new AsyncCallback<ArrayList<Attendee>>(){
			public void onFailure(Throwable caught){}
			public	void onSuccess(ArrayList<Attendee> attendeeList){
				
				//infoBox.setText("number:"+attendeeList.size());
				for (int i = 0; i < attendeeList.size(); i++) {
			    	//System.out.println("Name:"+attendeeList.get(i).getName());
			    	organizerAttendeeList.add(new CheckBox(attendeeList.get(i).getName()));
				final String info=attendeeList.get(i).toString();
				final String id=attendeeList.get(i).getAttendeeId();
				eventAttendeeTreeItem.addItem(organizerAttendeeList.get(i));
				
				//organizerAttendeeList.get(1).setChecked(false);
				organizerAttendeeList.get(i).addClickHandler(new ClickHandler(){
					@SuppressWarnings("deprecation")
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						infoBox.setText(info);
						if(((CheckBox) event.getSource()).isChecked())
						{
							AttendeeIDs.add(id);
							//nfoBox.setText(""+AttendeeIDs.toString());
						}
						else
						{
							AttendeeIDs.remove(id);
							//infoBox.setText(""+AttendeeIDs.toString());
						}
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
						infoBox.setText("info of eventAttendeeList will appear here");
						AttendeeIDs.clear();
						eventAttendeeTreeItem.removeItems();
						organizerAttendeeList.clear();
						RootPanel.get("XuXuan").setVisible(false);
						RootPanel.get("XiaYuan").setVisible(true);
					
					}
					
				});
			}
			
		});
	
	
	}
// Used by only inspect the eventAttendeeList. 
	public static void showAttendees(String eventID) {
		
			attendeeService.getAttendeeListByEventId(eventID, new AsyncCallback<ArrayList<Attendee>>(){
				public void onFailure(Throwable caught){}
				public	void onSuccess(ArrayList<Attendee> attendeeList){

			
				    for (int i = 0; i < attendeeList.size(); i++) {

				    	System.out.println("Name:"+attendeeList.get(i).getName());
					eventAttendeeList.add(new CheckBox(attendeeList.get(i).getName()));
					final String info=attendeeList.get(i).toString();
					eventAttendeeTreeItem.addItem(eventAttendeeList.get(i));
					eventAttendeeList.get(i).addClickHandler(new ClickHandler(){

						
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							infoBox.setText(info);
						}
					} );
				}
				} 
			});

		Tree eventAttendeeTree = new Tree();
		eventAttendeeTree.addItem(eventAttendeeTreeItem);

		hPanel.add(eventAttendeeTree);
		hPanel.add(infoBox);
		
	}

}
