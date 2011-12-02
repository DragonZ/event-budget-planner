package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;

import budgeteventplanner.client.entity.User;
import budgeteventplanner.client.entity.User.UserType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserRegistrationPanel extends Composite {
  
  private final UserServiceAsync userService = GWT.create(UserService.class);
  
  final TextBox txtEmail = new TextBox();
  final ListBox listUserType = new ListBox();
  final PasswordTextBox first_pw = new PasswordTextBox();
  final PasswordTextBox second_pw = new PasswordTextBox();
  final TextArea txtAddress = new TextArea();
  final TextBox txtCity = new TextBox();
  final TextBox txtState = new TextBox();
  final TextBox txtZipCode = new TextBox();
  final Button btnSubmit = new Button("Submit");
  final Button btnCancel = new Button("Cancel");
  
  public UserRegistrationPanel() {
    super();
    txtEmail.setWidth("200px");
    for (UserType userType : User.UserType.values()) {
      listUserType.addItem(userType.getRoleText());
    }
    first_pw.setWidth("200px");
    second_pw.setWidth("200px");
    txtAddress.setSize("200px", "50px");
    txtCity.setWidth("120px");
    txtState.setMaxLength(2);
    txtState.setWidth("40px");
    txtZipCode.setWidth("60px");
    txtZipCode.setMaxLength(5);
    btnSubmit.getElement().setId("btnSubmit");
    btnCancel.getElement().setId("btnCancel");
    
    final VerticalPanel signupVPanel = new VerticalPanel();
    final Label title = new Label("Attendee Registration");
    title.addStyleName("panelTitle");
    signupVPanel.add(title);
    signupVPanel.add(new HTML("<b>*Username:</b>"));
    signupVPanel.add(txtEmail);
    signupVPanel.add(new HTML("<br><b>*User Type:</b>"));
    signupVPanel.add(listUserType);
    signupVPanel.add(new HTML("<br><b>*Password:</b>"));
    signupVPanel.add(first_pw);
    signupVPanel.add(new HTML("<br><b>*Retype Password:</b>"));
    signupVPanel.add(second_pw);
    signupVPanel.add(new HTML("<br><b>Address:</b>"));
    signupVPanel.add(txtAddress);
    signupVPanel.add(new HTML("<br><b>City:</b>"));
    signupVPanel.add(txtCity);
    signupVPanel.add(new HTML("<br><b>State:</b>"));
    signupVPanel.add(txtState);
    signupVPanel.add(new HTML("<br><b>Zip Code:</b>"));
    signupVPanel.add(txtZipCode);
    signupVPanel.add(new HTML(""));
    signupVPanel.add(new HTML(""));
    signupVPanel.add(new HTML(""));
    signupVPanel.add(new HTML(""));
    signupVPanel.add(new HTML(""));
    signupVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    signupVPanel.add(btnSubmit);
    signupVPanel.add(btnCancel);
    signupVPanel.setSpacing(5);

    // register RPC call
    // Submit Button Handler
    btnSubmit.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        final String EMAIL_VALIDATION_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (txtEmail.getText().matches(EMAIL_VALIDATION_REGEX)) {
          showDialog("Error", "Invalid email address");
          return;
        } else if (first_pw.getText().isEmpty() || first_pw.getText().length() < 6) {
          showDialog("Error", "Password should be more than 6 characters");
          return;
        } else if (!first_pw.getText().equals(second_pw.getText())) {
          showDialog("Error", "Password doesn't match");
          return;
        } else {
          try {
            userService.register(
                txtEmail.getText(),
                first_pw.getText(),
                UserType.getRoleIdByText(listUserType.getItemText(listUserType.getSelectedIndex())),
                new AsyncCallback<Void>() {
                  @Override
                  public void onFailure(Throwable caught) {
                    showDialog("Register", "RPC Call Failed!");
                  }

                  @Override
                  public void onSuccess(Void result) {
                    listUserType.setItemSelected(0, true);
                    for (Widget w : signupVPanel) {
                      if (w instanceof TextBox) {
                        ((TextBox) w).setText("");
                      }
                    }
                    showDialog("Register", "Registration Successfully!");
                    RootPanel.get("divPopup").setVisible(false);
                    RootPanel.get("divHome").setVisible(true);
                  }
                });
          } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
          }
        }
      }
    });

    // Add a handler to cancel the sign up
    btnCancel.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        for (Widget w : signupVPanel) {
          if (w instanceof TextBox) {
            ((TextBox) w).setText("");
          }
        }
        listUserType.setItemSelected(0, true);
        RootPanel.get("divPopup").setVisible(false);
        RootPanel.get("divHome").setVisible(true);
      }
    });
    initWidget(signupVPanel);
  }
  
  public void showDialog(String title, String text) {
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
