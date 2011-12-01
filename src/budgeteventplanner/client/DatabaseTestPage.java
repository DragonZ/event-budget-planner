package budgeteventplanner.client;

import java.util.List;

import budgeteventplanner.shared.Pair;

import com.google.common.collect.Lists;
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
	private final BudgetServiceAsync budgetService = GWT
	.create(BudgetService.class);

	List<Pair<String, Double>> datatable = Lists.newArrayList();
	PieChart pie;
	BarChart bar;
	TextBox changeValueBox;
	Button modifyButton;
	int selectionID;
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

		final Button drawButton = new Button("Draw");
		drawButton.setEnabled(false);
		RootPanel.get("databaseContainer").add(drawButton);

		final Button reloadButton = new Button("reload");
		reloadButton.setEnabled(false);
		RootPanel.get("databaseContainer").add(reloadButton);

		budgetService.getLimitsByBudgetId(
				"7C1A90FE-A1A2-41A5-8A48-AB7746201829",
				new AsyncCallback<List<Pair<String, Double>>>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("failure");
					}

					@Override
					public void onSuccess(List<Pair<String, Double>> result) {
						datatable.addAll(result);
						drawButton.setEnabled(true);
					}
				});

		changeValueBox = new TextBox();
		changeValueBox.setName("Enter new value");
		changeValueBox.setEnabled(false);
		RootPanel.get("databaseContainer").add(changeValueBox);
		
		modifyButton = new Button("modify");
		modifyButton.setEnabled(false);
		RootPanel.get("databaseContainer").add(modifyButton);
		
		
		
		drawButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Runnable onLoadCallback = new Runnable() {
					public void run() {
						try{
							RootPanel.get("databaseContainer").remove(pie);
							RootPanel.get("databaseContainer").remove(bar);
						}catch(Exception e){}
						
						
						DataTable data = DataTable.create();

						if (data.getNumberOfColumns() == 0) {
							data.addColumn(ColumnType.STRING, "Category");
							data.addColumn(ColumnType.NUMBER, "Limit");
						}

						for (Pair<String, Double> pair : datatable) {
							data.addRow();
							data.setValue(data.getNumberOfRows() - 1, 0,
									pair.getA());
							data.setValue(data.getNumberOfRows() - 1, 1,
									pair.getB());
						}

						Panel panel = RootPanel.get("databaseContainer");
						
						
						pie = new PieChart(data, createOptions());
						bar = new BarChart(data, createOptions());
						
						pie.addSelectHandler(createSelectHandler(pie));
						bar.addSelectHandler(createSelectHandler(bar));
						
						panel.add(pie);
						panel.add(bar);
						
					}
				};
				VisualizationUtils.loadVisualizationApi(onLoadCallback, CoreChart.PACKAGE);
				
				reloadButton.setEnabled(true);
			}
		});

		modifyButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Pair<String, Double> newPair = 
					new Pair<String, Double>(datatable.get(selectionID).getA(), 
							Double.parseDouble(changeValueBox.getText()));
				
				datatable.set(selectionID, newPair);
			}
		});
		
		
		reloadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				budgetService.getLimitsByBudgetId("7C1A90FE-A1A2-41A5-8A48-AB7746201829",
						new AsyncCallback<List<Pair<String, Double>>>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("failure");
					}

					@Override
					public void onSuccess(List<Pair<String, Double>> result) {
						datatable.clear();
						datatable.addAll(result);
//						DomEvent.fireNativeEvent(nativeEvent, this);
//						NativeEvent createClickEvent = new GwtEvent();
						Panel panel = RootPanel.get("databaseContainer");
						
					}
				});
			}
		});
		
		
		
		
		
		
	}

	private Options createOptions() {
		Options options = Options.create();
		options.setWidth(400);
		options.setHeight(240);
		options.setTitle("My Daily Activities");
		options.set("is3D", true);
		return options;
	}

	private SelectHandler createSelectHandler(final CoreChart chart) {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				changeValueBox.setEnabled(true);
				modifyButton.setEnabled(true);
				
				
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
						selectionID = row;
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
