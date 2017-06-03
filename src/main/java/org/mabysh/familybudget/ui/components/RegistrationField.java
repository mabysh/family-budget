package org.mabysh.familybudget.ui.components;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mabysh.familybudget.backend.entity.Account;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class RegistrationField extends SignInField {

	private static final String PATTERN = "[0-9a-zA-Z ]*+";
	private PasswordField confirmPassword = new PasswordField();

	@Override
	protected void configure() {
		
		confirmPassword.setDescription("Enter password again");
		confirmPassword.setPlaceholder("Confirm Password");
		confirmPassword.setWidth(100, Unit.PERCENTAGE);
		
		button = new Button("Register");
		button.setWidth(100, Unit.PERCENTAGE);
		button.addStyleName(ValoTheme.BUTTON_PRIMARY);
		button.setClickShortcut(KeyCode.ENTER);
		hl.addComponent(button);
		hl.setSizeFull();

		loginField.addValueChangeListener(e -> {
			loginField.setComponentError(null);
			doLoginValidation();
		});
		passField.addValueChangeListener(e -> {
			passField.setComponentError(null);
			doPasswordValidation();
		});
		confirmPassword.addValueChangeListener(e -> {
			confirmPassword.setComponentError(null);
			doConfirmValidation();
		});
		button.addClickListener(e -> {
			doLoginValidation();
			doPasswordValidation();
			doConfirmValidation();
			if (isLoginValid && isPassValid) {
				account.setLogin(loginField.getValue());
				account.setPassword(passField.getValue());
				this.setValue(new Account(account));
			}
		});
		vl.addComponents(loginField, passField, confirmPassword, hl);
	}
	
	@Override
	protected void doLoginValidation() {
		String login = loginField.getValue();
		isLoginValid = true;
		if (login.isEmpty()) {
			loginField.setComponentError(new UserError("Enter login to proceed"));
			isLoginValid = false;
		}
		Pattern p = Pattern.compile(PATTERN);
		Matcher m = p.matcher(login);
		if (!m.matches()) {
			loginField.setComponentError(new UserError("Login must contain digits and characters only"));
			isLoginValid = false;
		}
	}
	
	@Override
	protected void doPasswordValidation() {
		String password = passField.getValue();
		isPassValid = true;
		if (password.isEmpty()) {
			passField.setComponentError(new UserError("Enter password to proceed"));
			isPassValid = false;
		}
		if (password.length() < 3 || password.length() > 25) {
			passField.setComponentError(new UserError("Password length must be from 3 to 25 characters"));
			isPassValid = false;
		}
	}
	
	private void doConfirmValidation() {
		String confirmPass = confirmPassword.getValue();
		String password = passField.getValue();
		if (confirmPass.isEmpty()) {
			confirmPassword.setComponentError(new UserError("Enter password to proceed"));
			isPassValid = false;
		}
		if(!confirmPass.equals(password)) {
			confirmPassword.setComponentError(new UserError("Password doesn't match"));
			isPassValid = false;
		}
	}
	
}
