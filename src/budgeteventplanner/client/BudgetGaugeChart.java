package budgeteventplanner.client;

import java.util.List;
import java.util.Stack;

import budgeteventplanner.client.entity.BudgetItem;
import budgeteventplanner.shared.Pair;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Gauge;

public class BudgetGaugeChart extends Composite {

  private final BudgetServiceAsync budgetService = GWT.create(BudgetService.class);

  private final List<Pair<String, BudgetItem>> catBitPairList = Lists.newArrayList();
  private List<Pair<String, Double>> catCostPairList = Lists.newArrayList();
  private final VerticalPanel panelTop = new VerticalPanel();
  private final HorizontalPanel panel = new HorizontalPanel();;
  int selectionID;
  DialogBox promptModifyBox = new DialogBox();
  TextBox modificationBox = new TextBox();

  Stack<Gauge> oldCharts = new Stack<Gauge>();

  public BudgetGaugeChart(final String budgetId) {
    super();
    panel.clear();

    final Anchor btnDraw = new Anchor("Draw BudgetGaugeChart");
    btnDraw.setEnabled(false);

    modificationBox.setEnabled(false);
    initializeServiceRequestList(budgetId);
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
    modificationBox.addKeyPressHandler(new KeyPressHandler() {

      @Override
      public void onKeyPress(KeyPressEvent event) {

        if (event.getUnicodeCharCode() == 13 && !modificationBox.getText().isEmpty()) {
          BudgetItem temp = catBitPairList.get(selectionID).getB();
          temp = new BudgetItem.Builder(temp).setLimit(
              Double.parseDouble(modificationBox.getText())).build();
          // Window.alert(modificationBox.getText()+" entered");

          // Update coreChartData locally
          catBitPairList.set(selectionID,
              new Pair<String, BudgetItem>(catBitPairList.get(selectionID).getA(), temp));

          // Update coreChartData to Server
          updateEntryToServer(catBitPairList.get(selectionID).getB().getBudgetItemId(),
              Double.parseDouble(modificationBox.getText()));

          modificationBox.setText("");
          promptModifyBox.hide();
        } else if (event.getUnicodeCharCode() == 13) {
          promptModifyBox.hide();
        }

      }
    });

    promptModifyBox.add(modificationBox);
    panel.add(promptModifyBox);
    promptModifyBox.setVisible(false);

    btnDraw.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Runnable onLoadCallback = new Runnable() {
          public void run() {
            panel.clear();
            panel.add(btnDraw);

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
              int red = Double.valueOf(max * 0.9).intValue();
              int yellow = Double.valueOf(max * 0.75).intValue();

              options.setGaugeRange(0, max);
              options.setGreenRange(0, yellow);
              options.setYellowRange(yellow, red);
              options.setRedRange(red, max);

              Gauge gaugeChart = new Gauge(dataTable, options);
              oldCharts.add(gaugeChart);
              dataTable = null;
              panel.add(gaugeChart);

            }
          }
        };
        VisualizationUtils.loadVisualizationApi(onLoadCallback, Gauge.PACKAGE);
      }
    });

    panelTop.add(btnDraw);
    panelTop.add(panel);
    initWidget(panelTop);
  }


  void updateEntryToServer(String budgetItemId, Double limit) {
    budgetService.updateBudgetItemLimit(budgetItemId, limit, new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable caught) {
      }

      @Override
      public void onSuccess(Void result) {
      }
    });
  }

  private Double mapToServiceRequestList(String catId) {
	  Double subtotal = 0.0;
		
		for (Pair<String, Double> e: catCostPairList) {
			if(e.getA().equals(catId))
				subtotal += e.getB();
		}
		return subtotal;

  }

  void initializeServiceRequestList(String budgetId) {
    budgetService.getSubtotalsByBudgetId(budgetId, new AsyncCallback<List<Pair<String, Double>>>() {

      @Override
      public void onFailure(Throwable caught) {
        Window
            .alert("initializeServiceRequestList -> budgetService.getSubtotalsByEventId - failure\n");
      }

      @Override
      public void onSuccess(List<Pair<String, Double>> result) {
        catCostPairList = result;
      }
    });

  }
}
