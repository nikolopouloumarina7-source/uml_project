
package backEnd.bankAccounts;

import backEnd.user.Company;


//Singleton class that represents the central bank of the system
public class CentralBankManager {
    private static CentralBankManager instance;
    private BusinessAccount bankAccount;
    private Company company;

    private CentralBankManager() {
        this.company = new Company("BANK_USERNAME", "BANK_PASSWORD", "BANK_VAT", "BANK_OF_TUC");
        this.bankAccount = new BusinessAccount("BANK_IBAN", 0.0f, 1000000.0, this.company, "2025-01-01", 0 );
    }

    
  //------------ ensure singleton------------
    public static CentralBankManager getInstance() {
        if (instance == null) {
            instance = new CentralBankManager();
        }
        return instance;
    }

    
    public BusinessAccount getBankAccount() {
        return bankAccount;
    }
    
    public Company getCompany() {
		return company;
	}
    
}
