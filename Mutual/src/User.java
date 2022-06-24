
import java.awt.image.BufferedImage;

public class User {
    private String username;
    private String password;
    private String phoneNum;
    private String email;
    private Enum<Status> status;
    private BufferedImage profilePhoto;


    public User(String username, String password, String email) throws IllegalArgumentException {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws IllegalArgumentException  {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws IllegalArgumentException  {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) throws IllegalArgumentException  {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws IllegalArgumentException  {
        this.email = email;
    }

    public Enum<Status> getStatus() {
        return status;
    }

    public void setStatus(String status) throws IllegalArgumentException {
        this.status = Status.valueOf(status);
    }

    public BufferedImage getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(BufferedImage profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
