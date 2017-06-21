package org.mabysh.familybudget.backend.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.mabysh.familybudget.backend.entity.Account;
import org.mabysh.familybudget.backend.entity.Wallet;
import org.mabysh.familybudget.backend.entity.WalletOperation;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AccountService {

	@PersistenceContext(unitName = "family_budget_db")
	EntityManager em;
	
	public AccountService() { }

	@Transactional
	public Wallet findWallet(Long id) {
		return em.find(Wallet.class, id);
	}
	
	@Transactional
	public Account findAccount(Long id) {
		return em.find(Account.class, id);
	}
	
	@Transactional
	public Account findIdenticalAccount(Account account) {
		try {
			Account result = em.createQuery("SELECT a FROM Account a WHERE a.login=:accLogin AND a.password=:accPassword", Account.class)
					.setParameter("accLogin", account.getLogin())
					.setParameter("accPassword", account.getPassword())
					.getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Transactional
	public Account findAccountByLogin(String login) {
		try {
			Account result = em.createQuery("SELECT a FROM Account a WHERE a.login=:accLogin", Account.class)
					.setParameter("accLogin", login).getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Transactional
	public List<WalletOperation> findAllWalletOperations(Long accountId) {
		try {
			List<WalletOperation> result = em.createQuery("SELECT o FROM WalletOperation o WHERE o.wallet.walletId=:accountId",
					WalletOperation.class)
					.setParameter("accountId", accountId).getResultList();
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Transactional
	public WalletOperation findWalletOperationByCalendar(Calendar calendar) {
		try {
			WalletOperation result = em.createQuery("SELECT a FROM WalletOperation a WHERE a.opDateTime=:calendar", WalletOperation.class)
					.setParameter("calendar", calendar).getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveAccount(Account account) {
		Account acc = em.merge(account);
		em.persist(acc);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveWallet(Wallet wallet) {
		Wallet wal = em.merge(wallet);
		em.persist(wal);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveWalletOperation(WalletOperation operation) {
		WalletOperation op = em.merge(operation);
		em.persist(op);
	}
}
