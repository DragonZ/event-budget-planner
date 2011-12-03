//<<<<<<< .mine
/*
 *
 needs to make sure that the phone number is not letter
 * clear everything after jump
 * enter the information that already have in the data base after the attendee reenter the registration code.
 * 
 */

//=======
// can cancel their attending anytime.
// in event view page the canceled attendee needs another color.
//>>>>>>> .r187
package budgeteventplanner.client;

import budgeteventplanner.client.entity.Attendee;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AttendeeRegistration implements EntryPoint {
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	final static AttendeeServiceAsync attendeeService = GWT
			.create(AttendeeService.class);
	final HTML emailLabel = new HTML("<h4>*</h4><b>Email:</b>");
	final static TextBox emailBox = new TextBox();
	final HTML firstNameLabel = new HTML("<h4>*</h4><b>First Name:</b>");
	final static TextBox firstNameBox = new TextBox();
	final HTML midNameLabel = new HTML("<b>Mid Name:</b>");
	final TextBox midNameBox = new TextBox();
	final HTML lastNameLabel = new HTML("<h4>*</h4><b>Last Name:</b>");
	final TextBox lastNameBox = new TextBox();
	final HTML jobTitleLabel = new HTML("<b>Job Title:</b>");
	final static TextBox jobTitleBox = new TextBox();
	final HTML companyLabel = new HTML("<b>Company:</b>");
	final static TextBox companyBox = new TextBox();
	final HTML address1Label = new HTML("<h4>*</h4><b>Address 1:</b>");
	final static TextBox address1Box = new TextBox();
	final HTML address2Label = new HTML("<b>Address 2:</b>");
	final static TextBox address2Box = new TextBox();
	final HTML cityLabel = new HTML("<h4>*</h4><b>City:</b>");
	final static TextBox cityBox = new TextBox();
	final HTML stateLabel = new HTML("<h4>*</h4><b>State:</b>");
	final static TextBox stateBox = new TextBox();
	final HTML zipLabel = new HTML("<h4>*</h4><b>Zip:</B>");
	final static TextBox zipBox = new TextBox();
	final HTML phoneLabel = new HTML("<h4>*</h4><b>Phone Number:</b>");
	final static TextBox phoneBox = new TextBox();
	final Button submit = new Button();
	final DialogBox dialogBox = new DialogBox();
	final Button closeButton = new Button();
	VerticalPanel dialogVPanel = new VerticalPanel();
	private static String attendeeID = null;
	final ListBox status = new ListBox();
	final HTML statusLabel=new HTML("<b>Are You Going To Attend:<b>");
	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		//status 0 yes 1 no 2 maybe
		status.addItem("yes");
		status.addItem("no");
		status.addItem("maybe");
		
		RootPanel.get("status").add(statusLabel);
		RootPanel.get("statusBox").add(status);

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

	    submit.setHTML("<h2>Submit</h2>");
	    closeButton.setHTML("<h2>Close</h2>");
		RootPanel.get("submit").add(submit);

		// dialog box

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
				RootPanel.get("XuXuan2").setVisible(true);
				//RootPanel.get("main").setVisible(true);
			}
		});
		// emailBox.setFocus(true);

		class MyHandler implements ClickHandler {

			public void onClick(ClickEvent event) {

				if (!checkForm()){
					return;
				}

				attendeeService.updateAttendeeInfo(
						attendeeID,
						firstNameBox.getText() + " " + midNameBox.getText(),
						emailBox.getText(),
						jobTitleBox.getText(),
						companyBox.getText(),
						address1Box.getText() + "\n" + address2Box.getText()
								+ "\n" + cityBox.getText() + "\n"
								+ stateBox.getText() + "\n" + zipBox.getText(),
						phoneBox.getText(),status.getSelectedIndex(), new AsyncCallback<Void>() {
							public void onFailure(Throwable caught) {
							}

							public void onSuccess(Void result) {

								((HTML) dialogVPanel.getWidget(0))
										.setHTML("<b>Submit Successful!</b>");
								dialogBox.center();
								closeButton.setFocus(true);
								// RootPanel.get("attendeeManager").setVisible(true);
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
								status.setItemSelected(0, true);
								//RootPanel.get("XuXuan2").setVisible(false);
								//RootPanel.get("main").setVisible(true);
							}

						}

				);

			}
		}
		MyHandler handler = new MyHandler();
		submit.addClickHandler(handler);
	}

	// used by BudgetEventPlanner UI to set the current attendeeID
	public static void setAttendeeID(String attendeeIDc) {
		attendeeID = attendeeIDc;
		//Attendee newAttendee = new Attendee();
		attendeeService.getAttendeeByAttendeeId(attendeeID, new AsyncCallback<Attendee>() {
			@Override
			public void onFailure(Throwable caught) {
			}
			
			@Override
			public void onSuccess(Attendee attendeeResult) {
				emailBox.setText(attendeeResult.getEmail());
				firstNameBox.setText(attendeeResult.getName());
				jobTitleBox.setText(attendeeResult.getJobTitle());
				companyBox.setText(attendeeResult.getCompanyName());
				address1Box.setText(attendeeResult.getAddress().split("\n")[0]);
				address2Box.setText(attendeeResult.getAddress().split("\n")[1]);
				cityBox.setText(attendeeResult.getAddress().split("\n")[2]);
				stateBox.setText(attendeeResult.getAddress().split("\n")[3]);
				zipBox.setText(attendeeResult.getAddress().split("\n")[4]);
				phoneBox.setText(attendeeResult.getPhoneNumber());
			}
		});
		
	}

	// check if the form is legal
	boolean checkForm() {
		if (emailBox.getText().compareTo("") == 0) {
			((HTML) dialogVPanel.getWidget(0))
					.setHTML("<b>Be sure input your email address</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if (!(emailBox.getText().contains("@") && emailBox.getText().contains(
				"."))) {
			((HTML) dialogVPanel.getWidget(0))
					.setHTML("<b>Be sure input a legal email address</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if (firstNameBox.getText().compareTo("") == 0) {
			((HTML) dialogVPanel.getWidget(0))
					.setHTML("<b>Be sure input your first name</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if (lastNameBox.getText().compareTo("") == 0) {
			((HTML) dialogVPanel.getWidget(0))
					.setHTML("<b>Be sure input your last name</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if (address1Box.getText().compareTo("") == 0) {
			((HTML) dialogVPanel.getWidget(0))
					.setHTML("<b>Be sure input your address</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if (cityBox.getText().compareTo("") == 0) {
			((HTML) dialogVPanel.getWidget(0))
					.setHTML("<b>Be sure input the city you live in</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if (stateBox.getText().compareTo("") == 0) {
			((HTML) dialogVPanel.getWidget(0))
					.setHTML("<b>Be sure input the state you live in</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if (zipBox.getText().compareTo("") == 0) {
			((HTML) dialogVPanel.getWidget(0))
					.setHTML("<b>Be sure input the ZIP code</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if (phoneBox.getText().compareTo("") == 0) {
			((HTML) dialogVPanel.getWidget(0))
					.setHTML("<b>Be sure input your phone number</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		if (phoneBox.getText().length()!=10) {
			((HTML) dialogVPanel.getWidget(0))
					.setHTML("<b>Be sure input ten digits phone number</b>");
			dialogBox.center();
			closeButton.setFocus(true);
			return false;
		}
		

		return true;
	}

}
