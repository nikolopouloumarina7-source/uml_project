           
package backEnd.simulateTimePassing;

import java.time.LocalDate;

import bankAccounts.AccountManager;
import bills.BillManager;
import  standingOrders.StandingOrderManager;



//Simulates the passage of time for the banking system
public class SimulateTimePassing {

    public static LocalDate currentDate = LocalDate.now(); 
    private static LocalDate targetDate;
    

    
	public SimulateTimePassing(LocalDate currentDate, LocalDate targetDate) {
		SimulateTimePassing.currentDate = currentDate;
		SimulateTimePassing.targetDate = targetDate;

    }

   

    private static void setCurrentDate(LocalDate newDate) {
    	SimulateTimePassing.currentDate = newDate;
    }
    
   
//Generates the filename for today's bill file	
	public static String getBillTodayFilename() {
	    return currentDate.toString() + ".csv";
	}


//Simulates the processing of a single day:Applies interest, processes end-of-month updates, loads daily bills, executes standing orders
	public static  void simulateOneDay() {
       
    	        AccountManager accountManager = AccountManager.getInstance();
    	        BillManager billManager = BillManager.getInstance();
    	        StandingOrderManager standingOrderManager = StandingOrderManager.getInstance();
    	        
    	        
    	        
            System.out.println("Processing date: " + currentDate);
            
            String billFilename = getBillTodayFilename();
            System.out.println("Today's bill file: " + billFilename);

           
            accountManager.applyDailyInterest(currentDate);

             
           if (currentDate.getDayOfMonth() == currentDate.lengthOfMonth()) {
               accountManager.processAccountUpdates(currentDate);
            }

           
            
                billManager.loadBillsForDate(currentDate);
                standingOrderManager.performStandingOrders();
             

            currentDate = currentDate.plusDays(1);
            
    }

	
//Simulates the passage of time until a specified date
	public static void simulateUntilDate(LocalDate targetDate) {
		
		if (targetDate.isBefore(currentDate)) {
      	    System.out.println("Target date must be after the current system date: " + SimulateTimePassing.currentDate);
      	    return;
      	}
		
		while (!currentDate.isAfter(targetDate)) {
			simulateOneDay();
		}
		setCurrentDate(targetDate);
		
	}
	

	
	
	
}




