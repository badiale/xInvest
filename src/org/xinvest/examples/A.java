package org.xinvest.examples;

import org.xinvest.db.DBManager;

import org.hibernate.*;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class A implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="id_a")
	private Integer id;

	public A() {
		id = null;
		oneb = null;
	}

	public Integer getId() { return this.id; }

	public static void main (String[] args) {
		A a = new A();

		Session session = DBManager.getSession();
		session.beginTransaction();
		session.save(a);
		session.save(b);
		session.getTransaction().commit();
	}
}
