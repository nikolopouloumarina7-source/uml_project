package backEnd.user;



public class Company extends Customer{

	public Company(String username, String password, String vat,String legalName) {
	        super(username, password, vat,legalName);
	    }
	 
	 //default constructor
	public Company() {
	        super("", "", "", "");
	    }
	 @Override
	    public String getUserType() {
	        return "Company";
	    }

	
	 
	
	
	
	
}
