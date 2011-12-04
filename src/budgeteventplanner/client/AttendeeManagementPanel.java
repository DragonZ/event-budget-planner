package budgeteventplanner.client;

import java.util.List;

import budgeteventplanner.client.entity.Attendee;

import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AttendeeManagementPanel extends Composite {

	private final AttendeeServiceAsync attendeeService = GWT.create(AttendeeService.class);

	private final List<Attendee> listAllAttendee = Lists.newArrayList();
	private final List<Attendee> listEventAttendee = Lists.newArrayList();

	private final List<Attendee> listToBeDupicate = Lists.newArrayList();
	private final List<Attendee> listToBeRemove = Lists.newArrayList();

	final Tree treeAllAttendee = new Tree();
	final Tree treeEventAttendee = new Tree();

	private final String eventId;

	public AttendeeManagementPanel(final String eventName, final String eventId, final String organizerId) {
		this.eventId = eventId;

		DockPanel panelMain = new DockPanel();
		// Top panels
		VerticalPanel hpanelToolBoxTitle = new VerticalPanel();
		HorizontalPanel hpanelTitle = new HorizontalPanel();
		HorizontalPanel hpanelToolBox = new HorizontalPanel();
		VerticalPanel vpanelAllAttendee = new VerticalPanel();
		VerticalPanel vpanelEventAttendee = new VerticalPanel();
		HorizontalPanel hpanelTopEventAll = new HorizontalPanel();
		
		final VerticalPanel vpanelAttendeeInfo = new VerticalPanel();
		Button btnEdit = new Button("Edit");
		Button btnCanceledit = new Button("Cancel");
		HorizontalPanel hpanelEdit = new HorizontalPanel();
		HorizontalPanel hpanelEditInner = new HorizontalPanel();
		hpanelEdit.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hpanelEdit.setSize("100%", "100%");
		hpanelEditInner.add(btnEdit);
		hpanelEditInner.add(btnCanceledit);
		hpanelEditInner.setSpacing(5);
		hpanelEdit.add(hpanelEditInner);
		
		btnEdit.addClickHandler( new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				listToBeDupicate.clear();
				listToBeRemove.clear();
				uncheckCheckBox(treeAllAttendee);
				uncheckCheckBox(treeEventAttendee);
				AttendeeInfoPanel at = (AttendeeInfoPanel)vpanelAttendeeInfo.getWidget(0);
				at.setEnable(true);
			}});
		
		// ToolBox
	
		final Button btnCreateAttendee = new Button("Create");
		btnCreateAttendee.setEnabled(false);
		btnCreateAttendee.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createAttendee();
			}
		});

		final Button btnDuplicateAttendee = new Button("Duplicate");
		btnDuplicateAttendee.setEnabled(false);
		
		btnDuplicateAttendee.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<Attendee> selectedAttendees = getSelectedAttendees(treeAllAttendee);
				for (Attendee a : selectedAttendees) {
					if (!checkDuplicateAttendee(a.getEmail(), listEventAttendee)) {
						if (!checkDuplicateAttendee(a.getEmail(), listToBeDupicate)) {
							listToBeDupicate.add(a);
							putAttendeeInTree(a, treeEventAttendee);
						} else {
							// showDialog("Attendee Management",
							// "You have already duplicated the same attendee!");
						}
					} else {
						// showDialog("Attendee Management",
						// "This attendee is already in the event!");
					}
				}
				uncheckCheckBox(treeAllAttendee);
			}
		});

		final Button btnRemoveAttendee = new Button("Remove");
		btnRemoveAttendee.setEnabled(false);
		btnRemoveAttendee.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<Attendee> selectedEventAttendees = getSelectedAttendees(treeEventAttendee);
				for (Attendee a : selectedEventAttendees) {
					if (listToBeDupicate.contains(a)) {
						// Situation1 : already in Dup list
						listToBeDupicate.remove(a);
					} else {
						// Situation2 : already in Event list
						listToBeRemove.add(a);
					}
					removeAttendeeInTree(a.getAttendeeId(), treeEventAttendee);
				}
				uncheckCheckBox(treeEventAttendee);
			}
		});

		final Button btnApplyAttendee = new Button("Apply");
		btnApplyAttendee.setEnabled(false);
		btnApplyAttendee.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				attendeeService.fillAttendeesInEvent(eventId, listToBeDupicate,
						new AsyncCallback<List<Attendee>>() {
							@Override
							public void onSuccess(List<Attendee> result) {
								// Duplicate Attendee
								for (Attendee a : listToBeDupicate) {
									
									removeAttendeeInTree(a.getAttendeeId(), treeEventAttendee);
								}
								for (Attendee a : result) {
									//listEventAttendee.add(a);
									putAttendeeInTree(a, treeEventAttendee);
								}
								listToBeDupicate.clear();
							}

							@Override
							public void onFailure(Throwable caught) {

							}
						});
				attendeeService.removeAttendeeByAttendeeList(listToBeRemove,
						new AsyncCallback<Void>() {
							@Override
							public void onSuccess(Void result) {
								listToBeRemove.clear();
							}

							@Override
							public void onFailure(Throwable caught) {
							}
						});

				uncheckCheckBox(treeAllAttendee);
				uncheckCheckBox(treeEventAttendee);
			}

		});
		
		Button btnBack = new Button("Go Back");
		btnBack.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				RootPanel.get("XiaYuan").setVisible(true);
				RootPanel.get("XuXuan").setVisible(false);
			}
		});
		
		Button btnLoad = new Button("Load Attendees");
		btnLoad.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//load all
				attendeeService.getSortedAttendeeList(organizerId, eventId,
						new AsyncCallback<SetMultimap<String, Attendee>>() {
							public void onFailure(Throwable caught) {
								showDialog("Attendee Management", "Error while load attendee list.");
							}

							public void onSuccess(
									SetMultimap<String, Attendee> multimapEventAttendee) {
								treeAllAttendee.clear();
								listAllAttendee.clear();

								for (String eventName : multimapEventAttendee.keySet()) {
									treeAllAttendee.add(new Label(eventName));
									for (Attendee attendee : multimapEventAttendee.get(eventName)) {
										CheckBox cb = new CheckBox(attendee.getName());
										cb.setTitle(attendee.getAttendeeId());
										treeAllAttendee.add(cb);
										listAllAttendee.add(attendee);
									}
								}
							}
						});
				//load current event
				attendeeService.getSortedEventAttendeeByStatus(eventId,new AsyncCallback<SetMultimap<Integer,Attendee>>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(SetMultimap<Integer, Attendee> result) {
						listEventAttendee.clear();
						treeEventAttendee.clear();
						treeEventAttendee.add(new Label(eventName));
						for (Integer status : result.keySet()) {
							switch (status) {
							case Attendee.YES:
								  treeEventAttendee.add(new Label("Will Attend"));
								  break;
							case Attendee.NO:
								  treeEventAttendee.add(new Label("Not Attend"));
								  break;
							case Attendee.MAYBE:
								  treeEventAttendee.add(new Label("May Attend"));
								  break;
							case Attendee.PENDING:
								  treeEventAttendee.add(new Label("Pending"));
								  break;
							  default:
							}
							
							for (Attendee a : result.get(status)) {
								putAttendeeInTree(a, treeEventAttendee);
							}
							listEventAttendee.addAll(result.get(status));
						}
					}
				});
				// setenable
				btnCreateAttendee.setEnabled(true);
				btnDuplicateAttendee.setEnabled(true);
				btnRemoveAttendee.setEnabled(true);
				btnApplyAttendee.setEnabled(true);
			}
		});
		hpanelToolBox.add(btnLoad);
		hpanelToolBox.add(btnCreateAttendee);
		hpanelToolBox.add(btnDuplicateAttendee);
		hpanelToolBox.add(btnRemoveAttendee);
		hpanelToolBox.add(btnApplyAttendee);
		hpanelToolBox.add(btnBack);

		// All Attendee
		//Anchor btnLoadAllAttendee = new Anchor("Load All Attendee");

		vpanelAllAttendee.add(treeAllAttendee);

		treeAllAttendee.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				vpanelAttendeeInfo.clear();
				vpanelAttendeeInfo.add(new AttendeeInfoPanel(event.getSelectedItem().getWidget().getTitle()));
			}
		});

/*		btnLoadAllAttendee.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				attendeeService.getSortedAttendeeList(organizerId, eventId,
						new AsyncCallback<SetMultimap<String, Attendee>>() {
							public void onFailure(Throwable caught) {
								showDialog("Attendee Management", "Error while load attendee list.");
							}

							public void onSuccess(
									SetMultimap<String, Attendee> multimapEventAttendee) {
								treeAllAttendee.clear();
								listAllAttendee.clear();

								for (String eventName : multimapEventAttendee.keySet()) {
									treeAllAttendee.add(new Label(eventName));
									for (Attendee attendee : multimapEventAttendee.get(eventName)) {
										CheckBox cb = new CheckBox(attendee.getName());
										cb.setTitle(attendee.getAttendeeId());
										treeAllAttendee.add(cb);
										listAllAttendee.add(attendee);
									}
								}
							}
						});
			}
		});*/
		//vpanelAllAttendee.add(btnLoadAllAttendee);
		vpanelAllAttendee.add(treeAllAttendee);

		/*Anchor btnLoadEventAttendee = new Anchor("Load Event Attendee");

		btnLoadEventAttendee.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				attendeeService.getSortedEventAttendeeByStatus(eventId,new AsyncCallback<SetMultimap<Integer,Attendee>>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(SetMultimap<Integer, Attendee> result) {
						listEventAttendee.clear();
						treeEventAttendee.clear();

						for (Integer status : result.keySet()) {
							switch (status) {
							case Attendee.YES:
								  treeEventAttendee.add(new Label("Yes"));
								  break;
							case Attendee.NO:
								  treeEventAttendee.add(new Label("No"));
								  break;
							case Attendee.MAYBE:
								  treeEventAttendee.add(new Label("Maybe"));
								  break;
							case Attendee.PENDING:
								  treeEventAttendee.add(new Label("Pending"));
								  break;
							  default:
							}
							
							for (Attendee a : result.get(status)) {
								putAttendeeInTree(a, treeEventAttendee);
							}
							listEventAttendee.addAll(result.get(status));
						}
					}
				});
			}
		});*/

		treeEventAttendee.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				vpanelAttendeeInfo.clear();
				vpanelAttendeeInfo.add(new AttendeeInfoPanel(event.getSelectedItem().getWidget().getTitle()));
			}
		});

		//vpanelEventAttendee.add(btnLoadEventAttendee);
	
		hpanelTitle.add(new HTML("<p align=\"center\"><h1>"+eventName+"</h1></p>"));
		vpanelEventAttendee.add(treeEventAttendee);
		hpanelTopEventAll.add(vpanelAllAttendee);
		hpanelTopEventAll.add(vpanelEventAttendee);
		hpanelTopEventAll.setSize("400px","300px");
		hpanelTopEventAll.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		hpanelTopEventAll.setSpacing(5);
		panelMain.setSpacing(10);
		vpanelAttendeeInfo.setSize("400px","300px");
		vpanelAttendeeInfo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hpanelToolBoxTitle.add(hpanelTitle);
		hpanelToolBoxTitle.add(hpanelToolBox);
		hpanelToolBoxTitle.setSize("100%","100%");
		hpanelToolBox.setSize("100%","50px");
		hpanelToolBoxTitle.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		hpanelTitle.setSize("100%","100%");
		hpanelTitle.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelMain.add(hpanelToolBoxTitle, DockPanel.NORTH);
		panelMain.add(hpanelTopEventAll, DockPanel.WEST);
		panelMain.add(vpanelAttendeeInfo, DockPanel.CENTER);
		panelMain.add(hpanelEdit,DockPanel.NORTH);
		//panelMain.add(vpanelAttendeeInfo, DockPanel.EAST);
		vpanelAttendeeInfo.add(new AttendeeInfoPanel("0FAA1D5F-EA29-448B-BC4C-D8C1B4D708B2"));
		initWidget(panelMain);
	}

	public static void showDialog(String title, String text) {
		final DialogBox dialogError = new DialogBox();
		final Button btnClose = new Button("Close");
		dialogError.setText(title);
		dialogError.setAnimationEnabled(true);
		btnClose.getElement().setId("btnClose");
		VerticalPanel panelError = new VerticalPanel();
		panelError.add(new Label(text));
		panelError.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		panelError.add(btnClose);
		dialogError.setWidget(panelError);

		btnClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogError.hide();
			}
		});
		dialogError.center();
		btnClose.setFocus(true);
	}

	private void createAttendee() {
		final DialogBox dialogBox = new DialogBox();

		VerticalPanel vpanelMain = new VerticalPanel();

		VerticalPanel vpanelInfo = new VerticalPanel();
		final TextBox txtName = new TextBox();
		final TextBox txtEmail = new TextBox();
		vpanelInfo.add(new Label("* Name:"));
		vpanelInfo.add(txtName);
		vpanelInfo.add(new Label("* Email:"));
		vpanelInfo.add(txtEmail);

		HorizontalPanel hpanelButton = new HorizontalPanel();
		Button btnCreate = new Button("Create");
		Button btnCancel = new Button("Cancel");
		hpanelButton.add(btnCreate);
		hpanelButton.add(btnCancel);

		vpanelMain.add(vpanelInfo);
		vpanelMain.add(hpanelButton);
		dialogBox.add(vpanelMain);

		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		btnCreate.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (checkDuplicateAttendee(txtEmail.getText(), listEventAttendee)) {
					txtEmail.setText("");
					showDialog("Attendee Management",
							"Sorry, this email address has already been added into this event!");
				} else {
					attendeeService.createAttendee(eventId, txtName.getText(), txtEmail.getText(),
							new AsyncCallback<Attendee>() {
								@Override
								public void onFailure(Throwable caught) {
								}

								@Override
								public void onSuccess(Attendee result) {
									putAttendeeInTree(result, treeEventAttendee);
									listEventAttendee.add(result);
									showDialog("Attendee Management",
											"Attendee created successfully");
								}
							});
					dialogBox.hide();
				}
			}
		});
		dialogBox.show();
	}

	private boolean checkDuplicateAttendee(String email, List<Attendee> list) {
		for (Attendee a : list) {
			if (a.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}

	private List<Attendee> getSelectedAttendees(Tree tree) {
		List<Attendee> selectedAttendee = Lists.newArrayList();
		for (Widget w : tree) {
			if (w instanceof CheckBox) {
				if (((CheckBox) w).getValue()) {
					selectedAttendee.add(getAttendeeByAttendeeId(w.getTitle()));
				}
			}
		}
		return selectedAttendee;
	}

	private void putAttendeeInTree(Attendee attendee, Tree tree) {
		CheckBox cb = new CheckBox(attendee.getName());
		cb.setTitle(attendee.getAttendeeId());
		tree.add(cb);
	}

	private void removeAttendeeInTree(String attendeeId, Tree tree) {
		for (Widget w : tree) {
			if (w instanceof CheckBox) {
				if (((CheckBox) w).getTitle().equals(attendeeId)) {
					tree.remove(w);
				}
			}
		}
	}

	private Attendee getAttendeeByAttendeeId(String attendeeId) {
		for (Attendee a : listAllAttendee) {
			if (a.getAttendeeId().equals(attendeeId)) {
				return a;
			}
		}

		for (Attendee a : listEventAttendee) {
			if (a.getAttendeeId().equals(attendeeId)) {
				return a;
			}
		}

		return null;
	}

	private void uncheckCheckBox(Tree tree) {
		for (Widget w : tree) {
			if (w instanceof CheckBox) {
				((CheckBox) w).setValue(false);
			}
		}
	}
}
