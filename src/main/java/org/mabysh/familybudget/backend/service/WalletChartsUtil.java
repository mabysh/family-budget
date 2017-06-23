package org.mabysh.familybudget.backend.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.mabysh.familybudget.backend.entity.WalletOperation;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletChartsUtil {
	
	@Autowired
	private WalletManager walletManager;
	
	private HashMap<Integer, HashMap<Integer, ArrayList<Long[]>>> history;

	private ArrayList<Double> availableList;
	private ArrayList<Double> postponedList;
	private ArrayList<Double> incomeList;
	private ArrayList<Double> expensesList;
	private ArrayList<String> labelList;
	
	public void calculateSelectedPeriod(Integer year, Integer month) {
		history = walletManager.getHistory();
		
		ArrayList<Long[]> monthList = history.get(year).get(month);
		Calendar cal = new GregorianCalendar(year, month, 1);
		
		availableList = new ArrayList<>(monthList.size());
		postponedList = new ArrayList<>(monthList.size());
		incomeList = new ArrayList<>(monthList.size());
		expensesList = new ArrayList<>(monthList.size());
		labelList = new ArrayList<>(monthList.size());
		
		Long inc = monthList.get(0)[2];
		Long expens = monthList.get(0)[3];
		for (int i = 1; i <= monthList.size(); i++) {
			cal.set(Calendar.DAY_OF_MONTH, i);
			labelList.add(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH)
					+ ", " + i);
			availableList.add((monthList.get(i - 1)[0]) * 1.0);
			postponedList.add(monthList.get(i - 1)[1] * 1.0);
			incomeList.add(monthList.get(i - 1)[2] - inc * 1.0);
			expensesList.add(monthList.get(i - 1)[3] - expens * 1.0);
		}
	}
	
	public ArrayList<Double> getAvailableList() {
		return availableList;
	}
	
	public ArrayList<Double> getPostponedList() {
		return postponedList;
	}
	
	public ArrayList<Double> getIncomeList() {
		return incomeList;
	}
	
	public ArrayList<Double> getExpensesList() {
		return expensesList;
	}
	
	public ArrayList<String> getLabelList() {
		return labelList;
	}

}
