
import java.awt.image.BufferedImage;

public class User {
    private String username;
    private String password;
    private String phoneNum;
    private String email;
    private Enum<Status> status;
    private byte[] profilePhoto;
    private String profilePhotoFormat;


    public User (String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public User() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username)   {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password)  {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Enum<Status> getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto, String format) {
        this.profilePhoto = profilePhoto;
        this.profilePhotoFormat = format;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", status=" + status +
                '}';
    }
}
