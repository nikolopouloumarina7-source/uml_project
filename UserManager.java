package backEnd.user;

import java.io.IOException;
import java.util.ArrayList;

import backEnd.bankAccounts.AccountManager;
import backEnd.bankAccounts.CentralBankManager;
import backEnd.bankAccounts.PersonalAccount;
import backEnd.store.StorableList;
import backEnd.store.StorageManager;
import backEnd.store.UnMarshalingException;


//Handles all users in the system
public class UserManager  {
     
	private StorableList<User> Users;
	private static UserManager instance;
	

	public UserManager(StorageManager  storage) {
		
		this.Users = new StorableList<User>();
		
	}



	private StorableList<User> getUsers() {
		return Users;
	}

	private void setUsers(StorableList<User> users) {
		Users = users;
	}
	
	//------------ ensure singleton------------
	public static UserManager getInstance() {
		StorageManager storage = StorageManager.getInstance();
		if (instance == null) {
			instance = new UserManager(storage);
		
		}
		return instance;
	}
	 

	 

	//Authenticates user based on user name and password. 
	public User authenticate(String username, String password) {
	    for (User user : Users) {

	        if (!user.getUserName().equals(username))
	            continue;

	        if (!user.isActive()) {
	            System.out.println("User is locked.");
	            return null;
	        }

	        if (user.getPassword().equals(password)) {
	            user.resetFailedLogins();
	            return user;
	        } else {
	            user.registerFailedLogin();
	            System.out.println("Invalid password.");
	            return null;
	        }
	    }
	    return null;
	}

		 
	
	
	//Finds user based on legal name
   public User findUserByName(String name) {
			    for (User user : Users) {
			        if (user.getLegalName().equals(name)) {
			            return user;
			        }
			    }
		 return null;
	}
		
  
	 
//Returns a Customer by VAT number	 
	public Customer getCustomerByVat(String vat) {
			    for (User user : Users) {
			    	if(user instanceof Customer ) {
			    		Customer Customer = (Customer) user;
			        if (Customer.getVat().equals(vat)) {
			            return Customer;
			        }
			      }
			    }
		return null;
     }
	
	
		 
		 
//updates user field
	private boolean updateUserField(User user, String newValue, String fieldName) {
				switch (fieldName.toLowerCase()) {
				case "password":
					user.setPassword(newValue);
					return true;
				case "legalname":
					user.setLegalName(newValue);
					return true;
				case "username":
					user.setUserName(newValue);
					return true;
				case "vat":
					if (user instanceof Customer) {
						((Customer) user).setVat(newValue);
						return true;
					}
					return false; 
				default:
					return false;
		}
	}

	
	
//----------saves the users to file-------------
	public void saveUsersToFile() {
		StorageManager sm = StorageManager.getInstance();
			  try {
			      sm.storeObject(Users,"./data/users/users.csv", false);  // Το "this" καλεί το marshal()
			  } catch (IOException e) {
			      System.err.println("Error saving users to file: " + e.getMessage());
			  }
	}

		 
		 
		 
//---------loads the users from file------------
    public void loadUsersFromFile() {
		StorageManager sm = StorageManager.getInstance();
	 try {  
	   try {
			  sm.loadObject(Users,"./data/users/users.csv");  // διαβάζει το αρχείο ως String  
			   } catch (IOException e) {
			       System.err.println("Error loading users from file: " + e.getMessage());
			   } }
			 catch(UnMarshalingException e) {
			    System.err.println("Error loading users from file: " + e.getMessage());	
		}
	}
 
		 
		 
//prints the legalName of all customers 
 	 public void printCustomers(UserManager userManager) {
			for (User user : Users) {
				if (user instanceof Customer) 
					System.out.println(user.getLegalName());
				
			}
 	 }
 	 
//prints the details of a customer based VAT number
public void printCustomersDetails(String vat, UserManager userManager) {
    Customer customer = getCustomerByVat(vat); // Use the method to find the customer by VAT
    if (customer != null) {
        // Print the details of the found customer
        System.out.println("Name: " + customer.getLegalName() + ", " +
                           "UserName: " + customer.getUserName() + ", " +
                           "VAT: " + customer.getVat());
    } else {
        // Handle the case where no customer is found
        System.out.println("No customer found with VAT: " + vat);
    }
}


//returns the co-owners of a personal account
private ArrayList<Customer> getCoOwners(PersonalAccount account) {
    ArrayList<Customer> coOwners = new ArrayList<>();
    for (Customer coOwner : account.getCoOwner()) {
        if (coOwner != null) {
            coOwners.add(coOwner);
        }
    }
    return coOwners;
}


//returns the user by legal name
public User getUserByLegalName(String legalName) {
	CentralBankManager centralBankManager = CentralBankManager.getInstance();
	if (centralBankManager.getCompany().getLegalName().equalsIgnoreCase(legalName)) 
        return centralBankManager.getCompany();
	
	
    for (User user : Users) {
        if (user.getLegalName().equalsIgnoreCase(legalName)) {
            return user;
        }
    }
    return null;
}



//prints the details of a customer based on VAT number
public void printCustomersDetails(String vat) {
	AccountManager accountManager = AccountManager.getInstance();
    Customer customer = getCustomerByVat(vat); 
    if (customer != null) {
     
        System.out.println("Name: " + customer.getLegalName() + ", " +
                           "UserName: " + customer.getUserName() + ", " +
                           "VAT: " + customer.getVat()+", "+
                           "iban: " + accountManager.getBankAccountByCompany((Company) customer).getIBAN()+ ", " +
                           "balance: " + accountManager.getBankAccountByCompany((Company) customer).getBalance());
    } else {
       
        System.out.println("No customer found with VAT: " + vat);
    }
}

public void deactivateUser(User admin, User targetUser) {
    if (!(admin instanceof Admin)) {
        throw new SecurityException("Only Admin can deactivate users");
    }
    targetUser.setStatus(UserStatus.LOCKED);
}

public void activateUser(User admin, User targetUser) {
    if (!(admin instanceof Admin)) {
        throw new SecurityException("Only Admin can activate users");
    }
    targetUser.resetFailedLogins();
    targetUser.setStatus(UserStatus.ACTIVE);
}





}