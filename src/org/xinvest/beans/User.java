package org.xinvest.beans;

import java.io.*;                   // File and Serializable imports
import java.util.*;                 // Set
import javax.persistence.*;         // Persistence/Annotations
import org.hibernate.Session;	    // Database session
import org.xinvest.db.DBManager;    // Hibernate session handler
import org.xinvest.config.Config;   // Hibernate session handler

/**
 * User class to hold user data and handle related operations in a stock
 * simulator application.
 * @author Fábio Abrão Luca
 */
@Entity
@Table(name="user_xinvest")
@Inheritance(strategy=InheritanceType.JOINED)
public class User implements Serializable, Config {
    public static final String imagesFolder = appFolder+"/xInvest/user_pictures";
    
    @Id
    @Column(name="email",unique=true,nullable=false)
    private String email;

    @Column(name="name",nullable=false)
    private String name;

    @Column(name="password",nullable=false)
    private String password;

    @Column(name="money")
    private Float money;
    
	@OneToMany
	@JoinColumn(name="user_active")
	private Set<Transaction> tactives = new HashSet<Transaction>();
	
	@OneToMany
	@JoinColumn(name="user_passive")
	private Set<Transaction> tpassives = new HashSet<Transaction>();

    // quotes
    // selling
    // debts
    // loans
    
    static {
        File filesdir = new File(imagesFolder);
        filesdir.mkdir();
    }

    public User() {}
    
    public User(String email, String name, String pass, float money) {
        setEmail(email);
        setName(name);
        setPassword(pass);
        setMoney(new Float(money));
    }
    
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setMoney(Float money) { this.money = money; }

    public String getEmail() { return this.email; }
    public String getName() { return this.name; }
    public String getPassword() { return this.password; }
    public Float getMoney() { return this.money; }
	public Set<Transaction> getTransactionActives() { return this.tactives; }
	public Set<Transaction> getTransactionPassives() { return this.tpassives; }
    
    /**
     * Inserts the object of this class in the database and touches the hard
     * drive for a blank file to write the uploaded picture to.
     */
    public void insert () { //throws IOException {
        Session session = DBManager.getSession();
        session.save(this);
        // Touching hard drive
        //File file = new File(imagesFolder+"/"+this.email);
        //file.createNewFile(); // May generate IOException
    }
    
    /**
     * Updates the object of this class in the database.
     */
    public void update () {
        Session session = DBManager.getSession();
        session.update(this);
    }
    
    /**
     * Removes the object of this class from the database and the corresponding
     * picture from the hard drive.
     */
    public void remove () {
        Session session = DBManager.getSession();
        // Removes file from the hard drive
        File file = new File(imagesFolder+"/"+this.email);
        file.delete();
        // Removes entry from the database
        session.delete(this);
    }
    
    /**
     * @param email String E-mail of the user.
     * @return User object with all data of the user with given e-mail.
     */
    public static User find (String email) {
        Session session = DBManager.getSession();
        User user = (User) session.get("org.xinvest.beans.User", email);
        return user;
    }
    
    /**
     * @return List All users from the database.
     */
    public static List findAll () {
        Session session = DBManager.getSession();
        // Query in Hibernate Query Language
        String hql = "from User";
        org.hibernate.Query query = session.createQuery(hql);
        List list = query.list();
        return list;
    }
    
    /**
     * Function to authenticate a user in the system. It's done by verifying if
     * the given e-mail and password are a match and exists in the database.
     * @param email String User's e-mail
     * @param password String User's password
     * @return User The authenticated user or null if user not found
     */
    public static User authenticate(String email, String password) {
        User u = new User();

        Session session = DBManager.getSession();
        session.load(u, email);
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }
    
    private static void Test01 () {
        Session session = DBManager.getSession();
        session.beginTransaction();

        User user = new User("mail01","name01","pass01",100);
        System.out.println("Email: "+user.getEmail());
        System.out.println("Name: "+user.getName());
        System.out.println("Pass: "+user.getPassword());
        System.out.println("Money: "+user.getMoney());
        try {
            user.insert(); 
        } catch (Exception e) {
            System.err.println("ERRO:Test01:INSERT: "+e);
            e.printStackTrace();
        }
        
		session.getTransaction().commit();
    }
    private static void Test02 () {
        Session session = DBManager.getSession();
        session.beginTransaction();
        User user1 = new User("mail01","name01","pass01",100);
        User user2 = new User("mail02","name02","pass02",200);
        User user3 = new User("mail03","name03","pass03",300);

        try {
            user1.insert(); 
			for (int i = 0; i < 10; i++) {
				Transaction t = new Transaction();
				t.setPassive(user1);
				t.setValue(new Float(1000 * (i + 1)));
				t.insert();
				user1.getTransactionActives().add(t);
			}
            user2.insert(); 
            user3.insert(); 
        } catch (Exception e) {
            System.err.println("ERRO:Test02:INSERT: "+e);
            e.printStackTrace();
        }
        List list = User.findAll();
        Iterator itr = list.iterator();
        while (itr.hasNext()) {
            User userdb = (User) itr.next();
            System.out.println("Email: "+userdb.getEmail());
            System.out.println("Name: "+userdb.getName());
            System.out.println("Pass: "+userdb.getPassword());
            System.out.println("Money: "+userdb.getMoney());

			Iterator it2 = userdb.getTransactionActives().iterator();
			while (it2.hasNext()) {
				Transaction t = (Transaction) it2.next();
				System.out.println("\tData da transacao: " + t.getTimestamp());
				System.out.println("\tValor da transacao: " + t.getValue());
			}
			System.out.println("");
        }
		session.getTransaction().commit();
    }
    private static void Test03 () {
        Session session = DBManager.getSession();
        session.beginTransaction();
        User user1 = new User("mail01","name01","pass01",100);
        User user2 = new User("mail02","name02","pass02",200);
        User user3 = new User("mail03","name03","pass03",300);
        try {
            user1.insert(); 
            user2.insert(); 
            user3.insert(); 
        } catch (Exception e) {
            System.err.println("ERRO:Test03:INSERT: "+e);
            e.printStackTrace();
        }
        User userdb = User.find("mail02");
        System.out.println("Email: "+userdb.getEmail());
        System.out.println("Name: "+userdb.getName());
        System.out.println("Pass: "+userdb.getPassword());
        System.out.println("Money: "+userdb.getMoney()+"\n");
		session.getTransaction().commit();
    }
    
    public static void main (String args[]){
        //Test01();
        Test02();
        //Test03();
    } 
}
