package org.mabysh.familybudget.ui.views;

import javax.annotation.PostConstruct;

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

	public static final String VIEW_NAME = "balance";
	
	@PostConstruct
	public void init() {
		Label l1 = new Label("Budget Balance");
		l1.addStyleName("h1");
		addComponents(l1);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		ui.setButtonsEnabled(ui.getCurrentAccount() != null);
		ui.setWelcomeButtonCaption("Log Out");
	}

}
