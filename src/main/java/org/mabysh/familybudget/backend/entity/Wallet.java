package org.mabysh.familybudget.backend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name="WALLET")
public class Wallet implements Serializable, Cloneable{
	
	@Id
	private Long id;
	
	@OneToOne
	@MapsId
	private Account account;
	
	private Long inUse;
	
	private Long postponed;
	
	@OneToMany(
			mappedBy = "operationWallet",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private List<WalletOperation> operationList = new ArrayList<>();
	
	public void addOperation(WalletOperation wop) {
		operationList.add(wop);
		wop.setOperationWallet(this);
	}
	
	public void removeOperation(WalletOperation wop) {
		operationList.remove(wop);
		wop.setOperationWallet(null);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getWalletAccount() {
		return account;
	}

	public void setWalletAccount(Account walletAccount) {
		this.account = walletAccount;
	}

	public Long getInUse() {
		return inUse;
	}

	public void setInUse(Long inUse) {
		this.inUse = inUse;
	}

	public Long getPostponed() {
		return postponed;
	}

	public void setPostponed(Long postponed) {
		this.postponed = postponed;
	}

	public List<WalletOperation> getOperationList() {
		return operationList;
	}

	public void setOperationList(List<WalletOperation> operationList) {
		this.operationList = operationList;
	}

	@Override
	public String toString() {
		return "Wallet id: " + getId() + ", account login: " + getWalletAccount().getLogin();	
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

        if (obj instanceof Wallet && obj.getClass().equals(getClass())) {
            return this.id.equals(((Wallet) obj).getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;

	}
	
}
