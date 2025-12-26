package backEnd.user;

import backEnd.store.Storable;

public abstract class User implements Storable {

    private String userName;
    private String password;
    private String legalName;

    private UserStatus status;
    private int failedLoginAttempts;

    public User(String username, String password, String legalName) {
        this.userName = username;
        this.password = password;
        this.legalName = legalName;
        this.status = UserStatus.ACTIVE;
        this.failedLoginAttempts = 0;
    }

    // ---------------- BASIC GETTERS ----------------
    public String getLegalName() { return legalName; }
    protected String getUserName() { return userName; }
    protected String getPassword() { return password; }
    public UserStatus getStatus() { return status; }

    // ---------------- SETTERS ----------------
    protected void setLegalName(String legalName) { this.legalName = legalName; }
    protected void setUserName(String username) { this.userName = username; }
    protected void setPassword(String password) { this.password = password; }

    // ---------------- LOGIN LOGIC ----------------
    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }
    
    protected void setStatus(UserStatus status) {
        this.status = status;
    }


    public void registerFailedLogin() {
        failedLoginAttempts++;
        if (failedLoginAttempts >= 3) {
            status = UserStatus.LOCKED;
        }
    }

    public void resetFailedLogins() {
        failedLoginAttempts = 0;
    }

    public abstract String getUserType();

    // ---------------- STORAGE ----------------
    @Override
    public String marshal() {
        return "type:" + getClass().getSimpleName() + "," +
               "legalName:" + legalName + "," +
               "userName:" + userName + "," +
               "password:" + password + "," +
               "status:" + status + "," +
               "failed:" + failedLoginAttempts;
    }
}
