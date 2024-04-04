package com.example.demo.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
    public void sendCustomEmail(String to, String subject, String username, String confirmationUrl) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);

        // Đọc nội dung từ tệp HTML và thay thế các biến động
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Xác nhận Email</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #ffffff;\n" +
                "            border-radius: 5px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        h2 {\n" +
                "            color: #333333;\n" +
                "        }\n" +
                "\n" +
                "        p {\n" +
                "            color: #666666;\n" +
                "            line-height: 1.5;\n" +
                "        }\n" +
                "\n" +
                "        a {\n" +
                "            color: #007bff;\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "\n" +
                "        a:hover {\n" +
                "            text-decoration: underline;\n" +
                "        }\n" +
                "\n" +
                "        .footer {\n" +
                "            margin-top: 20px;\n" +
                "            font-size: 12px;\n" +
                "            color: #999999;\n" +
                "        }\n" +
                "\n" +
                "        .logo {\n" +
                "            display: block;\n" +
                "            margin: 0 auto;\n" +
                "            max-width: 200px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "    <img src=\"https://img.freepik.com/premium-vector/bird-logo-design_646665-636.jpg\" alt=\"Logo\" class=\"logo\">\n" +
                "    <h2>Xác minh tài khoản</h2>\n" +
                "    <p>Xin chào <strong>{{ username }}</strong>,</p>\n" +
                "    <p>Vui lòng nhấn vào đường dẫn dưới đây để xác minh địa chỉ email của bạn:</p>\n" +
                "    <p><a href=\"{{ confirmationUrl }}\">Xác nhận Email</a></p>\n" +
                "    <p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.</p>\n" +
                "    <p class=\"footer\">Trân trọng,<br>Đội ngũ hỗ trợ của chúng tôi</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";
        htmlContent = htmlContent.replace("{{ username }}", username);
        htmlContent = htmlContent.replace("{{ confirmationUrl }}", confirmationUrl);

        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }


    public String readHtmlFile(String filePath) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/" + filePath);
            byte[] content = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // Xử lý lỗi tùy thuộc vào yêu cầu của bạn
        }
    }
    public String generateOtp() {
        return RandomStringUtils.randomNumeric(6);
    }
}