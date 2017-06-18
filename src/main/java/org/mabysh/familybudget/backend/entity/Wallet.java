package org.mabysh.familybudget.backend.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name="WALLET")
public class Wallet implements Serializable, Cloneable{
	
	@Id
	private Long walletId;
	
	private Long version;
	
	@OneToOne
	@MapsId
	private Account account;
	
	public Long getId() {
		return walletId;
	}

	public void setId(Long id) {
		this.walletId = id;
	}
	
	public Account getWalletAccount() {
		return account;
	}

	public void setWalletAccount(Account walletAccount) {
		this.account = walletAccount;
	}


	public Long getVersion() {
		return version;
	}
	
	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Wallet id: " + getId() + ", account login: " + getWalletAccount().getLogin();	
	}
	
	public boolean isPersisted() {
		return walletId != null;
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(this.walletId == null) {
            return false;
        }

        if (obj instanceof Wallet && obj.getClass().equals(getClass())) {
            return this.walletId.equals(((Wallet) obj).getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.walletId);
        return hash;

	}
    
    public Wallet() {
    	this.version = 0L;
    }
	
}
