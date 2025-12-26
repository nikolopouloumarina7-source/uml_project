package bankEnd.standingOrders;

import java.time.LocalDate;
import java.util.ArrayList;

import bankAccounts.BankAccount;
import user.Customer;

public class TransferOrder extends StandingOrders {

    private BankAccount creditAccount;

    public TransferOrder(
            LocalDate startDate,
            LocalDate endDate,
            LocalDate nextExecutionDate,
            String orderId,
            String title,
            String description,
            BankAccount chargeAccount,
            BankAccount creditAccount,
            Customer customer,
            Double fee,
            Double amount,
            int numOfTries,
            int numOfFails,
            boolean active,
            ArrayList<LocalDate> datesThatFailed) {

        super(startDate, endDate, nextExecutionDate, orderId, title,
              description, chargeAccount, customer, fee, amount,
              numOfTries, numOfFails, active, datesThatFailed);

        this.creditAccount = creditAccount;
    }

    
    @Override
    public void execute() {
        try {
            chargeAccount.transferTo(creditAccount, amount);
            markSuccess();
            reschedule();
        } catch (Exception e) {
            markFailure(LocalDate.now());
        }
    }

    @Override
    protected void reschedule() {
        nextExecutionDate = nextExecutionDate.plusMonths(1);
        if (endDate != null && nextExecutionDate.isAfter(endDate)) {
            deactivate();
        }
    }
}
