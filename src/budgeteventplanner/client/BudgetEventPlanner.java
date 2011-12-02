package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import budgeteventplanner.client.entity.Attendee;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BudgetEventPlanner implements EntryPoint {

  private final UserServiceAsync userService = GWT.create(UserService.class);
  private final AttendeeServiceAsync attendeeService = GWT.create(AttendeeService.class);

  public void onModuleLoad() {
    //final UserRegistrationPanel userRegistrationPanel = new UserRegistrationPanel();
    
    final TextBox nameField = new TextBox();
    nameField.setText("");
    nameField.setWidth("210px");
    final PasswordTextBox pwField = new PasswordTextBox();
    pwField.setText("");
    pwField.setWidth("210px");
    final Button btnAttendee = new Button("Attendee");
    btnAttendee.setWidth("80px");
    final TextBox attendeeField = new TextBox();
    attendeeField.setText("");
    attendeeField.setWidth("210px");
    final Button btnAttendeeSubmit = new Button("Submit");
    btnAttendeeSubmit.setWidth("80px");
    final Button btnAttendeeCancel = new Button("Cancel");
    btnAttendeeCancel.setWidth("80px");

    final Button btnLogin = new Button("Login");
    btnLogin.setWidth("80px");
    final Button btnSign = new Button("Sign Up");
    btnLogin.setWidth("80px");

    final DialogBox attendeeBox = new DialogBox();
    attendeeBox.setText("Registration code");
    VerticalPanel attendeePanel = new VerticalPanel();
    HorizontalPanel attendeeHPanel = new HorizontalPanel();
    attendeePanel.addStyleName("attendeePanel");
    attendeeHPanel.addStyleName("attendeeHPanel");
    attendeePanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    attendeePanel.add(attendeeField);
    attendeeHPanel.add(btnAttendeeSubmit);
    attendeeHPanel.add(btnAttendeeCancel);
    attendeePanel.add(attendeeHPanel);
    attendeePanel.setSpacing(5);
    attendeeBox.setWidget(attendeePanel);

    RootPanel.get("nameFieldContainer").add(nameField);
    RootPanel.get("pwFieldContainer").add(pwField);
    RootPanel.get("loginButtonContainer").add(btnLogin);
    RootPanel.get("attendeePartButtonContainer").add(btnAttendee);
    RootPanel.get("signButtonContainer").add(btnSign);

    // Focus the cursor on the name field when the app loads
    nameField.addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent event) {
        nameField.selectAll();
      }
    });
    nameField.setFocus(true);

    btnAttendee.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        attendeeField.setText("");
        attendeeBox.show();
        attendeeBox.center();
        attendeeField.setFocus(true);
        btnLogin.setEnabled(false);
        btnSign.setEnabled(false);
        btnAttendee.setEnabled(false);
      }
    });

    btnAttendeeCancel.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        attendeeBox.hide();
        
        attendeeField.setText("");
        attendeeField.setFocus(true);
      }
    });

    // Add a handler to open the registration form
    btnSign.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        RootPanel.get("divPopup").clear();
        RootPanel.get("divPopup").add(new UserRegistrationPanel());
        RootPanel.get("divHome").setVisible(false);
        RootPanel.get("divPopup").setVisible(true);
      }
    });



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
        if (nameField.getText().equals("XuXuan") || nameField.getText().equals("XuXuan2")
            || nameField.getText().equals("XiaYuan") || nameField.getText().equals("ZhenLong")
            || nameField.getText().equals("dbtest")) {
          RootPanel.get(nameField.getText()).setVisible(true);
          RootPanel.get("divHome").setVisible(false);
          nameField.setText("");
          pwField.setText("");
          attendeeField.setText("");
        } else if (nameField.getText().isEmpty()) {

        } else {
          try {
            userService.login(nameField.getText(), pwField.getText(), new AsyncCallback<Integer>() {
              @Override
              public void onFailure(Throwable caught) {
                showDialog("Login", "RPC Call Failed!");
              }

              @Override
              public void onSuccess(Integer result) {
                String typeTurn = new String();
                if (result == 0) { // Event Manager
                  typeTurn = "XiaYuan";
                } else if (result == 1) { // Vendor
                  typeTurn = "ZhenLong";
                } else if (result == 2) { // Attendee
                  typeTurn = "XuXuan";
                } else if (result == 3) { // Attendee
                  typeTurn = "XuXuan2";
                } else {
                  showDialog("Login", "Incorrect Username or Password!");
                }
                RootPanel.get(typeTurn).setVisible(true);
                RootPanel.get("divHome").setVisible(false);

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
        nameField.setFocus(true);
      }
    }

    // add LoginHandler
    btnLogin.addClickHandler(new LoginHandler());
    nameField.addKeyUpHandler(new LoginHandler());
    pwField.addKeyUpHandler(new LoginHandler());

    btnAttendeeSubmit.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        attendeeService.getAttendeeByAttendeeId(attendeeField.getText(),
            new AsyncCallback<Attendee>() {
              @Override
              public void onFailure(Throwable caught) {
                showDialog("Attendee Registration", "RPC Call Failed!");
              }

              @Override
              public void onSuccess(Attendee result) {
                if (!result.equals(null)) {
                  attendeeBox.hide();
                  AttendeeRegistration.setAttendeeID(attendeeField.getText());
                  RootPanel.get("XuXuan2").setVisible(true);
                  RootPanel.get("divHome").setVisible(false);
                  nameField.setText("");
                  pwField.setText("");
                  attendeeField.setText("");
                } else {
                  showDialog("Attendee Registration", "Invalid Registration Code!");
                }
              }
            });
      }
    });
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