package bankEnd.standingOrders;

public class StandingOrderUI {

	 private StandingOrderManager manager =
	            StandingOrderManager.getInstance();

	    public void showCreateSuccess() {
	        System.out.println("Η πάγια εντολή δημιουργήθηκε επιτυχώς.");
	    }

	    public void showError(String msg) {
	        System.out.println("Σφάλμα: " + msg);
	    }

	    public void addStandingOrder(StandingOrders order) {
	        manager.addOrder(order);
	        showCreateSuccess();
	    }
	    
	
}
