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
			log.info("Investment Encontrado");
		}

		public static void main (String args[]) {
			//test01();
			test02();

		}
    
}
