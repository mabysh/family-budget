package org.mabysh.familybudget.backend.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
	
	private HashMap<Integer, HashMap<Integer, ArrayList<Long[]>>> history = new HashMap<>();
	
	private Long available = 0L;
	private Long postponed = 0L;
	private Long income = 0L;
	private Long expenses = 0L;
	
	
	public void initWallet(Long accountId) {
		currentWallet = accountService.findWallet(accountId);
		currentVersion = currentWallet.getVersion();
		
		loadHistory();
	}

	private void updateCurrentWallet() {
		accountService.saveWallet(currentWallet);
		currentWallet = accountService.findWallet(currentWallet.getId());
		currentVersion = currentWallet.getVersion();
	}
	
	public void addOperation(OperationType opType, Long amount) {
		WalletOperation newOperation = new WalletOperation(amount, opType);
		newOperation.setWallet(currentWallet);
		accountService.saveWalletOperation(newOperation);
		newOperation = accountService.findWalletOperationByCalendar(newOperation.getOpDateTime());
		allOperations.add(newOperation);
		
		addOperationToHistory(newOperation);
		
		currentWallet.setVersion(++currentVersion);
		updateCurrentWallet();
	}
	
	private void loadHistory() {
		allOperations = accountService.findAllWalletOperations(currentWallet.getId());
		available = 0L;
		postponed = 0L;
		income = 0L;
		expenses = 0L;
		
		history.clear();

		for (WalletOperation op : allOperations) {
			addOperationToHistory(op);
		}
		fillHistoryEmptyDays();
	}
	
	private void addOperationToHistory(WalletOperation op) {
		Long amount = op.getAmount();
		Calendar cal = op.getOpDateTime();
		switch (op.getOpType()) {
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
		
		HashMap<Integer, ArrayList<Long[]>> year = history.get(cal.get(Calendar.YEAR));
		if (year == null) {
			history.put(cal.get(Calendar.YEAR), new HashMap<>());
			year = history.get(cal.get(Calendar.YEAR));
		}
		ArrayList<Long[]> month = year.get(cal.get(Calendar.MONTH));
		if (month == null) {
			year.put(cal.get(Calendar.MONTH), new ArrayList<Long[]>(cal.getActualMaximum(Calendar.DAY_OF_MONTH)));
			month = year.get(cal.get(Calendar.MONTH));
			for (int i = 0; i < cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
				month.add(new Long[] {0L, 0L, 0L, 0L} );
			}
		}
		Long[] day = month.get(cal.get(Calendar.DAY_OF_MONTH) - 1);

		day[0] = available;
		day[1] = postponed;
		day[2] = income;
		day[3] = expenses;
		
		fillHistoryEmptyDays();			//TODO find a way to optimize this
	
	}
	
	private void fillHistoryEmptyDays() {
		Long av = 0L;
		Long post = 0L;
		Long inc = 0L;
		Long exp = 0L;
		for (Integer y : getPeriodYears()) {
			for (Integer m : getPeriodMonths(y)) {
				ArrayList<Long[]> month = history.get(y).get(m);
				for (Long[] d : month) {
					if (d[2] == 0L) {
						d[0] = av;
						d[1] = post;
						d[2] = inc;
						d[3] = exp;
					} else {
						av = d[0];
						post = d[1];
						inc = d[2];
						exp = d[3];
					}
				}
			}
		}
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
	
	public HashMap<Integer, HashMap<Integer, ArrayList<Long[]>>> getHistory() {
		return history;
	}
	
	public ArrayList<Integer> getPeriodYears() {
		ArrayList<Integer> y = new ArrayList<>(history.keySet());
		Collections.sort(y);
		return y;
	}
	
	public ArrayList<Integer> getPeriodMonths(Integer year) {
		HashMap<Integer, ArrayList<Long[]>> months = history.get(year);
		ArrayList<Integer> m = new ArrayList<>(months.keySet());
		Collections.sort(m);
		return m;
	}

}
