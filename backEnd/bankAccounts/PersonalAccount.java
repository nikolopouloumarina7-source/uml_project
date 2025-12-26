package backEnd.bankAccounts;

import java.util.ArrayList;
import java.util.List;

import backEnd.store.UnMarshalingException;
import backEnd.user.Customer;
import backEnd.user.Individual;
import backEnd.user.UserManager;

public class PersonalAccount extends BankAccounts {

    private List<Individual> coOwner = new ArrayList<>();

    public PersonalAccount(String iban, float rate, double balance,
                           Customer primaryOwner, String dateCreated,
                           List<Individual> coOwner) {
        super(iban, rate, balance, primaryOwner, dateCreated);
        this.coOwner = coOwner;
    }

    public PersonalAccount() {
        super("", 0f, 0, null, "");
    }

    public List<Individual> getCoOwner() {
        return coOwner;
    }

    @Override
    public String getAccountType() {
        return "Personal";
    }

    @Override
    public String marshal() {
        StringBuilder sb = new StringBuilder(super.marshal());
        for (Customer c : coOwner) {
            sb.append(",coOwner:").append(c.getVat());
        }
        return sb.toString();
    }

    @Override
    public void unmarshal(String data) throws UnMarshalingException {
        UserManager um = UserManager.getInstance();
        coOwner.clear();

        for (String part : data.split(",")) {
            String[] kv = part.split(":");
            if (kv.length < 2) continue;

            switch (kv[0]) {
                case "iban": setIBAN(kv[1]); break;
                case "primaryOwner": setPrimaryOwner(um.getCustomerByVat(kv[1])); break;
                case "dateCreated": setDateCreated(kv[1]); break;
                case "rate": setRate(Float.parseFloat(kv[1])); break;
                case "balance": setBalance(Double.parseDouble(kv[1])); break;
                case "coOwner":
                    Individual ind = (Individual) um.getCustomerByVat(kv[1]);
                    if (ind != null) coOwner.add(ind);
                    break;
                case "status": setStatus(AccountStatus.valueOf(kv[1])); break;
            }
        }
    }
}
