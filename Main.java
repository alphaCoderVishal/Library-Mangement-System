package pkg1;

import java.awt.*;
import java.awt.desktop.UserSessionEvent;
import java.security.PublicKey;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.jar.*;
import net.proteanit.sql.DbUtils;

import javax.swing.*;

import com.mysql.cj.x.protobuf.MysqlxSql.StmtExecute;

//import org.graalvm.compiler.core.common.type.ArithmeticOpTable.BinaryOp.Add;

//import org.graalvm.compiler.nodes.StaticDeoptimizingNode;

public class Main    {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		//create();
		//User_menu("2");
		//login();
		Admin_menu("1");

	}
	public static void login()
	{
		JFrame f= new JFrame("Login");// class to create the canvas for the GUI
		JLabel j1= new JLabel("Username");// to add label on the 
		j1.setBounds(50,10 ,30, 5);//set boundries for the label
		JLabel j2= new JLabel("Password");
		j2.setBounds(50,15,30, 5);

		JTextField t1,t2;
		t1=new JTextField(10);
		t1.setBounds(80, 10, 30,5);// set dimensions for text field
		t2=new JTextField(10);
		t2.setBounds(80, 15, 30, 5);

		JButton jButton=new JButton("Login");
		jButton.setBounds(95,20,20,5);
		jButton.addActionListener(ae->{
			String userName =t1.getText() ;
			String pswd=t2.getText();
			if(userName.equals(""))
			{
				JOptionPane.showMessageDialog(null,"Please enter the username:");// pane to show message

			}
			else if(pswd.equals(""))
			{
				JOptionPane.showMessageDialog(null,"password can't be empty");
			}
			else {
				Connection conn = connect();
				try {
					String string=("SELECT * FROM users where USERNAME ='"+userName+"'and PASSWORD ='"+pswd+"'");

					Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					st.executeUpdate("USE library_user_database");
					ResultSet rSet = st.executeQuery(string);// stores result in table
					if(rSet.next()==false)
					{
						//System.out.println(rSet.getString(1)+" "+rSet.getString(2));
						JOptionPane.showMessageDialog(null,"Not a valid credintial");
					}
					else {
						f.dispose();//kill the jframe used for login console
						//rSet.beforeFirst();//move the pointer to the initial
						String admin =rSet.getString("ADMIN");
						String  UID = rSet.getString("UID");
						System.out.println(UID);
						rSet.absolute(0);
						while(rSet.next())
						{
						      User_menu(UID) ;                     
						}
					}
					st.close();
					conn.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});


		f.add(j1);// add label on the canvas
		f.add(t1);//add textbox on the canvas
		f.add(j2);
		f.add(t2);
		f.add(jButton);
		f.setLayout(new FlowLayout());
		f.setVisible(true);
		f.setSize(400, 400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);

	}

	public static Connection connect()
	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");//import the package
			Connection con= DriverManager.getConnection("jdbc:mysql://localhost/mysql?user=root&password=5677");
			return con;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static void create()
	{
		try {
			Connection connection= connect();
			//ResultSet resultSet=connection.getMetaData().getCatalogs();
			// let's iterate each catalog in the resultSet
			//			while(resultSet.next())
			//			{
			//				String databaseName = resultSet.getString(1);
			//				if(databaseName.equals("library_user_database"))
			//				{
			//					//System.out.println("yes");
			//				}
			//			}



			Statement stmt = connection.createStatement();
			stmt.executeUpdate("USE library_user_database");
			String sql1 = "CREATE TABLE USERS(UID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, USERNAME VARCHAR(30), PASSWORD VARCHAR(30), ADMIN BOOLEAN)";
			stmt.executeUpdate(sql1);
			stmt.executeUpdate("INSERT INTO USERS(USERNAME, PASSWORD, ADMIN) VALUES('admin','admin',TRUE)");
			stmt.executeUpdate("CREATE TABLE BOOKS(BID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, BNAME VARCHAR(50), GENRE VARCHAR(20), PRICE INT)");
			//Create Issued Table
			stmt.executeUpdate("CREATE TABLE ISSUED(IID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, UID INT, BID INT, ISSUED_DATE VARCHAR(20), RETURN_DATE VARCHAR(20), PERIOD INT, FINE INT)");
			//Insert into books table
			stmt.executeUpdate("INSERT INTO BOOKS(BNAME, GENRE, PRICE) VALUES ('War and Peace', 'Mystery', 200),  ('The Guest Book', 'Fiction', 300), ('The Perfect Murder','Mystery', 150), ('Accidental Presidents', 'Biography', 250), ('The Wicked King','Fiction', 350)");


		}
		catch (Exception ex) {
			ex.printStackTrace();
		}


	}

	public static void Admin_menu(String uid)

	{
		JFrame frame = new JFrame("Admin Functions");
		
		/*
		 * view Book button functionality
		 */
		JButton viewButton= new JButton("View Books");// button for view books;
		viewButton.setBounds(20, 40, 120, 25);//specify size and position
		//time for some actionlistener so that our button responds
		viewButton.addActionListener(ae->{   //used concept of anonymous class
			// time for new JFrame after clicking the button
			JFrame jFrame=new JFrame("Books Available");
			Connection connection=connect();//with the help of jdbc we get connection to database
			String sqlQString="SELECT * FROM books;";
			try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate("USE library_user_database;");
				stmt=connection.createStatement();//broker for sending sql to database
				ResultSet rSet=stmt.executeQuery(sqlQString);//rset will hold result in table
				// executeQuery for DQl
				JTable bookList = new JTable();//show data in table format
				bookList.setModel(DbUtils.resultSetToTableModel(rSet));//DISPLY THE RESULT
				
				JScrollPane scrollPane= new JScrollPane(bookList);
				//let's add this scrollpane to our jFrame
				jFrame.add(scrollPane);
				jFrame.setLayout(new FlowLayout());
				jFrame.setVisible(true);
				jFrame.setSize(800, 800);
				jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



			} catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, e);
			}
		});

		/*
		 * View user functions i.e Admin can see who are the 
		 * registered users at the system
		 * 
		 * 
		 */


		JButton usersButton = new JButton("Users");//Hard coupling not good
		usersButton.setBounds(140, 20, 120, 25);
		usersButton.addActionListener(ae->{
			//action listener implementation;
			//lets create new jframe for the view of users list
			JFrame userFrame= new JFrame("Users List");
			//connect with the database first;
			Connection connection=connect();
			try {
				
				String sql = "SELECT * FROM USERS;";
				
				//stmt will create a sql query for our connected databases.
				Statement stmt = connection.createStatement();
				stmt.executeUpdate("USE  library_user_database;");
			    ResultSet resultSet  =stmt.executeQuery(sql);
			    JTable table= new JTable();
			    //it will show the table 
			    table.setModel(DbUtils.resultSetToTableModel(resultSet));
			    //lets create scrollable table
			    JScrollPane scrollPane=new JScrollPane(table);
			    
			    userFrame.add(scrollPane);
				
			} catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, e);
			}
			userFrame.setLayout(new FlowLayout());
			userFrame.setLocationRelativeTo(null);
			userFrame.setVisible(true);
			userFrame.setSize(800, 500);
			userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		});
		
		
		/*
		 * we will now create a button for issued books on frame.
		*/
		JButton issuedButtons = new JButton("Issued Books");
		issuedButtons.setBounds(20, 65, 120, 25);
		//time for some action listeners
		issuedButtons.addActionListener(ae->{
			//let's switch our frame after clicking the buttons
			JFrame issueFrame= new JFrame("books Issued");
			//time for our database to come into action
			Connection connection=connect();
			try {
				String sqlString= "SELECT * FROM issued";
				Statement stmt = connection.createStatement();
				stmt.executeUpdate("USE library_user_database;");
				ResultSet resultSet=stmt.executeQuery(sqlString);
				//time for the table to catch the data
				JTable table = new JTable();
				table.setModel(DbUtils.resultSetToTableModel(resultSet));
				issueFrame.add(table);
			} catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, e);
			}
		
			
			issueFrame.setLayout(new FlowLayout());
			issueFrame.setVisible(true);
		    issueFrame.setSize(600, 400);
			issueFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		});
		/*
			let's get the admin power to add users to the databases
			*/
		JButton addUserButton=new JButton("Add User");
		addUserButton.setBounds(20, 90, 120, 25);
		addUserButton.addActionListener(al->{
//			let's create a new frame
			JFrame addUserFrame=new JFrame("Add User ");
			JLabel userJLabel=new JLabel("UserName");
			userJLabel.setBounds(30, 15, 100, 30);
			JTextArea usernameArea= new JTextArea();
			usernameArea.setBounds(110, 15, 200, 30);

			JLabel passwdJLabel= new JLabel("Password");
			passwdJLabel.setBounds(30, 50, 100, 30);
			//create specific password field
			JPasswordField passwordField= new JPasswordField();
			passwordField.setBounds(110, 50, 200, 30);

			JRadioButton jRadioButton=new JRadioButton("Admin");
			jRadioButton.setBounds(100, 80, 100, 30);

			//let's create radio button for user
			JRadioButton jRadioButton2 = new JRadioButton("User");
			jRadioButton2.setBounds(200, 80, 100, 30);

			//let's group the button together.
			ButtonGroup bg = new ButtonGroup();
			bg.add(jRadioButton2);bg.add(jRadioButton);

			// now time for adding a button called create and boom user added!
			JButton createButton = new JButton("create");
			createButton.setBounds(150, 120, 120, 30);
			createButton.addActionListener(ae->{
				/*
				 * now time for getting the input and adding to our database
				 */
				String username = usernameArea.getText();
				String password = passwordField.getText();
				Boolean admin =false;
				if(jRadioButton.isSelected())
				{
					admin=true;
				}
				
				Connection connection = connect();
				try {
					Statement stmt = connection.createStatement();
					stmt.executeUpdate("USE library_user_database;");
					stmt.executeUpdate("INSERT INTO users (USERNAME,PASSWORD,ADMIN)"
							+ "VALUES ('"+username+"','"+password+"',"+admin+")");
					JOptionPane.showMessageDialog(null, "Congrats!User Added.");
					addUserFrame.dispose();//work finished.
					// now to add the user 

				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, e);
					
				}
			});



			addUserFrame.add(userJLabel);
			addUserFrame.add(usernameArea);
			addUserFrame.add(passwdJLabel);
			addUserFrame.add(passwordField);
			addUserFrame.add(jRadioButton);
			addUserFrame.add(jRadioButton2);
			addUserFrame.add(createButton);
			addUserFrame.setLayout(null);
			addUserFrame.setVisible(true);
			addUserFrame.setSize(400, 400);
			addUserFrame.setLocationRelativeTo(null);
			addUserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		});

		/*
		 * Now give the admin right to add the book in our database/library
		 * time for some buttons and listeners
		*/
		JButton addBookButton = new JButton("Add Books");
		addBookButton.setBounds(150, 60, 120, 25);
		// add action listener
		addBookButton.addActionListener(al->{
			//now time for some new frame
			JFrame addBookFrame= new JFrame("Add Book");
			JLabel l1,l2,l3;
			l1= new JLabel("Book Name");
			l1.setBounds(30,15,100,30);
			l2= new JLabel("Genre");
			l2.setBounds(30,50,100,30);
			l3= new JLabel("Price");
			l3.setBounds(30, 85, 100, 30);
			// now time for text area
			JTextField fBname =new JTextField();//for single line
			fBname.setBounds(140, 15, 200, 30);
			
			JTextField fGenre =new JTextField();//for single line
			fGenre.setBounds(140, 55, 200, 30);
			
			JTextField fPrice =new JTextField();//for single line
			fPrice.setBounds(140, 95, 200, 30);
		   
			JButton createButton = new JButton("Add Book");
			createButton.setBounds(150, 130, 150, 25);
			createButton.addActionListener(listner->{
				Connection connection=connect();
				try {
					String bname=fBname.getText();
					String genre=fGenre.getText();
					int price_int=Integer.parseInt(fPrice.getText());
					Statement stmt = connection.createStatement();
					stmt.executeUpdate("USE library_user_database;");
					stmt.executeUpdate("INSERT INTO BOOKS(BNAME,GENRE,PRICE) VALUES ('"+bname+"','"+genre+"',"+price_int+")");
					JOptionPane.showMessageDialog(null, "Book Added");
					addBookFrame.dispose();
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, e);
				}
				
			});

			
			//Connection connection=connect();
			
			
			
			addBookFrame.add(l1);
			addBookFrame.add(l2);
			addBookFrame.add(l3);
			addBookFrame.add(fBname);
			addBookFrame.add(fGenre);
			addBookFrame.add(fPrice);
			addBookFrame.add(createButton);
			addBookFrame.setLayout(null);//absolute reference
			addBookFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			addBookFrame.setVisible(true);
			addBookFrame.setLocationRelativeTo(null);//absolute reference
			addBookFrame.setSize(400, 400);

		});
		
		
		JButton issue_book = new JButton("issue_book");
		issue_book.setBounds(150,200,60,35);
		
		//time for some action listeners
		issue_book.addActionListener(al->{
			JFrame issuebookFrame= new JFrame("Issue Book");
			JLabel l1,l2,l3,l4;
			l1= new JLabel("Book Id");
			l1.setBounds(20, 25, 60, 25);
			JTextArea t1,t2,t3,t4;
			t1=new JTextArea();
			t1.setBounds(100, 25, 240, 25);
			
			
			l2= new JLabel("User Id:");
			l2.setBounds(20, 75, 60, 25);
			t2=new JTextArea();
			t2.setBounds(100,75,240,25);
			
			
			l3=new JLabel("Period");
			l3.setBounds(20, 125, 60, 25);
			t3=new JTextArea();
			t3.setBounds(100,125,240,25);
			
			l4=new JLabel("Issue Date");
			l4.setBounds(20, 175, 65, 25);
			t4=new JTextArea();
			t4.setBounds(100,175,240,25);
             //issued button
			JButton createButton = new JButton("Create");
			createButton.setBounds(150, 225, 100, 25);
			
			
			
			//now time for the collection of the database to our system.
			
			
			
			
			createButton.addActionListener(ae->{
				
			
			String bid = t1.getText();
			String userid =t2.getText();
			int period_int =Integer.parseInt(t3.getText());
			String issueDate = t4.getText();
			Connection connection=connect();
			try {
				Statement stmt = connection.createStatement();
				System.out.println("Here");
				stmt.executeUpdate("USE library_user_database;");
				stmt.executeUpdate("INSERT INTO issued(UID,BID,ISSUED_DATE,PERIOD) VALUES ('"+userid+"','"+bid+"','"+issueDate+"',"+period_int+")");
				issuebookFrame.dispose();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			});
						
			issuebookFrame.add(l1);
			issuebookFrame.add(t1);
			issuebookFrame.add(l2);
			issuebookFrame.add(t2);
			issuebookFrame.add(l3);
			issuebookFrame.add(t3);
			issuebookFrame.add(l4);
			issuebookFrame.add(t4);
			issuebookFrame.add(createButton);
			issuebookFrame.setLayout(null);
			issuebookFrame.setVisible(true);
			issuebookFrame.setSize(400, 400);
			issuebookFrame.setLocationRelativeTo(null);
			issuebookFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		});
		JButton return_book = new JButton("Return book");
		return_book.setBounds(20,200,120,25);
		return_book.addActionListener(ae->{
	    JFrame retFrame= new JFrame("Return book");
	    JLabel l1,l2,l3;
	    l1= new JLabel("Issue ID:");
	    l1.setBounds(30, 15, 150, 30);
	    JTextArea t1=new JTextArea();
	    t1.setBounds(180, 15, 200, 30);
	    
	    l2= new JLabel("Return date(DD-MM-YY)");
	    l2.setBounds(30, 60, 150, 30);
	    JTextArea t2= new JTextArea();
	    t2.setBounds(180,60,200,30);
	    
	    JButton returnButton = new JButton("return");
	    returnButton.setBounds(180,120,100,30);
	    //time for action listeners
	    returnButton.addActionListener(al->{
	    	String issueId = t1.getText();
	    	String returnDate =t2.getText();
	    	Connection connection=connect();
	    	try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate("USE library_user_database;");
				String date1=null;
				String date2=returnDate;
				 ResultSet rs = stmt.executeQuery("SELECT ISSUED_DATE FROM ISSUED WHERE IID="+issueId);
                 while (rs.next()) {
                     date1 = rs.getString(1);
                      
                   }
                 Date date_1 = (Date) new SimpleDateFormat("dd-MM-yyyy").parse(date1);
                 Date date_2 = (Date)new SimpleDateFormat("dd-MM-yyyy").parse(date2);
                 long diff= date_2.getTime()-date_1.getTime();
                 
                 diff=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                 String fine = Long.toString(diff*10);
                 JOptionPane.showInputDialog(null, fine);
                 JOptionPane.showMessageDialog(null, "book returned");
			} catch (Exception e) {
				// TODO: handle exception
			}
	    			
	    });
	    
	    
	    retFrame.add(l1);
	    retFrame.add(t1);
	    retFrame.add(l2);
	    retFrame.add(t2);
	    retFrame.add(returnButton);
		retFrame.setLayout(null);
		retFrame.setVisible(true);
		retFrame.setSize(400, 400);
		retFrame.setLocationRelativeTo(null);
	    retFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		});
		
		
		frame.add(viewButton);
		frame.add(usersButton);
		frame.add(issuedButtons);
		frame.add(addUserButton);
		frame.add(addBookButton);
		frame.add(issue_book);
		frame.add(return_book);
		frame.setVisible(true);
		frame.setSize(600, 400);
		frame.setLayout(new FlowLayout());
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
	}
	public static void User_menu(String uid)
	{
		JFrame frame = new JFrame("User Functions");
		
		
		
		JButton viewButton= new JButton("View Books");// button for view books;
		viewButton.setBounds(20, 20, 120, 25);//specify size and position
		//time for some actionlistener so that our button responds
		viewButton.addActionListener(ae->{   //used concept of anonymous class
			// time for new JFrame after clicking the button
			JFrame jFrame=new JFrame("Books Available");
			Connection connection=connect();//with the help of jdbc we get connection to database
			String sqlQString="SELECT * FROM books;";
			try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate("USE library_user_database;");
				stmt=connection.createStatement();//broker for sending sql to database
				ResultSet rSet=stmt.executeQuery(sqlQString);//rset will hold result in table
				// executeQuery for DQl
				JTable bookList = new JTable();//show data in table format
				bookList.setModel(DbUtils.resultSetToTableModel(rSet));//DISPLY THE RESULT
				
				JScrollPane scrollPane= new JScrollPane(bookList);
				//let's add this scrollpane to our jFrame
				jFrame.add(scrollPane);
				jFrame.setLayout(new FlowLayout());
				jFrame.setVisible(true);
				jFrame.setSize(800, 800);
				jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



			} catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, e);
			}
		});
		
		
		
		JButton myBook = new JButton("My Books");
		myBook.setBounds(150, 20, 120, 25);
		myBook.addActionListener(ae->{
			//System.out.println("ram");
			// need to enter another jframe
			//frame.dispose();
			JFrame f = new JFrame("MyBooks");
			Connection connection = connect();
			String sql = "SELECT * FROM books ";
			int Uid_int= Integer.parseInt(uid);
			try {
				Statement stmt= connection.createStatement();
				stmt.executeUpdate("USE library_user_database");
				stmt =connection.createStatement();
				ArrayList<String> book_list= new ArrayList<String>();
				ResultSet rs = stmt.executeQuery(sql);
				//System.out.println(rs.next());
				JTable bookList=new JTable();
				bookList.setModel(DbUtils.resultSetToTableModel(rs));
				JScrollPane scrollPane= new JScrollPane(bookList);
				f.add(scrollPane);
				f.setLayout(new FlowLayout());
				f.setVisible(true);
				f.setSize(800, 800);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				
 				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			f.setLayout(new FlowLayout());
			f.setVisible(true);
			f.setSize(400, 400);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		});
		
		frame.add(viewButton);
		frame.add(myBook);
		
		frame.setVisible(true);
		frame.setSize(400, 400);
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
	}

}





