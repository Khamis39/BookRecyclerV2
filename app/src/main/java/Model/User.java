package Model;

public class User {
    String Name;
    String PhoneNumber;
    String EmailAddress;
    String Password;

    public User() {
    }

    public User(String Name,String phoneNumber, String emailAddress, String password) {
        PhoneNumber = phoneNumber;
        EmailAddress = emailAddress;
        Password = password;
        this.Name = Name;
    }

    public User(String name, String phoneNumber, String emailAddress) {
        Name = name;
        PhoneNumber = phoneNumber;
        EmailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
