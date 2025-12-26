package backEnd.user;

public class Admin extends User {

	public Admin(String userName, String password,String legalName) {
	        super(userName, password,legalName);
	    }
	 
	 
	 //default constructor
	 public Admin() {
	        super("", "", "");
	    }
	 
	 
	 @Override
	    public String getUserType() {
	        return "Admin";
	    }

	 //loads admin data from string
	 @Override
	 public void unmarshal(String data) {
	     String[] parts = data.split(",");
	     for (String part : parts) {
	                 String[] keyValue = part.split(":");
	                 if (keyValue.length < 2) continue;
	                 if (keyValue[0].equals("legalName")) 
	                     setLegalName(keyValue[1]);
	                 else if (keyValue[0].equals("userName"))
	                	 setUserName(keyValue[1]);
	                 else if (keyValue[0].equals("password"))
	                	 setPassword(keyValue[1]);
	             }
	 
	          }
	 
	//Converts admin data to a string format for storage.
	 @Override
		public String marshal() {
			StringBuffer sb = new StringBuffer(super.marshal());
			return sb.toString();
}
}