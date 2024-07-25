package com.code.baseservice.service.impl;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

@Service
public class CaptchaService {

    private static final String CHARACTERS = "1234567890";
    private static final int CAPTCHA_LENGTH = 4;

    public String generateCaptchaText() {
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            captcha.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return captcha.toString();
    }

    public String generateCaptchaImage(String captchaText) throws Exception {
        int width = 200;
        int height = 50;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // 填充背景颜色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // 画边框
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, width - 1, height - 1);

        // 设置字体
        g2d.setFont(new Font("Arial", Font.BOLD, 40));

        // 生成随机验证码
        Random random = new Random();
        for (int i = 0; i < captchaText.length(); i++) {
            // 设置随机颜色
            g2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            // 绘制验证码
            g2d.drawString(String.valueOf(captchaText.charAt(i)), 45 * i + 10, 40);
        }

        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
