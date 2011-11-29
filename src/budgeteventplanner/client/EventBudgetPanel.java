/*
 CS307 Software Engineering, Event Budget Management
 Yuan Xia
 Computer Science, Purdue University
 */

package budgeteventplanner.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import budgeteventplanner.client.entity.Attendee;
import budgeteventplanner.client.entity.Category;
import budgeteventplanner.client.entity.Event;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;


public class EventBudgetPanel extends VerticalPanel implements EntryPoint {

	public TabPanel t_panel;
	public String organizerId;

	private final CategoryServiceAsync categoryService = GWT
			.create(CategoryService.class);
	private final EventServiceAsync eventService = GWT
			.create(EventService.class);
	private final AttendeeServiceAsync attendeeService = GWT.create(AttendeeService.class);

	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub

		organizerId = "YuanXia";

		// HorizontalSplitPanel event_h_panel=new HorizontalSplitPanel();
		final HorizontalPanel event_h_panel = new HorizontalPanel();
		event_h_panel.setBorderWidth(1);
		event_h_panel.setSize("100%", "500px");
		
		final VerticalPanel leftPanel = new VerticalPanel();
		Hyperlink activeLink = new Hyperlink();
		activeLink.setText("Active Events");
		Hyperlink deleteLink = new Hyperlink();
		deleteLink.setText("Deleted Events");
		
		leftPanel.add(activeLink);
		leftPanel.add(deleteLink);
		leftPanel.setWidth("100px");
		event_h_panel.add(leftPanel);
		
		
		activeLink.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event)
			{
				event_h_panel.clear();
				event_h_panel.add(leftPanel);
				AddActiveEventPanel(event_h_panel);
				//event_h_panel.add(event_right_v_panel);
			}
		});
		deleteLink.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event)
			{
				event_h_panel.clear();
				event_h_panel.add(leftPanel);
				AddDeleteEventPanel(event_h_panel);
				
			}
		});

		HorizontalPanel budget_h_panel = new HorizontalPanel();
		budget_h_panel.setBorderWidth(1);
		budget_h_panel.setSize("100%", "500px");
		VerticalPanel budget_right_v_panel = new VerticalPanel();

		final FlexTable budget_table = new FlexTable();

		budget_table.setSize("1000px", "100%");
		budget_table.setText(0, 0, "Budget ID");
		budget_table.getCellFormatter().setWidth(0, 0, "20%");
		budget_table.setText(0, 1, "Budget Name");
		budget_table.getCellFormatter().setWidth(0, 1, "20%");
		budget_table.setText(0, 2, "Event Name");
		budget_table.getCellFormatter().setWidth(0, 2, "20%");
		budget_table.setText(0, 3, "Limit");
		budget_table.getCellFormatter().setWidth(0, 3, "20%");
		budget_table.setText(0, 4, "Modify");
		budget_table.getCellFormatter().setWidth(0, 4, "20%");
		budget_table.setText(0, 5, "Delete");
		budget_table.getCellFormatter().setWidth(0, 5, "20%");

		int budget_row = 3;
		for (int i = 1; i <= budget_row; i++) {
			budget_table.setWidget(i, 0, new Label("Budget ID from Server"));
			budget_table.setWidget(i, 1, new Label("Budget Name from Server"));
			budget_table.setWidget(i, 2, new Label("Event Name from Server"));
			budget_table.setWidget(i, 3, new Label("Budget Limit from Server"));

			
			final Button budget_mod = new Button("Modify");
			budget_table.setWidget(i, 4, budget_mod);
			final Button budget_del = new Button("Delete");
			budget_table.setWidget(i, 5, budget_del);

			budget_mod.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					int row;
					int total_event = budget_table.getRowCount();
					for (row = 1; row <= total_event; row++) {
						if (budget_table.getWidget(row, 4).equals(budget_mod))
							break;
					}
					budget_pop_up(1, row, budget_table);
				}
			});

			budget_del.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					int temp_r;
					int total_r = budget_table.getRowCount();
					for (temp_r = 1; temp_r <= total_r; temp_r++) {
						if (budget_table.getWidget(temp_r, 5)
								.equals(budget_del))
							break;
					}
					budget_table.removeRow(temp_r);
				}
			});
		}

		Button budget_add = new Button("Add");
		budget_right_v_panel.add(budget_add);
		budget_right_v_panel.add(budget_table);

		Label l2 = new Label("Left Place Holder");
		l2.setWidth("100px");
		budget_h_panel.add(l2);
		budget_h_panel.add(budget_right_v_panel);

		budget_add.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				budget_pop_up(0, 0, budget_table);
			}
		});

		t_panel = new TabPanel();
		t_panel.add(event_h_panel, "Event");
		t_panel.add(budget_h_panel, "Budget");
		RootPanel.get("XiaYuan").add(t_panel);
		t_panel.setSize("100%", "100%");

		t_panel.selectTab(0);

	}
	public void EventTableRefresh(FlexTable table) {
		final FlexTable event_table = table;

		eventService.getEventsByOrganizerIdAndStatus(organizerId,Event.ACTIVE,
				new AsyncCallback<List<Event>>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(List<Event> result) {
						int i = 0;
						for (Event e : result) {
							i++;
							event_table.setWidget(i, 0,
									new Label(e.getEventId()));
							event_table.setWidget(i, 1, new Label(e.getName()));
							event_table.setWidget(i, 2, new Label(e
									.getStartTime().toString()));
							event_table.setWidget(i, 3, new Label(e
									.getEndTime().toString()));
							event_table.setWidget(i, 4,
									new Label(e.getAddress()));

							final Button itemMod = new Button("Item Modify");
							itemMod.setWidth("100px");
							event_table.setWidget(i, 5, itemMod);
							
							itemMod.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								int row;
								int total_event = event_table.getRowCount();
								for (row = 1; row <= total_event; row++) {
									if (event_table.getWidget(row, 5)
											.equals(itemMod))
										break;
								}
								String event_id = event_table.getText(row,
										0);
								item_pop_up(event_id);
							}
						});
							

							final Button event_mod = new Button("Event Modify");
							event_mod.setWidth("100px");
							event_table.setWidget(i, 6, event_mod);

							final Button attend_list = new Button(
									"Attendee Modify");
							attend_list.setWidth("120px");
							event_table.setWidget(i, 7, attend_list);

							final Button view_info = new Button("View All");
							view_info.setWidth("100px");
							event_table.setWidget(i, 8, view_info);

							final Button event_del = new Button("Delete");
							event_table.setWidget(i, 9, event_del);

							event_mod.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									int row;
									int total_event = event_table.getRowCount();
									for (row = 1; row <= total_event; row++) {
										if (event_table.getWidget(row, 6)
												.equals(event_mod))
											break;
									}
									event_pop_up(1, row, event_table);
								}
							});
							attend_list.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									int row;
									int total_event = event_table.getRowCount();
									for (row = 1; row <= total_event; row++)
										if (event_table.getWidget(row, 7)
												.equals(attend_list))
											break;
									String event_id = event_table.getText(row,
											0);
									attendee_pop_up(event_id);
								}
							});
							view_info.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									int row;
									int total_event = event_table.getRowCount();
									for (row = 1; row <= total_event; row++)
										if (event_table.getWidget(row, 8)
												.equals(view_info))
											break;
									String event_id = event_table.getText(row,
											0);
									String event_title = event_table.getText(
											row, 1);
									String startDate = event_table.getText(row,
											2);
									String endDate = event_table
											.getText(row, 3);
									String location = event_table.getText(row,
											4);
									view_info_pop_up(event_id, event_title,
											startDate, endDate, location);
								}
							});

							event_del.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									int row;
									int total = event_table.getRowCount();
									for (row = 1; row <= total; row++) {
										if (event_table.getWidget(row, 9)
												.equals(event_del))
											break;
									}
									final int r = row;
									String eventId = event_table.getText(row, 0);
									eventService.changeEventStatusByEventId(eventId, Event.INACTIVE, new AsyncCallback<Void>() {
										public void onFailure(Throwable caught)
										{
											
										}
										public void onSuccess(Void reslt)
										{
											event_table.removeRow(r);
										}
									});
								}
							});

						}
					}
				});

	}
	public void AddActiveEventPanel(HorizontalPanel m)
	{
		final HorizontalPanel mother = m;
		
		final VerticalPanel event_right_v_panel = new VerticalPanel();
		Button event_add = new Button("Add");
		event_right_v_panel.add(event_add);
		
		

		final FlexTable event_table = new FlexTable();
		event_table.setSize("1100px", "100%");
		event_table.setWidget(0, 0, new HTML("<strong>Event ID</strong>"));
		//event_table.getCellFormatter().setWidth(0, 0, "0px");
		event_table.getCellFormatter().setVisible(0, 0, false);
		event_table.setWidget(0, 1, new HTML("<strong>Event Title</strong>"));
		event_table.getCellFormatter().setWidth(0, 1, "20%");
		event_table.setWidget(0, 2, new HTML("<strong>Start Date</strong>"));
		event_table.getCellFormatter().setWidth(0, 2, "20%");
		event_table.setWidget(0, 3, new HTML("<strong>End Date</strong>"));
		event_table.getCellFormatter().setWidth(0, 3, "20%");
		event_table.setWidget(0, 4, new HTML("<strong>Location</strong>"));
		event_table.getCellFormatter().setWidth(0, 4, "20%");

		event_table.setWidget(0, 5, new HTML("<strong>Item</strong>"));
		event_table.getCellFormatter().setWidth(0, 5, "100%");

		event_table.setWidget(0, 6, new HTML("<strong>Modify</strong>"));
		event_table.getCellFormatter().setWidth(0, 6, "100%");

		event_table.setWidget(0, 7, new HTML("<strong>Attendee</strong>"));
		event_table.getCellFormatter().setWidth(0, 7, "30%");

		event_table.setWidget(0, 8, new HTML("<strong>Event Info</strong>"));
		event_table.getCellFormatter().setWidth(0, 8, "20%");

		event_table.setWidget(0, 9, new HTML("<strong>Delete</strong>"));
		event_table.getCellFormatter().setWidth(0, 9, "20%");
		
		
		EventTableRefresh(event_table);
		
		event_add.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				event_pop_up(0, 0, event_table);
			}
		});

		eventService.getEventsByOrganizerIdAndStatus(organizerId, Event.ACTIVE, new AsyncCallback<List<Event>>() {
			public void onFailure(Throwable caught)
			{
				
			}
			public void onSuccess(List<Event> result)
			{
				for (int i=1;i<=result.size();i++)
					event_table.getCellFormatter().setVisible(i, 0, false);
			}
		});
		
		
		
		event_right_v_panel.add(event_table);
		mother.add(event_right_v_panel);
	}
	public void AddDeleteEventPanel(HorizontalPanel m)
	{
		final HorizontalPanel mother = m;
		VerticalPanel panel = new VerticalPanel();
		
		final FlexTable eventTable = new FlexTable();
		eventTable.setSize("1100px", "100%");
		eventTable.setWidget(0, 0, new HTML("<strong>Event ID</strong>"));
		eventTable.getCellFormatter().setVisible(0, 0, false);
		eventTable.setWidget(0, 1, new HTML("<strong>Event Title</strong>"));
		eventTable.getCellFormatter().setWidth(0, 1, "20%");
		eventTable.setWidget(0, 2, new HTML("<strong>Start Date</strong>"));
		eventTable.getCellFormatter().setWidth(0, 2, "20%");
		eventTable.setWidget(0, 3, new HTML("<strong>End Date</strong>"));
		eventTable.getCellFormatter().setWidth(0, 3, "20%");

		eventTable.setWidget(0, 4, new HTML("<strong>View Info</strong>"));
		eventTable.getCellFormatter().setWidth(0, 4, "20%");
		
		eventTable.setWidget(0, 5, new HTML("<strong>Restore Info</strong>"));
		eventTable.getCellFormatter().setWidth(0, 5, "20%");
		
		eventTable.setWidget(0, 6, new HTML("<strong>Delete</strong>"));
		eventTable.getCellFormatter().setWidth(0, 6, "20%");
		
		eventService.getEventsByOrganizerIdAndStatus(organizerId, Event.INACTIVE, new AsyncCallback<List<Event>>() {
			public void onFailure(Throwable caught)
			{
				
			}
			public void onSuccess(List<Event> result)
			{
				int i=0;
				for (Event e: result)
				{
					i++;
					eventTable.setWidget(i, 0, new Label(e.getEventId()));
					eventTable.setWidget(i, 1, new Label(e.getName()));
					eventTable.setWidget(i, 2, new Label(e.getStartTime().toString()));
					eventTable.setWidget(i, 3, new Label(e.getEndTime().toString()));
					
					final Button view_info = new Button("View All");
					view_info.setWidth("100px");
					eventTable.setWidget(i, 4, view_info);

					final Button event_del = new Button("Delete");
					eventTable.setWidget(i, 6, event_del);
					
					final Button restore = new Button("Restore");
					eventTable.setWidget(i, 5, restore);
					
					
					view_info.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							int row;
							int total_event = eventTable.getRowCount();
							for (row = 1; row <= total_event; row++)
								if (eventTable.getWidget(row, 4).equals(view_info))
									break;
							String event_id = eventTable.getText(row, 0);
							String event_title = eventTable.getText(row, 1);
							String startDate = eventTable.getText(row,2);
							String endDate = eventTable.getText(row,3);
							String location = eventTable.getText(row,4);
							view_info_pop_up(event_id, event_title, startDate, endDate, location);
						}
					});

					event_del.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							int row;
							int total = eventTable.getRowCount();
							for (row = 1; row <= total; row++) {
								if (eventTable.getWidget(row, 6).equals(event_del))
									break;
							}
							final int r = row;
							String eventId = eventTable.getText(row, 0);
							eventService.changeEventStatusByEventId(eventId, Event.TRASHED, new AsyncCallback<Void>() {
								public void onFailure(Throwable caught)
								{
									
								}
								public void onSuccess(Void reslt)
								{
									eventTable.removeRow(r);
								}
							});
						}
					});
					
					restore.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							int r;
							int total = eventTable.getRowCount();
							for (r=1;r<=total;r++)
							{
								if (eventTable.getWidget(r, 5).equals(restore))
									break;
							}
							String eventId = eventTable.getText(r, 0);
							final int temp = r;
							eventService.changeEventStatusByEventId(eventId, Event.ACTIVE, new AsyncCallback<Void>(){
								public void onFailure(Throwable caught)
								{
									
								}
								public void onSuccess(Void result)
								{
									eventTable.removeRow(temp);
								}
							});
							
							
						}
					});
				}
			}
		});

		eventService.getEventsByOrganizerIdAndStatus(organizerId, Event.INACTIVE, new AsyncCallback<List<Event>>() {
			public void onFailure(Throwable caught)
			{
				
			}
			public void onSuccess(List<Event> result)
			{
				for (int i=1;i<=result.size();i++)
					eventTable.getCellFormatter().setVisible(i, 0, false);
			}
		});
		panel.add(eventTable);
		mother.add(panel);
		
	}
	public void item_pop_up(String eventId) {
		final DialogBox d = new DialogBox();
		//d.setSize("300px", "100%");
		VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		Button close = new Button("Close");
		panel.add(close);

		final FlexTable itemList = new FlexTable();
		itemList.setWidth("100%");
		itemList.setWidget(0, 0, new HTML("<strong>Category</strong>"));
		itemList.getColumnFormatter().setWidth(0, "10%");
		itemList.setWidget(0, 1, new HTML("<strong>Service</strong>"));
		itemList.getColumnFormatter().setWidth(1, "10%");
//		itemList.setWidget(0, 2, new HTML("<strong>View Service Details</strong>"));
//		itemList.getColumnFormatter().setWidth(2, "10%");
		itemList.setWidget(0, 4, new HTML("<strong>Due Date</strong>"));
		itemList.getColumnFormatter().setWidth(4, "20%");
		itemList.setWidget(0, 3, new HTML("<strong>Quantity</strong>"));
		itemList.getColumnFormatter().setWidth(3, "20%");
		itemList.setWidget(0, 5, new HTML("<strong>Request Details</strong>"));
		itemList.getColumnFormatter().setWidth(5, "20%");
		
		//sdfasdfasdfasdfasdfasdfasdf
		final ArrayList<Category> categoryList = new ArrayList<Category>();
		final ArrayList<Service> serviceList = new ArrayList<Service>();
		
		
		final ListBox category = new ListBox();

		categoryService
				.getAllCategory(new AsyncCallback<ArrayList<Category>>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(ArrayList<Category> result) {
						for (Category c : result)
						{
							categoryList.add(c);
							category.addItem(c.getName());
						}
					}
				});
		

		final ListBox service = new ListBox();
		category.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				service.clear();
				int total = category.getItemCount();
				for (int i=0;i<total;i++)
				{
					if (category.isItemSelected(i))
					{
						eventService.getServicesByCategoryId(categoryList.get(i).getCategoryId(), new AsyncCallback<List<Service>>(){
							public void onFailure(Throwable caught)
							{}
							public void onSuccess(List<Service> result)
							{
								for (Service s: result)
								{
									serviceList.add(s);
									service.addItem(s.getName());
								}
							}
						});
						break;
					}
				}
			}
		});	
		
		
		final TextBox dueDate = new TextBox();
		dueDate.setText("");
		dueDate.setWidth("70px");
		
		final TextArea details = new TextArea();
		details.setText("");
		details.setWidth("100px");
		
		final TextBox quantity = new TextBox();
		quantity.setText("");
		quantity.setWidth("20px");
		
		
		itemList.setWidget(1, 0, category);
		itemList.setWidget(1, 1, service);
		itemList.setWidget(1, 3, quantity);
		itemList.setWidget(1, 4, dueDate);
		itemList.setWidget(1, 5, details);
		
		
		panel.add(itemList);
		//final Button addNew = new Button("Add New");
		final Button update = new Button("Update");
		//HorizontalPanel p = new HorizontalPanel();
		//p.add(addNew);
		//p.add(update);
		panel.add(update);
		d.add(panel);
		d.setAnimationEnabled(true);
		d.center();
		d.show();
		
		
		
		
		update.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				
				d.hide();
			}
		});

		close.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				d.hide();
			}
		});
	}

	public void event_pop_up(int f, int r, FlexTable e) {
		final int flag = f;
		final int row = r;
		final FlexTable event_table = e;

		final String event_id = event_table.getText(row, 0);

		final DialogBox d = new DialogBox();

		VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);

		FlexTable table = new FlexTable();
		table.setWidget(0, 0, new Label("Event Title:"));
		final TextBox event_title = new TextBox();
		table.setWidget(0, 1, event_title);

		table.getColumnFormatter().setWidth(0, "100px");
		table.getColumnFormatter().setWidth(1, "150px");

		table.setWidget(1, 0, new Label("Start Date:"));
		final DatePicker start_date = new DatePicker();
		table.setWidget(1, 1, start_date);

		table.setWidget(2, 0, new Label("End Date:"));
		final DatePicker end_date = new DatePicker();
		table.setWidget(2, 1, end_date);

		table.setWidget(3, 0, new Label("Location:"));
		final TextArea location = new TextArea();
		table.setWidget(3, 1, location);

		if (flag == 1) {
			event_title.setText(event_table.getText(row, 1));
			// event_fee.setText(event_table.getText(row, 1));
			start_date.setValue(new Date());
			end_date.setValue(new Date());
			location.setText(event_table.getText(row, 4));
		}

		// for debug purpose, since without initilizing values, it will throw
		// exception
		else if (flag == 0) {
			String t = "";
			event_title.setText(t);
			// event_fee.setText(t);
			start_date.setValue(new Date());
			end_date.setValue(new Date());
			location.setText(t);

		}

		Button close_d = new Button("Close");
		Button save_d = new Button("Save");

		panel.add(close_d);
		panel.add(table);
		panel.add(save_d);

		d.setWidget(panel);
		d.setAnimationEnabled(true);
		d.center();
		d.show();

		// hp=new HorizontalPanel();

		close_d.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				d.hide();

			}
		});
		save_d.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				d.hide();
				if (flag == 0) {

					eventService.createEvent(organizerId,
							event_title.getText(), start_date.getValue(),end_date.getValue(), location.getText(),
							new AsyncCallback<Void>() {
								public void onFailure(Throwable caught) {

								}

								public void onSuccess(Void result) {
									EventTableRefresh(event_table);
								}
							});

				} else if (flag == 1) {

					eventService.updateEventByEventId(event_id, event_title.getText(), start_date.getValue(), end_date.getValue(), location.getText(), Event.ACTIVE,
							new AsyncCallback<Void>() {
								public void onFailure(Throwable caught) {
								}

								public void onSuccess(Void result) {
									EventTableRefresh(event_table);
								}
							});

				}
				 //RootPanel.get("XiaYuan").setVisible(false);
				 //RootPanel.get("XiaYuan").setVisible(true);
			}
		});
	}

	public void attendee_pop_up(String event_id) {
		// set cookie for organizer id and event id
		
		AttendeeManager.edittingAttendees(event_id, "YuanXia");
		RootPanel.get("XiaYuan").setVisible(false);
		RootPanel.get("XuXuan").setVisible(true);

	}

	public void view_info_pop_up(String eventId, String eventTitle,
			String startDate, String endDate, String location) {
		final DialogBox d = new DialogBox();

		VerticalPanel panel = new VerticalPanel();
		String panelTitle = "<strong><big>Event Information:</big></strong>";
		panel.add(new HTML(panelTitle));


		final FlexTable attendList = new FlexTable();
		attendList.setWidth("100%");

		// get all attendee by eventId
		attendeeService.getAttendeeListByEventId(eventId, new AsyncCallback<ArrayList<Attendee>>() {
			public void onFailure(Throwable caught)
			{
				
			}
			public void onSuccess(ArrayList<Attendee> result)
			{
				int i=-1;
				for (Attendee a : result)
				{
					i++;
					attendList.setWidget(i, 0, new Label(a.getName()));
					attendList.setWidget(i, 1, new Label(a.getEmail()));
				}
			}
		});
	
		attendList.setBorderWidth(1);
		
		final FlexTable itemList = new FlexTable();
		itemList.setWidth("100%");
		
		
		eventService.getServiceRequestsByEventId(eventId, new AsyncCallback<List<ServiceRequest>>() {
			public void onFailure(Throwable caught)
			{}
			public void onSuccess(List<ServiceRequest> result)
			{
				int i=0;
				for (ServiceRequest s : result)
				{
					itemList.setWidget(i, 0, new Label(s.getName()));
					itemList.setWidget(i, 1, new Label(s.getDueDate().toString()));
					itemList.setWidget(i, 2, new Label(s.getQuantity().toString()));
					String status;
					if (s.getStatus() == ServiceRequest.ACCEPTED)
						status = "Accepted";
					else if(s.getStatus() == ServiceRequest.PENDING)
						status = "Pending";
					else
						status = "Igored";
					itemList.setWidget(i, 3, new Label(status));
				}
			}
		});
		itemList.setBorderWidth(1);

		FlexTable table = new FlexTable();
		table.setWidget(0, 0, new HTML("<strong>Event Title:</strong>"));
		table.setWidget(0, 1, new Label(eventTitle));

		table.getColumnFormatter().setWidth(0, "100px");
		table.getColumnFormatter().setWidth(1, "150px");

		table.setWidget(1, 0, new HTML("<strong>Start Date:</strong>"));
		table.setWidget(1, 1, new Label(startDate));

		table.setWidget(2, 0, new HTML("<strong>End Date:</strong>"));
		table.setWidget(2, 1, new Label(endDate));

		table.setWidget(3, 0, new HTML("<strong>Location:</strong>"));
		table.setWidget(3, 1, new Label(location));

		panel.add(table);
		panel.add(new HTML("<strong>Attendee List:</strong>"));
		panel.add(attendList);
		panel.add(new HTML("<strong>Requested Services</strong>"));
		panel.add(itemList);
		Button close = new Button("Close");
		panel.add(close);

		d.add(panel);
		d.setAnimationEnabled(true);
		d.center();
		d.show();

		close.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				d.hide();
			}
		});

	}

	public void budget_pop_up(int f, int r, FlexTable e) {
		final int flag = f;
		final int row = r;
		final FlexTable budget_table = e;
		final DialogBox d = new DialogBox();
		VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);

		Button add_row = new Button("Add Entry");

		FlexTable upper_table = new FlexTable();
		upper_table.setWidget(0, 0, new Label("Budget Name:"));
		final TextBox budget_name = new TextBox();
		final ListBox list = new ListBox();
		upper_table.setWidget(0, 1, budget_name);
		upper_table.setWidget(0, 2, add_row);

		upper_table.getColumnFormatter().setWidth(0, "100px");
		upper_table.getColumnFormatter().setWidth(1, "100px");
		upper_table.getColumnFormatter().setWidth(2, "100px");

		if (flag == 0) {
			upper_table.setWidget(1, 0, new Label("Event List:"));
			list.addItem("Event 1 From Server");
			list.addItem("Event 2 from Server");
			upper_table.setWidget(1, 1, list);
		}

		Button close_d = new Button("Close");
		panel.add(close_d);
		panel.add(upper_table);

		final FlexTable table = new FlexTable();

		table.setText(0, 0, "Description");
		table.getCellFormatter().setWidth(0, 0, "500px");
		table.setText(0, 1, "Category");
		table.getCellFormatter().setWidth(0, 1, "300px");
		table.setText(0, 2, "Amount");
		table.getCellFormatter().setWidth(0, 2, "100px");
		table.setText(0, 3, "Delete");
		table.getCellFormatter().setWidth(0, 3, "100px");


		Button save_d = new Button("Save");

		panel.add(table);
		panel.add(save_d);

		d.setWidget(panel);
		d.setAnimationEnabled(true);

		d.center();
		d.show();

		add_row.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final int row = table.getRowCount();
				table.setWidget(row, 0, new TextBox());
				ListBox lb_temp = new ListBox();
				lb_temp.addItem("1000");
				lb_temp.addItem("2000");
				table.setWidget(row, 1, lb_temp);
				TextBox tb_temp = new TextBox();
				tb_temp.setWidth("20px");
				table.setWidget(row, 2, tb_temp);
				final Button del = new Button("Delete");
				table.setWidget(row, 3, del);

				del.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						int temp_row = 1;
						int total_row = table.getRowCount();
						for (int i = 1; i < total_row; i++) {
							if (table.getWidget(i, 3).equals(del)) {
								temp_row = i;
								break;
							}
						}
						table.removeRow(temp_row);
					}
				});
			}
		});

		close_d.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				d.hide();
			}
		});
		save_d.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				d.hide();
				if (flag == 0) {
					final int row_temp = table.getRowCount() + 1;
					budget_table.setWidget(row_temp, 0, new Label(
							"New ID generated by Server"));
					budget_table.setWidget(row_temp, 1,
							new Label(budget_name.getText()));
					budget_table.setWidget(
							row_temp,
							2,
							new Label(list.getItemText(list.getSelectedIndex())));
					budget_table
							.setWidget(row_temp, 3, new Label("New budget"));

					final Button temp_mod = new Button("Modify");
					budget_table.setWidget(row_temp, 4, temp_mod);
					final Button temp_del = new Button("Delete");
					budget_table.setWidget(row_temp, 5, temp_del);
				} else if (flag == 1) {
					final int row_temp = row;
					budget_table.setWidget(row_temp, 1, new Label(budget_name.getText()));
					budget_table.setWidget(row_temp, 2,	new Label(list.getItemText(list.getSelectedIndex())));
					budget_table.setWidget(row_temp, 3, new Label("New budget"));
				}
				RootPanel.get("XiaYuan").clear();
				onModuleLoad();
				t_panel.selectTab(1);
			}
		});

	}

}