package budgeteventplanner.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import budgeteventplanner.client.entity.TestEntity;
import budgeteventplanner.shared.Pair;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;

public class DatabaseTestPage implements EntryPoint {

	private final DataServiceAsync dataService = GWT.create(DataService.class);

	// private final UserServiceAsync userService =
	// GWT.create(UserService.class);

	private final EventServiceAsync eventService = GWT
			.create(EventService.class);

	private final BudgetServiceAsync budgetService = GWT
			.create(BudgetService.class);

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

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				eventService.addServiceRequest(
						"5ADD1AFE-0E73-4923-B9C3-78A3130EFDC7", "XXX",
						"food for today", null, new Date(2011, 11, 23),
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
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML("RPC Call Failed!");
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

		final Button getChart = new Button("chart");
		RootPanel.get("databseContainer").add(getChart);
		class DBChartHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				budgetService
						.getSubtotalsByEventId(
								"agtzfnB1cmR1ZWJlcHIvCxIFRXZlbnQiJDRCQ0M5MDc3LUM2QzgtNEVFNS1BNTQzLUYzQjk0RkM3MTZBOQw",
								new AsyncCallback<List<Pair<String, Double>>>() {
									@Override
									public void onFailure(Throwable caught) {
										// Show the RPC error message to the
										// user
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
									public void onSuccess(
											List<Pair<String, Double>> result) {
										dialogBox
												.setText("Remote Procedure Call");
										serverResponseLabel
												.removeStyleName("serverResponseLabelError");
										serverResponseLabel.setHTML("Success");
										dialogBox.center();
										closeButton.setFocus(true);

										/*-----------------------------------------------------------------*/
										DataTable data = DataTable.create();
										data.addColumn(ColumnType.STRING,
												"Category");
										data.addColumn(ColumnType.NUMBER,
												"Cost");

										int ithRow = 1;
										int ithCol = 1;

										for (Pair<String, Double> pair : result) {
											data.addRows(1);
											data.setValue(ithRow, ithCol++,
													pair.getA());
											data.setValue(ithRow, ithCol++,
													pair.getB());

											ithRow++;
											ithCol = 1;
										}

										final AbstractDataTable finalData = data;
										/*-----------------------------------------------------------------*/
										// Create a callback to be called when
										// the visualization API
										// has been loaded.
										Runnable onLoadCallback = new Runnable() {
											public void run() {
												Panel panel = RootPanel
														.get("databseContainer");

												// Create a pie chart
												// visualization.
												PieChart pie = new PieChart(
														finalData,
														createOptions());
												BarChart bar = new BarChart(
														finalData,
														createOptions());
												pie.addSelectHandler(createSelectHandler(pie));
												panel.add(pie);
												panel.add(bar);
											}
										};

										// Load the visualization api, passing
										// the onLoadCallback to be called
										// when loading is done.
										VisualizationUtils
												.loadVisualizationApi(
														onLoadCallback,
														CoreChart.PACKAGE);
										/*-----------------------------------------------------------------*/

									}
								});
			}
		}
		DBChartHandler dbChartHandler = new DBChartHandler();
		getChart.addClickHandler(dbChartHandler);
	}

	private Options createOptions() {
		Options options = Options.create();
		options.setWidth(400);
		options.setHeight(240);
		options.setCurveType("cone");
		options.setTitle("My Daily Activities");
		return options;
	}

	private SelectHandler createSelectHandler(final PieChart chart) {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				String message = "";

				// May be multiple selections.
				JsArray<Selection> selections = chart.getSelections();

				for (int i = 0; i < selections.length(); i++) {
					// add a new line for each selection
					message += i == 0 ? "" : "\n";

					Selection selection = selections.get(i);

					if (selection.isCell()) {
						// isCell() returns true if a cell has been selected.

						// getRow() returns the row number of the selected cell.
						int row = selection.getRow();
						// getColumn() returns the column number of the selected
						// cell.
						int column = selection.getColumn();
						message += "cell " + row + ":" + column + " selected";
					} else if (selection.isRow()) {
						// isRow() returns true if an entire row has been
						// selected.

						// getRow() returns the row number of the selected row.
						int row = selection.getRow();
						message += "row " + row + " selected";
					} else {
						// unreachable
						message += "Pie chart selections should be either row selections or cell selections.";
						message += "  Other visualizations support column selections as well.";
					}
				}

				Window.alert(message);
			}
		};
	}

}
