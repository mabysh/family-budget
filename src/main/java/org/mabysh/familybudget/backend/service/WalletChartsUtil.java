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

	private List<Double> availableList;
	private List<Double> postponedList;
	private List<Double> incomeList;
	private List<Double> expensesList;
	private List<String> labelList;
	
	public void calculateSelectedPeriod(Integer year, Integer month) {
		Calendar now = Calendar.getInstance();
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
		if (year == now.get(Calendar.YEAR) && month == now.get(Calendar.MONTH)) {
			Integer day = now.get(Calendar.DAY_OF_MONTH);
			availableList = availableList.subList(0, day);
			postponedList = postponedList.subList(0, day);
			incomeList = incomeList.subList(0, day);
			expensesList = expensesList.subList(0, day);
			labelList = labelList.subList(0, day);
		}
	}
	
	public List<Double> getAvailableList() {
		return availableList;
	}
	
	public List<Double> getPostponedList() {
		return postponedList;
	}
	
	public List<Double> getIncomeList() {
		return incomeList;
	}
	
	public List<Double> getExpensesList() {
		return expensesList;
	}
	
	public List<String> getLabelList() {
		return labelList;
	}

}
