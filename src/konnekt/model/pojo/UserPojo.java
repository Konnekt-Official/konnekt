package konnekt.model.pojo;

public class UserPojo {
    private int id;
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String profileUrl;
    private String bannerUrl;

    public UserPojo() {}

    public UserPojo(int id, String fullName, String username, String email, String password, String profileUrl, String bannerUrl) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileUrl = profileUrl;
        this.bannerUrl = bannerUrl;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfileUrl() { return profileUrl; }
    public void setProfileUrl(String profileUrl) { this.profileUrl = profileUrl; }

    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }
}