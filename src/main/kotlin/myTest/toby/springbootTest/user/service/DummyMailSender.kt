package myTest.toby.springbootTest.user.service

import org.springframework.mail.MailException
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage

class DummyMailSender : MailSender {
    @Throws(MailException::class)
    override fun send(mailMessage: SimpleMailMessage) {
    }

    @Throws(MailException::class)
    override fun send(mailMessage: Array<SimpleMailMessage>) {
    }
}