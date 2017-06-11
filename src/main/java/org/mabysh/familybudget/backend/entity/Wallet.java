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
import javax.persistence.Version;


@SuppressWarnings("serial")
@Entity
@Table(name="WALLET")
public class Wallet implements Serializable, Cloneable{
	
	@Id
	private Long id;
	
	@Version
	private Long version;
	
	@OneToOne
	@MapsId
	private Account account;
	
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


	public Long getVersion() {
		return version;
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
    
    public Wallet() {
    }
	
}
