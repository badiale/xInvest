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
                  
                  parser.parse("src/org/xinvest/robot/xmlteste.xml");
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

      public void startElement(String uri, String localName,
         String rawName, Attributes attributes)
      {

      }

		  public void endElement(String namespaceURI, String localName,
		          String rawName) {
				count++;
		  }

      public void characters(char[] ch, int start, int length)
            throws SAXException {

      }

      public void endDocument()
      {
					
      }
}
		
