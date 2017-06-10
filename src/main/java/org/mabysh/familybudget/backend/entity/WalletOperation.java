package org.mabysh.familybudget.backend.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

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
	@JoinColumn(name = "wallet_id")
	private Wallet operationWallet;
	
	private OperationType opType;
	
	private Long amount;
	
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
	
	public Calendar getOpDateTime() {
		return opDateTime;
	}
	
	public void setOpDateTime(Calendar c) {
		this.opDateTime = c;
	}

	public Wallet getOperationWallet() {
		return operationWallet;
	}

	public void setOperationWallet(Wallet operationWallet) {
		this.operationWallet = operationWallet;
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(this.id == null) {
            return false;
        }

        if (obj instanceof WalletOperation && obj.getClass().equals(getClass())) {
            return this.id.equals(((WalletOperation) obj).getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;

	}
    
    @Override
    public String toString() {
    	return "Operation id: " + getId() + ", type: " + getOpType() + ", wallet id: " + getOperationWallet().getId();
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
