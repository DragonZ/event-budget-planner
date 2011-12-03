package budgeteventplanner.client;

import java.util.List;

import budgeteventplanner.client.entity.BudgetItem;
import budgeteventplanner.shared.Pair;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.Table;

public class BudgetExLmTable extends Composite {

	private final BudgetServiceAsync budgetService = GWT.create(BudgetService.class);

	private final List<Pair<String, BudgetItem>> catBitPairList = Lists.newArrayList();
	private List<Pair<String, Double>> catCostPairList = Lists.newArrayList();
	private final DecoratorPanel panelTop = new DecoratorPanel();
	private final VerticalPanel panel = new VerticalPanel();
	
	int[] coordinate = new int[2];
	
	Table exLmTable;
	
	String budgetIdGLB = "";
	
	public BudgetExLmTable(String budgetId, String eventId) {
		super();
		panel.clear();
		budgetIdGLB = budgetId;
		final Button btnDraw = new Button("Draw BudgetExLmTable");
		btnDraw.setEnabled(false);
		panel.add(btnDraw);
		
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
					}
				});

		btnDraw.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Runnable onLoadCallback = new Runnable() {
					public void run() {
						try{
							panel.remove(exLmTable);
						}catch(Exception e){}
						
						// Synchronize with server
						budgetService.getLimitsByBudgetId(budgetIdGLB,
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
						
						DataTable dataTable = DataTable.create();

						if (dataTable.getNumberOfColumns() == 0) {
							dataTable.addColumn(ColumnType.STRING, "Category");
							dataTable.addColumn(ColumnType.NUMBER, "Expense");
							dataTable.addColumn(ColumnType.NUMBER, "Limit");
						}

						for (Pair<String, BudgetItem> pair : catBitPairList) {
							dataTable.addRow();
							dataTable.setValue(dataTable.getNumberOfRows() - 1, 0,
									pair.getA());
							dataTable.setValue(dataTable.getNumberOfRows() - 1, 1,
									mapToServiceRequestList(pair.getA()));
							dataTable.setValue(dataTable.getNumberOfRows() - 1, 2,
									pair.getB().getLimit());
						}
						
						exLmTable = new Table(dataTable, Table.Options.create());
						exLmTable.addSelectHandler(createSelectHandler(exLmTable));
						panel.add(exLmTable);
						
					}
				};
				VisualizationUtils.loadVisualizationApi(onLoadCallback, Table.PACKAGE);
				
			}
		});

		panelTop.add(panel);
		initWidget(panelTop);
	}

	private SelectHandler createSelectHandler(final Table table) {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				String message = "";
				// May be multiple selections.
				JsArray<Selection> selections = table.getSelections();


				
				
				
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
						coordinate[0] = row;
						coordinate[1] = column;

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
					//Window.alert(message);
				}
			}
		};
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
		//Window.alert("Using eventId: \n" + eventId);
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
