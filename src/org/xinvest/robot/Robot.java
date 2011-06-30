package org.xinvest.robot;

// Log4J
import org.apache.log4j.Logger;

// Xerces
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

// Hibernate
import org.xinvest.db.DBManager;
import org.hibernate.*;
import java.io.Serializable;
import javax.persistence.*;

import java.util.*;
import java.text.*; 

// URL
import java.io.*;
import java.net.*;

// Quotes/Ticks
import org.xinvest.beans.*;

/**
 * Robot
 * @author Gabriel Perri Gimenes
 **/

public class Robot implements Serializable {

		private String[] companies = {"YHOO","AAPL","GOOG","MSFT","PBR", "CSCO", "BAC", "AMZN", "AXP","T", "BA","HPQ","DELL","DIS","AMD","F","GE","INTC", "JNJ", "NOK", "NKE", "NVDA"};

		/**
		* Logger
		**/
		static Logger log = Logger.getLogger("org.xinvest.robot.Robot");

    public Robot() {
			
		}
		
		public String getXml() {
			URL u;
      InputStream is = null;
      DataInputStream dis;
      String s;
      String buffer = new String();

      try {

				//OBS AS EMPRESAS ESTAO DEFINIDAS HARCODED SE MUDAR TEM QUE MUDAR O VETOR COMPANIES
         u = new URL("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22YHOO%22%2C%22AAPL%22%2C%22GOOG%22%2C%22MSFT%22%2C%22PBR%22%2C%20%22CSCO%22%2C%20%22BAC%22%2C%20%22AMZN%22%2C%20%22AXP%22%2C%22T%22%2C%20%22BA%22%2C%22HPQ%22%2C%22DELL%22%2C%22DIS%22%2C%22AMD%22%2C%22F%22%2C%22GE%22%2C%22INTC%22%2C%20%22JNJ%22%2C%20%22NOK%22%2C%20%22NKE%22%2C%20%22NVDA%22)%0A%09%09&diagnostics=true&env=http%3A%2F%2Fdatatables.org%2Falltables.env");
         is = u.openStream();     
         dis = new DataInputStream(new BufferedInputStream(is));

         while ((s = dis.readLine()) != null) {
            buffer += s;
         }
				
				 is.close();
					
					return buffer;
					
			  } catch (Exception e) { e.printStackTrace();}
			     
			return buffer;
		 		
		 }
		 
		 public String getXmlHistorical(String quote) {
			URL u;
      InputStream is = null;
      DataInputStream dis;
      String s;
      String buffer = new String();

      try {


					//OBS AS DATAS ESTAO DEFINIDAS HARCODED
         u = new URL("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20in%20(%22"+quote+"%22)%20and%20startDate%20%3D%20%222010-06-01%22%20and%20endDate%20%3D%20%222050-01-01%22%0A%09%09&diagnostics=true&env=http%3A%2F%2Fdatatables.org%2Falltables.env");
         is = u.openStream();     
         dis = new DataInputStream(new BufferedInputStream(is));

         while ((s = dis.readLine()) != null) {
            buffer += s;
         }
				
				 is.close();
					
					return buffer;
					
			  } catch (Exception e) { e.printStackTrace();}
			     
			return buffer;
		 		
		 }
		
		 //popula os ticks das quotes escolhidas 
		 public void doHistory() throws Exception {
		 	 //Event Interface
		    xmlParser2 SAXHandler = new xmlParser2();
		    

		    //XERCES SAXParser CLASS 
		    SAXParser parser = new SAXParser();
		    parser.setContentHandler(SAXHandler);
		    parser.setErrorHandler(SAXHandler);
		    
		    Robot r = new Robot();
		    
		    
		 		int i = 0;
		 		for(i = 0; i< this.companies.length; i++) {
		 			SAXHandler.quote = companies[i];
		 			parser.parse(new org.xml.sax.InputSource(new java.io.StringReader(r.getXmlHistorical(companies[i]))));
		    	log.debug(SAXHandler.count);
		 		}
		
		 }
		 
		 
		 
		 //1 for insertion
		 //0 for update
		 public void robotize(int i) {
		 		try {
                  //Event Interface
                  xmlParser SAXHandler = new xmlParser(i);

                  //XERCES SAXParser CLASS 
                  SAXParser parser = new SAXParser();
                  parser.setContentHandler(SAXHandler);
                  parser.setErrorHandler(SAXHandler);
                  
                  Robot r = new Robot();
                  
                  parser.parse(new org.xml.sax.InputSource(new java.io.StringReader(r.getXml())));
                  log.debug(SAXHandler.count);
                  
                  
                  if(i == 1) {
                  	r.doHistory();
                  }
                                              
            }
            catch (Exception ex) {
                  ex.printStackTrace();
            }
		 }
		  
		public static void main(String args[]) {
			Robot r =  new Robot();
			r.robotize(1);
		}
}

/**
*	Handler class for the SAX Parser
**/
class xmlParser extends DefaultHandler
{
      String value = "";
      String currentElement = "";
      int mode;
      public int count = 0;

			xmlParser(int i) {
				this.mode = i;
			}
      
      // Quote Attributes
      String name;
      String quote;
      Float tick;
      Float daysHigh;
      Float daysLow;
      Float yearLow;
      Float yearHigh;
      Float fiftydayMovingAverage;
      Integer volume;
      String stockExchange;

      public void startElement(String uri, String localName,
         String rawName, Attributes attributes)
      {
					this.currentElement = localName;
					
					if(this.currentElement.equals("quote")) {
					 int length = attributes.getLength();

						for (int i=0; i<length; i++) {
						String attr = attributes.getQName(i);
							if(attr.equals("symbol")) {
								String quote = attributes.getValue(i);
								this.quote = quote;
								System.out.println();
								System.out.println(quote);

							}
						}
						
					}
		  }

		  public void endElement(String namespaceURI, String localName,
		          String rawName) {
		    
		    if(localName.equals("quote")) {

		    	Session session = DBManager.getSession();
					session.beginTransaction();
					
		    	WebQuotes w = WebQuotes.find(1);
		    	
		    			    
		    	if(mode == 1) {
		    		//insere
							Quote q = new Quote();
							q.setWebQuotes(w);
							
							q.setQuote(this.quote);
							q.setName(this.name);
							q.setDaysHigh(this.daysHigh);
							q.setDaysLow(this.daysLow);
							q.setYearLow(this.yearLow);
							q.setYearHigh(this.yearHigh);
							q.setFiftydayMovingAverage(this.fiftydayMovingAverage);
							q.setVolume(this.volume);
							q.setStockExchange(this.stockExchange);
							
							q.insert();
							
							w.getQuotes().add(q);
							
							Tick t = new Tick();
							
							t.setTick(this.tick);
							t.setQuote(q);
							t.insert();
							    	
							q.getTicks().add(t);   	
							
							session.getTransaction().commit();
							System.out.println("Quote Inserida");
		    	} else if(mode == 0) {
		    		//Atualiza
		    			Quote q = Quote.find(this.quote);
							
							q.setName(this.name);
							q.setDaysHigh(this.daysHigh);
							q.setDaysLow(this.daysLow);
							q.setYearLow(this.yearLow);
							q.setYearHigh(this.yearHigh);
							q.setFiftydayMovingAverage(this.fiftydayMovingAverage);
							q.setVolume(this.volume);
							q.setStockExchange(this.stockExchange);
							
							q.update();
							
							Tick t = new Tick();
							
							t.setTick(this.tick);
							t.setQuote(q);
							t.insert();
							    	
							q.getTicks().add(t);   	
							
							session.getTransaction().commit();
							System.out.println("Quote Atualizada");
		    	}
			
		    	this.name = null;
				  this.quote = null;
				  this.tick = null;
				  this.daysHigh = null;
				  this.daysLow = null;
				  this.yearLow = null;
				  this.yearHigh = null;
				  this.fiftydayMovingAverage = null;
				  this.volume = null;
				  this.stockExchange = null;
				  
		    }     
		    
				count++;
				this.currentElement = "";
        value = "";
		  }

      public void characters(char[] ch, int start, int length)
            throws SAXException {
            
            if(this.currentElement.equals("Name")) {
                String buffer = new String(ch, start, length);
                if(!buffer.trim().equals("")) {
                    this.value = buffer;
                    System.out.println("Name:"+value);
                    this.name = value;
                }
            }
             if(this.currentElement.equals("AskRealtime")) {
                String buffer = new String(ch, start, length);
                if(!buffer.trim().equals("")) {
                     this.value = buffer;
                      System.out.println("AskRealtime:"+value);
                      this.tick = Float.parseFloat(value);
                }
            }
             if(this.currentElement.equals("DaysHigh")) {
                String buffer = new String(ch, start, length);
                if(!buffer.trim().equals("")) {
                     this.value = buffer;
                     System.out.println("DaysHigh:"+value);
                     this.daysHigh = Float.parseFloat(value);
                }
            }
             if(this.currentElement.equals("DaysLow")) {
                String buffer = new String(ch, start, length);
                if(!buffer.trim().equals("")) {
                     this.value = buffer;
                      System.out.println("DaysLow:"+value);
                      this.daysLow = Float.parseFloat(value);
                }
            }
            if(this.currentElement.equals("YearHigh")) {
                String buffer = new String(ch, start, length);
                if(!buffer.trim().equals("")) {
                     this.value = buffer;
                      System.out.println("YearHigh:"+value);
                      this.yearHigh = Float.parseFloat(value);
                }
            }
             if(this.currentElement.equals("YearLow")) {
                String buffer = new String(ch, start, length);
                if(!buffer.trim().equals("")) {
                     this.value = buffer;
                      System.out.println("YearLow:"+value);
                      this.yearLow = Float.parseFloat(value);
                }
            }
             if(this.currentElement.equals("FiftydayMovingAverage")) {
                String buffer = new String(ch, start, length);
                if(!buffer.trim().equals("")) {
                     this.value = buffer;
                      System.out.println("FiftydayMovingAverage:"+value);
                      this.fiftydayMovingAverage = Float.parseFloat(value);
                }
            }
            if(this.currentElement.equals("Volume")) {
                String buffer = new String(ch, start, length);
                if(!buffer.trim().equals("")) {
                     this.value = buffer;
                      System.out.println("Volume:"+value);
                      this.volume = Integer.parseInt(value);
                }
            }
             if(this.currentElement.equals("StockExchange")) {
                String buffer = new String(ch, start, length);
                if(!buffer.trim().equals("")) {
                     this.value = buffer;
                      System.out.println("StockExchange:"+value);
                      this.stockExchange = value;
                }
            }
            
      }

      public void endDocument()
      {
					System.out.println("Parsing Terminado");
      }
}


/**
*	Handler class for the SAX Parser
**/
class xmlParser2 extends DefaultHandler
{
      String value = "";
      String currentElement = "";
      public int count = 0;
      
      // Quote Attributes
      public String quote;
      Float tick;
      Date timestamp;
   

      public void startElement(String uri, String localName,
         String rawName, Attributes attributes)
      {
					this.currentElement = localName;
					try{
							if(this.currentElement.equals("quote")) {
							 int length = attributes.getLength();

								for (int i=0; i<length; i++) {
								String attr = attributes.getQName(i);
									if(attr.equals("date")) {
										SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
										Date date = format.parse(attributes.getValue(i));
										this.timestamp = date;
										System.out.println();
										System.out.println(date.toString());

									}
								}
						
					}
					} catch (Exception e) {e.printStackTrace(); }
		  }

		  public void endElement(String namespaceURI, String localName,
		          String rawName) {
		    
		    if(localName.equals("quote")) {

		    	Session session = DBManager.getSession();
					session.beginTransaction();
					
					Quote q = Quote.find(this.quote);
					
					Tick t = new Tick();
							
							t.setTick(this.tick);
							t.setQuote(q);
							t.setTimestamp(this.timestamp);
							t.insert();
							    	
							q.getTicks().add(t);   	
		 
		 			session.getTransaction().commit();
		 			
				  this.tick = null;
				  this.timestamp = null;
			
		    }     
		    
				count++;
				this.currentElement = "";
        value = "";
		  }

      public void characters(char[] ch, int start, int length)
            throws SAXException {
            
            if(this.currentElement.equals("Close")) {
                String buffer = new String(ch, start, length);
                if(!buffer.trim().equals("")) {
                    this.value = buffer;
                    System.out.println("Close:"+value);
                    this.tick = Float.parseFloat(value);
                }
            }        
      }

      public void endDocument()
      {
					System.out.println("Parsing Terminado");
      }
}
		
