package org.xinvest.beans;

// Log4J
import org.apache.log4j.Logger;


import org.xinvest.db.DBManager;
import org.hibernate.*;
import java.io.Serializable;
import javax.persistence.*;

import java.util.*;

/**
 * Transaction
 * @author The mistery guy
 **/
 
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Transaction implements Serializable {

	@Id
	@Column
	@SequenceGenerator(name = "seq_trans", sequenceName = "seq_trans")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_trans")
	private Integer id;

	@Column
	private Date timestamp;
	
	@Column
	private Float value;;
	
	// TODO voltar esses JoinColumn quando fizer o OneToMany no usuario
	@ManyToOne
	@JoinColumn(name = "user_active", insertable=false, updatable=false)
	private User active = null;
	
	@ManyToOne
	@JoinColumn(name = "user_passive", insertable=false, updatable=false)
	private User passive = null;

	/**
	* Logger
	**/
	static Logger log = Logger.getLogger("org.xinvest.beans.Transaction");

	/**
	* Pojo
	**/
	public Transaction() {
		this.id = new Integer(-1);
		this.timestamp = new Date();
	}
	
	// sets
	public void setId        (Integer id   ) { this.id        = id   ; }
	public void setTimestamp (Date    time ) { this.timestamp = time ; }
	public void setValue     (Float   value) { this.value     = value; }
	public void setPassive   (User    user ) { this.passive   = user ; }
	public void setActive    (User    user ) { this.active    = user ; }

	// gets
	public Integer getId        () { return this.id       ; }
	public Date    getTimestamp () { return this.timestamp; }
	public Float   getValue     () { return this.value    ; }
	public User    getPassive   () { return this.passive  ; }
	public User    getActive    () { return this.active   ; }

	// sql
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

	public static Transaction find(Integer id) {
		Transaction t = new Transaction();
		Session session = DBManager.getSession();
		session.load(t, id);
		return t;
	}

	public static List findAll() {
		Session session = DBManager.getSession();
		return session.createQuery("SELECT t FROM Transaction t").list();
	}

	// unit test
	// teste com o insert
	private static void test01() {
		Session session = DBManager.getSession();
		session.beginTransaction();
		
		log.info("Inserindo um usario de teste");
		User user = new User();
		user.setEmail("teste@email.com");
		user.setName("Teste");
		user.setPassword("pass");
		user.setMoney(new Float(1000));
		user.insert();

		log.info("Inserindo 10 transacoes");
		for (int i = 0; i < 10; i++) {
			Transaction t = new Transaction();
			t.setPassive(user);
			t.setValue(new Float(1000 * (i + 1)));
			t.insert();
		}

		session.getTransaction().commit();
		log.info("Transacao inserida");
	}
	
	// teste com o find all
	private static void test02() {
		Session session = DBManager.getSession();
		session.beginTransaction();
		
		log.info("Lendo todas as transacoes");
		Iterator it = Transaction.findAll().iterator();
		while (it.hasNext()) {
			Transaction t = (Transaction) it.next();
			
			String str = "";
			str += "Timestamp: " + t.getTimestamp() + "\n";
			str += "Valor : " + t.getValue() + "\n";
			str += "User name: " + t.getPassive().getName() + "\n";
			str += "User Email: " + t.getPassive().getEmail() + "\n";
			str += "User money: " + t.getPassive().getMoney() + "\n";
			log.info(str);
		}

		session.getTransaction().commit();
		log.info("Transacoes lidas");
	}

	// find e update
	private static void test03 () {
		Session session = DBManager.getSession();
		session.beginTransaction();
		
		Transaction t = Transaction.find(new Integer(1));

		t.setValue(new Float(1000000));
		t.update();

		session.getTransaction().commit();
		log.info("Transacao atualizada.\nNovo Valor: " + t.getValue());
	}

	// remove
	private static void test04 () {
		Session session = DBManager.getSession();
		session.beginTransaction();
		
		log.info("removendo tudo");
		Iterator it = Transaction.findAll().iterator();
		while (it.hasNext()) {
			Transaction t = (Transaction) it.next();
			t.remove();
		}

		session.getTransaction().commit();
		log.info("Transacoes removidas");
	}

	public static void main (String args[]) {
		test01();
		test02();
		test03();
		test04();
	}
}
