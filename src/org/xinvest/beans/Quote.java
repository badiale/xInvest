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
public class Quote implements Serializable {
	
		@Id
		@Column
		private String quote;
		@Column
		private String name;
		
		@OneToMany
	    @JoinColumn(name="tick_fk")
		private Set<Tick> ticks = new HashSet<Tick>();

		
		/**
		* Logger
		**/
		static Logger log = Logger.getLogger("org.xinvest.beans.Quote");
	
		/**
		* Pojo
		**/
    public Quote() {
			this.quote = new String();
			this.name = new String();
		}

		//SETTERS E GETTERS
    public String getQuote() { return this.quote; }
    public void setQuote(String quote) { this.quote = quote; }
    
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }  

    public Set getTicks() { return this.ticks; }
    public void setTicks(Set ticks) { this.ticks = ticks; }
    

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
	
		public static Quote find(String quote) {
			Quote q = new Quote();
			Session session = DBManager.getSession();
			session.load(q, quote);
			return q;
		}

		public static List findAll() {
			Session session = DBManager.getSession();
			return session.createQuery("SELECT q FROM Quote q").list();
		}

		//TESTERS
		private static void test01() {
			Session session = DBManager.getSession();
			session.beginTransaction();
			
				Quote q = new Quote();
				q.setQuote("PBR");
				q.setName("Petrobras");
			
				q.insert();
			
			session.getTransaction().commit();
			log.info("Quote Inserida");
		}
		
		private static void test02() {
			Session session = DBManager.getSession();
			session.beginTransaction();
				Quote q = Quote.find("PBR");			
			
				log.info("Quote Recuperada");
			
				Iterator it = q.getTicks().iterator();
				while (it.hasNext()) {
					Tick t = (Tick) it.next();
					log.info(t.getTick());
				}
		
			session.getTransaction().commit();
			log.info("Todos os ticks listados");	
			
		}

		public static void main (String args[]) {
			//test01();
			test02();

		}
}
