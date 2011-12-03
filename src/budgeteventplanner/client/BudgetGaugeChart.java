package budgeteventplanner.client;

import java.util.List;
import java.util.Stack;

import budgeteventplanner.client.entity.BudgetItem;
import budgeteventplanner.shared.Pair;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selectable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.Gauge;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;

public class BudgetGaugeChart extends Composite {

	private final BudgetServiceAsync budgetService = GWT.create(BudgetService.class);

	private final List<Pair<String, BudgetItem>> catBitPairList = Lists.newArrayList();
	private List<Pair<String, Double>> catCostPairList = Lists.newArrayList();
	private final DecoratorPanel panelTop = new DecoratorPanel();
	private final VerticalPanel panel = new VerticalPanel();
	;
	int selectionID;
	DialogBox promptModifyBox = new DialogBox();
	TextBox modificationBox = new TextBox();
	
	Stack<Gauge> oldCharts = new Stack<Gauge>();

	public BudgetGaugeChart(final String budgetId, String eventId) {
		super();
		panel.clear();
		panelTop.setWidth("9000");
		panel.setWidth("9000");
		
		final Button btnDraw = new Button("Draw BudgetGaugeChart");
		btnDraw.setEnabled(false);
		panel.add(btnDraw);
		modificationBox.setEnabled(false);
		initializeServiceRequestList(eventId);
		budgetService.getLimitsByBudgetId(budgetId,
				new AsyncCallback<List<Pair<String, BudgetItem>>>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(List<Pair<String, BudgetItem>> result) {
				catBitPairList.addAll(result);
				btnDraw.setEnabled(true);
				modificationBox.setEnabled(true);
			}
		});

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
					BudgetItem temp = catBitPairList.get(selectionID).getB();
					temp = new BudgetItem.Builder(temp).setLimit(Double.parseDouble(modificationBox.getText())).build();
				//	Window.alert(modificationBox.getText()+" entered");

					//Update coreChartData locally
					catBitPairList.set(selectionID, 
							new Pair<String, BudgetItem>(catBitPairList.get(selectionID).getA(), temp));

					//Update coreChartData to Server
					updateEntryToServer(catBitPairList.get(selectionID).getB().getBudgetItemId(), Double.parseDouble(modificationBox.getText()));


					modificationBox.setText("");
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
		panel.add(promptModifyBox);
		promptModifyBox.setVisible(false);


		btnDraw.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Runnable onLoadCallback = new Runnable() {
					public void run() {
						try{
							if(!oldCharts.isEmpty())
							{
								panel.remove(oldCharts.pop());
								panel.clear();
								panel.add(btnDraw);
							}
						}catch(Exception e){}

						
						// Synchronize with server
						budgetService.getLimitsByBudgetId(budgetId,
								new AsyncCallback<List<Pair<String, BudgetItem>>>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("budgetService.getLimitsByBudgetId - failure");
							}

							@Override
							public void onSuccess(List<Pair<String, BudgetItem>> result) {
								catBitPairList.clear();
								catBitPairList.addAll(result);
							}
						});
						
						
						
						/*************************************************************/
						DataTable dataTable;
						
						Gauge.Options options = Gauge.Options.create();
					    options.setWidth(400);
					    options.setHeight(240);
					    
						
						for (Pair<String, BudgetItem> pair : catBitPairList) {
							dataTable = DataTable.create();
							dataTable.addColumn(ColumnType.NUMBER, pair.getA().toString());
							dataTable.addRow();
							
							dataTable.setValue(0, 0, mapToServiceRequestList(pair.getA()));
							
							int max = (Double.valueOf(pair.getB().getLimit())).intValue();
							int red = Double.valueOf(max*0.9).intValue();
							int yellow = Double.valueOf(max*0.75).intValue();
							
							options.setGaugeRange(0, max);
							options.setGreenRange(0, yellow);
							options.setYellowRange(yellow, red);
							options.setRedRange(red, max);
							
							Gauge gaugeChart = new Gauge(dataTable, options);
						    oldCharts.add(gaugeChart);
						    dataTable = null;
						//	((Selectable) gaugeChart).addSelectHandler(createSelectHandler(gaugeChart));
							panel.add(gaugeChart);
														
						}				
						/*************************************************************/
					}
				};
				VisualizationUtils.loadVisualizationApi(onLoadCallback, Gauge.PACKAGE);

			}
		});

		panelTop.add(panel);
		initWidget(panelTop);
	}

	private AbstractDataTable createTesetTable() {
	DataTable coreChartData = DataTable.create();
	coreChartData.addColumn(ColumnType.STRING, "Task");
	coreChartData.addColumn(ColumnType.NUMBER, "Hours per Day");
	coreChartData.addRows(2);
	coreChartData.setValue(0, 0, "Work");
	coreChartData.setValue(0, 1, 14);
	coreChartData.setValue(1, 0, "Sleep");
	coreChartData.setValue(1, 1, 10);
	return coreChartData;
}
	
	
	private SelectHandler createSelectHandler(final Gauge gaugeChart2) {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				String message = "";
				// May be multiple selections.
				JsArray<Selection> selections = ((Selectable) gaugeChart2).getSelections();

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
						if(column == 2)
						{
							selectionID = row;		
							promptModifyBox.center();
						}

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
			}
		};
	}

	private com.google.gwt.visualization.client.visualizations.Gauge.Options createOptions(Gauge gaugeChart2) {
		com.google.gwt.visualization.client.visualizations.Gauge.Options options = com.google.gwt.visualization.client.visualizations.Gauge.Options.create();
		options.setWidth(400);
		options.setHeight(240);
		//options.setTitle("My Daily Activities");
		options.set("is3D", true);
		return options;
	}

	void updateEntryToServer(String budgetItemId, Double limit)
	{
		budgetService.updateBudgetItemLimit(budgetItemId, limit, 
				new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("updateEntryToServer -> budgetService.updateBudgetItemLimit - failure\n");
			}

			@Override
			public void onSuccess(Void result) {
		//		Window.alert("server responds");
			}

		});

	}

	private Double mapToServiceRequestList(String catId)
	{
		for (Pair<String, Double> e: catCostPairList) {
			if(e.getA().endsWith(catId))
				return e.getB();
		}
		return 0.0;

	}

	void initializeServiceRequestList(String eventId)
	{
	//	Window.alert("Using eventId: \n" + eventId);
		budgetService.getSubtotalsByEventId(eventId, 
				new AsyncCallback<List<Pair<String, Double>>>()
				{

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("initializeServiceRequestList -> budgetService.getSubtotalsByEventId - failure\n");					
			}

			@Override
			public void onSuccess(List<Pair<String, Double>> result) {
				//			Window.alert("server responds");
				catCostPairList = result;					
			}
				});

	}
}
