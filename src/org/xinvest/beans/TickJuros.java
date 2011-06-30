package org.xinvest.beans;

// Log4J
import org.apache.log4j.Logger;


import org.xinvest.db.DBManager;
import org.hibernate.*;
import java.io.Serializable;
import javax.persistence.*;

import java.util.*;

/**
 * Tick
 * @author Gabriel Perri Gimenes
 **/
 
@Entity
public class TickJuros implements Serializable {
		
		@Id
		@Column
		@SequenceGenerator(name = "seq_tickjurosid", sequenceName = "seq_tickjurosid")
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_tickjurosid")
		private Integer id;
		
		@ManyToOne
    	@JoinColumn(name="tickjuros_fk", insertable=false, updatable=false)
    private Bank bank = null;
		
		@Column
		private Float tickJuros;	
		@Column
		private Date timestamp;
		
		/**
		* Logger
		**/
		static Logger log = Logger.getLogger("org.xinvest.beans.TickJuros");
	
		/**
		* Pojo
		**/
    public TickJuros() {
    	this.id = new Integer(-1);
			this.bank = new Bank();
			this.tickJuros = new Float(0);
			this.timestamp = new Date();
		}

		//SETTERS E GETTERS
		public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }
    
    public Bank getBank() { return this.bank; }
    public void setBank(Bank quote) { this.bank = bank; }

    public Float getTickJuros() { return this.tickJuros; }
    public void setTickJuros(Float tickJuros) { this.tickJuros = tickJuros; }
    
    public Date getTimestamp() { return this.timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }  

		//SQLERS
		public void insert() {
			Session session = DBManager.getSession();
			session.save(this);

		}

		public void remove() {
			Session session = DBManager.getSession();
			session.delete(this);
		}

		public void update() {
			Session session = DBManager.getSession();
			session.update(this);
		}
	
		public static TickJuros find(Integer id) {
			TickJuros tj = new TickJuros();
			Session session = DBManager.getSession();
			session.load(tj, id);
			return tj;
		}

		public static List findAll() {
			Session session = DBManager.getSession();
			return session.createQuery("SELECT tj FROM TickJuros tj").list();
		}

		//TESTERS
		private static void test01() {
			Session session = DBManager.getSession();
			session.beginTransaction();
			
				Bank b = Bank.find("bank@bank.com");
			
				log.debug(b.getName());
			for(int i = 0; i< 100; i++) {
				TickJuros tj = new TickJuros();
				tj.setBank(b);			
				tj.setTickJuros(new Float(1*i));
				tj.insert();

				b.getTicks().add(tj);

			}
			session.getTransaction().commit();
			log.info("TickJuros inserido");
		}
		
		public static void main (String args[]) {
			test01();
		}
}
