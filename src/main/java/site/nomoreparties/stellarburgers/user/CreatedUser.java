package site.nomoreparties.stellarburgers.user;

public class CreatedUser {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public CreatedUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public CreatedUser() {
    }
}
