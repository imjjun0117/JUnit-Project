package site.joony.junitproject.util;

import org.springframework.stereotype.Component;
// 테스트 스텁 가짜!!
// IoC 컨테이너 등록
// IoC 컨테이너는 싱글톤으로 같은 타입이 들어갈 수 없다. 구현되고 주석처리!
@Component
public class MailSenderStub implements MailSender {
    @Override
    public boolean send() {
        return true;
    }
}
