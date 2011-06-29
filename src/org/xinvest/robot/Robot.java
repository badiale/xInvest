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


         u = new URL("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22YHOO%22%2C%22AAPL%22%2C%22GOOG%22%2C%22MSFT%22)%0A%09%09&diagnostics=true&env=http%3A%2F%2Fdatatables.org%2Falltables.env");
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
		  
		//TESTERS
		private static void test01() {
      
		}

		public static void main(String args[]) {
			      try {
                  //Event Interface
                  xmlParser SAXHandler = new xmlParser();

                  //XERCES SAXParser CLASS
                  SAXParser parser = new SAXParser();
                  parser.setContentHandler(SAXHandler);
                  parser.setErrorHandler(SAXHandler);
                  
                  Robot r = new Robot();
                  r.getXml();
                  
                  parser.parse(new org.xml.sax.InputSource(new java.io.StringReader(r.getXml())));
                  log.debug(SAXHandler.count);
                                              
            }
            catch (Exception ex) {
                  System.out.println(ex);
            }

		}
}

/**
*	Handler class for the SAX Parser
**/
class xmlParser extends DefaultHandler
{
      String value = "";
      String currentElement = "";
      public int count = 0;

      
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
		    	//insere
		    	Session session = DBManager.getSession();
					session.beginTransaction();
					
		    	WebQuotes w = WebQuotes.find(1);
		    	
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
		
