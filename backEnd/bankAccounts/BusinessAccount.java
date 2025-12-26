package backEnd.bankAccounts;

import backEnd.store.UnMarshalingException;
import backEnd.user.Customer;
import backEnd.user.UserManager;

public class BusinessAccount extends BankAccounts {

    private float maintenanceFee;

    public BusinessAccount(String iBAN, float rate, double balance,
                           Customer primaryOwner, String dateCreated, float fee) {
        super(iBAN, rate, balance, primaryOwner, dateCreated);
        this.maintenanceFee = fee;
    }

    public BusinessAccount() {
        super("", 0f, 0, null, "");
        this.maintenanceFee = 0;
    }

    protected float getMaintenanceFee() {
        return maintenanceFee;
    }

    @Override
    public String getAccountType() {
        return "Business";
    }

    @Override
    public String marshal() {
        return super.marshal() + ",fee:" + maintenanceFee;
    }

    @Override
    public void unmarshal(String data) throws UnMarshalingException {
        UserManager um = UserManager.getInstance();

        for (String part : data.split(",")) {
            String[] kv = part.split(":");
            if (kv.length < 2) continue;

            switch (kv[0]) {
                case "iban": setIBAN(kv[1]); break;
                case "primaryOwner": setPrimaryOwner(um.getCustomerByVat(kv[1])); break;
                case "dateCreated": setDateCreated(kv[1]); break;
                case "rate": setRate(Float.parseFloat(kv[1])); break;
                case "balance": setBalance(Double.parseDouble(kv[1])); break;
                case "fee": maintenanceFee = Float.parseFloat(kv[1]); break;
                case "status": setStatus(AccountStatus.valueOf(kv[1])); break;
            }
        }
    }
}
