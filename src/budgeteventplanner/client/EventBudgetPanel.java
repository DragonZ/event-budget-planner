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
import budgeteventplanner.client.entity.Budget;
import budgeteventplanner.client.entity.BudgetItem;
import budgeteventplanner.client.entity.Category;
import budgeteventplanner.client.entity.Event;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class EventBudgetPanel extends Composite{

	public TabPanel t_panel;
	public String organizerId;

	public boolean categoryLoaded;
	public boolean pendingDelete = false;
	public int pendingDeleteTarget;

	private final CategoryServiceAsync categoryService = GWT
			.create(CategoryService.class);
	private final EventServiceAsync eventService = GWT
			.create(EventService.class);
	private final AttendeeServiceAsync attendeeService = GWT
			.create(AttendeeService.class);

	private final VendorServiceAsync vendorService = GWT
			.create(VendorService.class);
	private final BudgetServiceAsync budgetService = GWT
			.create(BudgetService.class);
	
	private Tree treeBudget;
	private Button modifyBudget;
	// private Button deleteBudget;
	private HorizontalPanel chartPanel;
	private HorizontalPanel buttonPanel;
	private Button draw1;
	private Button draw2;
	private Button draw3;


	private String sentBudgetID;

	private List<Event> tempEventList;
	private List<Category> tempCategoryList;
	

	public EventBudgetPanel(String usrID) {
		super();

		organizerId = usrID;

		// HorizontalSplitPanel event_h_panel=new HorizontalSplitPanel();
		final HorizontalPanel event_h_panel = new HorizontalPanel();
		event_h_panel.setBorderWidth(1);
		event_h_panel.setSize("100%", "500px");

		final VerticalPanel leftPanel = new VerticalPanel();
		Anchor activeLink = new Anchor("Active Events");
		Anchor deleteLink = new Anchor("Deleted Events");

		leftPanel.add(activeLink);
		leftPanel.add(deleteLink);
		leftPanel.setWidth("100px");
		event_h_panel.add(leftPanel);

		activeLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				event_h_panel.clear();
				event_h_panel.add(leftPanel);
				AddActiveEventPanel(event_h_panel);
				// event_h_panel.add(event_right_v_panel);
			}
		});
		deleteLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				event_h_panel.clear();
				event_h_panel.add(leftPanel);
				AddDeleteEventPanel(event_h_panel);

			}
		});

		HorizontalPanel budget_h_panel = new HorizontalPanel();
		budget_h_panel.setBorderWidth(1);
		budget_h_panel.setSize("100%", "500px");
		VerticalPanel budget_right_v_panel = new VerticalPanel();

		// Create a static tree and a container to hold it
		treeBudget = new Tree();
		treeBudget.setAnimationEnabled(true);
		treeBudget.ensureDebugId("cwTree-treeBudget");
		ScrollPanel treeBudgetWrapper = new ScrollPanel(treeBudget);
		treeBudgetWrapper.ensureDebugId("cwTree-treeBudget-Wrapper");
		treeBudgetWrapper.setWidth("300px");

		// Wrap the static tree in a DecoratorPanel
		DecoratorPanel staticDecorator = new DecoratorPanel();
		staticDecorator.setWidget(treeBudgetWrapper);
		final TreeItem budgetName = treeBudget.addItem("Budget Name");
		

		budgetService.getBudgetByOrganizerId(organizerId,
				new AsyncCallback<List<Budget>>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(List<Budget> result) {
						for (Budget b : result) {

							TreeItem item = new TreeItem(b.getName());
							item.setTitle(b.getBudgetId());
							budgetName.addItem(item);

						}
						
					}
				});

		treeBudget.addSelectionHandler(new SelectionHandler<TreeItem>() {
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem tmp = event.getSelectedItem();
				sentBudgetID = tmp.getTitle();
				//Window.alert(sentBudgetID);
			}
		});

		HorizontalPanel budget_left_v_panel_h_button_panel = new HorizontalPanel();

		FlexTable buttonTable = new FlexTable();

		Button budget_add = new Button("Add");
		buttonTable.setWidget(0, 0, budget_add);
		Button budget_delete = new Button("Delete");
		buttonTable.setWidget(0, 2, budget_delete);
		budget_left_v_panel_h_button_panel.add(budget_add);

		buttonTable.getColumnFormatter().setWidth(0, "100px");
		buttonTable.getColumnFormatter().setWidth(1, "100px");
		buttonTable.getColumnFormatter().setWidth(2, "10px");

		budget_left_v_panel_h_button_panel.add(buttonTable);

		budget_right_v_panel.add(budget_left_v_panel_h_button_panel);
		budget_right_v_panel.add(staticDecorator);

		budget_h_panel.add(budget_right_v_panel);

		DockPanel chartField = new DockPanel();
		buttonPanel = new HorizontalPanel();
		chartPanel = new HorizontalPanel();
		modifyBudget = new Button("Add Item");
		// deleteBudget = new Button("Delete");
		draw1 = new Button("Draw_1");
		draw2 = new Button("Draw_2");
		draw3 = new Button("Draw_3");
		buttonPanel.add(modifyBudget);
		// buttonPanel.add(deleteBudget);
		buttonPanel.add(draw1);
		buttonPanel.add(draw2);
		buttonPanel.add(draw3);
		chartField.add(buttonPanel, DockPanel.NORTH);
		chartField.add(chartPanel, DockPanel.CENTER);

		Label tmp = new Label("TODO for Budget");
		tmp.setWidth("1200px");
		chartPanel.add(tmp);

		budget_h_panel.add(chartField);

		budget_add.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				budget_pop_up();
			}
		});

		budget_delete.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				budget_delete();
			}
		});
		
		modifyBudget.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				budget_addItem();
			}
		});
		
		
		
		t_panel = new TabPanel();
		t_panel.add(event_h_panel, "Event");
		t_panel.add(budget_h_panel, "Budget");
		//RootPanel.get("XiaYuan").add(t_panel);
		t_panel.setSize("100%", "100%");

		t_panel.selectTab(0);
//		this.add(t_panel);
		initWidget(t_panel);

	}

	public void refreshBudgetTree() {
		budgetService.getBudgetByOrganizerId(organizerId,
				new AsyncCallback<List<Budget>>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(List<Budget> result) {
						treeBudget.clear();
						final TreeItem budgetName = treeBudget.addItem("Budget Name");
						for (Budget b : result) {
							TreeItem item = new TreeItem(b.getName());
							item.setTitle(b.getBudgetId());
							budgetName.addItem(item);
						}
						budgetName.setState(true);
					}
				});

		treeBudget.addSelectionHandler(new SelectionHandler<TreeItem>() {
			public void onSelection(SelectionEvent<TreeItem> event) {
				//Window.alert(event.getSelectedItem().getTitle());
			}
		});
	}

	public void EventTableRefresh(FlexTable table) {
		final FlexTable event_table = table;

		eventService.getEventsByOrganizerIdAndStatus(organizerId, Event.ACTIVE,
				new AsyncCallback<List<Event>>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(List<Event> result) {
						int i = 0;
						for (Event e : result) {
							i++;
							event_table.setWidget(i, 0,
									new Label(e.getEventId()));
							event_table.getCellFormatter().setVisible(i, 0,
									false);
							event_table.setWidget(i, 1, new Label(e.getName()));
							event_table.setWidget(i, 2, new Label(e
									.getStartTime().toString()));
							event_table.setWidget(i, 3, new Label(e
									.getEndTime().toString()));
							event_table.setWidget(i, 4,
									new Label(e.getAddress()));

							final Anchor itemMod = new Anchor("New Service Request");
							//itemMod.setWidth("100px");
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

							final Anchor event_mod = new Anchor("Event Modify");
							event_mod.setWidth("100px");
							event_table.setWidget(i, 6, event_mod);

							final Anchor attend_list = new Anchor(
									"Attendee Modify");
							attend_list.setWidth("120px");
							event_table.setWidget(i, 7, attend_list);

							final Anchor view_info = new Anchor("View All");
							view_info.setWidth("100px");
							event_table.setWidget(i, 8, view_info);

							final Anchor event_del = new Anchor("Delete");
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
									String event_name = event_table.getText(
											row, 1);
									attendee_pop_up(event_id, event_name);
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
									String eventId = event_table
											.getText(row, 0);
									eventService.changeEventStatusByEventId(
											eventId, Event.INACTIVE,
											new AsyncCallback<Void>() {
												public void onFailure(
														Throwable caught) {

												}

												public void onSuccess(Void reslt) {
													event_table.removeRow(r);
												}
											});
								}
							});

						}
					}
				});

	}

	public void AddActiveEventPanel(HorizontalPanel m) {
		final HorizontalPanel mother = m;

		final VerticalPanel event_right_v_panel = new VerticalPanel();
		Button event_add = new Button("Add");
		event_right_v_panel.add(event_add);

		final FlexTable event_table = new FlexTable();
		event_table.setSize("1100px", "100%");
		event_table.setWidget(0, 0, new HTML("<strong>Event ID</strong>"));
		// event_table.getCellFormatter().setWidth(0, 0, "0px");
		event_table.getCellFormatter().setVisible(0, 0, false);
		event_table.setWidget(0, 1, new HTML("<strong>Event Title</strong>"));
		event_table.getCellFormatter().setWidth(0, 1, "20%");
		event_table.setWidget(0, 2, new HTML("<strong>Start Date</strong>"));
		event_table.getCellFormatter().setWidth(0, 2, "20%");
		event_table.setWidget(0, 3, new HTML("<strong>End Date</strong>"));
		event_table.getCellFormatter().setWidth(0, 3, "20%");
		event_table.setWidget(0, 4, new HTML("<strong>Location</strong>"));
		event_table.getCellFormatter().setWidth(0, 4, "20%");

		event_table.setWidget(0, 5, new HTML("<strong>Service</strong>"));
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

		eventService.getEventsByOrganizerIdAndStatus(organizerId, Event.ACTIVE,
				new AsyncCallback<List<Event>>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(List<Event> result) {
						for (int i = 1; i <= result.size(); i++)
							event_table.getCellFormatter().setVisible(i, 0,
									false);
					}
				});

		event_right_v_panel.add(event_table);
		mother.add(event_right_v_panel);
	}

	public void AddDeleteEventPanel(HorizontalPanel m) {
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

		eventService.getEventsByOrganizerIdAndStatus(organizerId,
				Event.INACTIVE, new AsyncCallback<List<Event>>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(List<Event> result) {
						int i = 0;
						for (Event e : result) {
							i++;
							eventTable.setWidget(i, 0,
									new Label(e.getEventId()));
							eventTable.setWidget(i, 1, new Label(e.getName()));
							eventTable.setWidget(i, 2, new Label(e
									.getStartTime().toString()));
							eventTable.setWidget(i, 3, new Label(e.getEndTime()
									.toString()));

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
										if (eventTable.getWidget(row, 4)
												.equals(view_info))
											break;
									String event_id = eventTable
											.getText(row, 0);
									String event_title = eventTable.getText(
											row, 1);
									String startDate = eventTable.getText(row,
											2);
									String endDate = eventTable.getText(row, 3);
									String location = eventTable
											.getText(row, 4);
									view_info_pop_up(event_id, event_title,
											startDate, endDate, location);
								}
							});

							event_del.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									int row;
									int total = eventTable.getRowCount();
									for (row = 1; row <= total; row++) {
										if (eventTable.getWidget(row, 6)
												.equals(event_del))
											break;
									}
									final int r = row;
									String eventId = eventTable.getText(row, 0);
									eventService.changeEventStatusByEventId(
											eventId, Event.TRASHED,
											new AsyncCallback<Void>() {
												public void onFailure(
														Throwable caught) {

												}

												public void onSuccess(Void reslt) {
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
									for (r = 1; r <= total; r++) {
										if (eventTable.getWidget(r, 5).equals(
												restore))
											break;
									}
									String eventId = eventTable.getText(r, 0);
									final int temp = r;
									eventService.changeEventStatusByEventId(
											eventId, Event.ACTIVE,
											new AsyncCallback<Void>() {
												public void onFailure(
														Throwable caught) {

												}

												public void onSuccess(
														Void result) {
													eventTable.removeRow(temp);
												}
											});

								}
							});
						}
					}
				});

		eventService.getEventsByOrganizerIdAndStatus(organizerId,
				Event.INACTIVE, new AsyncCallback<List<Event>>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(List<Event> result) {
						for (int i = 1; i <= result.size(); i++)
							eventTable.getCellFormatter().setVisible(i, 0,
									false);
					}
				});
		panel.add(eventTable);
		mother.add(panel);

	}

	public void item_pop_up(String eId) {
		final String eventId = eId;
		final DialogBox d = new DialogBox();
		// d.setSize("300px", "100%");
		VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		Button close = new Button("Close");
		panel.add(close);

		final FlexTable itemList = new FlexTable();
		itemList.setWidth("100%");
		itemList.setWidget(0, 0, new HTML("<strong>Name: </strong>"));
		itemList.setWidget(1, 0, new HTML("<strong>Category: </strong>"));
		itemList.getColumnFormatter().setWidth(0, "40%");
		itemList.setWidget(2, 0, new HTML("<strong>Service: </strong>"));
		// itemList.setWidget(0, 2, new
		// HTML("<strong>View Service Details</strong>"));
		// itemList.getColumnFormatter().setWidth(2, "10%");
		itemList.setWidget(3, 0, new HTML("<strong>Service Price: </strong>"));
		itemList.setWidget(4, 0, new HTML("<strong>Quantity: </strong>"));
		itemList.setWidget(5, 0, new HTML("<strong>Due Date: </strong>"));
		//itemList.setWidget(6, 0, new HTML("<strong>Request Details: </strong>"));

		// sdfasdfasdfasdfasdfasdfasdf
		final ArrayList<Category> categoryList = new ArrayList<Category>();
		final ArrayList<Service> serviceList = new ArrayList<Service>();

		final ListBox category = new ListBox();
		category.setWidth("100px");
		categoryService
				.getAllCategory(new AsyncCallback<List<Category>>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(List<Category> result) {
						for (Category c : result) {
							categoryList.add(c);
							category.addItem(c.getName());
						}
						categoryLoaded = true;
					}
				});

		final ListBox service = new ListBox();
		service.setWidth("100px");
		category.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				service.clear();
				int i = category.getSelectedIndex();
				while (true) {
					if (categoryLoaded == true) {
						eventService.getServicesByCategoryId(categoryList
								.get(i).getCategoryId(),
								new AsyncCallback<List<Service>>() {
									public void onFailure(Throwable caught) {
									}

									public void onSuccess(List<Service> result) {
										serviceList.clear();
										for (Service s : result) {
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

		final Label price = new Label("");

		service.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				int i = service.getSelectedIndex();
				price.setText(serviceList.get(i).getPrice().toString());
			}
		});
		final TextBox name = new TextBox();
		name.setText("");
		name.setWidth("100px");

		final TextBox quantity = new TextBox();
		quantity.setText("");
		quantity.setWidth("100px");

		final TextBox dueDate = new TextBox();
		dueDate.setText("");
		dueDate.setWidth("100px");

		itemList.setWidget(0, 1, name);
		itemList.setWidget(1, 1, category);
		itemList.setWidget(2, 1, service);
		itemList.setWidget(3, 1, price);
		itemList.setWidget(4, 1, quantity);
		itemList.setWidget(5, 1, dueDate);

		panel.add(itemList);
		// final Button addNew = new Button("Add New");
		final Button update = new Button("Update");
		// HorizontalPanel p = new HorizontalPanel();
		// p.add(addNew);
		// p.add(update);
		panel.add(update);
		d.add(panel);
		d.setAnimationEnabled(true);
		d.center();
		d.show();

		update.addClickHandler(new ClickHandler() {
			@SuppressWarnings("deprecation")
			public void onClick(ClickEvent event) {
				int i = service.getSelectedIndex();
				eventService.addServiceRequest(serviceList.get(i)
						.getServiceId(), eventId, name.getText(), Integer
						.parseInt(quantity.getText()),
						new Date(dueDate.getText()), new AsyncCallback<Void>() {
							public void onFailure(Throwable caught) {
							}

							public void onSuccess(Void result) {
								d.hide();
							}
						});
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
							event_title.getText(), start_date.getValue(),
							end_date.getValue(), location.getText(),
							new AsyncCallback<Void>() {
								public void onFailure(Throwable caught) {

								}

								public void onSuccess(Void result) {
									EventTableRefresh(event_table);
								}
							});

				} else if (flag == 1) {

					eventService.updateEventByEventId(event_id,
							event_title.getText(), start_date.getValue(),
							end_date.getValue(), location.getText(),
							Event.ACTIVE, new AsyncCallback<Void>() {
								public void onFailure(Throwable caught) {
								}

								public void onSuccess(Void result) {
									EventTableRefresh(event_table);
								}
							});

				}
				// RootPanel.get("XiaYuan").setVisible(false);
				// RootPanel.get("XiaYuan").setVisible(true);
			}
		});
	}

	public void attendee_pop_up(String event_id, String event_name) {
		RootPanel.get("XuXuan").clear();
		RootPanel.get("XuXuan").add(new AttendeeManagementPanel(event_name, event_id, "YuanXia"));
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
		attendeeService.getAttendeeListByEventId(eventId,
				new AsyncCallback<List<Attendee>>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(List<Attendee> result) {
						int i = -1;
						for (Attendee a : result) {
							i++;
							attendList.setWidget(i, 0, new Label(a.getName()));
							attendList.setWidget(i, 1, new Label(a.getEmail()));
						}
					}
				});

		attendList.setBorderWidth(1);

		final FlexTable itemList = new FlexTable();
		itemList.setWidth("100%");

		final ArrayList<ServiceRequest> serviceList = new ArrayList<ServiceRequest>();
		eventService.getServiceRequestsByEventId(eventId,
				new AsyncCallback<List<ServiceRequest>>() {
					public void onFailure(Throwable caught) {
					}

					public void onSuccess(List<ServiceRequest> result) {
						int i = 0;
						for (ServiceRequest sr : result) {
							final ServiceRequest s = sr;
							serviceList.add(s);
							itemList.setWidget(i, 0, new Label(s.getName()));
							itemList.setWidget(i, 1, new Label(s.getDueDate()
									.toString()));
							itemList.setWidget(i, 2, new Label(s.getQuantity()
									.toString()));
							String status;
							if (s.getStatus() == ServiceRequest.ACCEPTED) {
								status = "Accepted";
								itemList.setWidget(i, 3, new Label(status));
							} else if (s.getStatus() == ServiceRequest.PENDING) {
								status = "Pending";
								Anchor delete = new Anchor(status);
								itemList.setWidget(i, 3, delete);

								delete.addClickHandler(new ClickHandler() {

									@Override
									public void onClick(ClickEvent event) {
										vendorService.updateServiceRequestStatus(
												s.getRequestId(),
												ServiceRequest.IGNORED,
												new AsyncCallback<Void>() {
													public void onFailure(
															Throwable caught) {
													}

													public void onSuccess(
															Void result) {
														d.hide();
													}
												});
									}
								});
							} else {
								status = "Igored";
								itemList.setWidget(i, 3, new Label(status));
							}

							i++;
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

		if (pendingDelete == true) {
			vendorService.updateServiceRequestStatus(
					serviceList.get(pendingDeleteTarget).getRequestId(),
					ServiceRequest.IGNORED, new AsyncCallback<Void>() {
						public void onFailure(Throwable caught) {
						}

						public void onSuccess(Void result) {
							d.hide();
						}
					});
		}

		close.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				d.hide();
			}
		});

	}

	public void budget_pop_up()
    {
       final DialogBox d = new DialogBox();
       VerticalPanel panel = new VerticalPanel();
       panel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
       
       FlexTable addBudgetTable = new FlexTable();
       addBudgetTable.setWidget(0, 0, new Label("Budget Name:"));
       final TextBox budget_name = new TextBox();
       addBudgetTable.setWidget(0, 1, budget_name);
       addBudgetTable.setWidget(1, 0, new Label("Event List:"));
       final ListBox eventListBox = new ListBox();
       addBudgetTable.setWidget(1, 1, eventListBox);
       
		tempEventList = new ArrayList<Event>();
       
		eventService
		.getEventsByOrganizerIdAndStatus(organizerId, 0,
				new AsyncCallback<List<Event>>() {
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(List<Event> result) {
				for (Event c : result)
				{
					eventListBox.addItem(c.getName());
					tempEventList.add(c);
				}
			}
		});
       
       addBudgetTable.setWidget(2, 0, new Label("Total Limit:"));
       final TextBox budget_limit = new TextBox();
       addBudgetTable.setWidget(2, 1, budget_limit);
       
       Button addBudget = new Button("Add");
       addBudgetTable.setWidget(3, 0, addBudget);
       Button close_d = new Button("Close");
       addBudgetTable.setWidget(3, 2, close_d);
       
		addBudgetTable.getColumnFormatter().setWidth(0, "100px");
		addBudgetTable.getColumnFormatter().setWidth(1, "100px");
		addBudgetTable.getColumnFormatter().setWidth(2, "10px");
		
		panel.add(addBudgetTable);
		
		d.setWidget(panel);
		d.setAnimationEnabled(true);

		d.center();
		d.show();
		
		addBudget.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(budget_limit.getText().isEmpty()||budget_name.getText().isEmpty()){
					
				}
				else{
					budgetService.createBudget(tempEventList.get(eventListBox.getSelectedIndex()).getEventId(),
						budget_name.getText(), Double.parseDouble(budget_limit.getText()),
						new AsyncCallback<Budget>(){
						public void onFailure(Throwable caught) {
						}

						public void onSuccess(Budget result) {
							refreshBudgetTree();
						}
					});

					d.hide();
				}
			}
		});
		
		
		close_d.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				d.hide();
			}
		});
    }

	public void budget_delete() {
		refreshBudgetTree();
	}
	
	public void budget_addItem() {
	       final DialogBox d = new DialogBox();
	       VerticalPanel panel = new VerticalPanel();
	       panel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
	       
	       FlexTable addBudgetTable = new FlexTable();

	       addBudgetTable.setWidget(0, 0, new Label("Event List:"));
	       final ListBox categoryListBox = new ListBox();
	       addBudgetTable.setWidget(0, 1, categoryListBox);
	       
			tempCategoryList = new ArrayList<Category>();
	       
			categoryService
			.getAllCategory(
					new AsyncCallback<List<Category>>() {
				public void onFailure(Throwable caught) {

				}

				public void onSuccess(List<Category> result) {
					for (Category c : result)
					{
						categoryListBox.addItem(c.getName());
						tempCategoryList.add(c);
					}
				}
			});
	       
	       addBudgetTable.setWidget(1, 0, new Label("Set Limit:"));
	       final TextBox budget_limit = new TextBox();
	       budget_limit.setWidth("50px");
	       addBudgetTable.setWidget(1, 1, budget_limit);

	       Button addBudget = new Button("Add");
	       addBudget.setWidth("50px");
	       addBudgetTable.setWidget(2, 1, addBudget);
	       Button close_d = new Button("Close");
	       addBudgetTable.setWidget(2, 2, close_d);
	       
	       
			addBudgetTable.getColumnFormatter().setWidth(0, "100px");
			addBudgetTable.getColumnFormatter().setWidth(1, "50px");
			addBudgetTable.getColumnFormatter().setWidth(2, "50px");
			
			panel.add(addBudgetTable);
			
			d.setWidget(panel);
			d.setAnimationEnabled(true);

			d.center();
			d.show();
			
			addBudget.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(budget_limit.getText().isEmpty()){
					}
					else{
						budgetService
						.addBudgetItemToBudget(tempCategoryList.get(categoryListBox.getSelectedIndex()).getCategoryId(),
								sentBudgetID, Double.parseDouble(budget_limit.getText()),
								new AsyncCallback<BudgetItem>() {
							public void onFailure(Throwable caught) {

							}

							public void onSuccess(BudgetItem result) {
								
							}
						});
						d.hide();
					}
				}
			});
			
			
			close_d.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					d.hide();
				}
			});
	}

	// public void budget_pop_up(int f, int r, FlexTable e) {
	// final int flag = f;
	// final int row = r;
	// final FlexTable budget_table = e;
	// final DialogBox d = new DialogBox();
	// VerticalPanel panel = new VerticalPanel();
	// panel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
	//
	// Button add_row = new Button("Add Entry");
	//
	// FlexTable upper_table = new FlexTable();
	// upper_table.setWidget(0, 0, new Label("Budget Name:"));
	// final TextBox budget_name = new TextBox();
	// final ListBox list = new ListBox();
	// upper_table.setWidget(0, 1, budget_name);
	// upper_table.setWidget(0, 2, add_row);
	//
	// upper_table.getColumnFormatter().setWidth(0, "100px");
	// upper_table.getColumnFormatter().setWidth(1, "100px");
	// upper_table.getColumnFormatter().setWidth(2, "100px");
	//
	// if (flag == 0) {
	// upper_table.setWidget(1, 0, new Label("Event List:"));
	// list.addItem("Event 1 From Server");
	// list.addItem("Event 2 from Server");
	// upper_table.setWidget(1, 1, list);
	// }
	//
	// Button close_d = new Button("Close");
	// panel.add(close_d);
	// panel.add(upper_table);
	//
	// final FlexTable table = new FlexTable();
	//
	// table.setText(0, 0, "Description");
	// table.getCellFormatter().setWidth(0, 0, "500px");
	// table.setText(0, 1, "Category");
	// table.getCellFormatter().setWidth(0, 1, "300px");
	// table.setText(0, 2, "Amount");
	// table.getCellFormatter().setWidth(0, 2, "100px");
	// table.setText(0, 3, "Delete");
	// table.getCellFormatter().setWidth(0, 3, "100px");
	//
	//
	// Button save_d = new Button("Save");
	//
	// panel.add(table);
	// panel.add(save_d);
	//
	// d.setWidget(panel);
	// d.setAnimationEnabled(true);
	//
	// d.center();
	// d.show();
	//
	// add_row.addClickHandler(new ClickHandler() {
	// public void onClick(ClickEvent event) {
	// final int row = table.getRowCount();
	// table.setWidget(row, 0, new TextBox());
	// ListBox lb_temp = new ListBox();
	// lb_temp.addItem("1000");
	// lb_temp.addItem("2000");
	// table.setWidget(row, 1, lb_temp);
	// TextBox tb_temp = new TextBox();
	// tb_temp.setWidth("20px");
	// table.setWidget(row, 2, tb_temp);
	// final Button del = new Button("Delete");
	// table.setWidget(row, 3, del);
	//
	// del.addClickHandler(new ClickHandler() {
	// public void onClick(ClickEvent event) {
	// int temp_row = 1;
	// int total_row = table.getRowCount();
	// for (int i = 1; i < total_row; i++) {
	// if (table.getWidget(i, 3).equals(del)) {
	// temp_row = i;
	// break;
	// }
	// }
	// table.removeRow(temp_row);
	// }
	// });
	// }
	// });
	//
	// close_d.addClickHandler(new ClickHandler() {
	// public void onClick(ClickEvent event) {
	// d.hide();
	// }
	// });
	// save_d.addClickHandler(new ClickHandler() {
	// public void onClick(ClickEvent event) {
	// d.hide();
	// if (flag == 0) {
	// final int row_temp = table.getRowCount() + 1;
	// budget_table.setWidget(row_temp, 0, new Label(
	// "New ID generated by Server"));
	// budget_table.setWidget(row_temp, 1,
	// new Label(budget_name.getText()));
	// budget_table.setWidget(
	// row_temp,
	// 2,
	// new Label(list.getItemText(list.getSelectedIndex())));
	// budget_table
	// .setWidget(row_temp, 3, new Label("New budget"));
	//
	// final Button temp_mod = new Button("Modify");
	// budget_table.setWidget(row_temp, 4, temp_mod);
	// final Button temp_del = new Button("Delete");
	// budget_table.setWidget(row_temp, 5, temp_del);
	// } else if (flag == 1) {
	// final int row_temp = row;
	// budget_table.setWidget(row_temp, 1, new Label(budget_name.getText()));
	// budget_table.setWidget(row_temp, 2, new
	// Label(list.getItemText(list.getSelectedIndex())));
	// budget_table.setWidget(row_temp, 3, new Label("New budget"));
	// }
	// RootPanel.get("XiaYuan").clear();
	// onModuleLoad();
	// t_panel.selectTab(1);
	// }
	// });
	//
	// }

}