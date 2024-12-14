package Hospital.Management.System.Health.Care.bean;

public class Patient {
    private int patientId;
    private String name;
    private int age;
    private String Gender;
    private String username;
    private String  email;
    private String password;
    private String newPassword;
    private String otp;

    public Patient(int patientId,String name, int age, String gender, String username, String email, String password,String newPassword,String otp){
        this.patientId=patientId;
        this.name = name;
        this.age = age;
        Gender = gender;
        this.username = username;
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
        this.otp=otp;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

