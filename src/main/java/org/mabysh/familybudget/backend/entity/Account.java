package org.mabysh.familybudget.backend.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@SuppressWarnings("serial")
@Entity
@Table(name="ACCOUNT")
public class Account implements Serializable, Cloneable{
	
	@Id
    @GeneratedValue
    private Long id;
	
	@Version
	private Long version;
	
	private String login;
	
	private String password;
	
	public Long getId() { return id; }
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getVersion() {
		return version;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Account login: " + getLogin() + ", id:" + getId();
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

        if (obj instanceof Account && obj.getClass().equals(getClass())) {
            return this.id.equals(((Account) obj).getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;

	}
    
    public Account() { }
    
    public Account(Account acc) {
    	this.login = acc.login;
    	this.password = acc.password;
    }
}

