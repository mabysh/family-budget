package org.mabysh.familybudget.ui.components;

import org.mabysh.familybudget.backend.entity.Account;
import org.mabysh.familybudget.ui.views.RegistrationView;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SignInField extends CustomField<Account> {
	
	protected Account account = new Account();
	protected TextField loginField;
	protected PasswordField passField;
	protected Button button;
	protected boolean isLoginValid = false;
	protected boolean isPassValid = false;
	protected VerticalLayout vl;
	protected HorizontalLayout hl;
	private Button register;
	
	@Override
	public Account getValue() {
		return account;
	}

	@Override
	protected Component initContent() {
		setWidth(600, Unit.PIXELS);

		vl = new VerticalLayout();
		hl = new HorizontalLayout();

		loginField = new TextField();
		loginField.setPlaceholder("Login...");
		loginField.setDescription("Account Login");
		loginField.setWidth(100, Unit.PERCENTAGE);
		loginField.setRequiredIndicatorVisible(true);
		loginField.setCaption("Login");

		passField = new PasswordField();
		passField.setPlaceholder("Password...");
		passField.setDescription("Account Password");
		passField.setWidth(100, Unit.PERCENTAGE);
		passField.setRequiredIndicatorVisible(true);
		passField.setCaption("Password");

		configure();
		
		vl.setMargin(false);
		return vl;
	}

	protected void configure() {

		button = new Button("Sign In");
		button.addStyleName(ValoTheme.BUTTON_PRIMARY);
		button.setSizeFull();
		button.setClickShortcut(KeyCode.ENTER);
		register = new Button("Register new account");
		register.addClickListener(e -> getUI().getNavigator().navigateTo(RegistrationView.VIEW_NAME));
		register.setSizeFull();
		
		hl.addComponents(button, register);
		hl.setSizeFull();

		loginField.addValueChangeListener(e -> {
			loginField.setComponentError(null);
			doLoginValidation();
		});
		passField.addValueChangeListener(e -> {
			passField.setComponentError(null);
			doPasswordValidation();
		});
		button.addClickListener(e -> {
			doLoginValidation();
			doPasswordValidation();
			if (isLoginValid && isPassValid) {
				account.setLogin(loginField.getValue());
				account.setPassword(passField.getValue());
				this.setValue(new Account(account));
			}
		});
		vl.addComponents(loginField, passField, hl);
	}

	protected void doLoginValidation() {
		String login = loginField.getValue();
		isLoginValid = true;
		if (login.isEmpty()) {
			loginField.setComponentError(new UserError("Enter login to proceed"));
			isLoginValid = false;
		}
	}
	
	protected void doPasswordValidation() {
		String password = passField.getValue();
		isPassValid = true;
		if (password.isEmpty()) {
			passField.setComponentError(new UserError("Enter password to proceed"));
			isPassValid = false;
		}
	}

	@Override
	protected void doSetValue(Account value) {
		this.account = value == null ? new Account() : value;
	}
	
	@Override
	public void clear() {
		loginField.clear();
		loginField.setComponentError(null);
		passField.clear();
		passField.setComponentError(null);
	}

}
