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
		@SequenceGenerator(name = "seq_tickid", sequenceName = "seq_tickid")
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_tickid")
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
    public Tick() {
    	this.id = new Integer(-1);
			this.bank = new Bank();
			this.tickJuros = new Float(0);
			this.timestamp = new Date();
		}

		//SETTERS E GETTERS
		public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }
    
    public Quote getQuote() { return this.quote; }
    public void setQuote(Quote quote) { this.quote = quote; }

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
			
				Quote q = Quote.find("PBR");
			
				log.debug(q.getQuote());
			
				Tick t = new Tick();
				t.setQuote(q);			
				t.setTick(new Float(31.23));
				t.insert();

				q.getTicks().add(t);

			
			session.getTransaction().commit();
			log.info("Tick inserido");
		}
		
		private static void test02() {
			Session session = DBManager.getSession();
			session.beginTransaction();
				Tick t2 = Tick.find(2);
			session.getTransaction().commit();

			log.info("Tick recuperado");
			log.debug(t2.getQuote().getQuote());
		}
		
		private static void test03() {
			Session session = DBManager.getSession();
			session.beginTransaction();
				Tick t2 = Tick.find(1);
				
				t2.remove();
				
			session.getTransaction().commit();
			log.info("Tick removido");

		}
		
		private static void test04() {
			Session session = DBManager.getSession();
			session.beginTransaction();
				Tick t2 = Tick.find(2);
				t2.setTick(new Float(2));
				
			session.getTransaction().commit();
			log.info("Tick alterado");

		}

		public static void main (String args[]) {
			test01();
			//test02();
			//test03();
			//test04();
		}
}
