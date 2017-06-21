package org.mabysh.familybudget.backend.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.mabysh.familybudget.backend.entity.OperationType;
import org.mabysh.familybudget.backend.entity.Wallet;
import org.mabysh.familybudget.backend.entity.WalletOperation;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletManager {
	
	@Autowired
	private AccountService accountService;
	
	private Wallet currentWallet;
	private Long currentVersion;
	
	private List<WalletOperation> allOperations;
	
	private Calendar now = Calendar.getInstance();
	private HashMap<Integer, HashSet<String>> period = new HashMap<>();
	
	private Long available = 0L;
	private Long postponed = 0L;
	private Long income = 0L;
	private Long expenses = 0L;
	
	public void initWallet(Long accountId) {
		currentWallet = accountService.findWallet(accountId);
		currentVersion = currentWallet.getVersion();
		allOperations = accountService.findAllWalletOperations(currentWallet.getId());
		if (allOperations.isEmpty()) {
			return;
		}
		
		for (WalletOperation op : allOperations) {
			updatePeriod(op.getOpDateTime());
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
		newOperation = accountService.findWalletOperationByCalendar(newOperation.getOpDateTime());
		allOperations.add(newOperation);
		updatePeriod(newOperation.getOpDateTime());
		
		currentWallet.setVersion(++currentVersion);
		updateCurrentWallet();
	}
	
	private void updatePeriod(Calendar cal) {
		HashSet<String> months = period.get(cal.get(Calendar.YEAR));
		if (months == null) {
			months = new HashSet<>();
		}
		months.add(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));

		period.put(cal.get(Calendar.YEAR), months);
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
	
	public String getPeriod() {
		StringBuilder str = new StringBuilder();
		for (Map.Entry<Integer, HashSet<String>> map : period.entrySet()) {
			str.append(map.getKey()).append(" ");
			for (String m : map.getValue()) {
				str.append(m).append(" ");
			}
			str.append("\n");
		}
		return str.toString();
	}
}
