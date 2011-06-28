package org.xinvest.examples;

import org.xinvest.db.DBManager;

import org.hibernate.*;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class B implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "a_id", insertable=false, updatable=false)
	private A a;

	public B() {
		id = null;
		a = null;
	}

	public Integer getId() { return this.id; }
	public void setA(A a) { this.a = a; }
	public A getA() {return this.a; }

	public static void main (String[] args) {
		A a = new A();
		B b = new B();
		B b2 = null;
		
		b.setA(a);

		Session session = DBManager.getSession();
		session.beginTransaction();
		// tem que salvar A primeiro
		session.save(a);

		// e depois B
		Integer id = (Integer) session.save(b);

		b2 = (B) session.load(B.class, id);
		session.getTransaction().commit();

		System.out.println("Id de B: " + b2.getId());
		System.out.println("Id de A: " + b2.getA().getId());
	}
}
