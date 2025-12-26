package backEnd.bankAccounts;



import backEnd.store.StorableList;
import backEnd.store.StorageManager;
import backEnd.store.UnMarshalingException;
import backEnd.transactions.TransactionManager;
import backEnd.user.Company;
import backEnd.user.Customer;
import backEnd.user.User;
import backEnd.user.Admin;
import backEnd.user.Individual;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


public class AccountManager  {
	private StorableList<BankAccounts> BankAccounts;
	private BusinessAccount centralBankAccount;
	private static AccountManager instance;
	
	
	private AccountManager(StorageManager storage) {
		this.BankAccounts = new StorableList<BankAccounts>();
		this.centralBankAccount = CentralBankManager.getInstance().getBankAccount();
	}


	 private StorableList<BankAccounts> getBankAccounts() {
		return BankAccounts;
	}



	private void setBankAccounts(StorableList<BankAccounts> bankAccounts) {
		BankAccounts = bankAccounts;
	}

//------------ ensure singleton------------
	public static AccountManager getInstance() {
		StorageManager storage = StorageManager.getInstance();
	    if (instance == null) {
	      instance = new AccountManager(storage);
	    }
	    return instance;
	  }

	
//------------save accounts to file------------
	public void saveAccountsToFile() {
		 StorageManager sm = StorageManager.getInstance();
		    try {
		        sm.storeObject(BankAccounts,"./data/accounts/accounts.csv", false);  // Το "this" καλεί το marshal()
		    } catch (IOException e) {
		        System.err.println("Error saving accounts to file: " + e.getMessage());
		    }
		}

	
//------------load accounts from file------------ 
	 public void loadAccountsFromFile() {
		 StorageManager sm = StorageManager.getInstance();
		 
		 try {
		        sm.loadObject(BankAccounts,"./data/accounts/accounts.csv");  // διαβάζει το αρχείο ως String  
		    } catch (IOException e) {
		        System.err.println("Error loading accounts from file: " + e.getMessage());
		    } 
		 catch(UnMarshalingException e) {
		    	 System.err.println("Error loading accounts from file: " + e.getMessage());	
		    }
	 }


	 
public BankAccounts findAccountByIBAN(String iban) {
			for (BankAccounts account : BankAccounts) {
				if (account.getIBAN().equals(iban)) {
					return account;
				}
			}
			return null;
		}
	 
	 

public void processAccountUpdates(LocalDate currentDate) {

		TransactionManager transactionManager = TransactionManager.getInstance();
		
		  if (transactionManager == null) {
		        System.err.println("TransactionManager is null. Cannot process account updates.");
		        return;
		    }
		  
	            payInterest(currentDate);
	            processMaintenanceFee(currentDate);
	        
	    }
	

	
public void payInterest(LocalDate currentDate) {
    TransactionManager transactionManager = TransactionManager.getInstance();
    CentralBankManager CM = CentralBankManager.getInstance();

    for (int i = 0; i < BankAccounts.size(); i++) {

        // ✅ ΝΕΟ CHECK (ΑΠΑΙΤΗΣΗ SRS)
        if (BankAccounts.get(i).getStatus() != AccountStatus.ACTIVE) {
            continue;
        }

        if (BankAccounts.get(i).getInterest() <= 0) {
            continue;
        }

        transactionManager.performTransaction(
            centralBankAccount,
            BankAccounts.get(i),
            BankAccounts.get(i).getInterest(),
            CM.getCompany(),
            "Transfer",
            "Interest Credit",
            null
        );

        BankAccounts.get(i).setInterest(0);
    }
}



private void processMaintenanceFee(LocalDate currentDate) {
    TransactionManager transactionManager = TransactionManager.getInstance();
    CentralBankManager CM = CentralBankManager.getInstance();

    for (BankAccounts account : BankAccounts) {
        if (account instanceof BusinessAccount) {
            BusinessAccount businessAccount = (BusinessAccount) account;

            // ✅ ΝΕΟ CHECK (ΑΠΑΙΤΗΣΗ SRS – FR-13)
            if (businessAccount.getStatus() != AccountStatus.ACTIVE) {
                continue;
            }

            if (businessAccount.getMaintenanceFee() <= 0) {
                continue;
            }

            transactionManager.performTransaction(
                businessAccount,
                centralBankAccount,
                businessAccount.getMaintenanceFee(),
                CM.getCompany(),
                "Transfer",
                "Maintenance Fee",
                null
            );
        }
    }
}



  
private String generateIBAN(String accountType) {
      String countryCode = "GR"; 
      String uniqueAccountCode;

      if(accountType.equals("BusinessAccount")){
      	        accountType = "100"; 
      } else if(accountType.equals("PersonalAccount")) {
          accountType = "200";
      } else {
          throw new IllegalArgumentException("Invalid account type: " + accountType);
      }
      
      
     
      do {
          uniqueAccountCode = String.format("%015d", (long) (Math.random() * Math.pow(10, 15)));
      } while (findAccountByIBAN(countryCode + accountType + uniqueAccountCode) != null);

      return countryCode + accountType + uniqueAccountCode;

  }
  

	
	
public void showBankAccounts(AccountManager accountManager) {
		for (BankAccounts account : BankAccounts) {
			System.out.println("IBAN:" +account.getIBAN());
		}
	}
	


public BankAccounts getBankAccountByIban(String iban) {
    for (BankAccounts account : BankAccounts) {
        if (account.getIBAN().equals(iban)) {
            return account; 
        }
    }
    return null; 
}


public BusinessAccount getBankAccountByCompany(Company company) {
    for (BankAccounts account : BankAccounts) {
        if (account instanceof BusinessAccount) {
            BusinessAccount businessAccount = (BusinessAccount) account;
            if (businessAccount.getPrimaryOwner().equals(company)) {
                return businessAccount; // Return the matching account
            }
        }
    }
    return null; 
}

	


public void applyDailyInterest(LocalDate currentDate) {

    for (int i = 0; i < BankAccounts.size(); i++) {
        BankAccounts acc = BankAccounts.get(i);

        if (acc.getStatus() != AccountStatus.ACTIVE) {
            continue;
        }

        double dailyInterest =
            (acc.getBalance() * acc.getRate()) / currentDate.lengthOfYear();

        acc.setInterest(acc.getInterest() + dailyInterest);
    }
}

	


	

public boolean checkBankAccountOwnerByIBAN(String iban, User loggedInUser) { 
    BankAccounts account = findAccountByIBAN(iban);

    if (account != null) {
       
        if (account.getPrimaryOwner() != null && account.getPrimaryOwner().equals(loggedInUser)) {
            return true;
        }

       
        if (account instanceof PersonalAccount) {
            ArrayList<Customer> coOwners = new ArrayList<>();
            for (Customer coOwner : ((PersonalAccount) account).getCoOwner()) {
                if (coOwner != null) {
                    coOwners.add(coOwner);
                }
            }
            if (coOwners.contains(loggedInUser)) {
                return true;
            }
        }
    }
    

   
    return false;
}
	

public void printUserAccounts(User loggedInUser) {
    for (BankAccounts account : BankAccounts) {
      
        if (account.getPrimaryOwner() != null && account.getPrimaryOwner().equals(loggedInUser)) {
            System.out.println("IBAN: " + account.getIBAN() + ", Balance: " + account.getBalance());
        }

        if (account instanceof PersonalAccount) {
            for (Customer coOwner : ((PersonalAccount) account).getCoOwner()) {
                if (coOwner != null && coOwner.equals(loggedInUser)) {
                    System.out.println("IBAN: " + account.getIBAN() + ", Balance: " + account.getBalance());
                    break; 
                }
            }
        }
    }
}





public void showBankAccounts() {
	for (BankAccounts account : BankAccounts) {
		System.out.println("IBAN:" +account.getIBAN());
	}
}
////////////
public void approveAccount(BankAccounts account) {
    account.setStatus(AccountStatus.ACTIVE);
}

public void deactivateAccount(BankAccounts account) {
    account.setStatus(AccountStatus.INACTIVE);
}

public void requestAccount(BankAccounts account, User requester) {
    if (!(requester instanceof Customer)) {
        throw new SecurityException("Only customers can request accounts");
    }
    account.setStatus(AccountStatus.PENDING);
    BankAccounts.add(account);
}

public void changeAccountStatus(User admin, BankAccounts account, AccountStatus newStatus) {
    if (!(admin instanceof Admin)) {
        throw new SecurityException("Only Admin can change account status");
    }
    account.setStatus(newStatus);
}


////////////

public void showBankAccountInfo(String IBAN) {
    BankAccounts account = findAccountByIBAN(IBAN);

    if (account == null) {
        System.out.println("No account found with IBAN: " + IBAN);
        return;
    }

    System.out.println(
        "IBAN: " + account.getIBAN() +
        ", Owner: " + account.getPrimaryOwner().getLegalName() +
        ", Balance: " + account.getBalance() +
        ", Rate: " + account.getRate() +
        ", Status: " + account.getStatus() +
        ", Created: " + account.getDateCreated()
    );

    if (account instanceof BusinessAccount) {
        System.out.println(
            "Maintenance Fee: " +
            ((BusinessAccount) account).getMaintenanceFee()
        );
    }

    if (account instanceof PersonalAccount) {
        if (!((PersonalAccount) account).getCoOwner().isEmpty()) {
            System.out.print("Co-Owners: ");
            for (Customer coOwner : ((PersonalAccount) account).getCoOwner()) {
                System.out.print(coOwner.getLegalName() + " ");
            }
            System.out.println();
        }
    }
}

public void requestPersonalAccount(Individual requester, float rate) {

    BankAccounts account =
        AccountFactory.createPersonalAccount(requester, rate);

    BankAccounts.add(account);
}




}
	
	
	
	


	
	
	

