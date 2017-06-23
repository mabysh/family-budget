package org.mabysh.familybudget.ui.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.mabysh.familybudget.backend.service.AccountService;
import org.mabysh.familybudget.backend.service.WalletChartsUtil;
import org.mabysh.familybudget.backend.service.WalletManager;
import org.mabysh.familybudget.ui.FamilyBudgetUI;
import org.springframework.beans.factory.annotation.Autowired;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.Position;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.CategoryScale;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringComponent
@UIScope
@SpringView(name = BalanceView.VIEW_NAME)
public class BalanceView extends VerticalLayout implements View {

	@Autowired 
	private FamilyBudgetUI ui;
	
	@Autowired
	private WalletManager walletManager;
	
	@Autowired
	private WalletChartsUtil chartsUtil;
	
	public static final String VIEW_NAME = "balance";
	
	private Label l1;
	private Label wallet;
	private Label postponed;
	private Label income;
	private Label expenses;
	private Label period;
	private HorizontalLayout periodLayout = new HorizontalLayout();
	private ComboBox<Integer> selectedYear, selectedMonth;
	private Button applyPeriod;
	private Integer selYear, selMonth;
	
	private Long oldWalletVersion = -1L;
	private LineChartConfig availChartConfig;
	private ChartJs availChart;
	
	private Boolean redrawRequired = Boolean.FALSE;
	
	@PostConstruct
	public void init() {
		l1 = new Label("Wallet Balance");
		l1.addStyleName("h1");
		
		wallet = new Label();
		postponed = new Label();
		income = new Label();
		expenses = new Label();
		period = new Label();
		period.setValue("Period: ");
		selectedYear = new ComboBox<Integer>();
		selectedYear.setPlaceholder("Year...");
		selectedYear.setEmptySelectionAllowed(false);
		selectedMonth = new ComboBox<Integer>();
		selectedMonth.setPlaceholder("Month...");
//		selectedMonth.setEmptySelectionCaption("All Year");
		selectedMonth.setEmptySelectionAllowed(false);
		applyPeriod = new Button("Apply");
		applyPeriod.addClickListener(e -> {
			if (selYear != null && selMonth != null) {
				redrawRequired = Boolean.TRUE;
				redrawBalanceView();
			}
		});
		
		ArrayList<Integer> years = walletManager.getPeriodYears();
		selYear = years.get(years.size() - 1);
		ArrayList<Integer> months = walletManager.getPeriodMonths(selYear);
		selMonth = months.get(months.size() - 1);
		
		selectedYear.setItems(years);
		selectedYear.setSelectedItem(selYear);
		selectedYear.addValueChangeListener(e -> {
			selYear = e.getValue();
			ArrayList<Integer> m = walletManager.getPeriodMonths(selYear);
			selectedMonth.setItems(m);
			if (m.contains(selMonth)) {
				selectedMonth.setValue(selMonth);
			} else {
				selectedMonth.setValue(m.get(m.size() - 1));
			}
		});
		selectedMonth.setItems(months);
		selectedMonth.setSelectedItem(selMonth);
		selectedMonth.addValueChangeListener(e -> selMonth = e.getValue());
		
		periodLayout.addComponents(period, selectedYear, selectedMonth, applyPeriod);
	
		availChartConfig = new LineChartConfig();
		availChartConfig.data()
			.labels("")
			.addDataset(new LineDataset().label("Money available").fill(false))
			.and()
		.options()
			.responsive(true)
			.title()
			.display(true)
			.text("Money Available")
			.and()
		.tooltips()
			.mode(InteractionMode.NEAREST)
			.intersect(true)
			.and()
		.scales()
		.add(Axis.X, new CategoryScale()
			.display(true)
			.scaleLabel()
				.display(true)
				.labelString("Period")
				.and()
			.position(Position.BOTTOM))
		.add(Axis.Y, new LinearScale()
				.display(true)
				.scaleLabel()
					.display(true)
					.labelString("Value")
					.and()
				.ticks()
					.suggestedMin(-10)
					.suggestedMax(250)
					.and()
				.position(Position.RIGHT))
		.and()
		.done();
		
		availChart = new ChartJs(availChartConfig);

		addComponents(l1, periodLayout, availChart, wallet, postponed, income, expenses);
		setWidth(100, Unit.PERCENTAGE);
		redrawBalanceView();
	}

	private void redrawBalanceView() {
		Long newWalletVersion = walletManager.getCurrentVersion();
		if (newWalletVersion > oldWalletVersion || redrawRequired) {
			
			oldWalletVersion = newWalletVersion;
			
			selectedYear.setItems(walletManager.getPeriodYears());
			selectedYear.setSelectedItem(selYear);
			selectedMonth.setItems(walletManager.getPeriodMonths(selYear));
			selectedMonth.setSelectedItem(selMonth);
			
			chartsUtil.calculateSelectedPeriod(selYear, selMonth);
			availChartConfig.data().labelsAsList(chartsUtil.getLabelList());			
			LineDataset lds = (LineDataset) availChartConfig.data().getFirstDataset();
			lds.dataAsList(chartsUtil.getAvailableList());
			lds.borderColor(ColorUtils.randomColor(0.3));
            lds.backgroundColor(ColorUtils.randomColor(0.5));
            
            availChart = new ChartJs(availChartConfig);
            availChart.setJsLoggingEnabled(true);
            availChart.setWidth(50, Unit.PERCENTAGE);
			

			wallet.setValue("Available: " + walletManager.getAvailable());
			postponed.setValue("Postponed: " + walletManager.getPostponed());
			income.setValue("Income: " + walletManager.getIncome());
			expenses.setValue("Expenses: " + walletManager.getExpenses());
			
			removeAllComponents();
			addComponents(l1, periodLayout, availChart, wallet, postponed, income, expenses);
			
			redrawRequired = Boolean.FALSE;
		}	
	}

	@Override
	public void enter(ViewChangeEvent event) {
		ui.setMenuVisible(true);
		redrawBalanceView();
	}

}
