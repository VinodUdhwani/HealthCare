package Hospital.Management.System.Health.Care.service;

import Hospital.Management.System.Health.Care.bean.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(EmailDetails emailDetails){
        try{
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setFrom("mr.vinod615@gmail.coom");
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setSubject(emailDetails.getSubject());
            simpleMailMessage.setText(emailDetails.getBody());
            javaMailSender.send(simpleMailMessage);
        }
        catch (MailException e){
            e.printStackTrace();
        }
    }
}
