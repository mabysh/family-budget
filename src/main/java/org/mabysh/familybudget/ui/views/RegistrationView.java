package org.mabysh.familybudget.ui.views;

import javax.annotation.PostConstruct;

import org.mabysh.familybudget.backend.entity.Account;
import org.mabysh.familybudget.backend.service.AccountService;
import org.mabysh.familybudget.ui.FamilyBudgetUI;
import org.mabysh.familybudget.ui.components.RegistrationField;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringComponent
@UIScope
@SpringView(name = RegistrationView.VIEW_NAME)
public class RegistrationView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "registration";

	@Autowired 
	private FamilyBudgetUI ui;
		
	@Autowired
	private AccountService accountService;
	
	@PostConstruct
	public void init() {
		Label l1 = new Label("Register new account");
		l1.addStyleName("h1");
		RegistrationField field = new RegistrationField();
		
		field.addValueChangeListener(e -> {
			Account acc = e.getValue();
			Account identical = accountService.findAccountByLogin(acc.getLogin());
			if (identical == null) {
				accountService.saveAccount(acc);
				ui.getNavigator().navigateTo(WelcomeView.VIEW_NAME);
				Notification.show("Account successfully registered! Now you may sing in.", Type.TRAY_NOTIFICATION);
			} else {
				Notification.show("Account with such login already exists.", Type.ERROR_MESSAGE);
			}
		});
		
		addComponents(l1, field);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		ui.setButtonsEnabled(ui.getCurrentAccount() != null);
		ui.setWelcomeButtonCaption("Registration");
	}

}
