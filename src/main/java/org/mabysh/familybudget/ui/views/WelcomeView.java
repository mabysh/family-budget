package org.mabysh.familybudget.ui.views;

import java.time.format.SignStyle;

import javax.annotation.PostConstruct;

import org.mabysh.familybudget.backend.entity.Account;
import org.mabysh.familybudget.backend.service.AccountService;
import org.mabysh.familybudget.ui.FamilyBudgetUI;
import org.mabysh.familybudget.ui.components.SignInField;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringComponent
@UIScope
@SpringView(name = WelcomeView.VIEW_NAME)
public class WelcomeView extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "welcome";
	
	@Autowired 
	private FamilyBudgetUI ui;
		
	@Autowired
	private AccountService accountService;
	
	private SignInField signField;
	
	@PostConstruct
	public void init() {
		Label l1 = new Label("Welcome to Family Budget Application");
		l1.addStyleName("h1");
		signField = new SignInField();
	
		signField.addValueChangeListener(e -> {
			Account acc = e.getValue();
			Account identical = accountService.findIdenticalAccount(acc);
			if (identical != null) {
				ui.setCurrentAccount(identical);
				ui.getNavigator().navigateTo(BalanceView.VIEW_NAME);
				Notification.show("Welcome, " + identical.getLogin(), Type.TRAY_NOTIFICATION);
			} else {
				Notification.show("Account with such login and password not registered", Type.ERROR_MESSAGE);
			}
		});
		
		addComponents(l1, signField);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		ui.setButtonsEnabled(ui.getCurrentAccount() != null);
		ui.setWelcomeButtonCaption("Welcome");
	}

}
