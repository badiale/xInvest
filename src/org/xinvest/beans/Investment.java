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
public class Investment /*extends Transaction*/ implements Serializable {
    
    @ManyToOne
    @JoinColumn(name="investment_fk", insertable=false, updatable=false)
    private Quote quote = null;
    
    @Column(name="amount")
    private Integer amount;
    
    public Investment () {}
    
    public void setQuote(Quote quote) { this.quote = quote; }
    public void setAmount(Integer amount) { this.amount = amount; }
    
    public Quote getQuote() { return this.quote; }
    public Integer getAmount() { return this.amount; }
    
    public Float getAverageTick () {
        float tick = this.value/this.amount;
        return new Float(tick);
    }
    
    public static Investment findByQuote (Quote quote) {
        Session session = DBManager.getSession();
        session.beginTransaction();
        Investment investment = (Investment) session.get("org.xinvest.beans.Investment", quote);
        session.getTransaction().commit();
        return user;
    }
    
}
