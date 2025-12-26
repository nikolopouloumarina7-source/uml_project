package bankEnd.standingOrders;


public class StandingOrderFactory {

	 public static TransferOrder createTransferOrder(
	            String orderId,
	            String title,
	            String description,
	            Customer customer,
	            BankAccount from,
	            BankAccount to,
	            double amount,
	            double fee,
	            LocalDate start,
	            LocalDate end,
	            LocalDate next) {

	        return new TransferOrder(orderId, title, description,
	                customer, from, to, amount, fee,
	                start, end, next);
	    }

	    public static PaymentOrder createPaymentOrder(
	            String orderId,
	            String title,
	            String description,
	            Customer customer,
	            BankAccount from,
	            double amount,
	            double fee,
	            String rfCode,
	            LocalDate start,
	            LocalDate end,
	            LocalDate next) {

	        return new PaymentOrder(orderId, title, description,
	                customer, from, amount, fee, rfCode,
	                start, end, next);
	    }
	
}
