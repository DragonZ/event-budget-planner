package budgeteventplanner.client;

import budgeteventplanner.client.entity.Attendee;
import budgeteventplanner.server.AttendeeServiceImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AttendeeInfoPanel extends Composite {
	private final AttendeeServiceAsync attendeeService = GWT.create(AttendeeService.class);
	
	VerticalPanel panelMain = new VerticalPanel();
	
	public AttendeeInfoPanel(final String attendeeId) {
		
		// panel info and note area
		HorizontalPanel hPanelInfo = new HorizontalPanel();
		//TextArea txtAreaNote = new TextArea();
		//txtAreaNote.setText("Add a Note");
		//txtAreaNote.setSize("100%", "100%");
		// in panelInfo
		VerticalPanel vPanelInfoLeft = new VerticalPanel();
		VerticalPanel vPanelInfoRight = new VerticalPanel();
		hPanelInfo.add(vPanelInfoLeft);
		hPanelInfo.add(vPanelInfoRight);

		vPanelInfoLeft.add(new Label("Name:"));
		final TextBox txtBoxName = new TextBox();
		vPanelInfoLeft.add(txtBoxName);
		vPanelInfoLeft.add(new Label("Email:"));
		final TextBox txtBoxEmail = new TextBox();
		vPanelInfoLeft.add(txtBoxEmail);
		vPanelInfoLeft.add(new Label("Job Title:"));
		final TextBox txtBoxJobTitle = new TextBox();
		vPanelInfoLeft.add(txtBoxJobTitle);

		vPanelInfoLeft.add(new Label("Company:"));
		final TextBox txtBoxCompany = new TextBox();
		vPanelInfoLeft.add(txtBoxCompany);

		vPanelInfoLeft.add(new Label("Address 1:"));
		final TextBox txtBoxAddress1 = new TextBox();
		vPanelInfoLeft.add(txtBoxAddress1);

		vPanelInfoRight.add(new Label("Address 2:"));
		final TextBox txtBoxAddress2 = new TextBox();
		vPanelInfoRight.add(txtBoxAddress2);

		vPanelInfoRight.add(new Label("City:"));
		final TextBox txtBoxCity = new TextBox();
		vPanelInfoRight.add(txtBoxCity);

		vPanelInfoRight.add(new Label("State:"));
		final TextBox txtBoxState = new TextBox();
		vPanelInfoRight.add(txtBoxState);

		vPanelInfoRight.add(new Label("Zip:"));
		final TextBox txtBoxZip = new TextBox();
		vPanelInfoRight.add(txtBoxZip);

		vPanelInfoRight.add(new Label("Phone Number:"));
		final TextBox txtBoxPhone = new TextBox();
		vPanelInfoRight.add(txtBoxPhone);
		
		Button btnUpdate = new Button("Update");
		HorizontalPanel hpanelUpdate = new HorizontalPanel();
		hpanelUpdate.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hpanelUpdate.setSize("100%","100%");
		hpanelUpdate.add(btnUpdate);
		
		
		btnUpdate.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				attendeeService.updateAttendeeInfo(
						attendeeId,
						txtBoxName.getText(),
						txtBoxName.getText(),
						txtBoxJobTitle.getText(),
						txtBoxCompany.getText(),
						txtBoxAddress1.getText() + "\n" + txtBoxAddress2.getText()
								+ "\n" + txtBoxCity.getText() + "\n"
								+ txtBoxState.getText() + "\n" + txtBoxZip.getText(),
								txtBoxPhone.getText(),AttendeeServiceImpl.STATUS_NONE, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
									}

									@Override
									public void onSuccess(Void result) {
										showDialog("Attendee managerMent","Update Successful");
									}});
			}});
		panelMain.add(hPanelInfo);
		panelMain.add(hpanelUpdate);

		// readInfo
		attendeeService.getAttendeeByAttendeeId(attendeeId, new AsyncCallback<Attendee>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Attendee result) {
				txtBoxName.setText(result.getName());
				txtBoxEmail.setText(result.getEmail());
				txtBoxJobTitle.setText(result.getJobTitle());
				txtBoxCompany.setText(result.getCompanyName());
				txtBoxAddress1.setText(result.getAddress().split("\n")[0]);
				txtBoxAddress2.setText(result.getAddress().split("\n")[1]);
				txtBoxCity.setText(result.getAddress().split("\n")[2]);
				txtBoxState.setText(result.getAddress().split("\n")[3]);
				txtBoxZip.setText(result.getAddress().split("\n")[4]);
				txtBoxPhone.setText(result.getPhoneNumber());
			}

		});
		
		setEnable(false);
		initWidget(panelMain);
	}
	
	public void setEnable(boolean enabled) {
		HorizontalPanel hPanel = (HorizontalPanel)panelMain.getWidget(0);
		VerticalPanel vPanelLeft = (VerticalPanel)hPanel.getWidget(0);
		VerticalPanel vPanelRight = (VerticalPanel)hPanel.getWidget(1);
		
		if (enabled) {
			for (Widget w : vPanelLeft) {
				if (w instanceof TextBox) {
					((TextBox) w).setStyleName("enabledTextBox");
					((TextBox) w).setReadOnly(false);
				}
			}
			for (Widget w : vPanelRight) {
				if (w instanceof TextBox) {
					((TextBox) w).setStyleName("enabledTextBox");
					((TextBox) w).setReadOnly(false);
				}
			}
			
		} else {
				for (Widget w : vPanelLeft) {
				if (w instanceof TextBox) {
					((TextBox) w).setStyleName("disabledTextBox");
					((TextBox) w).setReadOnly(true);
				}
			}
				for (Widget w : vPanelRight) {
					if (w instanceof TextBox) {
						((TextBox) w).setStyleName("disabledTextBox");
						((TextBox) w).setReadOnly(true);
					}
				}
		}
	}
	public static void showDialog(String title, String text) {
		final DialogBox dialogError = new DialogBox();
		final Button btnClose = new Button("Close");
		dialogError.setText(title);
		dialogError.setAnimationEnabled(true);
		btnClose.getElement().setId("btnClose");
		VerticalPanel panelError = new VerticalPanel();
		panelError.add(new Label(text));
		panelError.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		panelError.add(btnClose);
		dialogError.setWidget(panelError);

		btnClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogError.hide();
			}
		});
		dialogError.center();
		btnClose.setFocus(true);
	}
}
