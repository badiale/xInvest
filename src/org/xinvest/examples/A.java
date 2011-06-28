package org.xinvest.examples;

import org.xinvest.db.DBManager;

import org.hibernate.*;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

// Set
import java.util.*;

@Entity
public class A implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column
	private Integer id;
	
	@OneToMany
	@JoinColumn(name = "a_id") // essa coluna vai ficar na tabela de B
	private Set<B> bs = new HashSet<B>();

	public A() {
		id = null;
	}

	public Integer getId() { return this.id; }
	public Set<B> getBs() { return this.bs; }

	public static void main (String[] args) {
		A a = new A();
		A a2 = null;

		Session session = DBManager.getSession();
		session.beginTransaction();
		Integer id = (Integer) session.save(a);

		for (int i = 0; i < 10; i++) {
			B b = new B();
			
			// A GENTE TEM QUE COLOCAR O a EM b, ELE NAO FAZ SOZINHO
			b.setA(a);
			// TEM Q SALVAR OS Bs ANTES
			session.save(b);
			a.getBs().add(b);
		}
		session.getTransaction().commit();
		
		session = DBManager.getSession();
		session.beginTransaction();
		a2 = (A) session.load(A.class, id);
		
		System.out.println("Id de A: " + a.getId());
		// tem q ser dentro da transacao
		Iterator it = a2.getBs().iterator();
		while (it.hasNext()) {
			B b = (B) it.next();
			System.out.println("Id de B: " + b.getId() + " do A de codigo: " + b.getA().getId());
		}

		session.getTransaction().commit();
	}
}
