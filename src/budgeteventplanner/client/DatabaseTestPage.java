package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import budgeteventplanner.client.entity.Organizer;
import budgeteventplanner.client.entity.TestEntity;

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

public class DatabaseTestPage implements EntryPoint {

	private final DataServiceAsync dataService = GWT.create(DataService.class);

	private final UserServiceAsync userService = GWT.create(UserService.class);
	
	private final EventServiceAsync eventService = GWT.create(EventService.class);

	@Override
	public void onModuleLoad() {
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

		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		// database component
		final Button putButton = new Button("Put");
		final Button getButton = new Button("Get");
		final TextBox dbname = new TextBox();
		final TextBox dbaddress = new TextBox();
		final TextBox dblimit = new TextBox();

		RootPanel.get("databseContainer").add(dbname);
		RootPanel.get("databseContainer").add(dbaddress);
		RootPanel.get("databseContainer").add(dblimit);
		RootPanel.get("databseContainer").add(putButton);
		RootPanel.get("databseContainer").add(getButton);

		class DBPutHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
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
								serverResponseLabel.setHTML("RPC Call Failed!");
								dialogBox.center();
								closeButton.setFocus(true);
							}

							@Override
							public void onSuccess(Void result) {
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
								serverResponseLabel.setHTML("RPC Call Failed!");
								dialogBox.center();
								closeButton.setFocus(true);
							}

							@Override
							public void onSuccess(ArrayList<TestEntity> result) {
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



		
		
		//registyer component
		final Button registerButton = new Button("Register");
		final Button loginButton = new Button("Login");
		final TextBox dbemail = new TextBox();
		final TextBox dbrole = new TextBox();
		final TextBox dbpasswd = new TextBox();

		RootPanel.get("registerContainer").add(dbemail);
		RootPanel.get("registerContainer").add(dbpasswd);
		RootPanel.get("registerContainer").add(dbrole);
		RootPanel.get("registerContainer").add(registerButton);
		RootPanel.get("registerContainer").add(loginButton);

		class DBRegisterHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				try {
					userService.register(dbemail.getText(), dbpasswd.getText(),
							Integer.parseInt(dbrole.getText()),
							new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									dialogBox
											.setText("Remote Procedure Call - Failure");
									serverResponseLabel
											.addStyleName("serverResponseLabelError");
									serverResponseLabel
											.setHTML("RPC Call Failed!");
									dialogBox.center();
									closeButton.setFocus(true);
								}

								@Override
								public void onSuccess(Void result) {
									dialogBox.setText("Remote Procedure Call");
									serverResponseLabel
											.removeStyleName("serverResponseLabelError");
									serverResponseLabel
											.setHTML("Registered Successfully!");
									dialogBox.center();
									closeButton.setFocus(true);
								}
							});
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}

		}
		registerButton.addClickHandler(new DBRegisterHandler());
		
		class DBLoginHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				try {
					userService.login(dbemail.getText(), dbpasswd.getText(),
							new AsyncCallback<Integer>() {
								@Override
								public void onFailure(Throwable caught) {
									// Show the RPC error message to the user
									dialogBox
											.setText("Remote Procedure Call - Failure");
									serverResponseLabel
											.addStyleName("serverResponseLabelError");
									serverResponseLabel.setHTML("RPC Call Failed!");
									dialogBox.center();
									closeButton.setFocus(true);
								}

								@Override
								public void onSuccess(Integer result) {
									dialogBox.setText("Remote Procedure Call - Success");
									serverResponseLabel
											.removeStyleName("serverResponseLabelError");
									serverResponseLabel.setHTML(result.toString());
									dialogBox.center();
									closeButton.setFocus(true);
								}
							});
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
		}

		loginButton.addClickHandler(new DBLoginHandler());
		
		//event component
		final Label eventTestLabel = new Label("Event service test");
		final Button putButton2 = new Button("put2");
		final Button getButton2 = new Button("get2");
		final TextBox dbeventname = new TextBox();
		final TextBox dborganizer = new TextBox();
		final TextBox dbvisibility = new TextBox();

		RootPanel.get("eventContainer").add(eventTestLabel);
		RootPanel.get("eventContainer").add(dbeventname);
		RootPanel.get("eventContainer").add(dborganizer);
		RootPanel.get("eventContainer").add(dbvisibility);
		RootPanel.get("eventContainer").add(putButton2);
		RootPanel.get("eventContainer").add(getButton2);

		class DBPut2Handler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				try {
					eventService.createEvent(dbeventname.getText(), 
							dborganizer.getText(), Integer.parseInt(dbvisibility.getText()),
							new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									dialogBox
											.setText("Remote Procedure Call - Failure");
									serverResponseLabel
											.addStyleName("serverResponseLabelError");
									serverResponseLabel
											.setHTML("RPC Call Failed!");
									dialogBox.center();
									closeButton.setFocus(true);
								}

								@Override
								public void onSuccess(Void result) {
									dialogBox.setText("Remote Procedure Call");
									serverResponseLabel
											.removeStyleName("serverResponseLabelError");
									serverResponseLabel
											.setHTML("Registered Successfully!");
									dialogBox.center();
									closeButton.setFocus(true);
								}
							});
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}

		}
		putButton2.addClickHandler(new DBPut2Handler());
		
	/*	class DBGet2Handler implements ClickHandler {


			@Override
			public void onClick(ClickEvent event) {
				try {
					userService.login(dbemail2.getText(), dbpasswd.getText(),
							new AsyncCallback<Integer>() {
								@Override
								public void onFailure(Throwable caught) {
									// Show the RPC error message to the user
									dialogBox
											.setText("Remote Procedure Call - Failure");
									serverResponseLabel
											.addStyleName("serverResponseLabelError");
									serverResponseLabel.setHTML("RPC Call Failed!");
									dialogBox.center();
									closeButton.setFocus(true);
								}

								@Override
								public void onSuccess(Integer result) {
									dialogBox.setText("Remote Procedure Call");
									serverResponseLabel
											.removeStyleName("serverResponseLabelError");
									serverResponseLabel.setHTML(result.toString());
									dialogBox.center();
									closeButton.setFocus(true);
								}
							});
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
		}
		getButton2.addClickHandler(new DBLoginHandler());*/
	}

}
