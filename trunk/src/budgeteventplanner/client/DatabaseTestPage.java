package budgeteventplanner.client;

import java.util.List;

import budgeteventplanner.client.entity.BudgetItem;
import budgeteventplanner.shared.Pair;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
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

	List<Pair<String, BudgetItem>> datatable = Lists.newArrayList();
	PieChart pie;
	BarChart bar;
	TextBox changeValueBox;
	Button modifyButton;
	int selectionID;
	DialogBox promptModifyBox = new DialogBox();
	boolean promptModifyBoxFLOP = false;
	TextBox modificationBox = new TextBox();
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
				new AsyncCallback<List<Pair<String, BudgetItem>>>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("failure");
					}

					@Override
					public void onSuccess(List<Pair<String, BudgetItem>> result) {
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

		promptModifyBox.hide();
		promptModifyBox.setText("Enter Value to Modify:");
		promptModifyBox.setAnimationEnabled(true);
		promptModifyBox.setTitle("Modification");
		modificationBox = new TextBox();
		modificationBox.setFocus(true);
		modificationBox.addKeyPressHandler(new KeyPressHandler()
		{

			@Override
			public void onKeyPress(KeyPressEvent event) {

				if(event.getUnicodeCharCode()== 13 && !modificationBox.getText().isEmpty())
				{
					BudgetItem temp = datatable.get(selectionID).getB();
					temp = new BudgetItem.Builder(temp).setLimit(Double.parseDouble(modificationBox.getText())).build();
					Window.alert(modificationBox.getText()+" entered");
					
					//Update data locally
					datatable.set(selectionID, 
							new Pair<String, BudgetItem>(datatable.get(selectionID).getA(), temp));
					//Update data to Server
					updateEntryToServer(datatable.get(selectionID).getB().getBudgetItemId(), Double.parseDouble(modificationBox.getText()));
					
					promptModifyBox.hide();
				}
				else if(event.getUnicodeCharCode()== 13)
				{
					promptModifyBox.hide();
				}


			}
		}
		);
		

		promptModifyBox.add(modificationBox);
		RootPanel.get("databaseContainer").add(promptModifyBox);
		promptModifyBox.setVisible(false);
		
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

						for (Pair<String, BudgetItem> pair : datatable) {
							data.addRow();
							data.setValue(data.getNumberOfRows() - 1, 0,
									pair.getA());
							data.setValue(data.getNumberOfRows() - 1, 1,
									pair.getB().getLimit());
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
				
				BudgetItem temp = datatable.get(selectionID).getB();
				temp = new BudgetItem.Builder(temp).setLimit(Double.parseDouble(modificationBox.getText())).build();
				
				Pair<String, BudgetItem> newPair = 
					new Pair<String, BudgetItem>(datatable.get(selectionID).getA(), temp);
				//Update data locally
				datatable.set(selectionID, newPair);
				
				//Update data to Server
				updateEntryToServer(datatable.get(selectionID).getA(), Double.parseDouble(modificationBox.getText()));
			}
		});
		
		
		reloadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				budgetService.getLimitsByBudgetId("7C1A90FE-A1A2-41A5-8A48-AB7746201829",
						new AsyncCallback<List<Pair<String, BudgetItem>>>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("failure");
					}

					@Override
					public void onSuccess(List<Pair<String, BudgetItem>> result) {
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
						promptModifyBox.center();
					} else {
						// unreachable
						message += "Pie chart selections should be either row selections or cell selections.";
						message += "  Other visualizations support column selections as well.";
					}
				}
			//	Window.alert(message);
			}
		};
	}
	
	void updateEntryToServer(String budgetItemId, Double limit)
	{
		budgetService.updateBudgetItemLimit(budgetItemId, limit, 
				new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("server update failure");
			}

			@Override
			public void onSuccess(Void result) {
				Window.alert("server responds");
			}

		});

	}
}
