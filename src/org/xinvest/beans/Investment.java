package org.xinvest.beans;

import java.io.*;                   // File and Serializable imports
import java.util.*;                 // Set
import javax.persistence.*;         // Persistence/Annotations
import org.hibernate.Session;	    // Database session
import org.xinvest.db.DBManager;    // Hibernate session handler
import org.xinvest.config.Config;   // Hibernate session handler

/**
 * 
 * @author Fábio Abrão Luca
 */
@Entity
public class Investment extends Transaction implements Serializable {
    
    @ManyToOne
    @JoinColumn(name="investment_fk", insertable=false, updatable=false)
    private Quote quote = null;
    
    @Column(name="amount")
    private Integer amount;
    
    public Investment () {
    	super();
    	this.amount = new Integer(0);
    }
    
    public void setQuote(Quote quote) { this.quote = quote; }
    public void setAmount(Integer amount) { this.amount = amount; }
    
    public Quote getQuote() { return this.quote; }
    public Integer getAmount() { return this.amount; }
    
    public Float getAverageTick () {
        float tick = this.value/this.amount;
        return new Float(tick);
    }

	public static Investment find (Integer id) {
		Session session = DBManager.getSession();
		return (Investment) session.load(Investment.class, id);
	}
    
    public static List findByActive (User user) {
        Session session = DBManager.getSession();
        // Query in Hibernate Query Language
        String hql = "select i from Investment i where i.active.email = :email";
        org.hibernate.Query query = session.createQuery(hql).setParameter("email", user.getEmail());
        return query.list();
    }
    
	public static List findByPassive (User user) {
        Session session = DBManager.getSession();
        // Query in Hibernate Query Language
        String hql = "select i from Investment i where i.passive.email = :email";
        org.hibernate.Query query = session.createQuery(hql).setParameter("email", user.getEmail());
        return query.list();
    }
    
   	//TESTERS
	private static void test01() {
		Session session = DBManager.getSession();
		session.beginTransaction();
			
			Quote q = Quote.find("PBR");
		
			Investment i = new Investment();
			
			i.setAmount(new Integer(100));
			i.setValue(new Float(1.11));
			i.setQuote(q);
			i.insert();
			
			q.getInvestments().add(i);
		
		session.getTransaction().commit();
		log.info("Investment Inserido");
	}
	
	private static void test02() {
		Session session = DBManager.getSession();
		session.beginTransaction();

		
			Investment i = (Investment) Investment.find(new Integer(1));
			log.info("Investment Encontrado");
			
			log.info(i.getQuote().getQuote());

		
		session.getTransaction().commit();
	}
	
	private static void test03() {
		Session session = DBManager.getSession();
		session.beginTransaction();
		
		List list = Investment.findByActive(User.find("mail01"));
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Investment i = (Investment) it.next();
			String str = "";
			str += "User: " + i.getActive().getEmail() + "\n";
			str += "Amount: " + i.getAmount() + "\n";
			str += "Quote: " + i.getQuote().getQuote() + "\n";
			log.info(str);
		}
		log.info("Mostrou todos os investments");

		session.getTransaction().commit();
	}
	public static void main (String args[]) {
		//test01();
		//test02();
		test03();
	}
}
