package budgeteventplanner.client;

import java.util.List;

import budgeteventplanner.shared.Pent;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.Table;

public class BudgetDetailsTable extends Composite {

	private final BudgetServiceAsync budgetService = GWT.create(BudgetService.class);
	
	// Svc Req name | categoryName | serviceName | qty | price
	List<Pent<String, String, String, Integer, Double>> detailList = Lists.newArrayList();

	private final DecoratorPanel panelTop = new DecoratorPanel();
	private final VerticalPanel panel = new VerticalPanel();
	
	int[] coordinate = new int[2];
	
	Table detailsTable;
	
	String budgetId = "";
	
	public BudgetDetailsTable(final String budgetId) {
		super();
		panel.clear();
		this.budgetId = budgetId;
		final Anchor btnDraw = new Anchor("Draw BudgetDetailsTable");
		btnDraw.setEnabled(false);
		panel.add(btnDraw);

		
		budgetService.getAllCostInfoByBudgetId(budgetId,
				new AsyncCallback<List<Pent<String, String, String, Integer, Double>>>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(List<Pent<String, String, String, Integer, Double>> result) {
						detailList.addAll(result);
						btnDraw.setEnabled(true);
						
					}
				});

		btnDraw.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Runnable onLoadCallback = new Runnable() {
					public void run() {
						try{
							panel.remove(detailsTable);
						}catch(Exception e){}
						
						// Synchronize with server
						budgetService.getAllCostInfoByBudgetId(budgetId,
								new AsyncCallback<List<Pent<String, String, String, Integer, Double>>>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("budgetService.getLimitsByBudgetId - failure");
							}

							@Override
							public void onSuccess(
									List<Pent<String, String, String, Integer, Double>> result) {
								detailList.clear();
								detailList.addAll(result);
								
							}
						});
						
						DataTable dataTable = DataTable.create();
						// Svc Req name | categoryName | serviceName | qty | price
						
						if (dataTable.getNumberOfColumns() == 0) {
							dataTable.addColumn(ColumnType.STRING, "Service Request");
							dataTable.addColumn(ColumnType.STRING, "Category Name");
							dataTable.addColumn(ColumnType.STRING, "Service Name");
							dataTable.addColumn(ColumnType.NUMBER, "Quantity");
							dataTable.addColumn(ColumnType.NUMBER, "Expense");
						}

						for (Pent<String, String, String, Integer, Double> pent : detailList) {
							dataTable.addRow();
							dataTable.setValue(dataTable.getNumberOfRows() - 1, 0, pent.getA());
							dataTable.setValue(dataTable.getNumberOfRows() - 1, 1, pent.getB());
							dataTable.setValue(dataTable.getNumberOfRows() - 1, 2, pent.getC());
							dataTable.setValue(dataTable.getNumberOfRows() - 1, 3, pent.getD());
							dataTable.setValue(dataTable.getNumberOfRows() - 1, 4, pent.getD()*pent.getE());
						}
						
						detailsTable = new Table(dataTable, Table.Options.create());
						detailsTable.addSelectHandler(createSelectHandler(detailsTable));
						panel.add(detailsTable);
						
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

	
	
}
