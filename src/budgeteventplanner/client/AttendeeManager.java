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
 *  
 *  
 *  
 *  Delete the attendee from organizer after deleting from event// 
 *  need to a method get eventIdList(String organizerid)
 *  from server how to find the eventinfo from attendeeID
 *  for those attendee don't wannt to attend, just show in red.
 *  
 *  
 *  after click set the load unclickible. can cancel whole thing and go back,
 */

package budgeteventplanner.client;

import java.util.ArrayList;
import java.util.List;

import budgeteventplanner.client.entity.Attendee;
import budgeteventplanner.client.entity.Event;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
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
	//private final static EventServiceAsync eventService = GWT
		//	.create(AttendeeService.class);
	private  static AttendeeServiceAsync attendeeService = GWT
			.create(AttendeeService.class);
	private  static EventServiceAsync eventService = GWT
	.create(EventService.class);

	/*
	 * static ArrayList<String> eventAttendeeEmailList =new ArrayList<String>();
	 * static ArrayList<CheckBox> eventAttendeeList= new
	 * ArrayList<CheckBox>();// = new Label[5]; static ArrayList<String>
	 * readyToRemoveIdList=new ArrayList<String>(); static ArrayList<TreeItem>
	 * readyToRemoveCheckBoxList=new ArrayList<TreeItem>(); static
	 * ArrayList<String> readyToSendNotifyList=new ArrayList<String>();
	 * 
	 * static ArrayList<CheckBox> organizerAttendeeList= new
	 * ArrayList<CheckBox>(); static ArrayList< ArrayList<String>>
	 * readyToCreateList=new ArrayList< ArrayList<String>>(); static
	 * ArrayList<ArrayList<String>> readyToMoveToEventList= new ArrayList<
	 * ArrayList<String>>();
	 */
	// /////////////////////////////////////////////
	static ArrayList<ArrayList<String>> leventAttendeeInfoList = new ArrayList<ArrayList<String>>();// the
																									// email,
																									// info,
																									// name
																									// in
																									// the
																									// list
	static ArrayList<CheckBox> leventAttendeeCheckBoxList = new ArrayList<CheckBox>();
	static ArrayList<TreeItem> leventCheckedTreeItemList = new ArrayList<TreeItem>();
	static ArrayList<ArrayList<String>> leventCheckedAttendeeInfoList = new ArrayList<ArrayList<String>>();// the
																											// email,
																											// info,
																											// name
																											// of
																											// checked
																										// box
	static ArrayList<String> leventReadyToRemoveAttendeeIdList = new ArrayList<String>();
	static ArrayList<String> readyToSendNotifyList = new ArrayList<String>();
//arrayList for organizer
	static ArrayList<ArrayList<String>> lorganizerOwnedEventInfoList = new ArrayList<ArrayList<String>>();//the id and name of event.
	static ArrayList<CheckBox> lorganizerAttendeeCheckBoxList = new ArrayList<CheckBox>();
	static ArrayList<String> lorganizerReadyToCreateList = new ArrayList<String>();
	static ArrayList<ArrayList<String>> lorganizerCheckedAttendeeInfoList = new ArrayList<ArrayList<String>>();
	static ArrayList<ArrayList<String>> lorganizerAllAttendeeInfoList = new ArrayList<ArrayList<String>>();//id name info eventId

	// //////////////////////////////////////////////

	final static TextArea infoBox = new TextArea();
	final static TextArea emailContentBox = new TextArea();
	static Button sendEmail = new Button("Send");// To attendees in This Event
	static Button sendEmailPop = new Button("Send Email");
	static HorizontalPanel hPanel = new HorizontalPanel();
	static VerticalPanel vPanel = new VerticalPanel();
	static ArrayList<String> AttendeeIDs = new ArrayList<String>();
	static TreeItem eventAttendeeTreeItem ;//= new TreeItem(
			//"Attendees in This Event");
	static TreeItem organizerAttendeeTreeItem = new TreeItem(new HTML("<b>Attendees in other events</b>"));
	static Button submit = new Button("Apply All Changes");
	static Button addToEvent = new Button("Add");// checked attendees to current
													// event
	static Button removeFromEvent = new Button("Remove");// checked attendees
															// from current
															// event
	static Tree eventAttendeeTree = new Tree();
	static Tree organizerAttendeeTree = new Tree();
	Label nameLabel = new Label("*Name:");
	static TextBox nameBox = new TextBox();
	Label emailLabel = new Label("*Email:");
	static TextBox emailBox = new TextBox();
	static Button inputAttendee = new Button("Create Attendee");
	static Button inputAttendeePop = new Button("Create Attendee");
	DialogBox sendEmailBox = new DialogBox();
	static DialogBox inputAttendeeBox = new DialogBox();
	static Button loadOrganizerAttendee = new Button("Load");
	static Button loadEventAttendee = new Button("Load");
	static Button jmpbackToEvent = new Button("Back");
	// static TreeItem rootCheckable =new TreeItem(new CheckBox("Attendees"));
	// static VerticalPanel dialogVPanel = new VerticalPanel();
	// final static DialogBox wusuowei = new DialogBox();
	@Override
	public void onModuleLoad() {

		// dialogVPanel.addStyleName("dialogVPanel");
		// dialogVPanel.add(new HTML("<b>Submit Successful!</b>"));
		//
		// dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		// wusuowei.setWidget(dialogVPanel);

		VerticalPanel vPanelmain = new VerticalPanel();
		hPanel.setSpacing(50);
		RootPanel.get("attendees").add(vPanelmain);
		hPanel.setSize("100%", "100%");
		infoBox.setSize("100%", "300px");
		infoBox.setReadOnly(true);
		infoBox.setText("info of eventAttendeeList will appear here");
		//eventAttendeeTree.addItem(eventAttendeeTreeItem);
		organizerAttendeeTree.addItem(organizerAttendeeTreeItem);

		VerticalPanel vPanelorganizer = new VerticalPanel();
		HorizontalPanel loadAndAdd = new HorizontalPanel();
		loadAndAdd.add(loadOrganizerAttendee);
		loadAndAdd.add(addToEvent);
		addToEvent.setVisible(false);
		vPanelorganizer.add(loadAndAdd);
		vPanelorganizer.add(organizerAttendeeTree);

		hPanel.add(vPanelorganizer);

		VerticalPanel vPanelInfoBox = new VerticalPanel();
		HorizontalPanel hPanelInfoBox = new HorizontalPanel();
		vPanelInfoBox.add(infoBox);
		vPanelInfoBox.add(hPanelInfoBox);
		hPanel.add(vPanelInfoBox);
		hPanelInfoBox.add(sendEmailPop);
		sendEmailPop.setEnabled(false);
		hPanelInfoBox.add(inputAttendeePop);
		inputAttendeePop.setEnabled(false);
		hPanelInfoBox.add(submit);
		submit.setEnabled(false);
		hPanelInfoBox.add(jmpbackToEvent);

		VerticalPanel vPanelEvent = new VerticalPanel();
		HorizontalPanel loadAndAddEvent = new HorizontalPanel();
		loadAndAddEvent.add(loadEventAttendee);
		loadAndAddEvent.add(removeFromEvent);
		removeFromEvent.setVisible(false);
		vPanelEvent.add(loadAndAddEvent);
		vPanelEvent.add(eventAttendeeTree);
		// hPanel.add(eventAttendeeTree);
		hPanel.add(vPanelEvent);

		vPanelmain.add(hPanel);

		sendEmailBox.setText("Send Email");
		VerticalPanel sendEmailVPanel = new VerticalPanel();
		// sendEmailVPanel.setSize("100%", "100%");
		// sendEmailVPanel.setSpacing(5);
		emailContentBox.setSize("300px", "100px");
		sendEmailVPanel.add(emailContentBox);
		HorizontalPanel sendEmailHPanel = new HorizontalPanel();
		sendEmailHPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		sendEmailHPanel.add(sendEmail);
		Button cancelSendEmailButton = new Button("cancel");
		sendEmailHPanel.add(cancelSendEmailButton);
		sendEmailHPanel.setSpacing(3);
		sendEmailVPanel.add(sendEmailHPanel);
		sendEmailBox.setWidget(sendEmailVPanel);
		// vPanelmain.add(sendEmailVPanel);

		inputAttendeeBox.setText("InPut Attendee Manuly");
		VerticalPanel inputAttendeeVPanel = new VerticalPanel();
		nameLabel.setWidth("200px");
		nameBox.setWidth("200px");
		emailLabel.setWidth("200px");
		emailBox.setWidth("200px");
		inputAttendeeVPanel.add(nameLabel);
		inputAttendeeVPanel.add(nameBox);
		inputAttendeeVPanel.add(emailLabel);
		inputAttendeeVPanel.add(emailBox);
		HorizontalPanel inputAttendeeHPanel = new HorizontalPanel();
		inputAttendeeHPanel
				.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		inputAttendeeHPanel.add(inputAttendee);
		Button cancelInputAttendeeButton = new Button("cancel");
		inputAttendeeHPanel.add(cancelInputAttendeeButton);
		inputAttendeeVPanel.add(inputAttendeeHPanel);
		inputAttendeeHPanel.setSpacing(3);
		inputAttendeeBox.setWidget(inputAttendeeVPanel);

		cancelSendEmailButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				sendEmailBox.hide();
				sendEmail.setFocus(false);
			}
		});

		cancelInputAttendeeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				inputAttendeeBox.hide();
				inputAttendee.setFocus(false);
			}
		});

		sendEmailPop.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				emailContentBox.setText("");
				sendEmailBox.show();
				sendEmailBox.center();
				sendEmail.setFocus(true);
			}
		});
		inputAttendeePop.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				nameBox.setText("");
				emailBox.setText("");
				inputAttendeeBox.show();
				inputAttendeeBox.center();
				inputAttendee.setFocus(true);
			}
		});
		sendEmail.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

				for (ArrayList<String> attendeeInfo : leventCheckedAttendeeInfoList)
					attendeeService.sendCustomizedEmail(attendeeInfo.get(0),
							"An Letter About your Event",
							emailContentBox.getText(),
							new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									sendEmailBox.hide();
									sendEmail.setFocus(false);
									cleanEventTree();
								}

								@Override
								public void onSuccess(Void result) {
									// TODO Auto-generated method stub
									sendEmailBox.hide();
									sendEmail.setFocus(false);
									cleanEventTree();
								}
							});
			}
		});
		removeFromEvent.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for (TreeItem ti : leventCheckedTreeItemList)
					eventAttendeeTreeItem.removeItem(ti);

				for (ArrayList<String> aInfo : leventCheckedAttendeeInfoList) {
					removeAttendeeFromEventAttendeeInfoList(aInfo.get(0));
					boolean ifFound = false;
					for (String attInfo : lorganizerReadyToCreateList) {
						// rm from readyTocreate if found in organizer's
						if (aInfo.get(0).compareTo(attInfo) == 0) {
							lorganizerReadyToCreateList.remove(attInfo);
							ifFound = true;
							break;
						}
					}
					// if not found then add to ready to remove
					if (!ifFound)
						leventReadyToRemoveAttendeeIdList.add(aInfo.get(0));

				}
				cleanEventTree();

			}
		});

		addToEvent.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for (final ArrayList<String> attendeeInfo : lorganizerCheckedAttendeeInfoList) {
					// check if already exist in event
					boolean ifExist = false;
					for (ArrayList<String> ai : leventAttendeeInfoList) {
						if (attendeeInfo.get(2).contains(ai.get(2))) {
							ifExist = true;
							break;
						}
					}
					if (ifExist)
						continue;

					setAttendeeInEventTree(attendeeInfo.get(0),
							attendeeInfo.get(1), attendeeInfo.get(2));
					lorganizerReadyToCreateList.add(attendeeInfo.get(0));
					//infoBox.setText(lorganizerReadyToCreateList.toString());
				}

				clearOrganizerTree();
			}

		});
		jmpbackToEvent.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				clearEverythingBeforeJumpBack();
				RootPanel.get("XuXuan").setVisible(false);
				RootPanel.get("XiaYuan").setVisible(true);
			}});
	
		//edittingAttendees("A811938E-93C5-4373-8478-C8FC28E5C509","Build2 Demo", "YuanXia");
		// showAttendees("ddd");
		// TreeItem eventAttendeeTree =new TreeItem();

	}

	// test For new UI
	/*
	 * private static String getEmailFromInfo(String info){ int
	 * start=info.indexOf("Email: "); start=start+7; int
	 * end=info.indexOf("\nAddress:"); return info.substring(start, end); }
	 */

	static void clearOrganizerTree() {
		// clean tree and clear lorganizerCheckedAttendeeInfoList
		for (CheckBox cb : lorganizerAttendeeCheckBoxList) {
			cb.setValue(false);
		}
		lorganizerCheckedAttendeeInfoList.clear();
	}

	static void cleanEventTree() {
		// clean tree and clear leventCheckedTreeItemList and
		// leventCheckedAttendeeInfoList
		for (CheckBox cb : leventAttendeeCheckBoxList) {
			cb.setValue(false);
		}
		leventCheckedTreeItemList.clear();
		leventCheckedAttendeeInfoList.clear();
	}

	
	@SuppressWarnings("deprecation")
	static void clearSthForReusing(){
		leventCheckedTreeItemList.clear();
		leventCheckedAttendeeInfoList.clear();
		leventReadyToRemoveAttendeeIdList.clear();
		readyToSendNotifyList.clear();
		lorganizerReadyToCreateList.clear();
		lorganizerCheckedAttendeeInfoList.clear();
		for(CheckBox cb : leventAttendeeCheckBoxList){
			cb.setChecked(false);
		}
		for(CheckBox cb : lorganizerAttendeeCheckBoxList){
			cb.setChecked(false);
		}
	}
	static void clearEverythingBeforeJumpBack() {
		leventAttendeeInfoList.clear();
		leventCheckedTreeItemList.clear();
		leventCheckedAttendeeInfoList.clear();
		leventReadyToRemoveAttendeeIdList.clear();
		readyToSendNotifyList.clear();
		leventAttendeeCheckBoxList.clear();
		lorganizerAttendeeCheckBoxList.clear();
		lorganizerReadyToCreateList.clear();
		lorganizerCheckedAttendeeInfoList.clear();
		lorganizerAllAttendeeInfoList.clear();
		lorganizerOwnedEventInfoList.clear();
		eventAttendeeTreeItem.removeItems();
		organizerAttendeeTree.removeItems();
		organizerAttendeeTree.addItem(organizerAttendeeTreeItem);
		eventAttendeeTree.removeItems();
		//remove listener
		HorizontalPanel loadAndAdd=(HorizontalPanel) loadOrganizerAttendee.getParent();
		loadOrganizerAttendee.removeFromParent();
		loadOrganizerAttendee=new Button("Load");
		loadAndAdd.insert(loadOrganizerAttendee, 0);
		
		HorizontalPanel loadAndAddEvent=(HorizontalPanel) loadEventAttendee.getParent();
		loadEventAttendee.removeFromParent();
		loadEventAttendee=new Button("Load");
		loadAndAddEvent.insert(loadEventAttendee, 0);
		
		HorizontalPanel inputAttendeeHPanel =(HorizontalPanel) inputAttendee.getParent();
		inputAttendee.removeFromParent();
		inputAttendee=new Button("Create Attendee");
		inputAttendeeHPanel.insert(inputAttendee,0);
		
		HorizontalPanel hPanelInfoBox = (HorizontalPanel) submit.getParent();
		submit.removeFromParent();
		submit= new Button("Apply All Changes");
		//hPanelInfoBox.insert(submit,2);
		hPanelInfoBox.insert(submit,2);
		
		//set button 
		addToEvent.setVisible(false);
		loadOrganizerAttendee.setVisible(true);
		removeFromEvent.setVisible(false);
		loadEventAttendee.setVisible(true);
		sendEmailPop.setEnabled(false);
		inputAttendeePop.setEnabled(false);
		submit.setEnabled(false);
	};

	static void removeAttendeeFromEventAttendeeInfoList(String id) {
		for (ArrayList<String> aInfo : leventAttendeeInfoList) {
			if (aInfo.get(0).compareTo(id) == 0)
				leventAttendeeInfoList.remove(aInfo);
		}
	}

/*	static void setAttendeeInOrganizerTree(String id, String name, String info) {
		final ArrayList<String> attendeeInfo = new ArrayList<String>();
		attendeeInfo.add(id);
		attendeeInfo.add(name);
		attendeeInfo.add(info);
		CheckBox cb = new CheckBox(name);
		lorganizerAttendeeCheckBoxList.add(cb);
		organizerAttendeeTreeItem.addItem(cb);
		cb.addClickHandler(new ClickHandler() {
			@SuppressWarnings("deprecation")
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				infoBox.setText(attendeeInfo.get(2));
				if (((CheckBox) event.getSource()).isChecked()) {
					lorganizerCheckedAttendeeInfoList.add(attendeeInfo);
				} else {
					lorganizerCheckedAttendeeInfoList.remove(attendeeInfo);
				}
			}
		});
	}
*/
	static void setAttendeeInOrganizerTree(String id, String name, String info,TreeItem ti) {
		final ArrayList<String> attendeeInfo = new ArrayList<String>();
		attendeeInfo.add(id);
		attendeeInfo.add(name);
		attendeeInfo.add(info);
		CheckBox cb = new CheckBox(name);
		lorganizerAttendeeCheckBoxList.add(cb);
		ti.addItem(cb);
		cb.addClickHandler(new ClickHandler() {
			@SuppressWarnings("deprecation")
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				infoBox.setText(attendeeInfo.get(2));
				if (((CheckBox) event.getSource()).isChecked()) {
					lorganizerCheckedAttendeeInfoList.add(attendeeInfo);
				} else {
					lorganizerCheckedAttendeeInfoList.remove(attendeeInfo);
				}
			}
		});
	}
	
	static void setAttendeeInEventTree(String id, String name, final String info) {
		final ArrayList<String> attendeeInfo = new ArrayList<String>();
		attendeeInfo.add(id);
		attendeeInfo.add(name);
		attendeeInfo.add(info);
		CheckBox cb = new CheckBox(name);
		leventAttendeeCheckBoxList.add(cb);
		leventAttendeeInfoList.add(attendeeInfo);
		final TreeItem ti = eventAttendeeTreeItem.addItem(cb);
		cb.addClickHandler(new ClickHandler() {
			@SuppressWarnings("deprecation")
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				infoBox.setText(info);
				if (((CheckBox) event.getSource()).isChecked()) {
					leventCheckedAttendeeInfoList.add(attendeeInfo);
					leventCheckedTreeItemList.add(ti);
				} else {
					leventCheckedAttendeeInfoList.remove(attendeeInfo);
					leventCheckedTreeItemList.remove(ti);
				}
			}
		});
	}

	static void getOrganizerAttendeeList(){
		//infoBox.setText("lorganizerOwnedEventInfoList:"+lorganizerOwnedEventInfoList.toString());
		for(ArrayList<String> eventInfo :lorganizerOwnedEventInfoList ){
			final TreeItem ti= new TreeItem(new HTML("<b>"+eventInfo.get(1)+"</b>"));
			for(ArrayList<String> attendeeInfo: lorganizerAllAttendeeInfoList){
				if(attendeeInfo.get(3).compareTo(eventInfo.get(0))==0){
					setAttendeeInOrganizerTree(attendeeInfo.get(0), attendeeInfo.get(1), attendeeInfo.get(2),ti);
				}
			}
					
			organizerAttendeeTree.addItem(ti);
		}
	}
	public static void edittingAttendees(final String eventID, String eventName,
			final String organizerID) {
		// set organizer's attendeelist
		

		eventAttendeeTreeItem=new TreeItem(new HTML("<b>"+eventName+"</b>"));
		eventAttendeeTree.addItem(eventAttendeeTreeItem);
		eventService.getEventsByOrganizerIdAndStatus(organizerID,0, new AsyncCallback<List<Event>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				infoBox.setText("error at getEventsByOrganizerId:"
						+ caught);
			}

			@Override
			public void onSuccess(List<Event> result) {
				// TODO Auto-generated method stub
				
				for(Event e:result){
					ArrayList<String> eventInfo=new ArrayList<String>();
					eventInfo.add(e.getEventId());
					eventInfo.add(e.getName());
					if(e.getEventId()!=eventID)
					lorganizerOwnedEventInfoList.add(eventInfo);
				}
				//infoBox.setText(lorganizerOwnedEventInfoList.toString());
				
			}} );
	
		loadOrganizerAttendee.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				attendeeService.getAttendeeListByOrganizerId(organizerID,
						new AsyncCallback<ArrayList<Attendee>>() {
							public void onFailure(Throwable caught) {
								infoBox.setText("error while get attendeelist by organizer:"
										+ caught);
							}

							public void onSuccess(ArrayList<Attendee> attendeeList) {

								// infoBox.setText("number:"+attendeeList.size());
								for (int i = 0; i < attendeeList.size(); i++) {
									String info = attendeeList.get(i).toString();
									String id = attendeeList.get(i).getAttendeeId();
									String name = attendeeList.get(i).getName();
									String eventId = attendeeList.get(i).getEventId();
									//setAttendeeInOrganizerTree(id, name, info);
									 ArrayList<String> attendeeInfo = new ArrayList<String>();
									attendeeInfo.add(id);
									attendeeInfo.add(name);
									attendeeInfo.add(info);
									attendeeInfo.add(eventId);
									lorganizerAllAttendeeInfoList.add(attendeeInfo);
								}
								
								
								getOrganizerAttendeeList();
								loadOrganizerAttendee.setVisible(false);
								addToEvent.setVisible(true);
							}
						});
			
			}});
		//get all event id of certain organizer
	
		
		
		// set event's attendeelist
		loadEventAttendee.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				attendeeService.getAttendeeListByEventId(eventID,
						new AsyncCallback<ArrayList<Attendee>>() {
							public void onFailure(Throwable caught) {
							}

							public void onSuccess(ArrayList<Attendee> attendeeList) {

								// infoBox.setText("number:"+attendeeList.size());
								for (int i = 0; i < attendeeList.size(); i++) {
									final String info = attendeeList.get(i).toString();
									final String id = attendeeList.get(i)
											.getAttendeeId();
									String name = attendeeList.get(i).getName();
									setAttendeeInEventTree(id, name, info);
								}
								removeFromEvent.setVisible(true);
								loadEventAttendee.setVisible(false);
								sendEmailPop.setEnabled(true);
								inputAttendeePop.setEnabled(true);
								submit.setEnabled(true);
							}
						});
			}});
		
		// setListenerFor inputattendee
		inputAttendee.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				attendeeService.createAttendee(eventID, nameBox.getText(),
						emailBox.getText(), new AsyncCallback<Attendee>() {
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
							}

							@Override
							public void onSuccess(final Attendee result) {
								String id = result.getAttendeeId();
								String name = result.getName();
								String info = result.toString();
								readyToSendNotifyList.add(id);
								// infoBox.setText(readyToSendNotifyList.toString());
								setAttendeeInEventTree(id, name, info);
								//setAttendeeInOrganizerTree(id, name, info);
							}
						});
				inputAttendeeBox.hide();
				inputAttendee.setFocus(false);

			}
		});
		submit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

				attendeeService.fillAttendeesInEvent(eventID,
						lorganizerReadyToCreateList, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								infoBox.setText("error while fill attendeesInEvent:"
										+ caught);
							}

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub

							}

						});
				/*
				 * attendeeService.sendEmailBatchByOrganizer(
				 * lorganizerReadyToCreateList,1,new AsyncCallback<Void>(){
				 * 
				 * @Override public void onFailure(Throwable caught) { // TODO
				 * Auto-generated method stub
				 * 
				 * }
				 * 
				 * @Override public void onSuccess(Void result) { // TODO
				 * Auto-generated method stub
				 * 
				 * }});
				 */
				// remove readyToRemoveIdList

				/*
				 * attendeeService.sendEmailBatchByOrganizer(
				 * leventReadyToRemoveAttendeeIdList,-1,new
				 * AsyncCallback<Void>(){
				 * 
				 * @Override public void onFailure(Throwable caught) { // TODO
				 * Auto-generated method stub infoBox.setText("error:"+caught);
				 * }
				 * 
				 * @Override public void onSuccess(Void result) { // TODO
				 * Auto-generated method stub
				 * 
				 * }});
				 */
				attendeeService.deleteAttendeeByAttendeeIdList(
						leventReadyToRemoveAttendeeIdList,
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								infoBox.setText("error while removing:" + caught);
							}

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								// infoBox.setText(leventReadyToRemoveAttendeeIdList.toString());
							}
						});

				// Start Send email to notify attendees.
				attendeeService.sendEmailBatchByOrganizer(
						readyToSendNotifyList, 1, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub

							}
						});

				infoBox.setText("info of eventAttendeeList will appear here");
				clearSthForReusing();
				//clearEverythingBeforeJumpBack();
				//RootPanel.get("XuXuan").setVisible(false);
				//RootPanel.get("XiaYuan").setVisible(true);
			}

		});

	}

	/*
	 * public static void edittingAttendees (final String eventID, String
	 * organizerID){ attendeeService.getAttendeeListByOrganizerId(organizerID,
	 * new AsyncCallback<ArrayList<Attendee>>(){ public void onFailure(Throwable
	 * caught){ infoBox.setText("failure"+caught); } public void
	 * onSuccess(ArrayList<Attendee> attendeeList){
	 * 
	 * //infoBox.setText("number:"+attendeeList.size()); for (int i = 0; i <
	 * attendeeList.size(); i++) {
	 * //System.out.println("Name:"+attendeeList.get(i).getName());
	 * 
	 * String info=attendeeList.get(i).toString(); String
	 * id=attendeeList.get(i).getAttendeeId(); String
	 * name=attendeeList.get(i).getName(); final ArrayList<String> attendeeInfo
	 * =new ArrayList<String> (); attendeeInfo.add(id); attendeeInfo.add(name);
	 * attendeeInfo.add(info); organizerAttendeeList.add(new CheckBox(name));
	 * organizerAttendeeTreeItem.addItem(organizerAttendeeList.get(i));
	 * 
	 * //organizerAttendeeList.get(1).setChecked(false);
	 * organizerAttendeeList.get(i).addClickHandler(new ClickHandler(){
	 * 
	 * @SuppressWarnings("deprecation") public void onClick(ClickEvent event) {
	 * // TODO Auto-generated method stub infoBox.setText(attendeeInfo.get(2));
	 * if(((CheckBox) event.getSource()).isChecked()) {
	 * 
	 * // readyToCreateList.add(attendeeInfo);
	 * readyToMoveToEventList.add(attendeeInfo);
	 * //readyToCreateList_info.add(info);
	 * //infoBox.setText(""+readyToCreateList.toString()); } else {
	 * //readyToCreateList.remove(attendeeInfo);
	 * readyToMoveToEventList.remove(attendeeInfo);
	 * 
	 * //infoBox.setText(""+readyToCreateList.toString()); } } } ); } } });
	 * 
	 * attendeeService.getAttendeeListByEventId(eventID, new
	 * AsyncCallback<ArrayList<Attendee>>(){ public void onFailure(Throwable
	 * caught){} public void onSuccess(ArrayList<Attendee> attendeeList){
	 * 
	 * //infoBox.setText("number:"+attendeeList.size()); for (int i = 0; i <
	 * attendeeList.size(); i++) {
	 * //System.out.println("Name:"+attendeeList.get(i).getName());
	 * eventAttendeeEmailList.add(attendeeList.get(i).getEmail());
	 * eventAttendeeList.add(new CheckBox(attendeeList.get(i).getName())); final
	 * String info=attendeeList.get(i).toString(); final String
	 * id=attendeeList.get(i).getAttendeeId(); final TreeItem
	 * ti=eventAttendeeTreeItem.addItem(eventAttendeeList.get(i));
	 * 
	 * //organizerAttendeeList.get(1).setChecked(false);
	 * eventAttendeeList.get(i).addClickHandler(new ClickHandler(){
	 * 
	 * @SuppressWarnings("deprecation") public void onClick(ClickEvent event) {
	 * // TODO Auto-generated method stub infoBox.setText(info); if(((CheckBox)
	 * event.getSource()).isChecked()) { //eventAttendeeList.contains((CheckBox)
	 * event.getSource()); readyToRemoveIdList.add(id);
	 * readyToRemoveCheckBoxList.add(ti);
	 * //infoBox.setText(""+readyToRemoveIdList.toString()); } else {
	 * readyToRemoveIdList.remove(id); readyToRemoveCheckBoxList.remove(ti); //
	 * infoBox.setText(""+readyToRemoveIdList.toString()); } } } ); } } }); //
	 * TODO Auto-generated method stub // addToCurrentEvent: add
	 * currentorganizerAttendee to eventattendeeList // show out in the tree //
	 * add to readyToCreateListe
	 * 
	 * removeFromEvent.addClickHandler(new ClickHandler(){
	 * 
	 * @Override public void onClick(ClickEvent event) { button rmove from
	 * CurrentEvent: rm from the current Tree rm from eventAttendeeList if
	 * already exist in readyToCreateList then rm in that list else add in
	 * readyToRemoveList for(TreeItem ti:readyToRemoveCheckBoxList)
	 * eventAttendeeTreeItem.removeItem(ti); for(String id:readyToRemoveIdList){
	 * for(ArrayList<String> attInfo:readyToCreateList) {
	 * if(id.compareTo(attInfo.get(0))==0) { readyToCreateList.remove(attInfo);
	 * readyToRemoveIdList.remove(id); break; } } }
	 * 
	 * }}); addToEvent.addClickHandler(new ClickHandler(){
	 * 
	 * @Override public void onClick(ClickEvent event) { for(final
	 * ArrayList<String> attendeeInfo:readyToMoveToEventList) { boolean
	 * ifExist=false; for(String ai : eventAttendeeEmailList) {
	 * if(attendeeInfo.get(2).contains(ai)) { ifExist=true; break; } }
	 * if(ifExist) continue; //final ArrayList<String>
	 * attendeeInfo_cpy=(ArrayList<String>) attendeeInfo.clone(); CheckBox
	 * cbox=new CheckBox(attendeeInfo.get(1));
	 * 
	 * eventAttendeeEmailList.add(getEmailFromInfo(attendeeInfo.get(2)));
	 * eventAttendeeList.add(cbox); final TreeItem
	 * ti=eventAttendeeTreeItem.addItem(cbox); cbox.addClickHandler(new
	 * ClickHandler(){
	 * 
	 * @SuppressWarnings("deprecation")
	 * 
	 * @Override public void onClick(ClickEvent event) { // TODO Auto-generated
	 * method stub infoBox.setText(getEmailFromInfo(attendeeInfo.get(2)));
	 * //infoBox.setText(attendeeInfo.get(2)); if(((CheckBox)
	 * event.getSource()).isChecked()) {
	 * 
	 * readyToRemoveIdList.add(attendeeInfo.get(0));
	 * readyToRemoveCheckBoxList.add(ti); //
	 * infoBox.setText(""+readyToRemoveIdList.toString()); } else {
	 * readyToRemoveIdList.remove(attendeeInfo.get(0));
	 * readyToRemoveCheckBoxList.remove(ti);
	 * //infoBox.setText(""+readyToRemoveIdList.toString()); } }});
	 * 
	 * 
	 * } readyToCreateList.addAll(readyToMoveToEventList);
	 * readyToMoveToEventList.clear(); }
	 * 
	 * }); inputAttendee.addClickHandler(new ClickHandler(){
	 * 
	 * @Override public void onClick(ClickEvent event) {
	 * attendeeService.createAttendee(eventID, nameBox.getText(),
	 * emailBox.getText(),new AsyncCallback<Attendee>(){
	 * 
	 * @Override public void onFailure(Throwable caught) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onSuccess( final Attendee result) {
	 * eventAttendeeEmailList.add(emailBox.getText()); // TODO Auto-generated
	 * method stub //final ArrayList<String>
	 * attendeeInfo_cpy=(ArrayList<String>) attendeeInfo.clone();
	 * readyToSendNotifyList.add(result.getAttendeeId()); CheckBox cbox=new
	 * CheckBox(nameBox.getText()); eventAttendeeList.add(cbox); final TreeItem
	 * ti=eventAttendeeTreeItem.addItem(cbox); cbox.addClickHandler(new
	 * ClickHandler(){
	 * 
	 * @SuppressWarnings("deprecation")
	 * 
	 * @Override public void onClick(ClickEvent event) { // TODO Auto-generated
	 * method stub infoBox.setText(result.toString()); if(((CheckBox)
	 * event.getSource()).isChecked()) {
	 * 
	 * readyToRemoveIdList.add(result.getAttendeeId());
	 * readyToRemoveCheckBoxList.add(ti); //
	 * infoBox.setText(""+readyToRemoveIdList.toString()); } else {
	 * readyToRemoveIdList.remove(result.getAttendeeId());
	 * readyToRemoveCheckBoxList.remove(ti);
	 * //infoBox.setText(""+readyToRemoveIdList.toString()); } }});
	 * 
	 * CheckBox cboxOrganizer = new CheckBox(nameBox.getText()); final
	 * ArrayList<String> attendeeInfo =new ArrayList<String> ();
	 * attendeeInfo.add(result.getName()); attendeeInfo.add(nameBox.getText());
	 * attendeeInfo.add(result.toString());
	 * organizerAttendeeList.add(cboxOrganizer);
	 * organizerAttendeeTreeItem.addItem(cboxOrganizer);
	 * cboxOrganizer.addClickHandler(new ClickHandler(){
	 * 
	 * @Override public void onClick(ClickEvent event) {
	 * 
	 * infoBox.setText(result.toString()); if(((CheckBox)
	 * event.getSource()).isChecked()) {
	 * 
	 * readyToCreateList.add(attendeeInfo); //readyToCreateList_info.add(info);
	 * //infoBox.setText(""+readyToCreateList.toString()); } else {
	 * readyToCreateList.remove(attendeeInfo);
	 * 
	 * //infoBox.setText(""+readyToCreateList.toString()); } }});
	 * 
	 * 
	 * 
	 * }}); inputAttendeeBox.hide(); inputAttendee.setFocus(false);
	 * 
	 * }}); submit.addClickHandler(new ClickHandler(){
	 * 
	 * @Override public void onClick(ClickEvent event) { // TODO Auto-generated
	 * method stub ArrayList<String> attendeeList=new ArrayList<String>();
	 * for(ArrayList<String> al : readyToCreateList) {
	 * attendeeList.add(al.get(0)); }
	 * attendeeService.fillAttendeesInEvent(eventID, attendeeList, new
	 * AsyncCallback<Void>(){
	 * 
	 * @Override public void onFailure(Throwable caught) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onSuccess(Void result) { // TODO Auto-generated
	 * method stub //
	 * infoBox.setText("info of eventAttendeeList will appear here"); //
	 * AttendeeIDs.clear(); // eventAttendeeTreeItem.removeItems(); //
	 * organizerAttendeeList.clear(); //
	 * RootPanel.get("XuXuan").setVisible(false); //
	 * RootPanel.get("XiaYuan").setVisible(true);
	 * 
	 * }
	 * 
	 * }); attendeeService.sendEmailBatchByOrganizer(attendeeList,1,new
	 * AsyncCallback<Void>(){
	 * 
	 * @Override public void onFailure(Throwable caught) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onSuccess(Void result) { // TODO Auto-generated
	 * method stub
	 * 
	 * }}); //remove readyToRemoveIdList
	 * attendeeService.deleteAttendeeByAttendeeIdList(readyToRemoveIdList,new
	 * AsyncCallback<Void>(){
	 * 
	 * @Override public void onFailure(Throwable caught) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onSuccess(Void result) { // TODO Auto-generated
	 * method stub
	 * 
	 * }}); attendeeService.sendEmailBatchByOrganizer(readyToRemoveIdList,-1,new
	 * AsyncCallback<Void>(){
	 * 
	 * @Override public void onFailure(Throwable caught) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onSuccess(Void result) { // TODO Auto-generated
	 * method stub
	 * 
	 * }}); //Start Send email to notify attendees.
	 * attendeeService.sendEmailBatchByOrganizer(readyToSendNotifyList,1,new
	 * AsyncCallback<Void>(){
	 * 
	 * @Override public void onFailure(Throwable caught) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onSuccess(Void result) { // TODO Auto-generated
	 * method stub
	 * 
	 * }}); }
	 * 
	 * });
	 * 
	 * 
	 * } //used by creating or editing event public static void
	 * edittingAttendees_past(final String eventID, String OrganizerID){
	 * attendeeService.getAttendeeListByOrganizerId(OrganizerID, new
	 * AsyncCallback<ArrayList<Attendee>>(){ public void onFailure(Throwable
	 * caught){} public void onSuccess(ArrayList<Attendee> attendeeList){
	 * 
	 * //infoBox.setText("number:"+attendeeList.size()); for (int i = 0; i <
	 * attendeeList.size(); i++) {
	 * //System.out.println("Name:"+attendeeList.get(i).getName());
	 * organizerAttendeeList.add(new CheckBox(attendeeList.get(i).getName()));
	 * final String info=attendeeList.get(i).toString(); final String
	 * id=attendeeList.get(i).getAttendeeId();
	 * eventAttendeeTreeItem.addItem(organizerAttendeeList.get(i));
	 * 
	 * //organizerAttendeeList.get(1).setChecked(false);
	 * organizerAttendeeList.get(i).addClickHandler(new ClickHandler(){
	 * 
	 * @SuppressWarnings("deprecation") public void onClick(ClickEvent event) {
	 * // TODO Auto-generated method stub infoBox.setText(info); if(((CheckBox)
	 * event.getSource()).isChecked()) { AttendeeIDs.add(id);
	 * //nfoBox.setText(""+AttendeeIDs.toString()); } else {
	 * AttendeeIDs.remove(id); //infoBox.setText(""+AttendeeIDs.toString()); } }
	 * } ); } } }); submit.addClickHandler(new ClickHandler(){
	 * 
	 * @Override public void onClick(ClickEvent event) { // TODO Auto-generated
	 * method stub attendeeService.fillAttendeesInEvent(eventID, AttendeeIDs,
	 * new AsyncCallback<Void>(){
	 * 
	 * @Override public void onFailure(Throwable caught) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onSuccess(Void result) { // TODO Auto-generated
	 * method stub
	 * infoBox.setText("info of eventAttendeeList will appear here");
	 * AttendeeIDs.clear(); eventAttendeeTreeItem.removeItems();
	 * organizerAttendeeList.clear(); RootPanel.get("XuXuan").setVisible(false);
	 * RootPanel.get("XiaYuan").setVisible(true);
	 * 
	 * }
	 * 
	 * }); }
	 * 
	 * });
	 * 
	 * 
	 * } // Used by only inspect the eventAttendeeList. public static void
	 * showAttendees(String eventID) {
	 * 
	 * attendeeService.getAttendeeListByEventId(eventID, new
	 * AsyncCallback<ArrayList<Attendee>>(){ public void onFailure(Throwable
	 * caught){} public void onSuccess(ArrayList<Attendee> attendeeList){
	 * 
	 * 
	 * for (int i = 0; i < attendeeList.size(); i++) {
	 * 
	 * System.out.println("Name:"+attendeeList.get(i).getName());
	 * eventAttendeeList.add(new CheckBox(attendeeList.get(i).getName())); final
	 * String info=attendeeList.get(i).toString();
	 * eventAttendeeTreeItem.addItem(eventAttendeeList.get(i));
	 * eventAttendeeList.get(i).addClickHandler(new ClickHandler(){
	 * 
	 * 
	 * public void onClick(ClickEvent event) { // TODO Auto-generated method
	 * stub infoBox.setText(info); } } ); } } });
	 * 
	 * Tree eventAttendeeTree = new Tree();
	 * eventAttendeeTree.addItem(eventAttendeeTreeItem);
	 * 
	 * hPanel.add(eventAttendeeTree); hPanel.add(infoBox);
	 * 
	 * }
	 */

}
