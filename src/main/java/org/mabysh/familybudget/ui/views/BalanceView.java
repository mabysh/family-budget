package org.mabysh.familybudget.ui.views;

import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.mabysh.familybudget.backend.service.AccountService;
import org.mabysh.familybudget.backend.service.WalletManager;
import org.mabysh.familybudget.ui.FamilyBudgetUI;
import org.springframework.beans.factory.annotation.Autowired;

import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
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
	
	public static final String VIEW_NAME = "balance";
	
	private Label wallet;
	private Label income;
	private Label expenses;
	
	private Long oldWalletVersion = -1L;
	private LineChartConfig availChartConfig;
	
	private Calendar now;
	
	@PostConstruct
	public void init() {
		Label l1 = new Label("Wallet Balance");
		l1.addStyleName("h1");
		
		wallet = new Label();
		income = new Label();
		expenses = new Label();
		
		now = walletManager.getCurrentCalendar();
	
		availChartConfig = new LineChartConfig();
		availChartConfig.data()
			.addDataset(new LineDataset().label("Money available").fill(false))
			.la
			
		addComponents(l1, wallet, income, expenses);
		redrawBalanceView();
	}

	private void redrawBalanceView() {
		Long newWalletVersion = walletManager.getCurrentVersion();
		if (newWalletVersion > oldWalletVersion) {
			oldWalletVersion = newWalletVersion;

			wallet.setValue("Available: " + walletManager.getAvailable());
			income.setValue("Income: " + walletManager.getIncome());
			expenses.setValue("Expenses: " + walletManager.getExpenses());
			
		}	
	}

	@Override
	public void enter(ViewChangeEvent event) {
		ui.setMenuVisible(true);
		redrawBalanceView();
	}

}
