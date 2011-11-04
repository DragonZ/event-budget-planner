package budgeteventplanner.client;


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

public class AttendeeManager implements EntryPoint {

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		
		
		HorizontalPanel hPanel=new HorizontalPanel();
		hPanel.setSpacing(50);
		RootPanel.get("attendees").add(hPanel);
		hPanel.setSize("100%", "100%");
		

		
		final TextArea infoBox=new TextArea();
		infoBox.setSize("500px","500px");
		infoBox.setReadOnly(true);
		infoBox.setText("here");
		
		//TreeItem hTree =new TreeItem();
		TreeItem root=new TreeItem("Attendees");
		Label [] attendees= new Label[5];
		
	      class MyHandler implements ClickHandler {
	    		
				
				public void onClick(ClickEvent event) {
					
					greetingService.getAttendeeInfo(((Label) event.getSource()).getText(), new AsyncCallback<String>(){
						public void onFailure(Throwable caught) {
						}

						public void onSuccess(String result) {
							
							
						infoBox.setText(result);	
						}
						
					}
							
					);
					
				}
	        }
		for(int i=0;i<5;i++)
		{
			attendees[i]=new Label("attendees"+i);
			root.addItem(attendees[i]);
			attendees[i].addClickHandler(new MyHandler());
			
		}
		
		Tree hTree=new Tree();
		hTree.addItem(root);
		
		hPanel.	add(hTree);
		hPanel.	add(infoBox);


  
        
        
	}

}


