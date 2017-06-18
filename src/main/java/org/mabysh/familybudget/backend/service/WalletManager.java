package org.mabysh.familybudget.backend.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.mabysh.familybudget.backend.entity.OperationType;
import org.mabysh.familybudget.backend.entity.Wallet;
import org.mabysh.familybudget.backend.entity.WalletOperation;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletManager {
	
	private Wallet currentWallet;
	private Long currentVersion;
	
	private List<WalletOperation> allOperations;
	
	private Calendar now = Calendar.getInstance();
	
	private Long available = 0L;
	private Long postponed = 0L;
	private Long income = 0L;
	private Long expenses = 0L;
	

	@Autowired
	private AccountService accountService;

	public void initWallet(Long accountId) {
		currentWallet = accountService.findWallet(accountId);
		currentVersion = currentWallet.getVersion();
		allOperations = accountService.findAllWalletOperations(currentWallet.getId());
		if (allOperations.isEmpty()) {
			return;
		}
		WalletOperation last = allOperations.get(allOperations.size() - 1);
		this.available = last.getAvailabe();
		this.postponed = last.getPostponed();
		this.income = last.getIncome();
		this.expenses = last.getExpenses();
	}
	
	private void updateCurrentWallet() {
		accountService.saveWallet(currentWallet);
		currentWallet = accountService.findWallet(currentWallet.getId());
		currentVersion = currentWallet.getVersion();
	}
	
	public void addOperation(OperationType opType, Long amount) {
		WalletOperation newOperation = new WalletOperation(amount, opType);
	
		switch (opType) {
		case WITHDRAW : 
			expenses += amount;
			available -= amount;
			break;
		case DEPOSIT :
			income += amount;
			available += amount;
			break;
		case POSTPONE :
			postponed += amount;
			available -= amount;
			break;
		case UNDEFINED :
			break;
		}
		newOperation.setWallet(currentWallet);
		newOperation.setAvailable(available);
		newOperation.setPostponed(postponed);
		newOperation.setIncome(income);
		newOperation.setExpenses(expenses);
		accountService.saveWalletOperation(newOperation);
		allOperations.add(accountService.findWalletOperationByCalendar(newOperation.getOpDateTime()));

		currentWallet.setVersion(++currentVersion);
		updateCurrentWallet();
	}

	public Long getCurrentVersion() {
		return currentVersion;
	}
	
	public List<WalletOperation> getAllOperations() {
		return allOperations;
	}
	
	public Long getAvailable() {
		return available;
	}
	
	public Long getPostponed() {
		return postponed;
	}
	
	public Long getIncome() {
		return income;
	}
	
	public Long getExpenses() {
		return expenses;
	}
	
	public Calendar getCurrentCalendar() {
		return now;
	}
}
