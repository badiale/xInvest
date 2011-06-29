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
public class Tick implements Serializable {
		
		@Id
		@Column
		@SequenceGenerator(name = "seq_tickid", sequenceName = "seq_tickid")
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_tickid")
		private Integer id;
		
		@ManyToOne
    	@JoinColumn(name="tick_fk", insertable=false, updatable=false)
    private Quote quote = null;
		
		@Column
		private Float tick;	
		@Column
		private String timestamp;
		
		/**
		* Logger
		**/
		static Logger log = Logger.getLogger("org.xinvest.beans.Tick");
	
		/**
		* Pojo
		**/
    public Tick() {
    	this.id = new Integer(-1);
			this.quote = new Quote();
			this.tick = new Float(0);
			this.timestamp = new String();
		}

		//SETTERS E GETTERS
		public Integer getId() { return this.id; }
    public void setQuote(Integer id) { this.id = id; }
    
    public Quote getQuote() { return this.quote; }
    public void setQuote(Quote quote) { this.quote = quote; }

    public Float getTick() { return this.tick; }
    public void setTick(Float tick) { this.tick = tick; }
    
    public String getTimestamp() { return this.timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }  

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
	
		public static Tick find(Integer id) {
			Tick t = new Tick();
			Session session = DBManager.getSession();
			session.load(t, id);
			return t;
		}

		public static List findAll() {
			Session session = DBManager.getSession();
			return session.createQuery("SELECT t FROM Tick t").list();
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
				t.setTimestamp("28/06/2011");
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
			//test01();
			//test02();
			//test03();
			test04();
		}
}
