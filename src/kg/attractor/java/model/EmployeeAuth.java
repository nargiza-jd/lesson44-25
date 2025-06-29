package kg.attractor.java.model;

public class EmployeeAuth {
    private String email;
    private String fullName;
    private String password;

    public EmployeeAuth() {}
    public EmployeeAuth(String email,String fn,String pw){
        this.email=email; this.fullName=fn; this.password=pw;
    }


    public String getEmail(){return email;}
    public String getFullName(){return fullName;}
    public String getPassword(){return password;}
    public void setEmail(String e){this.email=e;}
    public void setFullName(String n){this.fullName=n;}
    public void setPassword(String p){this.password=p;}
}