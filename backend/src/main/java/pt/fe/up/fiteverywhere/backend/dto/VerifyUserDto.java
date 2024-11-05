package pt.fe.up.fiteverywhere.backend.dto;

public class VerifyUserDto {
    private String email;
    private String verificationCode;

    // Manually implemented getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
