package backEnd.bankAccounts;

import backEnd.user.Customer;
import backEnd.user.Individual;
import backEnd.user.Company;

import java.time.LocalDate;
import java.util.ArrayList;

public class AccountFactory {

	public static PersonalAccount createPersonalAccount(
	        Individual owner, float rate) {

	    PersonalAccount account = new PersonalAccount(
	        "",
	        rate,
	        0.0,
	        owner,
	        LocalDate.now().toString(),
	        new ArrayList<>()
	    );

	    account.setStatus(AccountStatus.PENDING);
	    return account;
	}


    public static BankAccounts createBusinessAccount(
            Company owner, float rate, float fee) {

        return new BusinessAccount(
                "",
                rate,
                0.0,
                owner,
                LocalDate.now().toString(),
                fee
        );
    }
}
