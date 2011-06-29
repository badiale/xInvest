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
public class Loan extends Transaction implements Serializable {
    @Column(name="interest")
    private Float interest;
    
    public Loan () {
        this.interest = new Float(0);
    }
    
    public void setInterest(Float interest) { this.interest = interest; }
    public Float getInterest() { return this.interest; }
    
    /**
     * Function to accumulate composite interest over time. A call to this
     * function adds the interest to the total amount in debt.
     */
    public void addInterest () {
        this.value += this.value*this.interest;
    }

	public static Loan find (Integer id) {
		Session session = DBManager.getSession();
		return (Loan) session.load(Loan.class, id);
	}
}
