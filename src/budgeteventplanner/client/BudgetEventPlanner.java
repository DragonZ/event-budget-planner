package budgeteventplanner.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BudgetEventPlanner implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "Please enter at least four characters.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	private final DataServiceAsync dataService = GWT.create(DataService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		final Button sendButton = new Button("Send");
		final Button loginButton = new Button("Login");
		loginButton.setWidth("80px");
		final Button signButton = new Button("Sign Up");
		signButton.setWidth("80px");
		final TextBox nameField = new TextBox();
		nameField.setText("");
		nameField.setWidth("210px");
		final PasswordTextBox pwField = new PasswordTextBox();
		pwField.setText("");
		pwField.setWidth("210px");
		final Label errorLabel = new Label();

		// database component
		final Button putButton = new Button("Put");
		final Button getButton = new Button("Get");
		final TextBox dbname = new TextBox();
		final TextBox dbaddress = new TextBox();
		final TextBox dblimit = new TextBox();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		loginButton.addStyleName("loginButton");
		signButton.addStyleName("signButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("pwFieldContainer").add(pwField);
		RootPanel.get("loginButtonContainer").add(loginButton);
		RootPanel.get("signButtonContainer").add(signButton);

		RootPanel.get("databseContainer").add(dbname);
		RootPanel.get("databseContainer").add(dbaddress);
		RootPanel.get("databseContainer").add(dblimit);
		RootPanel.get("databseContainer").add(putButton);
		RootPanel.get("databseContainer").add(getButton);
		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				loginButton.setEnabled(true);
				signButton.setEnabled(true);
				nameField.setFocus(true);
				RootPanel.get(nameField.getText()).setVisible(true);
				RootPanel.get("main").setVisible(false);
				nameField.setText("");
				pwField.setText("");
			}
		});

		// Create the popup signup box
		final DialogBox signupBox = new DialogBox();
		signupBox.setText("Sign Up");
		signupBox.setAnimationEnabled(true);
		final TextBox emailAdd = new TextBox();
		emailAdd.setWidth("200px");
		final PasswordTextBox first_pw = new PasswordTextBox();
		first_pw.setWidth("200px");
		final PasswordTextBox second_pw = new PasswordTextBox();
		second_pw.setWidth("200px");
		final TextArea address = new TextArea();
		address.setSize("200px", "50px");
		final TextBox city = new TextBox();
		city.setWidth("120px");
		final TextBox state = new TextBox();
		state.setMaxLength(2);
		state.setWidth("40px");
		final TextBox zipCode = new TextBox();
		zipCode.setWidth("60px");
		zipCode.setMaxLength(5);
		final Button submitButton = new Button("Submit");
		submitButton.getElement().setId("submitButton");
		final Button cancelButton = new Button("Cancel");
		cancelButton.getElement().setId("cancelButton");
		final Label textToServerLabel_su = new Label();
		final HTML serverResponseLabel_su = new HTML();
		VerticalPanel signupVPanel = new VerticalPanel();
		signupVPanel.addStyleName("signupVPanel");
		signupVPanel.add(new HTML("<b>Username:</b>"));
		signupVPanel.add(emailAdd);
		signupVPanel.add(textToServerLabel_su);
		signupVPanel.add(new HTML("<br><b>Password:</b>"));
		signupVPanel.add(serverResponseLabel_su);
		signupVPanel.add(first_pw);
		signupVPanel.add(new HTML("<br><b>Retype Password:</b>"));
		signupVPanel.add(second_pw);
		signupVPanel.add(new HTML("<br><b>Address:</b>"));
		signupVPanel.add(address);
		signupVPanel.add(new HTML("<br><b>City:</b>"));
		signupVPanel.add(city);
		signupVPanel.add(new HTML("<br><b>State:</b>"));
		signupVPanel.add(state);
		signupVPanel.add(new HTML("<br><b>Zip Code:</b>"));
		signupVPanel.add(zipCode);
		signupVPanel.add(new HTML(""));
		signupVPanel.add(new HTML(""));
		signupVPanel.add(new HTML(""));
		signupVPanel.add(new HTML(""));
		signupVPanel.add(new HTML(""));
		signupVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		signupVPanel.add(submitButton);
		signupVPanel.add(cancelButton);
		signupVPanel.setSpacing(5);
		signupBox.setWidget(signupVPanel);

		// Add a handler to open the signup dialog
		signButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				nameField.setText("");
				pwField.setText("");
				signupBox.show();
				signupBox.center();
				emailAdd.setFocus(true);
				loginButton.setEnabled(false);
				signButton.setEnabled(false);
			}
		});

		final DialogBox signedBox = new DialogBox();
		signedBox.setText("Success!");
		signedBox.setAnimationEnabled(true);
		final Button backButton = new Button("Back");
		backButton.getElement().setId("backButton");
		VerticalPanel successVPanel = new VerticalPanel();
		successVPanel.addStyleName("successVPanel");
		successVPanel.add(new HTML("<b>You have successfully signed up.</b>"));
		successVPanel.add(new HTML("<b>Welcome to Budget Event Manager!</b>"));
		successVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		successVPanel.add(backButton);
		signedBox.setWidget(successVPanel);

		final HTML errorSign = new HTML("");

		final DialogBox errorBox = new DialogBox();
		errorBox.setText("Error!");
		errorBox.setAnimationEnabled(true);
		final Button okButton = new Button("OK");
		backButton.getElement().setId("okButton");
		VerticalPanel errorVPanel = new VerticalPanel();
		errorVPanel.addStyleName("errorVPanel");
		errorVPanel.add(errorSign);
		errorVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		errorVPanel.add(okButton);
		errorBox.setWidget(errorVPanel);

		// Add a handler to submit the information
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (emailAdd.getText().isEmpty()) {
					errorSign.setText("Invalid username");
				} else if (first_pw.getText().isEmpty()) {
					errorSign.setText("Invalid password");
				} else if (!first_pw.getText().equals(second_pw.getText())) {
					errorSign.setText("Re-typed password is not correct");
				} else if (address.getText().isEmpty()) {
					errorSign.setText("Invalid address");
				} else if (city.getText().isEmpty()) {
					errorSign.setText("Invalid city");
				} else if (state.getText().length() < 2) {
					errorSign.setText("Invalid state");
				} else if (zipCode.getText().length() < 5) {
					errorSign.setText("Invalid zipcode");
				}

				if (errorSign.getText().isEmpty()) {
					signupBox.hide();
					emailAdd.setText("");
					first_pw.setText("");
					second_pw.setText("");
					address.setText("");
					city.setText("");
					state.setText("");
					zipCode.setText("");

					signedBox.show();
					signedBox.center();
					backButton.setFocus(true);
				} else {
					errorBox.show();
					errorBox.center();
					okButton.setFocus(true);
				}
			}
		});

		// Add a handler to cancel the sign up
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				signupBox.hide();
				loginButton.setEnabled(true);
				signButton.setEnabled(true);
				nameField.setFocus(true);
			}
		});

		// Add a handler to back to the index
		backButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				signedBox.hide();
				loginButton.setEnabled(true);
				signButton.setEnabled(true);
				nameField.setFocus(true);
			}
		});

		// Add a handler to back to the sign up
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				errorSign.setText("");
				errorBox.hide();
				emailAdd.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a
			 * response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();

				// Then, we send the input to the server.
				loginButton.setEnabled(false);
				signButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");

				greetingService.greetServer(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		loginButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);

		class DBPutHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				dataService.putEntity(dbname.getText(), dbaddress.getText(),
						Integer.parseInt(dblimit.getText()),
						new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML("Success");
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}

		}

		DBPutHandler dbputhandler = new DBPutHandler();
		putButton.addClickHandler(dbputhandler);

		class DBGetHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				dataService.getEntityByLimit(
						Integer.parseInt(dblimit.getText()),
						new AsyncCallback<ArrayList<TestEntity>>() {
							@Override
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							@Override
							public void onSuccess(ArrayList<TestEntity> result) {
								// TODO Auto-generated method stub
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								String html = new String();
								for (TestEntity ent : result) {
									html += ent.toString();
								}
								serverResponseLabel.setHTML(html);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		DBGetHandler dbgethandler = new DBGetHandler();
		getButton.addClickHandler(dbgethandler);

	}
}
