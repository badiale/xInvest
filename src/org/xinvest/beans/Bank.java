package org.xinvest.beans;

import java.io.*;                   // File and Serializable imports
import java.util.*;                 // Set
import javax.persistence.*;         // Persistence/Annotations
import org.hibernate.Session;	    // Database session
import org.xinvest.db.DBManager;    // Hibernate session handler

/**
 * Bank class to handle bank operations within a stock simulator application.
 * @author Fábio Abrão Luca
 */
@Entity
public class Bank extends User {
    @Column(name="interest")
    private Float interest; // taxa de juros atual
    
    public Bank() {}
    
    public Bank(String email, String name, String pass,
            float money,float interest) {
        setEmail(email);
        setName(name);
        setPassword(pass);
        setMoney(new Float(money));
        setInterest(new Float(interest));
    }
    
    public void setInterest(Float interest) { this.interest = interest; }
    public Float getInterest() { return this.interest; }
	
	public static Bank find (String email) {
		Session session = DBManager.getSession();
		return (Bank) session.load(Bank.class, email);
	}
    
    private static void Test01 () {
		Session session = DBManager.getSession();
		session.beginTransaction();
        Bank bank = new Bank();
        bank.setEmail("bank@bank.com");
        bank.setName("Bank");
        bank.setPassword("bank");
        bank.setMoney(new Float(100000));
        bank.setInterest(new Float(0.5));
		for (int i = 0; i < 10; i++) {
			Transaction t = new Transaction();
			t.setPassive(bank);
			t.setValue(new Float(1000 * (i + 1)));
			t.insert();
			bank.getTransactionActives().add(t);
		}
        try {
            bank.insert();
        } catch (Exception e) {
            System.err.println("ERRO:Test01:INSERT: "+e);
            e.printStackTrace();
        }
        Bank bankdb = Bank.find("bank2");
        System.out.println("Email: "+bankdb.getEmail());
        System.out.println("Name: "+bankdb.getName());
        System.out.println("Pass: "+bankdb.getPassword());
        System.out.println("Money: "+bankdb.getMoney());
        System.out.println("Interest: "+bankdb.getInterest());
		Iterator it2 = bankdb.getTransactionActives().iterator();
		while (it2.hasNext()) {
			Transaction t = (Transaction) it2.next();
			System.out.println("\tData da transacao: " + t.getTimestamp());
			System.out.println("\tValor da transacao: " + t.getValue());
		}
		System.out.println("");
        bankdb.setInterest(new Float(0.8));
        bankdb.update();
		session.getTransaction().commit();
    }
    
    public static void main (String args[]) {
        Test01();
    }
}
