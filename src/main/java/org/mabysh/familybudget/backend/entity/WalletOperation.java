package org.mabysh.familybudget.backend.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name="WALLET_OPERATION")
public class WalletOperation implements Serializable, Cloneable{
	
	@Id
    @GeneratedValue
    private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "walletId")
	private Wallet wallet;
	
	private OperationType opType;
	
	private Long amount;
	
	private Long available;	//these four are balance values at
	private Long postponed;	//the moment of this operation
	private Long income;		//
	private Long expenses;		//
	
	@Column(name = "date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar opDateTime;
	
	public Long getId() { return id; }
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	
	public Long getAmount() {
		return amount;
	}
	
	public Long getAvailabe() {
		return available;
	}
	
	public void setAvailable(Long available) {
		this.available = available;
	}

	public Long getIncome() {
		return income;
	}
	
	public void setIncome(Long income) {
		this.income = income;
	}
	
	public Long getPostponed() {
		return postponed;
	}
	
	public void setPostponed(Long postponed) {
		this.postponed = postponed;
	}

	public Long getExpenses() {
		return expenses;
	}
	
	public void setExpenses(Long expenses) {
		this.expenses = expenses;
	}

	public Calendar getOpDateTime() {
		return opDateTime;
	}
	
	public void setOpDateTime(Calendar c) {
		this.opDateTime = c;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public OperationType getOpType() {
		return opType;
	}

	public void setOpType(OperationType opType) {
		this.opType = opType;
	}
	
	public boolean isPersisted() {
		return id != null;
	}
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((opDateTime == null) ? 0 : opDateTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WalletOperation other = (WalletOperation) obj;
		if (opDateTime == null) {
			if (other.opDateTime != null)
				return false;
		} else if (!opDateTime.equals(other.opDateTime))
			return false;
		return true;
	}

	@Override
    public String toString() {
    	return "Operation id: " + getId() == null ? "Not Persisted" : getId()
    		+ " | type: " + getOpType() + " | amount: " + getAmount()
    		+ " | date/time: " + opDateTime.getTime();
    }
    
    public WalletOperation(Long amount, OperationType opType) {
    	this.amount = amount;
    	this.opType = opType;
    	this.opDateTime = Calendar.getInstance();
    }
    
    public WalletOperation() {
    	this.opType = OperationType.UNDEFINED;
    	this.amount = 0L;
    	this.opDateTime = Calendar.getInstance();
    }
}
