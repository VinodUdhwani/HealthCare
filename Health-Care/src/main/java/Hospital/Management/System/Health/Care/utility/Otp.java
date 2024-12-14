package Hospital.Management.System.Health.Care.utility;

import java.util.Random;

public class Otp {

    public static String generateOtp(){
        Random random=new Random();
        StringBuffer stringBuffer=new StringBuffer();
        for (int i=1;i<=4;i++)
            stringBuffer.append(random.nextInt(0,9));
        return stringBuffer.toString();
    }
}
