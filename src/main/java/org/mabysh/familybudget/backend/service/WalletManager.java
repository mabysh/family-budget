package org.mabysh.familybudget.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.mabysh.familybudget.backend.entity.OperationType;
import org.mabysh.familybudget.backend.entity.Wallet;
import org.mabysh.familybudget.backend.entity.WalletOperation;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletManager {
	
	private Wallet currentWallet;
	private Long currentVersion;
	
	private List<WalletOperation> allOperations;
	
	private Long inUse = 0L;
	private Long postponed = 0L;
	private Long income = 0L;
	private Long expense = 0L;
	

	@Autowired
	private static AccountService accountService;

	public void initWallet(Long accountId) {
		currentWallet = accountService.findWallet(accountId);
		currentVersion = currentWallet.getVersion();
		allOperations = accountService.findAllWalletOperations(currentWallet.getId());
		if (allOperations == null) {
			allOperations = new ArrayList<WalletOperation>();
		}
		
		for (WalletOperation wop : allOperations) {
			Long amount = wop.getAmount();
			switch (wop.getOpType()) {
			case WITHDRAW : 
				expense += amount;
				inUse -= amount;
				break;
			case DEPOSIT :
				income += amount;
				inUse += amount;
				break;
			case POSTPONE :
				postponed += amount;
				inUse -= amount;
				break;
			case UNDEFINED :
				break;
			}
		}
	}
	
	private void updateCurrentWallet() {
		accountService.saveWallet(currentWallet);
		currentWallet = accountService.findWallet(currentWallet.getId());
		currentVersion = currentWallet.getVersion();
	}
	
	public void addOperation(OperationType opType, Long amount) {
		WalletOperation newOperation = new WalletOperation(amount, opType);
		allOperations.add(newOperation);
		switch (opType) {
		case WITHDRAW : 
			expense += amount;
			inUse -= amount;
			break;
		case DEPOSIT :
			income += amount;
			inUse += amount;
			break;
		case POSTPONE :
			postponed += amount;
			inUse -= amount;
			break;
		case UNDEFINED :
			break;
		}
		updateCurrentWallet();
	}

	public Long getCurrentVersion() {
		return currentVersion;
	}
	
	public List<WalletOperation> getAllOperations() {
		return allOperations;
	}
	
	public Long getInUse() {
		return inUse;
	}
	
	public Long getPostponed() {
		return postponed;
	}
	
	public Long getIncome() {
		return income;
	}
	
	public Long getExpense() {
		return expense;
	}
	
}
