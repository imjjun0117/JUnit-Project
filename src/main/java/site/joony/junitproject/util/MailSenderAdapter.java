package site.joony.junitproject.util;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

//추후 Mail 클래스가 완성되면 코드를 완성하면 된다.
@Component
public class MailSenderAdapter implements MailSender{

    private Mail mail;

    public MailSenderAdapter(){
        this.mail = new Mail();
    }

    @Override
    public boolean send() {

        return mail.sendMail();
        //return true;
    }
}
