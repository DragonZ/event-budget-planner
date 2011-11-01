package budgeteventplanner.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AttendeeRegistration implements EntryPoint {
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
	
	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		final Label emailLabel=new Label("*Email:");
		final TextBox emailBox=new TextBox();
		RootPanel.get("email").add(emailLabel);
		RootPanel.get("emailBox").add(emailBox);
		
		final Label firstNameLabel=new Label("*First Name:");
		final TextBox firstNameBox=new TextBox();
		RootPanel.get("firstName").add(firstNameLabel);
		RootPanel.get("firstNameBox").add(firstNameBox);
	
		final Label midNameLabel=new Label("Mid Name:");
		final TextBox midNameBox=new TextBox();
		RootPanel.get("midName").add(midNameLabel);
		RootPanel.get("midNameBox").add(midNameBox);
		
		final Label lastNameLabel=new Label("*Last Name:");
		final TextBox lastNameBox=new TextBox();
		RootPanel.get("lastName").add(lastNameLabel);
		RootPanel.get("lastNameBox").add(lastNameBox);
		
		
		final Label jobTitleLabel=new Label("Job Title:");
		final TextBox jobTitleBox=new TextBox();
		RootPanel.get("jobTitle").add(jobTitleLabel);
		RootPanel.get("jobTitleBox").add(jobTitleBox);
		
		final Label companyLabel=new Label("Company:");
		final TextBox companyBox=new TextBox();
		RootPanel.get("company").add(companyLabel);
		RootPanel.get("companyBox").add(companyBox);
		
		final Label address1Label=new Label("*Address 1:");
		final TextBox address1Box=new TextBox();
		RootPanel.get("address1").add(address1Label);
		RootPanel.get("address1Box").add(address1Box);
		
		final Label address2Label=new Label("Address 2:");
		final TextBox address2Box=new TextBox();
		RootPanel.get("address2").add(address2Label);
		RootPanel.get("address2Box").add(address2Box);
		
		final Label cityLabel=new Label("*City:");
		final TextBox cityBox=new TextBox();
		RootPanel.get("city").add(cityLabel);
		RootPanel.get("cityBox").add(cityBox);
		
		final Label stateLabel=new Label("*State:");
		final TextBox stateBox=new TextBox();
		RootPanel.get("state").add(stateLabel);
		RootPanel.get("stateBox").add(stateBox);
		
		final Label zipLabel=new Label("*Zip:");
		final TextBox zipBox=new TextBox();
		RootPanel.get("zip").add(zipLabel);
		RootPanel.get("zipBox").add(zipBox);
		
		final Label phoneLabel=new Label("*Phone Number:");
		final TextBox phoneBox=new TextBox();
		RootPanel.get("phone").add(phoneLabel);
		RootPanel.get("phoneBox").add(phoneBox);
		
		final Button submit=new Button("submit");
		RootPanel.get("submit").add(submit);
		//dialog box
		final DialogBox dialogBox=new DialogBox();
		final Button closeButton = new Button("Close");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Submit Successful!</b>"));
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		dialogBox.setAnimationEnabled(true);
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				submit.setEnabled(true);
				submit.setFocus(true);
			}
		});   
		
	        class MyHandler implements ClickHandler {
		
			
				public void onClick(ClickEvent event) {
					greetingService.sendEmail(firstNameBox.getText(), emailBox.getText(), new AsyncCallback<Integer>(){
						public void onFailure(Throwable caught) {
						}

						public void onSuccess(Integer result) {
							
							
							dialogBox.center();
							closeButton.setFocus(true);
							
						}
						
					}
							
					);
					
				}
	        }
	        MyHandler handler = new MyHandler();
	        submit.addClickHandler(handler);
	}

}
