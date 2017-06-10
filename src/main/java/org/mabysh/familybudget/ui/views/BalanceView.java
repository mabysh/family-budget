package org.mabysh.familybudget.ui.views;

import javax.annotation.PostConstruct;

import org.mabysh.familybudget.backend.service.AccountService;
import org.mabysh.familybudget.ui.FamilyBudgetUI;
import org.springframework.beans.factory.annotation.Autowired;

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
	private AccountService accountService;

	public static final String VIEW_NAME = "balance";
	
	private Label wallet;
	private Label income;
	private Label consumption;
	
	private Long oldWalletVersion;
	
	@PostConstruct
	public void init() {
		Label l1 = new Label("Budget Balance");
		l1.addStyleName("h1");
		
		wallet = new Label();
		income = new Label();
		consumption = new Label();
		
		addComponents(l1, wallet, income, consumption);
		
		redrawBalanceView();
	}

	private void redrawBalanceView() {
		Long newVersion = ui.getCurrentWallet().getVersion();
		if (oldWalletVersion == null || newVersion > oldWalletVersion) {
			oldWalletVersion = newVersion;
			
			wallet.setCaption(String.valueOf(ui.getCurrentWallet().getInUse()));
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		ui.setMenuVisible(true);
		redrawBalanceView();
	}

}
