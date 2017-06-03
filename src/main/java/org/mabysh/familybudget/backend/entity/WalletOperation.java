package org.mabysh.familybudget.backend.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	
	public Long getId() { return id; }
	
	public void setId(Long id) {
		this.id = id;
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
}
