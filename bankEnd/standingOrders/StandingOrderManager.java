package bankEnd.standingOrders;

import java.util.List;
import java.util.ArrayList;


public class StandingOrderManager {
	private List<StandingOrders> orders = new ArrayList<>();
	
	private static StandingOrderManager instance;

    public static StandingOrderManager getInstance() {
        if (instance == null) {
            instance = new StandingOrderManager();
        }
        return instance;
    }

    public void execute(StandingOrders order) {
        order.execute();
    }
    
    
    public void createTransferOrder() {
        StandingOrders order =
            StandingOrderFactory.createTransferOrder(...);

        addOrder(order);
    }
    
    public void addOrder(StandingOrders order) {
        orders.add(order);
    }

    public void executeAll() {
        for (StandingOrders order : orders) {
            order.execute();
        }
    }
}
