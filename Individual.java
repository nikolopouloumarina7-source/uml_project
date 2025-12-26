package backEnd.user;

import backEnd.store.UnMarshalingException;


//Represents an individual (personal) customer in the system.
public class Individual extends Customer{
	
	public Individual(String username, String password, String vat,String legalName) {
	        super(username, password, vat,legalName);
	    }
	 
	//default constructor 
	public Individual() {
	        super("", "", "", "");
	    }
	 
	 //returns the user type 
	 @Override
	    public String getUserType() {
	        return "Individual";
	    }	 
	 
}


