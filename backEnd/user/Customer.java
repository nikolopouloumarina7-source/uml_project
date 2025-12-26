package backEnd.user;

import backEnd.store.UnMarshalingException;


// Abstract class for all customers (companies and individuals)
public abstract class Customer extends User {
	protected String vat;  
	  
	  protected Customer(String username, String password, String vat ,String legalName) {
			super(username, password, legalName);
			this.vat = vat;
		}

	  
		

	  public  String getVat() {
		return vat;
	}

	protected void setVat(String vat) {
		this.vat = vat;
	} 
	  
	
	
	//Converts customer data to a string format for storage
	@Override
	public String marshal() {
		StringBuffer sb = new StringBuffer(super.marshal());
		sb.append("vatNumber:").append(vat).append(",");
		return sb.toString();


}
	
	
	//loads customer data from string
	@Override
	public void unmarshal(String data) throws UnMarshalingException {
String[] parts = data.split(",");
for (String part : parts) {
	String[] keyValue = part.split(":");
	if (keyValue.length < 2)
		continue;
	if (keyValue[0].equals("legalName"))
		setLegalName(keyValue[1]);
	else if (keyValue[0].equals("userName"))
		setUserName(keyValue[1]);
	else if (keyValue[0].equals("password"))
		setPassword(keyValue[1]);
	else if (keyValue[0].equals("vatNumber"))
		setVat(keyValue[1]);
      }

}


	 
	 
}