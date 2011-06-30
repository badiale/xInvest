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
 		@Column
		private Float daysHigh;
		@Column
		private Float daysLow;
		@Column
 		private Float yearLow;
 		@Column
 		private Float yearHigh;
 		@Column
  	private Float fiftydayMovingAverage;
  	@Column
  	private Integer volume;
  	@Column
  	private String stockExchange;
		
		@OneToMany
	    @JoinColumn(name="tick_fk")
		private Set<Tick> ticks = new HashSet<Tick>();
		
		@ManyToOne
    	@JoinColumn(name="quotes_fk", insertable=false, updatable=false)
    private WebQuotes webQuotes = null;
    
    @OneToMany
	    @JoinColumn(name="investment_fk")
		private Set<Investment> investments = new HashSet<Investment>();

		
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
    
    public Set getInvestments() { return this.investments; }
    public void setInvestments(Set investments) { this.ticks = investments; }
    
    public WebQuotes getWebQuotes() { return this.webQuotes; }
    public void setWebQuotes(WebQuotes webQuotes) { this.webQuotes = webQuotes; }

    public Float getDaysHigh() { return this.daysHigh; }
    public void setDaysHigh(Float daysHigh) { this.daysHigh = daysHigh; }  
    
		public Float getDaysLow() { return this.daysLow; }
    public void setDaysLow(Float daysLow) { this.daysLow = daysLow; }
    
    public Float getYearHigh() { return this.yearHigh; }
    public void setYearHigh(Float yearHigh) { this.yearHigh = yearHigh; }
    
    public Float getYearLow() { return this.yearLow; }
    public void setYearLow(Float yearLow) { this.yearLow = yearLow; }
    
    public Float getFiftydayMovingAverage() { return this.fiftydayMovingAverage; }
    public void setFiftydayMovingAverage(Float fiftydayMovingAverage) { this.fiftydayMovingAverage = fiftydayMovingAverage; }
    
    public Integer getVolume() { return this.volume; }
    public void setVolume(Integer volume) { this.volume = volume; }
    
    public String getStockExchange() { return this.stockExchange; }
    public void setStockExchange(String stockExchange) { this.stockExchange = stockExchange; }  
    
    
    
    

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
		
		public Float getLastestTick() {
			Session session = DBManager.getSession();
			return (Float) session.createQuery(
				"SELECT t.tick FROM Tick t,Quote q WHERE t.quote = q.quote and q.quote = :quote ORDER BY t.timestamp DESC "
				).setParameter("quote",this.quote).list().iterator().next();
		}
		
		//Suggestion Function
		public String suggest() {
			if(this.getLastestTick() < this.fiftydayMovingAverage) {
				return new String("Compre!");
			} else if(this.getLastestTick() > this.fiftydayMovingAverage) {
				return new String("Venda!");
			} else return new String("");	
		}

		//TESTERS
		private static void test01() {
			Session session = DBManager.getSession();
			session.beginTransaction();
				WebQuotes w = WebQuotes.find(new Integer(1));
			
				
				Quote q = new Quote();
				q.setQuote("PBR");
				q.setWebQuotes(w);
				q.setName("Petrobras");
			
				q.insert();
				
				w.getQuotes().add(q);
			
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
		
		private static void test03() {
			Session session = DBManager.getSession();
			session.beginTransaction();
				Quote q = Quote.find("PBR");			
			
				log.info("Quote Recuperada");
				q.remove();
			
				session.getTransaction().commit();
				log.info("Quote Removida");
		}
		
			private static void test04() {
			Session session = DBManager.getSession();
			session.beginTransaction();
				Quote q = Quote.find("PBR");			
			
				log.info("Quote Recuperada");
				
				q.setName("Petroobras");
				q.update();
			
				session.getTransaction().commit();
				log.info("Quote Alterada");
		}
		
		private static void test05() {
			Session session = DBManager.getSession();
			session.beginTransaction();
				Quote q = Quote.find("PBR");			
			
				log.info("Quote Recuperada");
			
				Iterator it = q.getInvestments().iterator();
				while (it.hasNext()) {
					Investment i = (Investment) it.next();
					log.info(i.getAmount());
				}
		
			session.getTransaction().commit();
			log.info("Todos os investments listados");	
		}
		
		private static void test06() {
			Session session = DBManager.getSession();
			session.beginTransaction();
				Quote q = Quote.find("GOOG");			
			
				log.info("Quote Recuperada");
				log.info(q.getLastestTick());
		
			session.getTransaction().commit();
			log.info("Todos os investments listados");	
		}

		public static void main (String args[]) {
			test01();
			//test02();
			//test03();
			//test04();
			//test05();
			//test06();

		}
}
