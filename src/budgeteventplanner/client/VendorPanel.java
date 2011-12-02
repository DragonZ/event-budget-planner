package budgeteventplanner.client;

import java.util.List;

import budgeteventplanner.client.entity.Category;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;
import budgeteventplanner.shared.Pair;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
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

public class VendorPanel extends Composite {
  private final CategoryServiceAsync categoryService = GWT.create(CategoryService.class);
  private final VendorServiceAsync vendorService = GWT.create(VendorService.class);

  TabPanel tab;

  DockPanel panelServiceRequest;
  Tree eventFolders;
  TreeItem pendingEvent;
  TreeItem acceptedEvent;
  TreeItem ignoredEvent;
  FlexTable events;
  // TreeItem eventHistory;
  // CAN DO

  DockPanel panelService;
  FlexTable existingService;
  HorizontalPanel addPanel;
  ListBox category;
  TextBox service;
  TextArea description;
  TextBox price;
  Button addService;

  Hyperlink acceptHyperlink[];
  Hyperlink ignoreHyperlink[];
  Hyperlink viewHyperlink[];
  List<String> serviceRequestId;

  Hyperlink deleteHyperlink[];
  String[] deleteServiceID;
  String[] displayCategoryID;
  String categoryName;

  public VendorPanel() {

    tab = new TabPanel(); // the top panel for vendor page

    panelServiceRequest = new DockPanel(); // vendor home page panel
    panelService = new DockPanel(); // vendor service management page panel
    addPanel = new HorizontalPanel(); // nested panel in the service panel

    eventFolders = new Tree(); // vendor home page folders
    pendingEvent = new TreeItem("Pending Requests"); // pending
    acceptedEvent = new TreeItem("Accepted Requests");// accepted
    ignoredEvent = new TreeItem("Ignored Requests"); // ignored
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

    panelServiceRequest.add(eventFolders, DockPanel.WEST);
    panelServiceRequest.add(events, DockPanel.EAST);

    category = new ListBox(); // get category from server
    initializeCategory();

    service = new TextBox();
    description = new TextArea();
    price = new TextBox();
    // description.setCharacterWidth(80); optional size method
    description.setWidth("600px");
    description.setVisibleLines(1);
    addService = new Button("Add");
    
    addService.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {

        // check the categoryID
        categoryService.getAllCategory(new AsyncCallback<List<Category>>() {
          @Override
          public void onFailure(Throwable caught) {
            new DialogBox().setText("Remote Procedure Call - Failure");
          }

          @Override
          public void onSuccess(List<Category> result) {
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
            vendorService.addService(sentCategoryID, userID, sentText, sentPrice,
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
                  }
                });
          }
        });
      }
    });

    addPanel.add(category);
    addPanel.add(service);
    addPanel.add(description);
    addPanel.add(price);
    addPanel.add(addService);
    panelService.add(existingService, DockPanel.CENTER);
    panelService.add(addPanel, DockPanel.SOUTH);
    category.setSize("150px", "100%");
    addPanel.setCellWidth(category, "200px");
    service.setSize("300px", "100%");
    addPanel.setCellWidth(service, "350px");
    addPanel.setCellWidth(description, "650px");
    price.setWidth("100px");
    addPanel.setCellWidth(price, "150px");
    addService.setWidth("100px");
    addPanel.setCellWidth(addService, "150px");

    tab.add(panelServiceRequest, "Service Request");
    tab.add(panelService, "Service");
    // add operations

    eventFolders.addSelectionHandler(new TreeHandler<TreeItem>());
    // add listeners

    refreshExistingService();
    tab.selectTab(0);
    tab.setSize("100%", "100%");

    initWidget(tab);
  }

  @SuppressWarnings("deprecation")
  public void refreshExistingService() {
    existingService.clear();

    existingService.getColumnFormatter().setWidth(0, "200px");
    existingService.getColumnFormatter().setWidth(1, "350px");
    existingService.getColumnFormatter().setWidth(2, "650px");
    existingService.getColumnFormatter().setWidth(3, "150px");
    existingService.getColumnFormatter().setWidth(4, "150px");
    existingService.setWidget(0, 0, new HTML("<b>Category</b>"));
    existingService.setWidget(0, 1, new HTML("<b>Service</b>"));
    existingService.setWidget(0, 2, new HTML("<b>Description</b>"));
    existingService.setWidget(0, 3, new HTML("<b>Price</b>"));
    existingService.setWidget(0, 4, new HTML("<b>Option</b>"));

    // String userID = Cookies.getCookie("USERNAME");
    String userID = "lzhen";
    
    vendorService.getServiceByVendorId(userID,
        new AsyncCallback<List<Pair<String, Service>>>() {
          @Override
          public void onFailure(Throwable caught) {
            // TODO
          }

          public void onSuccess(List<Pair<String, Service>> result) {
            deleteHyperlink = new Hyperlink[result.size()];
            deleteServiceID = new String[result.size()];
            displayCategoryID = new String[result.size()];

            for (int i = 0; i < result.size(); i++) {
              deleteServiceID[i] = result.get(i).getB().getServiceId();
              displayCategoryID[i] = result.get(i).getB().getCategoryId();
            }

            for (int i = 0; i < result.size(); i++) {

              existingService.setWidget(i + 1, 0, new Label(result.get(i).getA()));
              // category
              // name

              deleteHyperlink[i] = new Hyperlink("delete", Integer.toString(i));
              existingService.setWidget(i + 1, 1, new Label(result.get(i).getB().getName()));
              existingService.setWidget(i + 1, 2, new Label(result.get(i).getB().getDescription()));
              existingService.setWidget(i + 1, 3, new Label(result.get(i).getB().getPrice()
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
                  vendorService.deleteService(deleteServiceID[i - 1],
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
                }
              });
            }
          }
        });

  }

  public void initializeCategory() {
    // ArrayList<Category> allCategories;

    // get information from server
    categoryService.getAllCategory(new AsyncCallback<List<Category>>() {
      @Override
      public void onFailure(Throwable caught) {
        new DialogBox().setText("Remote Procedure Call - Failure");
      }

      @Override
      public void onSuccess(List<Category> result) {
        for (Category c : result) {
          category.addItem(c.getName());
        }
      }

    });

  }

  @SuppressWarnings("hiding")
  class TreeHandler<TreeItem> implements SelectionHandler<TreeItem> {
    public void onSelection(SelectionEvent<TreeItem> event) {
      Object tmp = event.getSelectedItem();
      // TODO cookie userID
      String userID = "lzhen";
      // TODO all status should be integer

      if (tmp == pendingEvent) {
        vendorService.getServiceRequestByStatus(userID, ServiceRequest.PENDING,
            new AsyncCallback<List<Pair<String, ServiceRequest>>>() {

              @Override
              public void onFailure(Throwable caught) {

              }

              @SuppressWarnings("deprecation")
              @Override
              public void onSuccess(List<Pair<String, ServiceRequest>> result) {
                events.clear();
                // 3 hyberlinks: view/accept/ignore
                // read from sever the number of entries

                acceptHyperlink = new Hyperlink[result.size()];
                ignoreHyperlink = new Hyperlink[result.size()];
                viewHyperlink = new Hyperlink[result.size()];
                serviceRequestId = Lists.newArrayList();

                panelServiceRequest.setBorderWidth(1);
                // border display

                for (int i = 0; i < result.size(); i++) {
                  serviceRequestId.add(result.get(i).getB().getRequestId());
                }

                events.setWidget(0, 0, new HTML("<b>Requests</b>"));
                events.setWidget(0, 3, new HTML("<b>Options</b>"));
                for (int i = 0; i < result.size(); i++) {
                  events.setWidget(i + 1, 0, new Label(result.get(i).getB().getName()));
                  events.getCellFormatter().setWidth(i, 0, "90%");
                  viewHyperlink[i] = new Hyperlink("View", Integer.toString(i));
                  ignoreHyperlink[i] = new Hyperlink("Ignore", Integer.toString(i));
                  acceptHyperlink[i] = new Hyperlink("Accept", Integer.toString(i));
                  events.setWidget(i + 1, 1, viewHyperlink[i]);
                  events.setWidget(i + 1, 2, acceptHyperlink[i]);
                  events.setWidget(i + 1, 3, ignoreHyperlink[i]);

                  final String requestService = result.get(i).getA();
                  final String requestName = result.get(i).getB().getName();
                  final String requestDate = result.get(i).getB().getDueDate().toString();
                  final String requestQuatity = result.get(i).getB().getQuantity().toString();
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

                      dialogVPanel.add(new HTML("<b>Request Service Name: </b>" + requestService));
                      dialogVPanel.add(new HTML("<b>Quantity: </b>" + "10"/*
                                                                           * +
                                                                           * requestQuatity
                                                                           */));
                      dialogVPanel.add(new HTML("<b>Due Date: </b>" + requestDate));
                      dialogVPanel.add(new HTML("<br>"));
                      dialogVPanel.add(new HTML("<b>Detail: </b>"));
                      dialogVPanel.add(new Label(requestName));

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
                      vendorService.updateServiceRequestStatus(serviceRequestId.get(i - 1),
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
                      serviceRequestId.remove(i - 1);
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
                      vendorService.updateServiceRequestStatus(serviceRequestId.get(i - 1),
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
                      serviceRequestId.remove(i - 1);

                    }
                  });

                }
              }
            });
      }// end of pending service request

      else if (tmp == acceptedEvent) {
        vendorService.getServiceRequestByStatus(userID, ServiceRequest.ACCEPTED,
            new AsyncCallback<List<Pair<String, ServiceRequest>>>() {

              @Override
              public void onFailure(Throwable caught) {

              }

              @SuppressWarnings("deprecation")
              @Override
              public void onSuccess(List<Pair<String, ServiceRequest>> result) {

                events.clear();
                // 2 buttons: view(/ignore)
                // ignoreHyperlink = new Hyperlink[3];
                viewHyperlink = new Hyperlink[result.size()];

                panelServiceRequest.setBorderWidth(1);
                // border display

                events.setWidget(0, 0, new HTML("<b>Requests</b>"));
                events.setWidget(0, 1, new HTML("<b>Options</b>"));
                for (int i = 0; i < result.size(); i++) {
                  events.setWidget(i + 1, 0, new Label(result.get(i).getB().getName()));
                  events.getCellFormatter().setWidth(i, 0, "90%");
                  viewHyperlink[i] = new Hyperlink("View", Integer.toString(i));
                  events.setWidget(i + 1, 1, viewHyperlink[i]);

                  final String requestService = result.get(i).getA();
                  final String requestName = result.get(i).getB().getName();
                  final String requestDate = result.get(i).getB().getDueDate().toString();
                  final String requestQuatity = result.get(i).getB().getQuantity().toString();
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

                      dialogVPanel.add(new HTML("<b>Request Service Name: </b>" + requestService));
                      dialogVPanel.add(new HTML("<b>Quantity: </b>" + "10"/*
                                                                           * +
                                                                           * requestQuatity
                                                                           */));
                      dialogVPanel.add(new HTML("<b>Due Date: </b>" + requestDate));
                      dialogVPanel.add(new HTML("<br>"));
                      dialogVPanel.add(new HTML("<b>Detail: </b>"));
                      dialogVPanel.add(new Label(requestName));
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

        vendorService.getServiceRequestByStatus(userID, ServiceRequest.IGNORED,
            new AsyncCallback<List<Pair<String, ServiceRequest>>>() {

              @Override
              public void onFailure(Throwable caught) {

              }

              @SuppressWarnings("deprecation")
              @Override
              public void onSuccess(List<Pair<String, ServiceRequest>> result) {

                events.clear();
                // 2 buttons: view(/accept)
                viewHyperlink = new Hyperlink[result.size()];

                panelServiceRequest.setBorderWidth(1);
                // border display

                events.setWidget(0, 0, new HTML("<b>Requests</b>"));
                events.setWidget(0, 1, new HTML("<b>Options</b>"));
                for (int i = 0; i < result.size(); i++) {
                  events.setWidget(i + 1, 0, new Label(result.get(i).getB().getName()));
                  events.getCellFormatter().setWidth(i, 0, "90%");
                  viewHyperlink[i] = new Hyperlink("View", Integer.toString(i));
                  events.setWidget(i + 1, 1, viewHyperlink[i]);

                  final String requestService = result.get(i).getA();
                  final String requestName = result.get(i).getB().getName();
                  final String requestDate = result.get(i).getB().getDueDate().toString();
                  final String requestQuatity = result.get(i).getB().getQuantity().toString();
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

                      dialogVPanel.add(new HTML("<b>Request Service Name: </b>" + requestService));
                      dialogVPanel.add(new HTML("<b>Quantity: </b>" + "10"/*
                                                                           * +
                                                                           * requestQuatity
                                                                           */));
                      dialogVPanel.add(new HTML("<b>Due Date: </b>" + requestDate));
                      dialogVPanel.add(new HTML("<br>"));
                      dialogVPanel.add(new HTML("<b>Detail: </b>"));
                      dialogVPanel.add(new Label(requestName));
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

      }
    }
  }

}
