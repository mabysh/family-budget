package org.mabysh.familybudget.ui.views;

import javax.annotation.PostConstruct;

import org.mabysh.familybudget.backend.entity.Account;
import org.mabysh.familybudget.backend.entity.Wallet;
import org.mabysh.familybudget.backend.service.AccountService;
import org.mabysh.familybudget.ui.FamilyBudgetUI;
import org.mabysh.familybudget.ui.components.RegistrationField;
import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.Field;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
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
	
	private RegistrationField field;

	@PostConstruct
	public void init() {
		Label l1 = new Label("Register new account");
		l1.addStyleName("h1");
		field = new RegistrationField();
		
		field.addValueChangeListener(e -> {
			Account acc = e.getValue();
			Account identical = accountService.findAccountByLogin(acc.getLogin());
			if (identical == null) {
				accountService.saveAccount(acc);
				identical = accountService.findIdenticalAccount(acc);
				Wallet wal = new Wallet();
				wal.setWalletAccount(identical);
				accountService.saveWallet(wal);

				ui.getNavigator().navigateTo(WelcomeView.VIEW_NAME);
				Notification.show("Account successfully registered! Now you may sign in.", Type.TRAY_NOTIFICATION);
			} else {
				Notification.show("Account with such login already exists.", Type.ERROR_MESSAGE);
			}
		});
		
		Button back = new Button("Back");
		back.setWidth(600, Unit.PIXELS);
		back.addClickListener(click -> ui.getNavigator().navigateTo(WelcomeView.VIEW_NAME));
		
		addComponents(l1, field, back);
		setComponentAlignment(l1, Alignment.TOP_CENTER);
		setComponentAlignment(field, Alignment.TOP_CENTER);
		setComponentAlignment(back, Alignment.TOP_CENTER);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		field.clear();
	}

}
