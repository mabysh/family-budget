package org.mabysh.familybudget.ui.views;
import javax.annotation.PostConstruct;

import org.mabysh.familybudget.backend.entity.OperationType;
import org.mabysh.familybudget.backend.entity.WalletOperation;
import org.mabysh.familybudget.backend.service.AccountService;
import org.mabysh.familybudget.backend.service.WalletManager;
import org.mabysh.familybudget.ui.FamilyBudgetUI;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringComponent
@UIScope
@SpringView(name = OperationsView.VIEW_NAME)
public class OperationsView extends VerticalLayout implements View {

	@Autowired 
	private FamilyBudgetUI ui;

	@Autowired
	private WalletManager walletManager;
	
	private Label opAmount, opList;
	private Long oldWalletVersion = -1L;
	
	public static final String VIEW_NAME = "operations";
	
	@PostConstruct
	public void init() {
		Label l1 = new Label("Wallet Operations");
		l1.addStyleName("h1");
		opAmount = new Label();
		opAmount.addStyleName("h3");
		opList = new Label();
		opList.setContentMode(ContentMode.PREFORMATTED);
		Button deposit = new Button("Deposit 10 BYN");
		Button withdraw = new Button("Withdraw 5 BYN");
		
		deposit.addClickListener(click -> {
			walletManager.addOperation(OperationType.DEPOSIT, 10L);
			redrawOperationsView();
		});
		withdraw.addClickListener(click -> {
			walletManager.addOperation(OperationType.WITHDRAW, 5L);
			redrawOperationsView();
		});

		addComponents(l1, deposit, withdraw, opAmount, opList);
		
		redrawOperationsView();
	}

	private void redrawOperationsView() {
		Long newWalletVersion = walletManager.getCurrentVersion();
		if (newWalletVersion > oldWalletVersion) {
			oldWalletVersion = newWalletVersion;
			
			opAmount.setValue("Wallet Operations: " + walletManager.getAllOperations().size());

			StringBuilder sb = new StringBuilder();
			for (WalletOperation wop : walletManager.getAllOperations()) {
				sb.append(wop).append("\n");
			}
			opList.setValue(sb.toString());
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		ui.setMenuVisible(true);
		redrawOperationsView();
		
	}

}
