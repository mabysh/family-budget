package org.mabysh.familybudget.ui;

import org.mabysh.familybudget.backend.entity.Account;
import org.mabysh.familybudget.backend.entity.Wallet;
import org.mabysh.familybudget.backend.service.AccountService;
import org.mabysh.familybudget.ui.views.BalanceView;
import org.mabysh.familybudget.ui.views.OperationsView;
import org.mabysh.familybudget.ui.views.StatisticsView;
import org.mabysh.familybudget.ui.views.WelcomeView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Title("Family Budget")
@Theme("budgetheme")
@SpringUI
@SpringViewDisplay
public class FamilyBudgetUI extends UI implements ViewDisplay {

	@Autowired
	private SpringViewProvider viewProvider;
	
	@Autowired
	private AccountService accountService;	

	private Account currentAccount;
	private Wallet currentWallet;
	
	private Navigator navigator;
	private HorizontalSplitPanel rootPanel;
	private Button accounts, main, operations, statistic;
	private CssLayout menu;
	private Panel content = new Panel();
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	setUpNavigator();
    	setUpMenuComponents();
    	setUpMenuListeners();
    	setUpRootPanel();
    	
    	setContent(rootPanel);
    
    	accounts.click();
    }

	private void setUpRootPanel() {
		rootPanel = new HorizontalSplitPanel();
		rootPanel.setSizeFull();
		rootPanel.setSplitPosition(180, Unit.PIXELS);
		rootPanel.setFirstComponent(menu);
		rootPanel.setLocked(true);
		rootPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		
		content.addStyleName("content");
		content.setSizeFull();
		
		rootPanel.setSecondComponent(content);
	}

	private void setUpMenuComponents() {
		accounts = new Button("Log Out");
		accounts.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		accounts.addStyleName("menubutton");
		accounts.setHeight(4, Unit.PERCENTAGE);
		accounts.setIcon(VaadinIcons.SIGN_OUT);

		main = new Button("Budget Balance");
		main.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		main.addStyleName("menubutton");
		main.setHeight(4, Unit.PERCENTAGE);
		main.setIcon(VaadinIcons.CHART);

		operations = new Button("View Operations");
		operations.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		operations.addStyleName("menubutton");
		operations.setHeight(4, Unit.PERCENTAGE);
		operations.setIcon(VaadinIcons.BULLETS);

		statistic = new Button("View Statistics");
		statistic.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		statistic.addStyleName("menubutton");
		statistic.setHeight(4, Unit.PERCENTAGE);
		statistic.setIcon(VaadinIcons.BAR_CHART_V);

		Panel space = new Panel();
		space.setHeight(84, Unit.PERCENTAGE);
		space.addStyleName("menu");
		space.addStyleName(ValoTheme.PANEL_BORDERLESS);
		
		menu = new CssLayout();
		menu.addComponents(main, operations, statistic, space, accounts);
		menu.addStyleName("menu");
		menu.setSizeFull();
	}
	
	private void setUpMenuListeners() {
		accounts.addClickListener(e -> {
			accounts.addStyleName("menubutton_clicked");
			main.removeStyleName("menubutton_clicked");
			operations.removeStyleName("menubutton_clicked");
			statistic.removeStyleName("menubutton_clicked");
			this.currentAccount = null;
			navigator.navigateTo(WelcomeView.VIEW_NAME);
		});
		main.addClickListener(e -> {
			accounts.removeStyleName("menubutton_clicked");
			main.addStyleName("menubutton_clicked");
			operations.removeStyleName("menubutton_clicked");
			statistic.removeStyleName("menubutton_clicked");
			navigator.navigateTo(BalanceView.VIEW_NAME);
		});
		operations.addClickListener(e -> {
			accounts.removeStyleName("menubutton_clicked");
			main.removeStyleName("menubutton_clicked");
			operations.addStyleName("menubutton_clicked");
			statistic.removeStyleName("menubutton_clicked");
			navigator.navigateTo(OperationsView.VIEW_NAME);
		});
		statistic.addClickListener(e -> {
			accounts.removeStyleName("menubutton_clicked");
			main.removeStyleName("menubutton_clicked");
			operations.removeStyleName("menubutton_clicked");
			statistic.addStyleName("menubutton_clicked");
			navigator.navigateTo(StatisticsView.VIEW_NAME);
		});
	}

	private void setUpNavigator() {
		navigator = new Navigator(this, content);
		navigator.addProvider(viewProvider);
	}

	@Override
	public void showView(View view) {
		content.setContent((Component) view);
	}
	
	public void setCurrentAccount(Account account) {
		this.currentAccount = account;
		this.currentWallet = accountService.findWallet(account.getId());
	}
	
	public Account getCurrentAccount() {
		return currentAccount;
	}
	
	public Wallet getCurrentWallet() {
		return currentWallet;
	}
	
	public void setMenuVisible(boolean b) {
		if (b == true) {
			rootPanel.setSplitPosition(180, Unit.PIXELS);
		} else {
			rootPanel.setSplitPosition(0, Unit.PIXELS);
		}
			rootPanel.getFirstComponent().setVisible(b);
	}

}
