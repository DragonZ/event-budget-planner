package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import budgeteventplanner.client.entity.Attendee;
import budgeteventplanner.client.entity.User;
import budgeteventplanner.client.entity.User.UserType;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BudgetEventPlanner implements EntryPoint {

  private final UserServiceAsync userService = GWT.create(UserService.class);
  private final AttendeeServiceAsync attendeeService = GWT.create(AttendeeService.class);

  public void onModuleLoad() {
    final TextBox nameField = new TextBox();
    nameField.setText("");
    nameField.setWidth("210px");
    final PasswordTextBox pwField = new PasswordTextBox();
    pwField.setText("");
    pwField.setWidth("210px");
    final Button attendeePartButton = new Button("Attendee");
    attendeePartButton.setWidth("80px");
    final TextBox attendeeField = new TextBox();
    attendeeField.setText("");
    attendeeField.setWidth("210px");
    final Button attendeeButton = new Button("Submit");
    attendeeButton.setWidth("80px");
    final Button attendeeCancelButton = new Button("Cancel");
    attendeeCancelButton.setWidth("80px");

    final Button btnLogin = new Button("Login");
    btnLogin.setWidth("80px");
    final Button btnSign = new Button("Sign Up");
    btnLogin.setWidth("80px");

    final DialogBox attendeeBox = new DialogBox();
    attendeeBox.setText("Please input the registration code");
    VerticalPanel attendeePanel = new VerticalPanel();
    HorizontalPanel attendeeHPanel = new HorizontalPanel();
    attendeePanel.addStyleName("attendeePanel");
    attendeeHPanel.addStyleName("attendeeHPanel");
    attendeePanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    attendeePanel.add(attendeeField);
    attendeeHPanel.add(attendeeButton);
    attendeeHPanel.add(attendeeCancelButton);
    attendeePanel.add(attendeeHPanel);
    attendeePanel.setSpacing(5);
    attendeeBox.setWidget(attendeePanel);

    // We can add style names to widgets
    btnLogin.addStyleName("loginButton");
    btnSign.addStyleName("signButton");
    attendeeButton.addStyleName("attendeeButton");
    attendeeButton.addStyleName("attendeePartButton");

    // Add the nameField and loginButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    RootPanel.get("nameFieldContainer").add(nameField);
    RootPanel.get("pwFieldContainer").add(pwField);
    RootPanel.get("loginButtonContainer").add(btnLogin);
    RootPanel.get("attendeePartButtonContainer").add(attendeePartButton);
    RootPanel.get("signButtonContainer").add(btnSign);
    // RootPanel.get("attendeePartContainer").add(attendeePart);
    // RootPanel.get("attendeeFieldContainer").add(attendeeField);
    // RootPanel.get("attendeeButtonContainer").add(attendeeButton);

    // Focus the cursor on the name field when the app loads
    nameField.setFocus(true);

    nameField.addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent event) {
        nameField.selectAll();
      }
    });

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

    attendeePartButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        nameField.setText("");
        pwField.setText("");
        attendeeField.setText("");
        attendeeBox.show();
        attendeeBox.center();
        attendeeField.setFocus(true);
        btnLogin.setEnabled(false);
        btnSign.setEnabled(false);
        attendeePartButton.setEnabled(false);
      }
    });

    attendeeCancelButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        attendeeBox.hide();
        btnLogin.setEnabled(true);
        btnSign.setEnabled(true);
        attendeePartButton.setEnabled(true);
        nameField.setText("");
        pwField.setText("");
        attendeeField.setText("");
        attendeeField.setFocus(true);
      }
    });

    // Sign-up Box
    final DialogBox signupBox = new DialogBox();
    final TextBox txtEmail = new TextBox();
    final ListBox listUserType = new ListBox();
    final PasswordTextBox first_pw = new PasswordTextBox();
    final PasswordTextBox second_pw = new PasswordTextBox();
    final TextArea address = new TextArea();
    final TextBox city = new TextBox();
    final TextBox state = new TextBox();
    final TextBox zipCode = new TextBox();
    final Button btnSubmit = new Button("Submit");
    final Button btnCancel = new Button("Cancel");

    signupBox.setText("Sign Up");
    txtEmail.setWidth("200px");
    for (UserType userType : User.UserType.values()) {
      listUserType.addItem(userType.getRoleText());
    }
    first_pw.setWidth("200px");
    second_pw.setWidth("200px");

    address.setSize("200px", "50px");

    city.setWidth("120px");

    state.setMaxLength(2);
    state.setWidth("40px");

    zipCode.setWidth("60px");
    zipCode.setMaxLength(5);
    btnSubmit.getElement().setId("btnSubmit");
    btnCancel.getElement().setId("btnCancel");

    final Label textToServerLabel_su = new Label();
    final HTML serverResponseLabel_su = new HTML();
    final VerticalPanel signupVPanel = new VerticalPanel();
    signupVPanel.addStyleName("signupVPanel");
    signupVPanel.add(new HTML("<b>*Username:</b>"));
    signupVPanel.add(txtEmail);
    signupVPanel.add(textToServerLabel_su);
    signupVPanel.add(new HTML("<br><b>*User Type:</b>"));
    signupVPanel.add(listUserType);
    signupVPanel.add(new HTML("<br><b>*Password:</b>"));
    signupVPanel.add(serverResponseLabel_su);
    signupVPanel.add(first_pw);
    signupVPanel.add(new HTML("<br><b>*Retype Password:</b>"));
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
    signupVPanel.add(btnSubmit);
    signupVPanel.add(btnCancel);
    signupVPanel.setSpacing(5);
    signupBox.setWidget(signupVPanel);

    // Add a handler to open the signup dialog
    btnSign.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        nameField.setText("");
        pwField.setText("");
        attendeeField.setText("");
        signupBox.show();
        signupBox.center();
        txtEmail.setFocus(true);
        btnLogin.setEnabled(false);
        btnSign.setEnabled(false);
        attendeePartButton.setEnabled(false);
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
    successVPanel.add(new HTML("<b>Welcome to Budget Event Planner!</b>"));
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

    // register RPC call
    // Submit Button Handler
    btnSubmit.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        final String EMAIL_VALIDATION_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (txtEmail.getText().matches(EMAIL_VALIDATION_REGEX)) {
          showError("Error", "Invalid email address");
          return;
        } else if (first_pw.getText().isEmpty() || first_pw.getText().length() < 6) {
          showError("Error", "Password should be more than 6 characters");
          return;
        } else if (!first_pw.getText().equals(second_pw.getText())) {
          showError("Error", "Password doesn't match");
          return;
        } else {
          if (errorSign.getText().isEmpty()) {
            try {
              userService.register(txtEmail.getText(), first_pw.getText(), UserType
                  .getRoleIdByText(listUserType.getItemText(listUserType.getSelectedIndex())),
                  new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                      showError("Register", "RPC Call Failed!");
                    }

                    @Override
                    public void onSuccess(Void result) {
                      signupBox.hide();
                      listUserType.setItemSelected(0, true);
                      for (Widget w : signupVPanel) {
                        if (w instanceof TextBox) {
                          ((TextBox) w).setText("");
                        }
                      }
                      signedBox.show();
                      signedBox.center();
                      backButton.setFocus(true);
                    }
                  });
            } catch (NoSuchAlgorithmException e) {
              e.printStackTrace();
            }

          }
        }
      }
    });

    // Add a handler to cancel the sign up
    btnCancel.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        signupBox.hide();
        for (Widget w : signupVPanel) {
          if (w instanceof TextBox) {
            ((TextBox) w).setText("");
          }
        }
        listUserType.setItemSelected(0, true);
        btnLogin.setEnabled(true);
        btnSign.setEnabled(true);
        attendeePartButton.setEnabled(true);
        nameField.setFocus(true);
      }
    });

    // Add a handler to back to the index
    backButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        signedBox.hide();
        btnLogin.setEnabled(true);
        btnSign.setEnabled(true);
        attendeePartButton.setEnabled(true);
        nameField.setFocus(true);
      }
    });

    // Add a handler to back to the sign up
    okButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        errorSign.setText("");
        errorBox.hide();
        txtEmail.setFocus(true);
      }
    });

    // Create a handler for the loginButton and nameField
    class LoginHandler implements ClickHandler, KeyUpHandler {
      public void onClick(ClickEvent event) {
        login();
      }

      public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          login();
        }
      }

      private void login() {
        dialogBox.hide();
        btnLogin.setEnabled(true);
        btnSign.setEnabled(true);
        attendeePartButton.setEnabled(true);
        nameField.setFocus(true);
        if (nameField.getText().equals("XuXuan") || nameField.getText().equals("XuXuan2")
            || nameField.getText().equals("XiaYuan") || nameField.getText().equals("ZhenLong")
            || nameField.getText().equals("dbtest")) {
          RootPanel.get(nameField.getText()).setVisible(true);
          RootPanel.get("main").setVisible(false);
          nameField.setText("");
          pwField.setText("");
          attendeeField.setText("");
        } else if (nameField.getText().isEmpty()) {

        } else {
          try {
            userService.login(nameField.getText(), pwField.getText(), new AsyncCallback<Integer>() {
              @Override
              public void onFailure(Throwable caught) {
                showError("Login", "RPC Call Failed!");
              }

              @Override
              public void onSuccess(Integer result) {
                String typeTurn = new String();
                if (result == 0) { // Event Manager
                  typeTurn = "XiaYuan";
                } else if (result == 1) { // Vender
                  typeTurn = "ZhenLong";
                } else if (result == 2) { // Vender
                  typeTurn = "XuXuan";
                } else if (result == 3) { // Vender
                  typeTurn = "XuXuan2";
                } else {
                  showError("Login", "Incorrect Username or Password!");
                }
                RootPanel.get(typeTurn).setVisible(true);
                RootPanel.get("main").setVisible(false);

                Date exp = new Date();
                exp = new Date(exp.getTime() + 60 * 60 * 24);

                Cookies.setCookie("USERNAME", nameField.getText());
                Cookies.setCookie("USERTYPE", result.toString());
                Cookies.setCookie("TIME", exp.toString());
                nameField.setText("");
                pwField.setText("");
                attendeeField.setText("");
              }
            });
          } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
          }
        }

        // First, we validate the input.
        if (nameField.getText().isEmpty()) {

        } else {
          String textToServer = nameField.getText();

          // Then, we send the input to the server.
          btnLogin.setEnabled(false);
          btnSign.setEnabled(false);
          attendeePartButton.setEnabled(false);
          textToServerLabel.setText(textToServer);
          serverResponseLabel.setText("");

        }
      }
    }

    // add LoginHandler
    btnLogin.addClickHandler(new LoginHandler());
    nameField.addKeyUpHandler(new LoginHandler());
    pwField.addKeyUpHandler(new LoginHandler());

    final DialogBox wrongAttendeeBox = new DialogBox();
    wrongAttendeeBox.setText("Failure");
    wrongAttendeeBox.setAnimationEnabled(true);
    final Button wrongButton = new Button("Close");
    closeButton.getElement().setId("wrongButton");
    VerticalPanel wrongPanel = new VerticalPanel();
    final Label wrongLabel = new Label();
    final HTML wrongResponseLabel = new HTML();
    wrongPanel.addStyleName("wrongPanel");
    wrongPanel.add(new HTML("<b>Incorrect Registration Code!</b>"));
    wrongPanel.add(wrongLabel);
    wrongPanel.add(wrongResponseLabel);
    wrongPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    wrongPanel.add(wrongButton);
    wrongAttendeeBox.setWidget(wrongPanel);

    wrongButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        wrongAttendeeBox.hide();
        attendeeButton.setEnabled(true);
        attendeeField.setText("");
        nameField.setFocus(true);
      }
    });

    attendeeButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (attendeeField.getText() == "xuxuan") {
          attendeeBox.hide();
          btnLogin.setEnabled(true);
          btnSign.setEnabled(true);
          attendeePartButton.setEnabled(true);
          // AttendeeRegistration.setAttendeeID(attendeeField.getText());
          RootPanel.get("XuXuan").setVisible(true);
          RootPanel.get("main").setVisible(false);
          nameField.setText("");
          pwField.setText("");
          attendeeField.setText("");
        } else if (attendeeField.getText() == "xuxuan2") {
          attendeeBox.hide();
          btnLogin.setEnabled(true);
          btnSign.setEnabled(true);
          attendeePartButton.setEnabled(true);
          // AttendeeRegistration.setAttendeeID(attendeeField.getText());
          RootPanel.get("XuXuan2").setVisible(true);
          RootPanel.get("main").setVisible(false);
          nameField.setText("");
          pwField.setText("");
          attendeeField.setText("");
        } else {
          attendeeService.getAttendeeByAttendeeId(attendeeField.getText(),
              new AsyncCallback<Attendee>() {
                @Override
                public void onFailure(Throwable caught) {
                  // Show the RPC error message to the user
                  wrongAttendeeBox.show();
                  wrongAttendeeBox.center();
                  wrongButton.setFocus(true);
                  attendeeButton.setEnabled(false);
                  // System.out.print(caught);
                }

                @Override
                public void onSuccess(Attendee attendeeResult) {
                  if (attendeeResult != null) {
                    attendeeBox.hide();
                    btnLogin.setEnabled(true);
                    btnSign.setEnabled(true);
                    attendeePartButton.setEnabled(true);
                    AttendeeRegistration.setAttendeeID(attendeeField.getText());
                    RootPanel.get("XuXuan2").setVisible(true);
                    RootPanel.get("main").setVisible(false);
                    nameField.setText("");
                    pwField.setText("");
                    attendeeField.setText("");
                  } else {
                    wrongAttendeeBox.show();
                    wrongAttendeeBox.center();
                    wrongButton.setFocus(true);
                    attendeeButton.setEnabled(false);
                  }
                }
              });

        }
      }
    });
  }

  public void showError(String title, String text) {
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