/**
 * 
 */
package budgeteventplanner.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import budgeteventplanner.client.entity.Category;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author lzhen
 * 
 */
public class VendorHomePage implements EntryPoint {

  // server communication
  private final CategoryServiceAsync categoryService = GWT.create(CategoryService.class);

  private final VendorServiceAsync vendorServiceProvider = GWT.create(VendorService.class);

  private static final Logger log = Logger.getLogger(VendorHomePage.class.getName());

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
   */
  TabPanel tab;

  DockPanel vendorPage;
  Tree eventFolders;
  TreeItem pendingEvent;
  TreeItem acceptedEvent;
  TreeItem ignoredEvent;
  FlexTable events;
  // TreeItem eventHistory;
  // CAN DO

  DockPanel vendorService;
  FlexTable existingService;
  HorizontalPanel addPanel;
  ListBox category;
  // TODO
  TextBox service;
  TextArea description;
  TextBox price;
  Button addService;

  Hyperlink acceptHyperlink[];
  Hyperlink ignoreHyperlink[];
  Hyperlink viewHyperlink[];
  ArrayList<String> serviceRequestID;
  // ArrayList<String> categoryList;

  Hyperlink deleteHyperlink[];
  String[] deleteServiceID;
  String[] displayCategoryID;
  String categoryName;

  @Override
  public void onModuleLoad() {

    tab = new TabPanel(); // the bottom tab panel for vendor page

    vendorPage = new DockPanel(); // vendor home page panel
    vendorService = new DockPanel(); // vendor service management page panel
    addPanel = new HorizontalPanel(); // nested panel in the service panel

    eventFolders = new Tree(); // vendor home page folders
    pendingEvent = new TreeItem("Pending Events"); // pending
    acceptedEvent = new TreeItem("Accepted Events");// accepted
    ignoredEvent = new TreeItem("Ignored Events"); // ignored
    // eventHistory = new TreeItem("Events History");
    // CAN DO

    eventFolders.setWidth("150px");
    // size issue to be done
    eventFolders.addItem(pendingEvent);
    eventFolders.addItem(acceptedEvent);
    eventFolders.addItem(ignoredEvent);
    // eventFolders.addItem(eventHistory);
    // CAN DO

    events = new FlexTable(); // table for entries in a certain folder
    events.getColumnFormatter().setWidth(0, "100px");
    events.getColumnFormatter().setWidth(1, "100px");
    events.getColumnFormatter().setWidth(3, "100px");
    // size issue to be done

    existingService = new FlexTable(); // table for service management page
    // initializeServiceTable(); //new method to initialize the service
    // table
    refreshExistingService();
    refreshExistingService();
    refreshExistingService();

    vendorPage.add(eventFolders, DockPanel.WEST);
    vendorPage.add(events, DockPanel.EAST);

    category = new ListBox(); // get category from server
    initializeCategory();

    service = new TextBox();
    description = new TextArea();
    price = new TextBox();
    // description.setCharacterWidth(80); optional size method
    description.setWidth("600px");
    description.setVisibleLines(1);
    addService = new Button("ADD");
    addService.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {

        // check the categoryID
        categoryService.getAllCategory(new AsyncCallback<ArrayList<Category>>() {
          @Override
          public void onFailure(Throwable caught) {
            new DialogBox().setText("Remote Procedure Call - Failure");
          }

          @Override
          public void onSuccess(ArrayList<Category> result) {
            // get userID from the cookie:
            // String userID =
            // Cookies.getCookie("USERNAME");
            String userID = "lzhen";

            int sentCategory = category.getSelectedIndex();

            String sentCategoryID = result.get(sentCategory).getCategoryId();

            // maybe we need the index not the category name
            String sentText = service.getText();
            String sentDescription = description.getText();
            double sentPrice = Double.parseDouble(price.getText());

            // add to server
            vendorServiceProvider.addService(sentCategoryID, userID, sentText, sentPrice,
                sentDescription, new AsyncCallback<Void>() {
                  @Override
                  public void onFailure(Throwable caught) {
                    new DialogBox().setText("Remote Procedure Call - Failure");
                  }

                  @Override
                  public void onSuccess(Void result) {
                    RootPanel.get("ZhenLong").setVisible(false);
                    refreshExistingService();
                    service.setText("");
                    description.setText("");
                    price.setText("");
                    RootPanel.get("ZhenLong").setVisible(true);
                    // TODO

                  }

                });

          }

        });

        // refresh the table

        // RootPanel.get("ZhenLong").setVisible(false);

        // int oldRows = existingService
        // .getRowCount();
        // int newRows = oldRows + 1;
        // while(oldRows != newRows)
        // {
        // refreshExistingService();
        // oldRows = existingService
        // .getRowCount();
        // }

        // RootPanel.get("ZhenLong").setVisible(true);

        // TODO refresh

      }
    });

    addPanel.add(category);
    addPanel.add(service);
    addPanel.add(description);
    addPanel.add(price);
    addPanel.add(addService);
    vendorService.add(existingService, DockPanel.CENTER);
    vendorService.add(addPanel, DockPanel.SOUTH);
    category.setSize("150px", "100%");
    addPanel.setCellWidth(category, "200px");
    service.setSize("300px", "100%");
    addPanel.setCellWidth(service, "350px");
    addPanel.setCellWidth(description, "650px");
    price.setWidth("100px");
    addPanel.setCellWidth(price, "150px");
    addService.setWidth("100px");
    addPanel.setCellWidth(addService, "150px");

    tab.add(vendorPage, "Vendor");
    tab.add(vendorService, "Service");
    RootPanel.get("ZhenLong").add(tab);
    // add operations

    eventFolders.addSelectionHandler(new treeHandler<TreeItem>());
    // add listeners

    tab.selectTab(0);
    tab.setSize("100%", "100%");
    // tab initialization

  }

  // TODO may need to be removed
  public void initializeServiceTable() {
    // get data from the service, list the existing services
    existingService.getColumnFormatter().setWidth(0, "200px");
    existingService.getColumnFormatter().setWidth(1, "350px");
    existingService.getColumnFormatter().setWidth(2, "650px");
    existingService.getColumnFormatter().setWidth(3, "150px");
    existingService.getColumnFormatter().setWidth(4, "150px");
    existingService.setWidget(0, 0, new Label("Category"));
    existingService.setWidget(0, 1, new Label("Service"));
    existingService.setWidget(0, 2, new Label("Description"));
    existingService.setWidget(0, 3, new Label("Price"));
    existingService.setWidget(0, 4, new Label("Option"));

    refreshExistingService();
  }

  private String categoryFromidToname(String categoryID) {
    categoryService.getCategoryName(categoryID, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable caught) {
        new DialogBox().setText("Remote Procedure Call - Failure");
      }

      @Override
      public void onSuccess(String result) {
        categoryName = result;

      }
    });
    return categoryName;
  }

  @SuppressWarnings("deprecation")
  public void refreshExistingService() {
    existingService.clear();

    existingService.getColumnFormatter().setWidth(0, "200px");
    existingService.getColumnFormatter().setWidth(1, "350px");
    existingService.getColumnFormatter().setWidth(2, "650px");
    existingService.getColumnFormatter().setWidth(3, "150px");
    existingService.getColumnFormatter().setWidth(4, "150px");
    existingService.setWidget(0, 0, new Label("Category"));
    existingService.setWidget(0, 1, new Label("Service"));
    existingService.setWidget(0, 2, new Label("Description"));
    existingService.setWidget(0, 3, new Label("Price"));
    existingService.setWidget(0, 4, new Label("Option"));

    // String userID = Cookies.getCookie("USERNAME");
    String userID = "lzhen";
    // DialogBox a = new DialogBox();
    // a.setText("Remote Procedure Call - Failure");
    // a.show();
    vendorServiceProvider.getServiceByVendorId(userID,
        new AsyncCallback<List<Entry<String, Service>>>() {
          @Override
          public void onFailure(Throwable caught) {
            // TODO
          }

          public void onSuccess(List<Entry<String, Service>> result) {
            deleteHyperlink = new Hyperlink[result.size()];
            deleteServiceID = new String[result.size()];
            displayCategoryID = new String[result.size()];
            // categoryList = new ArrayList<String>();

            for (int i = 0; i < result.size(); i++) {
              deleteServiceID[i] = result.get(i).getValue().getServiceId();
              displayCategoryID[i] = result.get(i).getValue().getCategoryId();
              // categoryList.add(new
              // String(categoryFromidToname(result.get(i)
              // .getCategoryId())));
            }

            for (int i = 0; i < result.size(); i++) {

              existingService.setWidget(i + 1, 0, new Label(result.get(i).getKey()));
              // category
              // name

              deleteHyperlink[i] = new Hyperlink("delete", Integer.toString(i));
              existingService.setWidget(i + 1, 1, new Label(result.get(i).getValue().getName()));
              existingService.setWidget(i + 1, 2, new Label(result.get(i).getValue()
                  .getDescription()));
              existingService.setWidget(i + 1, 3, new Label(result.get(i).getValue().getPrice()
                  .toString()));
              existingService.setWidget(i + 1, 4, deleteHyperlink[i]);

              // listener add
              deleteHyperlink[i].addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                  int i;
                  for (i = 0; i < existingService.getRowCount(); i++) {
                    if (event.getSource().equals(existingService.getWidget(i, 4))) {
                      break;
                    }
                  }

                  // TODO deleteServiceID[i - 1]
                  vendorServiceProvider.deleteService(deleteServiceID[i - 1],
                      new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                          // TODO
                        }

                        @Override
                        public void onSuccess(Void result) {
                          RootPanel.get("ZhenLong").setVisible(false);
                          refreshExistingService();// TODO
                          RootPanel.get("ZhenLong").setVisible(true);
                        }
                      });

                  // RootPanel.get("ZhenLong")
                  // .setVisible(false);

                  // refreshExistingService();

                  // RootPanel.get("ZhenLong")
                  // .setVisible(true);
                }
              });
            }
          }
        });

    // a.setText(displayCategoryID[2]);
    // a.show();
    // initialize category column here
    // log.warning(deleteServiceID[5]);
    // for(int i = 0; i < displayCategoryID.length; i++)
    // {
    // new DialogBox()
    // .setText("Remote Procedure Call - Failure");
    // existingService.setWidget(i + 1, 0, new
    // Label(categoryFromidToname(displayCategoryID[i])));
    // }

  }

  public void initializeCategory() {
    // ArrayList<Category> allCategories;

    // get information from server
    categoryService.getAllCategory(new AsyncCallback<ArrayList<Category>>() {
      @Override
      public void onFailure(Throwable caught) {
        new DialogBox().setText("Remote Procedure Call - Failure");
      }

      @Override
      public void onSuccess(ArrayList<Category> result) {
        for (Category c : result) {
          category.addItem(c.getName());
        }
      }

    });

  }

  @SuppressWarnings("hiding")
  public class treeHandler<TreeItem> implements SelectionHandler<TreeItem> {
    public void onSelection(SelectionEvent<TreeItem> event) {
      Object tmp = event.getSelectedItem();
      // TODO cookie userID
      String userID = "lzhen";
      // TODO all status should be integer

      if (tmp == pendingEvent) {
        vendorServiceProvider.getServiceRequestByStatus(userID, ServiceRequest.PENDING,
            new AsyncCallback<List<ServiceRequest>>() {

              @Override
              public void onFailure(Throwable caught) {

              }

              @SuppressWarnings("deprecation")
              @Override
              public void onSuccess(List<ServiceRequest> result) {
                events.clear();
                // 3 hyberlinks: view/accept/ignore
                // read from sever the number of entries

                acceptHyperlink = new Hyperlink[result.size()];
                ignoreHyperlink = new Hyperlink[result.size()];
                viewHyperlink = new Hyperlink[result.size()];
                serviceRequestID = new ArrayList<String>();

                vendorPage.setBorderWidth(1);
                // border display

                for (int i = 0; i < result.size(); i++) {
                  serviceRequestID.add(result.get(i).getRequestId());
                }

                events.setWidget(0, 0, new Label("Events"));
                events.setWidget(0, 3, new Label("Options"));
                for (int i = 0; i < result.size(); i++) {
                  events.setWidget(i + 1, 0, new Label(result.get(i).getName()));
                  events.getCellFormatter().setWidth(i, 0, "90%");
                  viewHyperlink[i] = new Hyperlink("View", Integer.toString(i));
                  ignoreHyperlink[i] = new Hyperlink("Ignore", Integer.toString(i));
                  acceptHyperlink[i] = new Hyperlink("Accept", Integer.toString(i));
                  events.setWidget(i + 1, 1, viewHyperlink[i]);
                  events.setWidget(i + 1, 2, acceptHyperlink[i]);
                  events.setWidget(i + 1, 3, ignoreHyperlink[i]);

                  final String requestName = result.get(i).getName();
                  final String requestDate = result.get(i).getDueDate().toString();
                  // listener add
                  viewHyperlink[i].addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                      int i;
                      for (i = 0; i < viewHyperlink.length; i++) {
                        if (event.getSource() == viewHyperlink[i]) {
                          break;
                        }
                      }

                      // pop up the current event
                      // info
                      final DialogBox currentEvent = new DialogBox();

                      currentEvent.setAnimationEnabled(true);
                      final Button closeButton = new Button("Close");
                      closeButton.getElement().setId("closeButton");

                      VerticalPanel dialogVPanel = new VerticalPanel();
                      dialogVPanel.addStyleName("dialogVPanel");

                      dialogVPanel.add(new Label(requestName));
                      dialogVPanel.add(new Label(requestDate));
                      // TODO display request
                      // detail info
                      dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
                      dialogVPanel.add(closeButton);
                      currentEvent.setWidget(dialogVPanel);

                      // Add a handler to close
                      // the DialogBox
                      closeButton.addClickHandler(new ClickHandler() {
                        public void onClick(ClickEvent event) {
                          currentEvent.hide();
                        }
                      });

                      currentEvent.center();

                    }
                  }); // view listener

                  acceptHyperlink[i].addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                      int i;
                      for (i = 0; i < events.getRowCount(); i++) {
                        if (event.getSource().equals(events.getWidget(i, 2))) {
                          break;
                        }
                      }
                      // remove from pending
                      // folder
                      // communication to server,
                      vendorServiceProvider.updateServiceRequestStatus(serviceRequestID.get(i - 1),
                          1, new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
                              // TODO
                            }

                            @Override
                            public void onSuccess(Void result) {
                              // TODO
                            }
                          });

                      events.removeRow(i);
                      serviceRequestID.remove(i - 1);
                    }
                  }); // accept listener

                  ignoreHyperlink[i].addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                      int i;
                      for (i = 0; i < events.getRowCount(); i++) {
                        if (event.getSource().equals(events.getWidget(i, 3))) {
                          break;
                        }
                      }
                      // remove from pending
                      // folder
                      // communication to server,
                      vendorServiceProvider.updateServiceRequestStatus(serviceRequestID.get(i - 1),
                          2, new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
                              // TODO
                            }

                            @Override
                            public void onSuccess(Void result) {
                              // TODO
                            }
                          });

                      events.removeRow(i);
                      serviceRequestID.remove(i - 1);

                    }
                  });

                }
              }
            });
      }// end of pending service request

      else if (tmp == acceptedEvent) {
        vendorServiceProvider.getServiceRequestByStatus(userID, ServiceRequest.ACCEPTED,
            new AsyncCallback<List<ServiceRequest>>() {

              @Override
              public void onFailure(Throwable caught) {

              }

              @Override
              public void onSuccess(List<ServiceRequest> result) {

                events.clear();
                // 2 buttons: view(/ignore)
                // ignoreHyperlink = new Hyperlink[3];
                viewHyperlink = new Hyperlink[result.size()];

                vendorPage.setBorderWidth(1);
                // border display

                events.setWidget(0, 0, new Label("Events"));
                events.setWidget(0, 3, new Label("Options"));
                for (int i = 0; i < result.size(); i++) {
                  events.setWidget(i + 1, 0, new Label(result.get(i).getName()));
                  events.getCellFormatter().setWidth(i, 0, "90%");
                  viewHyperlink[i] = new Hyperlink("View", Integer.toString(i));
                  events.setWidget(i + 1, 1, viewHyperlink[i]);

                  final String requestName = result.get(i).getName();
                  final String requestDate = result.get(i).getDueDate().toString();
                  // listener add
                  viewHyperlink[i].addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                      int i;
                      for (i = 0; i < viewHyperlink.length; i++) {
                        if (event.getSource() == viewHyperlink[i]) {
                          break;
                        }
                      }

                      // pop up the current event
                      // info
                      final DialogBox currentEvent = new DialogBox();

                      currentEvent.setAnimationEnabled(true);
                      final Button closeButton = new Button("Close");
                      closeButton.getElement().setId("closeButton");

                      VerticalPanel dialogVPanel = new VerticalPanel();
                      dialogVPanel.addStyleName("dialogVPanel");

                      dialogVPanel.add(new Label(requestName));
                      dialogVPanel.add(new Label(requestDate));
                      // TODO display request
                      // detail info
                      dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
                      dialogVPanel.add(closeButton);
                      currentEvent.setWidget(dialogVPanel);

                      // Add a handler to close
                      // the DialogBox
                      closeButton.addClickHandler(new ClickHandler() {
                        public void onClick(ClickEvent event) {
                          currentEvent.hide();
                        }
                      });

                      currentEvent.center();

                    }
                  }); // view listener
                }

              }
            });

      } else if (tmp == ignoredEvent) {

        vendorServiceProvider.getServiceRequestByStatus(userID, ServiceRequest.IGNORED,
            new AsyncCallback<List<ServiceRequest>>() {

              @Override
              public void onFailure(Throwable caught) {

              }

              @SuppressWarnings("deprecation")
              @Override
              public void onSuccess(List<ServiceRequest> result) {

                events.clear();
                // 2 buttons: view(/accept)
                // ignoreHyperlink = new Hyperlink[3];
                viewHyperlink = new Hyperlink[result.size()];

                vendorPage.setBorderWidth(1);
                // border display

                events.setWidget(0, 0, new Label("Events"));
                events.setWidget(0, 3, new Label("Options"));
                for (int i = 0; i < result.size(); i++) {
                  events.setWidget(i + 1, 0, new Label(result.get(i).getName()));
                  events.getCellFormatter().setWidth(i, 0, "90%");
                  viewHyperlink[i] = new Hyperlink("View", Integer.toString(i));
                  events.setWidget(i + 1, 1, viewHyperlink[i]);

                  final String requestName = result.get(i).getName();
                  final String requestDate = result.get(i).getDueDate().toString();
                  // listener add
                  viewHyperlink[i].addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                      int i;
                      for (i = 0; i < viewHyperlink.length; i++) {
                        if (event.getSource() == viewHyperlink[i]) {
                          break;
                        }
                      }

                      // pop up the current event
                      // info
                      final DialogBox currentEvent = new DialogBox();

                      currentEvent.setAnimationEnabled(true);
                      final Button closeButton = new Button("Close");
                      closeButton.getElement().setId("closeButton");

                      VerticalPanel dialogVPanel = new VerticalPanel();
                      dialogVPanel.addStyleName("dialogVPanel");

                      dialogVPanel.add(new Label(requestName));
                      dialogVPanel.add(new Label(requestDate));
                      // TODO display request
                      // detail info
                      dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
                      dialogVPanel.add(closeButton);
                      currentEvent.setWidget(dialogVPanel);

                      // Add a handler to close
                      // the DialogBox
                      closeButton.addClickHandler(new ClickHandler() {
                        public void onClick(ClickEvent event) {
                          currentEvent.hide();
                        }
                      });

                      currentEvent.center();

                    }
                  }); // view listener
                }

              }
            });

        // // 2 buttons: view/accept
        // events.clear();
        // // 2 buttons: view/ignore
        // // 3 hyberlinks: view/accept/ignore
        // // read from sever the number of entries
        // acceptHyperlink = new Hyperlink[30];
        // // ignoreHyperlink = new Hyperlink[3];
        // viewHyperlink = new Hyperlink[30];
        //
        // // ----------------
        // vendorPage.setBorderWidth(1);
        // // border display
        //
        // Date today = new Date();
        // // Event name
        //
        // // prints Tue Dec 18 12:01:26 GMT-500 2007 in the default
        // // locale.
        // // GWT.log(today.toString(), null);
        // final Label[] a = new Label[30];
        // for (int i = 0; i < 30; i++)
        // a[i] = new Label(today.toString());
        //
        // events.setWidget(0, 0, new Label("Events"));
        // events.setWidget(0, 2, new Label("Options"));
        // // events.setText(0, 2, "Events");
        // // events.setText(0, 3, "Events");
        // for (int i = 0; i < 30; i++) {
        // events.setWidget(i + 1, 0, a[i]);
        // events.getCellFormatter().setWidth(i, 0, "90%");
        // viewHyperlink[i] = new Hyperlink("View",
        // Integer.toString(i));
        // // ignoreHyperlink[i] = new Hyperlink("ignore", "");
        // acceptHyperlink[i] = new Hyperlink("Accept",
        // Integer.toString(i));
        // events.setWidget(i + 1, 1, viewHyperlink[i]);
        // events.setWidget(i + 1, 2, acceptHyperlink[i]);
        // // events.setWidget(i+1, 3, ignoreHyperlink[i]);
        //
        // // listener add
        // viewHyperlink[i].addClickHandler(new ClickHandler() {
        // public void onClick(ClickEvent event) {
        // int i;
        // for (i = 0; i < viewHyperlink.length; i++) {
        // if (event.getSource() == viewHyperlink[i]) {
        // break;
        // }
        // }
        //
        // // pop up the current event info
        // final DialogBox currentEvent = new DialogBox();
        // // .setWidget(a[i]);
        // currentEvent.setAnimationEnabled(true);
        // final Button closeButton = new Button("Close");
        // closeButton.getElement().setId("closeButton");
        // // final Label textToServerLabel = new Label();
        // // final HTML serverResponseLabel = new HTML();
        // VerticalPanel dialogVPanel = new VerticalPanel();
        // dialogVPanel.addStyleName("dialogVPanel");
        // dialogVPanel.add(new Label(a[i].getText()));
        // // dialogVPanel.add(textToServerLabel);
        // // dialogVPanel.add(new
        // // HTML("<br><b>Server replies:</b>"));
        // // dialogVPanel.add(serverResponseLabel);
        // dialogVPanel
        // .setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        // dialogVPanel.add(closeButton);
        // currentEvent.setWidget(dialogVPanel);
        //
        // // Add a handler to close the DialogBox
        // closeButton.addClickHandler(new ClickHandler() {
        // public void onClick(ClickEvent event) {
        // currentEvent.hide();
        // // sendButton.setEnabled(true);
        // // sendButton.setFocus(true);
        // }
        // });
        //
        // currentEvent.center();
        //
        // }
        // }); // view listener
        //
        // acceptHyperlink[i].addClickHandler(new ClickHandler() {
        // public void onClick(ClickEvent event) {
        // int i;
        // for (i = 0; i < events.getRowCount(); i++) {
        // if (event.getSource().equals(
        // events.getWidget(i, 2))) {
        // break;
        // }
        // }// do we need this loop? one listener only detect
        // // one hyper;ink
        //
        // // move to accepted folder
        // // remove from pending folder
        // // communication to server, inform the event manager
        // // re-get the list and refresh
        // events.removeRow(i);
        // }
        // }); // accept listener
        //
        // }
        // // hyberlinks listeners
        // // ----------------
      }
    }
  }

  public void requestTableRefresh(int status) {
    // TODO
    ;// receive data from server and refresh the entire table
  }

}