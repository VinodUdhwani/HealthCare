package Hospital.Management.System.Health.Care.Controller;

import Hospital.Management.System.Health.Care.bean.EmailDetails;
import Hospital.Management.System.Health.Care.bean.Patient;
import Hospital.Management.System.Health.Care.service.EmailService;
import Hospital.Management.System.Health.Care.utility.Otp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;


@RestController
public class HealthController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("signup")
    public String signUp(@RequestBody Patient patient){
        try(Connection connection=jdbcTemplate.getDataSource().getConnection()){
            String query="select patientId from patient where username=? or email=?";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1, patient.getUsername());
            preparedStatement.setString(2, patient.getEmail());
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                return "username or email already exist";
            }
            else{
                String otp= Otp.generateOtp();
                emailService.sendMail(new EmailDetails(patient.getEmail(),"Otp Generated","your verification otp is "+otp));
                String sql="insert into patient(username,email,password,otp,is_verified)values(?,?,?,?,?)";
                PreparedStatement preparedStatement1= connection.prepareStatement(sql);
                preparedStatement1.setString(1, patient.getUsername());
                preparedStatement1.setString(2, patient.getEmail());
                preparedStatement1.setString(3, patient.getPassword());
                preparedStatement1.setString(4,otp);
                preparedStatement1.setInt(5,0);
                preparedStatement1.executeUpdate();
                return "your details has been submitted";
            }
        }
        catch (SQLException e){
            return "Something went wrong.try again";
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("verify")
    public String verification(@RequestBody Patient patient){
        try(Connection connection=jdbcTemplate.getDataSource().getConnection()){
            String query="select otp from patient where email=?";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1, patient.getEmail());
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getString(1).equals(patient.getOtp())){
                    String sql="update patient set is_verified=1 where email=?";
                    PreparedStatement preparedStatement1= connection.prepareStatement(sql);
                    preparedStatement1.setString(1,patient.getEmail());
                    preparedStatement1.executeUpdate();
                    return "your account has been verified successfully";
                }
                else
                    return "Invalid otp";
            }
            else{
                return "email does not exist.";
            }
        }
        catch (SQLException e){
            return "Something went wrong.try again";
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("login")
    public String login(@RequestBody Patient patient){
        try(Connection connection=jdbcTemplate.getDataSource().getConnection()){
            String query="select * from patient where username=? and password=? and is_verified=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,patient.getUsername());
            preparedStatement.setString(2,patient.getPassword());
            preparedStatement.setInt(3,1);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                return "Login Successfully";
            }
            else
                return "either invalid username or password or email not verified";
        }
        catch (SQLException e){
            return "Something went wrong.try again";
        }
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("forgetpassword")
    public String forgetPassword(@RequestParam String username) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            String query = "select email,password from patient where username=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                EmailDetails emailDetails = new EmailDetails(resultSet.getString(1), "Password", "your login password is " + resultSet.getString(2)+ ". you can change your password for security purpose.");
                emailService.sendMail(emailDetails);
                return "your password has been sent over your email.";
            }
            else {
                return "username does not exist";
            }
        }
        catch(Exception e){
            return "Something went wrong.try again.";
        }
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("changepassword")
    public String changePassword(@RequestBody Patient patient){
        try(Connection connection=jdbcTemplate.getDataSource().getConnection()){
            String query="select * from patient where email=? and password=? and is_verified=1";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,patient.getEmail());
            preparedStatement.setString(2,patient.getPassword());
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                String sql="update patient set password=? where email=?";
                PreparedStatement preparedStatement1= connection.prepareStatement(sql);
                preparedStatement1.setString(1,patient.getNewPassword());
                preparedStatement1.setString(2, patient.getEmail());
                preparedStatement1.executeUpdate();
                return "your password has been changed successfully";
            }
            else{
                return "Invalid email or password or email not verified";
            }
        }
        catch (Exception e){
            return "Something went wrong.try again";
        }
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("profileupdate")
    public String profile(@RequestParam String name,int age,String gender,int patientId){
        try(Connection connection=jdbcTemplate.getDataSource().getConnection()){
            String query = "update patient set name=?,age=?,gender=? where patientId=?";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3, gender);
            preparedStatement.setInt(4,patientId);
            preparedStatement.executeUpdate();
            return "Patient added successfully";
        }
        catch (SQLException e){
            return "Something went wrong.try again";
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("viewdoctors")
    public ArrayList viewDoctors(){
        try(Connection connection=jdbcTemplate.getDataSource().getConnection()){
            String query="select * from doctors";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            ResultSet resultSet= preparedStatement.executeQuery();
            ArrayList arrayList=new ArrayList<>();
            ArrayList arrayList1=new ArrayList<>();
            while(resultSet.next()){
                arrayList1.add(resultSet.getString(2));
                arrayList1.add(resultSet.getString(3));
            }
            arrayList.add(arrayList1);
            return arrayList;
        }
        catch (SQLException e){
            return null;
        }
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("viewpatient")
    public ArrayList viewPatient(@RequestParam int patientId){
        try(Connection connection=jdbcTemplate.getDataSource().getConnection()){
                String query = "select name,age,gender,email from patient where patientId=?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,patientId);
                ResultSet resultSet = preparedStatement.executeQuery();
                ArrayList arrayList=new ArrayList<>();
                ArrayList arrayList1=new ArrayList<>();
                while (resultSet.next()){
                    arrayList1.add(resultSet.getString(1));
                    arrayList1.add(resultSet.getInt(2));
                    arrayList1.add(resultSet.getString(3));
                    arrayList1.add(resultSet.getString(4));
                }
                arrayList.add(arrayList1);
                return arrayList;
        }
        catch (SQLException e){
            return null;
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("checkappointment")
    public String checkAppointment(@RequestParam String specialization, Date date){
        try(Connection connection=jdbcTemplate.getDataSource().getConnection()){
            String query="select status from doctors where specialization=? and date=?";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,specialization);
            preparedStatement.setDate(2,date);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getString(1).equals("available"))
                return "Doctor is available on this date.you can schedule your appointment accordingly.";
                else
                    return "No doctors are available on this date.kindly check with another date";
            }
            else{
                return "No result found.";
            }
        }
        catch (SQLException e){
            return "Something went wrong.try again";
        }
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("bookappointment")
    public String  bookAppointment(@RequestParam int patientId, String specialization, Date date, Time timeSlot){
        try(Connection connection=jdbcTemplate.getDataSource().getConnection()){
            String query="select name,email from patient where patientId=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,patientId);
            ResultSet resultSet= preparedStatement.executeQuery();
            boolean patient=resultSet.next();
            String query1="select status,doctorId from doctors where specialization=? and date=?";
            PreparedStatement preparedStatement1=connection.prepareStatement(query1);
            preparedStatement1.setString(1,specialization);
            preparedStatement1.setDate(2,date);
            ResultSet resultSet1= preparedStatement1.executeQuery();
            boolean doctor=resultSet1.next();
            if(patient && doctor && resultSet1.getString(1).equals("available")){
                String query2="select appointmentId from appointment where specialization=? and date=? and time_slot=?";
                PreparedStatement preparedStatement2= connection.prepareStatement(query2);
                preparedStatement2.setString(1,specialization);
                preparedStatement2.setDate(2,date);
                preparedStatement2.setTime(3,timeSlot);
                ResultSet resultSet2=preparedStatement2.executeQuery();
                if(resultSet2.next())
                    return "time slot is already booked.kindly check with another time slot";
                else {
                    String sql = "insert into appointment(patientId,doctorId,specialization,Date,time_slot) values(?,?,?,?,?)";
                    PreparedStatement preparedStatement3 =connection.prepareStatement(sql);
                    preparedStatement3.setInt(1, patientId);
                    preparedStatement3.setInt(2, resultSet1.getInt(2));
                    preparedStatement3.setString(3, specialization);
                    preparedStatement3.setDate(4, date);
                    preparedStatement3.setTime(5,timeSlot);
                    preparedStatement3.executeUpdate();
                    emailService.sendMail(new EmailDetails(resultSet.getString(2), "Appointment Scheduled", "your appointment for " + specialization + " on " + date +" "+ timeSlot+ " has scheduled."));
                    return "your appointment has confirmed";
                }
            }
            else{
                if(patient==false)
                return "Kindly update profile details after that you are able to book your appointment";
                else
                    return "No doctors are available on this date.kindly check with another date.";
            }
        }
        catch (SQLException e){
            return "Something went wrong.";
        }
    }
}
