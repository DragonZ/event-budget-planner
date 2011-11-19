/**
 * 
 */
package budgeteventplanner.client;

	
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author lzhen
 *
 */
public class VendorHomePage implements EntryPoint {

	/* (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	TabPanel tab;
	
	DockPanel vendorPage;
	Tree eventFolders;
	TreeItem pendingEvent;
	TreeItem acceptedEvent;
	TreeItem ignoredEvent;
	FlexTable events;
	//TreeItem eventHistory;
	//CAN DO
	
	DockPanel vendorService;
	FlexTable existingService;
	HorizontalPanel addPanel;
	ListBox category;
	TextBox service;
	TextArea description;
	TextBox price;
	Button addService;
	
	Hyperlink acceptHyperlink[];
	Hyperlink ignoreHyperlink[];
	Hyperlink viewHyperlink[];
	
	Hyperlink deleteHyperlink[];
	
	@Override
	public void onModuleLoad() {
		
		tab = new TabPanel();			//the bottom tab panel for vendor page
		
		vendorPage = new DockPanel();		//vendor home page panel
		vendorService = new DockPanel();	//vendor service management page panel
		addPanel = new HorizontalPanel();	//nested panel in the service panel

		
		eventFolders = new Tree();			//vendor home page folders
		pendingEvent = new TreeItem("Pending Events");	//pending 
		acceptedEvent = new TreeItem("Accepted Events");//accepted
		ignoredEvent = new TreeItem("Ignored Events");	//ignored
		//eventHistory = new TreeItem("Events History");
		//CAN DO
		
		eventFolders.setWidth("150px");
		//size issue to be done
		eventFolders.addItem(pendingEvent);
		eventFolders.addItem(acceptedEvent);
		eventFolders.addItem(ignoredEvent);
		//eventFolders.addItem(eventHistory);
		//CAN DO
		
		
		events = new FlexTable();							//table for entries in a certain folder
		events.getColumnFormatter().setWidth(0, "100px");	
		events.getColumnFormatter().setWidth(1, "100px");
		events.getColumnFormatter().setWidth(3, "100px");
		//size issue to be done
		
		existingService = new FlexTable();					//table for service management page
		initializeServiceTable();							//new method to initialize the service table
		
		
		vendorPage.add(eventFolders, DockPanel.WEST);
		vendorPage.add(events, DockPanel.EAST);
		
		category = new ListBox();						//get category from server
		initializeCategory();
		 
		service = new TextBox();
		description = new TextArea();
		price = new TextBox();
	    //description.setCharacterWidth(80); optional size method
		description.setWidth("600px");
	    description.setVisibleLines(1);
		addService = new Button("ADD"); 
		addService.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				String sentCategory = category.getItemText(category.getSelectedIndex());
				String sentText = service.getText();
				String sentDescription = description.getText();
				double sentPrice = Double.parseDouble(price.getText());
				
				//add to server
				
				//refresh the table
				
				existingService.setWidget(6, 0, new Label(sentCategory));
				deleteHyperlink[5] = new Hyperlink("delete", Integer.toString(5));
				existingService.setWidget(6, 1, new Label(sentText));
				existingService.setWidget(6, 2, new Label(sentDescription));
				existingService.setWidget(6, 3, new Label(price.getText()));
				existingService.setWidget(6, 4, deleteHyperlink[5]);
			}
		});
		
		
		addPanel.add(category);
		addPanel.add(service);
		addPanel.add(description);
		addPanel.add(price);
		addPanel.add(addService);
		vendorService.add(existingService, DockPanel.CENTER);
		vendorService.add(addPanel, DockPanel.SOUTH);
		category.setSize("150px", "100%");
		addPanel.setCellWidth(category, "200px");
		service.setSize("300px", "100%");
		addPanel.setCellWidth(service, "350px");
		addPanel.setCellWidth(description, "650px");
		price.setWidth("100px");
		addPanel.setCellWidth(price, "150px");
		addService.setWidth("100px");
		addPanel.setCellWidth(addService, "150px");
		
		tab.add(vendorPage, "Vendor");
		tab.add(vendorService, "Service");
		RootPanel.get("ZhenLong").add(tab);
		//add operations
		
		eventFolders.addSelectionHandler(new treeHandler<TreeItem>());
		//add listeners
		
		tab.selectTab(0);
		tab.setSize("100%", "100%");
		//tab initialization
		
		
	}
	
	public void initializeServiceTable()
	{
		//get data from the service, list the existing services
		existingService.getColumnFormatter().setWidth(0, "200px");
		existingService.getColumnFormatter().setWidth(1, "350px");
		existingService.getColumnFormatter().setWidth(2, "650px");
		existingService.getColumnFormatter().setWidth(3, "150px");
		existingService.getColumnFormatter().setWidth(4, "150px");
		existingService.setWidget(0, 0, new Label("Category"));
		existingService.setWidget(0, 1, new Label("Service"));
		existingService.setWidget(0, 2, new Label("Description"));
		existingService.setWidget(0, 3, new Label("Price"));
		existingService.setWidget(0, 4, new Label("Option"));
		
		refreshExistingService();
	}
	
	@SuppressWarnings("deprecation")
	public void refreshExistingService()
	{
		existingService.clear();
		
		existingService.getColumnFormatter().setWidth(0, "200px");
		existingService.getColumnFormatter().setWidth(1, "350px");
		existingService.getColumnFormatter().setWidth(2, "650px");
		existingService.getColumnFormatter().setWidth(3, "150px");
		existingService.getColumnFormatter().setWidth(4, "150px");
		existingService.setWidget(0, 0, new Label("Category"));
		existingService.setWidget(0, 1, new Label("Service"));
		existingService.setWidget(0, 2, new Label("Description"));
		existingService.setWidget(0, 3, new Label("Price"));
		existingService.setWidget(0, 4, new Label("Option"));
		//List<Category> getExistingCategory(String VendorID);
		
		
		Date today = new Date();
		final Label[] a = new Label[11]; //TODO
		for(int i = 0; i < 11; i++)
			a[i] =  new Label(today.toString());
		
		
		deleteHyperlink = new Hyperlink[6];
		for(int i = 0; i < 5; i++)
		{
			existingService.setWidget(i+1, 0, new Label(Integer.toString(i)));
			deleteHyperlink[i] = new Hyperlink("delete", Integer.toString(i));
			existingService.setWidget(i+1, 1, new Label("lol idk the service name"));
			existingService.setWidget(i+1, 2, a[i]);
			existingService.setWidget(i+1, 3, new Label("10"));
			existingService.setWidget(i+1, 4, deleteHyperlink[i]);
			
			//listener add
			deleteHyperlink[i].addClickHandler(new ClickHandler()
			{
				public void onClick(ClickEvent event)
				{
					//void deleteExistingService(String ServiceID, String VendorID);
					refreshExistingService();
				}
			});
		}
	}
	
	public void initializeCategory()
	{
		//get information from server
		
		for(int i = 0; i < 7; i++)
		{
			category.addItem(Integer.toString(i));
		}
	}
	
	public class treeHandler<TreeItem> implements SelectionHandler<TreeItem>
	{
		@SuppressWarnings("deprecation")
		public void onSelection(SelectionEvent<TreeItem> event)
		{
			Object tmp = event.getSelectedItem();
			if(tmp == pendingEvent)
			{
				events.clear();
				//3 hyberlinks: view/accept/ignore
				//read from sever the number of entries
				acceptHyperlink = new Hyperlink[11];
				ignoreHyperlink = new Hyperlink[11];
				viewHyperlink = new Hyperlink[11];
				
				//----------------
				vendorPage.setBorderWidth(1);
				//border display
				
				Date today = new Date();
				//Event name
				
			    // prints Tue Dec 18 12:01:26 GMT-500 2007 in the default locale.
			    //GWT.log(today.toString(), null);
				final Label[] a = new Label[11]; //TODO
				for(int i = 0; i < 11; i++)
					a[i] =  new Label(today.toString());
				
				//a[0].setWidth("300px");
				events.setWidget(0, 0, new Label("Events"));
				events.setWidget(0, 3, new Label("Options"));
				//events.setText(0, 2, "Events");
				//events.setText(0, 3, "Events");
				for(int i = 0; i < 11; i++)
				{
					events.setWidget(i+1, 0, a[i]);
					events.getCellFormatter().setWidth(i, 0, "90%");
					viewHyperlink[i] = new Hyperlink("View", Integer.toString(i));
					ignoreHyperlink[i] = new Hyperlink("Ignore", Integer.toString(i));
					acceptHyperlink[i] = new Hyperlink("Accept", Integer.toString(i));
					events.setWidget(i+1, 1, viewHyperlink[i]);
					events.setWidget(i+1, 2, acceptHyperlink[i]);
					events.setWidget(i+1, 3, ignoreHyperlink[i]);
					
					
					//listener add
					viewHyperlink[i].addClickHandler(new ClickHandler()
					{
						public void onClick(ClickEvent event)
						{
							int i;
							for(i = 0; i < viewHyperlink.length; i++)
							{
								if(event.getSource() == viewHyperlink[i])
								{
									break;
								}
							}
							
							//pop up the current event info
							final DialogBox currentEvent = new DialogBox();
							//.setWidget(a[i]);
							currentEvent.setAnimationEnabled(true);
							final Button closeButton = new Button("Close");
							closeButton.getElement().setId("closeButton");
							//final Label textToServerLabel = new Label();
							//final HTML serverResponseLabel = new HTML();
							VerticalPanel dialogVPanel = new VerticalPanel();
							dialogVPanel.addStyleName("dialogVPanel");
							dialogVPanel.add(new Label(a[i].getText()));
							//dialogVPanel.add(textToServerLabel);
							//dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
							//dialogVPanel.add(serverResponseLabel);
							dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
							dialogVPanel.add(closeButton);
							currentEvent.setWidget(dialogVPanel);

							// Add a handler to close the DialogBox
							closeButton.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									currentEvent.hide();
									//sendButton.setEnabled(true);
									//sendButton.setFocus(true);
								}
							});
							
							currentEvent.center();
							
						}
					});  //view listener
					
					acceptHyperlink[i].addClickHandler(new ClickHandler()
					{
						public void onClick(ClickEvent event)
						{
							int i;
							for(i = 0; i < events.getRowCount(); i++)
							{
								if(event.getSource().equals(events.getWidget(i, 2)))
								{
									break;
								}
							}// do we need this loop? one listener only detect one hyper;ink
							
							//move to accepted folder
							//remove from pending folder
							//communication to server, inform the event manager
							//re-get the list and refresh
							events.removeRow(i);
						}
					}); //accept listener
					
					ignoreHyperlink[i].addClickHandler(new ClickHandler()
					{
						public void onClick(ClickEvent event)
						{
							int i;
							for(i = 0; i < events.getRowCount(); i++)
							{
								if(event.getSource().equals(events.getWidget(i, 3)))
								{
									break;
								}
							}// do we need this loop? one listener only detect one hyper;ink
							
							//move to accepted folder
							//remove from pending folder
							//communication to server, inform the event manager
							//re-get the list and refresh
							events.removeRow(i);
						}
					});
					
					
				}
				//hyberlinks listeners
				//----------------
			}
			else if(tmp == acceptedEvent)
			{
				events.clear();
				//2 buttons: view/ignore
				//3 hyberlinks: view/accept/ignore
				//read from sever the number of entries
				//acceptHyperlink = new Hyperlink[3];
				ignoreHyperlink = new Hyperlink[3];
				viewHyperlink = new Hyperlink[3];
				
				//----------------
				vendorPage.setBorderWidth(1);
				//border display
				
				Date today = new Date();
				//Event name
				
			    // prints Tue Dec 18 12:01:26 GMT-500 2007 in the default locale.
			    //GWT.log(today.toString(), null);
				final Label[] a = new Label[3];
				for(int i = 0; i < 3; i++)
					a[i] =  new Label(today.toString());
				
				events.setWidget(0, 0, new Label("Events"));
				events.setWidget(0, 2, new Label("Options"));
				//events.setText(0, 2, "Events");
				//events.setText(0, 3, "Events");
				for(int i = 0; i < 3; i++)
				{
					events.setWidget(i+1, 0, a[i]);
					events.getCellFormatter().setWidth(i, 0, "90%");
					viewHyperlink[i] = new Hyperlink("View", Integer.toString(i));
					ignoreHyperlink[i] = new Hyperlink("Ignore", Integer.toString(i));
					//acceptHyperlink[i] = new Hyperlink("Accept", "");
					events.setWidget(i+1, 1, viewHyperlink[i]);
					//events.setWidget(i+1, 2, acceptHyperlink[i]);
					events.setWidget(i+1, 2, ignoreHyperlink[i]);
					
					
					
					
					
					//listener add
					viewHyperlink[i].addClickHandler(new ClickHandler()
					{
						public void onClick(ClickEvent event)
						{
							int i;
							for(i = 0; i < viewHyperlink.length; i++)
							{
								if(event.getSource() == viewHyperlink[i])
								{
									break;
								}
							}
							
							//pop up the current event info
							final DialogBox currentEvent = new DialogBox();
							//.setWidget(a[i]);
							currentEvent.setAnimationEnabled(true);
							final Button closeButton = new Button("Close");
							closeButton.getElement().setId("closeButton");
							//final Label textToServerLabel = new Label();
							//final HTML serverResponseLabel = new HTML();
							VerticalPanel dialogVPanel = new VerticalPanel();
							dialogVPanel.addStyleName("dialogVPanel");
							dialogVPanel.add(new Label(a[i].getText()));
							//dialogVPanel.add(textToServerLabel);
							//dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
							//dialogVPanel.add(serverResponseLabel);
							dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
							dialogVPanel.add(closeButton);
							currentEvent.setWidget(dialogVPanel);

							// Add a handler to close the DialogBox
							closeButton.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									currentEvent.hide();
									//sendButton.setEnabled(true);
									//sendButton.setFocus(true);
								}
							});
							
							currentEvent.center();
							
						}
					});  //view listener
					
					ignoreHyperlink[i].addClickHandler(new ClickHandler()
					{
						public void onClick(ClickEvent event)
						{
							int i;
							for(i = 0; i < events.getRowCount(); i++)
							{
								if(event.getSource().equals(events.getWidget(i, 2)))
								{
									break;
								}
							}// do we need this loop? one listener only detect one hyper;ink
							
							//move to accepted folder
							//remove from pending folder
							//communication to server, inform the event manager
							//re-get the list and refresh
							events.removeRow(i);
						}
					});
					
				}
				//hyberlinks listeners
				//---------------	
			}
			else if(tmp == ignoredEvent)
			{
				//2 buttons: view/accept
				events.clear();
				//2 buttons: view/ignore
				//3 hyberlinks: view/accept/ignore
				//read from sever the number of entries
				acceptHyperlink = new Hyperlink[30];
				//ignoreHyperlink = new Hyperlink[3];
				viewHyperlink = new Hyperlink[30];
				
				//----------------
				vendorPage.setBorderWidth(1);
				//border display
				
				Date today = new Date();
				//Event name
				
			    // prints Tue Dec 18 12:01:26 GMT-500 2007 in the default locale.
			    //GWT.log(today.toString(), null);
				final Label[] a = new Label[30];
				for(int i = 0; i < 30; i++)
					a[i] =  new Label(today.toString());
				
				events.setWidget(0, 0, new Label("Events"));
				events.setWidget(0, 2, new Label("Options"));
				//events.setText(0, 2, "Events");
				//events.setText(0, 3, "Events");
				for(int i = 0; i < 30; i++)
				{
					events.setWidget(i+1, 0, a[i]);
					events.getCellFormatter().setWidth(i, 0, "90%");
					viewHyperlink[i] = new Hyperlink("View", Integer.toString(i));
					//ignoreHyperlink[i] = new Hyperlink("ignore", "");
					acceptHyperlink[i] = new Hyperlink("Accept", Integer.toString(i));
					events.setWidget(i+1, 1, viewHyperlink[i]);
					events.setWidget(i+1, 2, acceptHyperlink[i]);
					//events.setWidget(i+1, 3, ignoreHyperlink[i]);
					
					//listener add
					viewHyperlink[i].addClickHandler(new ClickHandler()
					{
						public void onClick(ClickEvent event)
						{
							int i;
							for(i = 0; i < viewHyperlink.length; i++)
							{
								if(event.getSource() == viewHyperlink[i])
								{
									break;
								}
							}
							
							//pop up the current event info
							final DialogBox currentEvent = new DialogBox();
							//.setWidget(a[i]);
							currentEvent.setAnimationEnabled(true);
							final Button closeButton = new Button("Close");
							closeButton.getElement().setId("closeButton");
							//final Label textToServerLabel = new Label();
							//final HTML serverResponseLabel = new HTML();
							VerticalPanel dialogVPanel = new VerticalPanel();
							dialogVPanel.addStyleName("dialogVPanel");
							dialogVPanel.add(new Label(a[i].getText()));
							//dialogVPanel.add(textToServerLabel);
							//dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
							//dialogVPanel.add(serverResponseLabel);
							dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
							dialogVPanel.add(closeButton);
							currentEvent.setWidget(dialogVPanel);

							// Add a handler to close the DialogBox
							closeButton.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									currentEvent.hide();
									//sendButton.setEnabled(true);
									//sendButton.setFocus(true);
								}
							});
							
							currentEvent.center();
							
						}
					});  //view listener
					
					
					
					acceptHyperlink[i].addClickHandler(new ClickHandler()
					{
						public void onClick(ClickEvent event)
						{
							int i;
							for(i = 0; i < events.getRowCount(); i++)
							{
								if(event.getSource().equals(events.getWidget(i, 2)))
								{
									break;
								}
							}// do we need this loop? one listener only detect one hyper;ink
							
							//move to accepted folder
							//remove from pending folder
							//communication to server, inform the event manager
							//re-get the list and refresh
							events.removeRow(i);
						}
					}); //accept listener
					
				}
				//hyberlinks listeners
				//----------------
			}
		}
	}
	
	public void tableRefresh()
	{
		;// receive data from server and refresh the entire table
	}

}