package budgeteventplanner.client;

import java.util.ArrayList;

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
	
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
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
								serverResponseLabel.setHTML("RPC Call Failed!");
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
								serverResponseLabel.setHTML("RPC Call Failed!");
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
