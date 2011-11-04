/**
 * 
 */
package budgeteventplanner.client;

	
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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
	//TreeItem eventHistory;
	FlexTable events;
	
	DockPanel vendorService;
	FlexTable existingService;
	HorizontalPanel addPanel;
	ListBox category;
	TextBox service;
	//TextBox/TextArea description;
	Button addService;
	//Button deleteService;
	//Button modify == delete then add new one
	
	Hyperlink acceptHyperlink[];
	Hyperlink ignoreHyperlink[];
	Hyperlink viewHyperlink[];
	
	Hyperlink deleteHyperlink[];
	
	//private final DataServiceAsync dataService = GWT.create(DataService.class);
	
	@Override
	public void onModuleLoad() {
		
		tab = new TabPanel();
		//the bottom tab panel
		
		vendorPage = new DockPanel();
		vendorService = new DockPanel();
		addPanel = new HorizontalPanel();
		//the panel contains all components
		
		eventFolders = new Tree();
		pendingEvent = new TreeItem("Pending Events");
		acceptedEvent = new TreeItem("Accepted Events");
		ignoredEvent = new TreeItem("Ignored Events");
		//eventHistory = new TreeItem("Events History");
		//tree and treeItems
		
		events = new FlexTable();
		events.getColumnFormatter().setWidth(0, "100px");
		events.getColumnFormatter().setWidth(1, "100px");
		events.getColumnFormatter().setWidth(3, "100px");
		existingService = new FlexTable();
		//events.setWidth("350px");      //TODO SIZE
		//new method to initialize the service table
		initializeServiceTable();
		//event table

		eventFolders.setWidth("50px"); //TODO SIZE
		eventFolders.addItem(pendingEvent);
		eventFolders.addItem(acceptedEvent);
		eventFolders.addItem(ignoredEvent);
		//eventFolders.addItem(eventHistory);
		
		vendorPage.add(eventFolders, DockPanel.WEST);
		vendorPage.add(events, DockPanel.EAST);
		
		category = new ListBox();
		//get category from server/store the categories in the client?
		//addItem("...");
		//setVisibleItemCount(1/5);
		service = new TextBox();
		addService = new Button("ADD");
		//click handler
		addPanel.add(category);
		addPanel.add(service);
		addPanel.add(addService);
		//vendorService.add(addService, DockPanel.NORTH);
		vendorService.add(existingService, DockPanel.CENTER);
		vendorService.add(addPanel, DockPanel.SOUTH);
		addPanel.setSize("100%", "100%");
		category.setSize("30%", "100%");
		service.setSize("40%", "100%");
		addService.setSize("20%", "100%");
		
		tab.add(vendorPage, "Vendor");
		tab.add(vendorService, "Service");
		RootPanel.get("ZhenLong").add(tab);
		//add operations
		
		eventFolders.addSelectionHandler(new treeHandler<TreeItem>());
		//add listeners
		
		tab.selectTab(0);
		tab.setSize("100%", "100%");
		//tab initialization
		
		//acceptButton = new Button("Accept");
		//ignoreButton = new Button("Ignore");
		//viewButton = new Button("View");
		//buttons initialization
		
	}
	
	public void initializeServiceTable()
	{
		//get data from the service, list the existing services
		existingService.getColumnFormatter().setWidth(0, "200px");
		existingService.getColumnFormatter().setWidth(1, "1100px");
		existingService.getColumnFormatter().setWidth(2, "150px");
		existingService.setWidget(0, 0, new Label("Category"));
		existingService.setWidget(0, 1, new Label("Service"));
		//existingService.setWidget(0, 0, new Label("Description"));
		existingService.setWidget(0, 2, new Label("Option"));
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