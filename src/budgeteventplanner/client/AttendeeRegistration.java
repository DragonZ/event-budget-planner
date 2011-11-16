package budgeteventplanner.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
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
	final Label emailLabel=new Label("*Email:");
	final TextBox emailBox=new TextBox();
	final Label firstNameLabel=new Label("*First Name:");
	final TextBox firstNameBox=new TextBox();
	final Label midNameLabel=new Label("Mid Name:");
	final TextBox midNameBox=new TextBox();
	final Label lastNameLabel=new Label("*Last Name:");
	final TextBox lastNameBox=new TextBox();
	final Label jobTitleLabel=new Label("Job Title:");
	final TextBox jobTitleBox=new TextBox();
	final Label companyLabel=new Label("Company:");
	final TextBox companyBox=new TextBox();
	final Label address1Label=new Label("*Address 1:");
	final TextBox address1Box=new TextBox();
	final Label address2Label=new Label("Address 2:");
	final TextBox address2Box=new TextBox();
	final Label cityLabel=new Label("*City:");
	final TextBox cityBox=new TextBox();
	final Label stateLabel=new Label("*State:");
	final TextBox stateBox=new TextBox();
	final Label zipLabel=new Label("*Zip:");
	final TextBox zipBox=new TextBox();
	final Label phoneLabel=new Label("*Phone Number:");
	final TextBox phoneBox=new TextBox();
	final Button submit=new Button("submit");
	final DialogBox dialogBox=new DialogBox();
	final Button closeButton = new Button("Close");
	VerticalPanel dialogVPanel = new VerticalPanel();
	private static String attendeeID=null;
	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		
		RootPanel.get("email").add(emailLabel);
		RootPanel.get("emailBox").add(emailBox);
		
		
		RootPanel.get("firstName").add(firstNameLabel);
		RootPanel.get("firstNameBox").add(firstNameBox);
	

		RootPanel.get("midName").add(midNameLabel);
		RootPanel.get("midNameBox").add(midNameBox);
		

		RootPanel.get("lastName").add(lastNameLabel);
		RootPanel.get("lastNameBox").add(lastNameBox);
		
		

		RootPanel.get("jobTitle").add(jobTitleLabel);
		RootPanel.get("jobTitleBox").add(jobTitleBox);
		

		RootPanel.get("company").add(companyLabel);
		RootPanel.get("companyBox").add(companyBox);
		

		RootPanel.get("address1").add(address1Label);
		RootPanel.get("address1Box").add(address1Box);
		

		RootPanel.get("address2").add(address2Label);
		RootPanel.get("address2Box").add(address2Box);
		

		RootPanel.get("city").add(cityLabel);
		RootPanel.get("cityBox").add(cityBox);
		

		RootPanel.get("state").add(stateLabel);
		RootPanel.get("stateBox").add(stateBox);
		

		RootPanel.get("zip").add(zipLabel);
		RootPanel.get("zipBox").add(zipBox);
		

		RootPanel.get("phone").add(phoneLabel);
		RootPanel.get("phoneBox").add(phoneBox);
		
	
		RootPanel.get("submit").add(submit);
		//dialog box

		
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
		//emailBox.setFocus(true);
		
	
	        class MyHandler implements ClickHandler {
		
			
				public void onClick(ClickEvent event) {
					
					if(!checkForm())	return;
					
					greetingService.sendEmail(firstNameBox.getText(), emailBox.getText(), new AsyncCallback<Integer>(){
						public void onFailure(Throwable caught) {
						}

						public void onSuccess(Integer result) {
							
							((HTML) dialogVPanel.getWidget(0)).setHTML("<b>Submit Successful!</b>");
							dialogBox.center();
							closeButton.setFocus(true);
							//RootPanel.get("attendeeManager").setVisible(true);
							emailBox.setText("");
							firstNameBox.setText("");
							midNameBox.setText("");
							jobTitleBox.setText("");
							lastNameBox.setText("");
							companyBox.setText("");
							address1Box.setText("");
							address2Box.setText("");
							cityBox.setText("");
							stateBox.setText("");
							zipBox.setText("");
							phoneBox.setText("");
						}
						
					}
							
					);
					
				
				}
	        }
	        MyHandler handler = new MyHandler();
	        submit.addClickHandler(handler);
	}
	
	public static void setAttendeeID(String attendeeIDc){
		attendeeID=attendeeIDc;
	} 
	boolean checkForm(){
		if(emailBox.getText().compareTo("")==0 ){
			((HTML) dialogVPanel.getWidget(0)).setHTML("<b>Be sure input your email address</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if(!(emailBox.getText().contains("@") &&emailBox.getText().contains("."))){
			((HTML) dialogVPanel.getWidget(0)).setHTML("<b>Be sure input a legal email address</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if(firstNameBox.getText().compareTo("")==0 ){
			((HTML) dialogVPanel.getWidget(0)).setHTML("<b>Be sure input your first name</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if(address1Box.getText().compareTo("")==0 ){
			((HTML) dialogVPanel.getWidget(0)).setHTML("<b>Be sure input your address</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if(cityBox.getText().compareTo("")==0 ){
			((HTML) dialogVPanel.getWidget(0)).setHTML("<b>Be sure input the city you live in</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if(stateBox.getText().compareTo("")==0 ){
			((HTML) dialogVPanel.getWidget(0)).setHTML("<b>Be sure input the state you live in</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if(zipBox.getText().compareTo("")==0 ){
			((HTML) dialogVPanel.getWidget(0)).setHTML("<b>Be sure input the ZIP code</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if(phoneBox.getText().compareTo("")==0 ){
			((HTML) dialogVPanel.getWidget(0)).setHTML("<b>Be sure input your phone number</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		
		 return true;
	}

}
