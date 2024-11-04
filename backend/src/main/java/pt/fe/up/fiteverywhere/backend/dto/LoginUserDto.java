package pt.fe.up.fiteverywhere.backend.dto;

public class LoginUserDto {
    private String email;
    private String password;

    // Manually implemented getters and setters

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
}
