package org.xinvest.beans;

// Log4J
import org.apache.log4j.Logger;


import org.xinvest.db.DBManager;
import org.hibernate.*;
import java.io.Serializable;
import javax.persistence.*;

import java.util.*;

/**
 * WebQuotes
 * @author Gabriel Perri Gimenes
 **/
 
@Entity
public class WebQuotes implements Serializable {
	
		@Id
		@Column
		private Integer id;
		
		@Column String name;
		
		@OneToMany
	    @JoinColumn(name="quotes_fk")
		private Set<Quote> quotes = new HashSet<Quote>();

		
		/**
		* Logger
		**/
		static Logger log = Logger.getLogger("org.xinvest.beans.WebQuotes");
	
		/**
		* Pojo
		**/
    public WebQuotes() {
			this.id = new Integer(0);
		}

		//SETTERS E GETTERS
    public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public Set getQuotes() { return this.quotes; }
    public void setQuotes(Set quotes) { this.quotes = quotes; }
    

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
	
		public static WebQuotes find(Integer id) {
			WebQuotes w = new WebQuotes();
			Session session = DBManager.getSession();
			session.load(w, id);
			return w;
		}

		public static List findAll() {
			Session session = DBManager.getSession();
			return session.createQuery("SELECT w FROM WebQuotes w").list();
		}

		//TESTERS
		private static void test01() {
			Session session = DBManager.getSession();
			session.beginTransaction();
			
				WebQuotes w = new WebQuotes();
				w.setId(new Integer(1));
				w.setName("Mainlist");
			
				w.insert();
			
			session.getTransaction().commit();
			log.info("WebQuotes Inserida");
		}
		
		private static void test02() {
			Session session = DBManager.getSession();
			session.beginTransaction();
			
			WebQuotes w = WebQuotes.find(new Integer(1));
			
			Iterator it = w.getQuotes().iterator();
			while (it.hasNext()) {
					Quote q = (Quote) it.next();
					log.info(q.getQuote());
			}
				
			session.getTransaction().commit();
			log.info("Todas as quotes foram listadas");
		}

		public static void main (String args[]) {
			//test01();
			test02();

		}
}
