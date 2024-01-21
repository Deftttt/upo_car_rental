package com.upo.springtest.service;

import com.upo.springtest.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            javaMailSender.send(message);
            System.out.println("Email sent successfully!");
        } catch (MailException e) {
            System.err.println("Error while sending email: " + e.getMessage());
        }
    }

    public String formatEmailContent(Booking booking){

        return String.format("Szanowny Kliencie,\n" +
                "\n" +
                "Dziękujemy za skorzystanie z usług naszej wypożyczalni samochodów. Potwierdzamy dokonanie rezerwacji oraz szczegóły dotyczące Twojego wypożyczenia.\n" +
                "\n" +
                "Szczegóły Wypożyczenia:\n" +
                "- Numer rezerwacji: # %d\n" +
                "- Data wypożyczenia: %s\n" +
                "- Data zwrotu: %s\n" +
                "- Samochód: %s %s (Numer rejestracyjny: %s)\n" +
                "- Miejsce odbioru: %s\n" +
                "- Miejsce zwrotu: %s\n" +
                "\n" +
                "Dodatkowe Informacje:\n" +
                "- Pracownik obsługujący: %s %s %s\n" +
                "\n" +
                "Prosimy o zachowanie numeru rezerwacji oraz powyższych informacji podczas odbioru samochodu.\n" +
                "\n" +
                "Życzymy bezpiecznej podróży!\n" +
                "\n" +
                "Z poważaniem,\n" +
                "Zespół UpoCarRental", booking.getId(), booking.getPickupDate().toString(), booking.getReturnDate().toString(),
                booking.getCar().getCarModel().getBrand(), booking.getCar().getCarModel().getModel(), booking.getCar().getRegistrationNumber(),
                booking.getPickupLocation().getName(), booking.getReturnLocation().getName(),
                booking.getEmployee().getUser().getFirstName(), booking.getEmployee().getUser().getLastName(), booking.getEmployee().getUser().getPhoneNumber()
                );
    }
}
