package com.code.baseservice.service;

import com.code.baseservice.util.Telegram;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private  Session mailSession;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    public void checkEmails() {
        try {
            Store store = mailSession.getStore("imaps");
            store.connect("imap.gmail.com", username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                if (!message.isSet(Flags.Flag.SEEN)) {
                    Address[] fromAddresses = message.getFrom();
                    if (fromAddresses != null && fromAddresses.length > 0) {
                        String sender = ((InternetAddress) fromAddresses[0]).getAddress();
                        // 过滤发送方不是 service@mail.alipay.com 的邮件
                        if ("service@mail.alipay.com".equalsIgnoreCase(sender)) {
                            log.info("匹配到的未读邮件 - 主题: {}", message.getSubject());
                            if (message instanceof MimeMessage) {
                                parseEmail((MimeMessage) message);
                                message.setFlag(Flags.Flag.SEEN, true);
                            }
                        } else {
                            log.info("忽略邮件，发件人: {}", sender);
                        }
                    }
                }
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            log.error("检查邮件时出错: ", e);
        }
    }

    private void parseEmail(MimeMessage message) {
        try {
            Object content = message.getContent();
            if (content instanceof Multipart) {
                Multipart multipart = (Multipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                        String fileName = bodyPart.getFileName();
                        if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
                            log.info("发现 PDF 附件: {}", fileName);
                            InputStream is = bodyPart.getInputStream();
                            extractPdfData(is);
                        }
                    } else if (bodyPart.isMimeType("text/plain")) {
                        log.info("邮件正文: {}", bodyPart.getContent().toString());
                    }
                }
            }
        } catch (Exception e) {
            log.error("解析邮件时出错: ", e);
        }
    }

    private void extractPdfData(InputStream pdfStream) {
        try (PDDocument document = PDDocument.load(pdfStream)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            log.info("PDF 原始内容:\n{}", text);
            StringBuilder stringBuilder = new StringBuilder();

            // 提取付款金额（小写）
            String amountRegex = "小写：([\\d,\\.]+)";
            Matcher amountMatcher = Pattern.compile(amountRegex).matcher(text);
            if (amountMatcher.find()) {
                stringBuilder.append("付款金额："+ amountMatcher.group(1)+"\n");
                log.info("付款金额（小写）: {}", amountMatcher.group(1));
            }

            // 提取付款\u65F6\u95F4
            String timeRegex ="付款时间\\s*(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})";
            Matcher timeMatcher = Pattern.compile(timeRegex).matcher(text);
            if (timeMatcher.find()) {
                stringBuilder.append("付款时间："+ timeMatcher.group(1)+"\n");

                log.info("付款时间: {}", timeMatcher.group(1));
            }

            // 提取\u6536\u6B3E\u65B9
            String receiverRegex = "收款方\n账户名：([\\u4e00-\\u9fa5a-zA-Z0-9]+)";
            Matcher receiverMatcher = Pattern.compile(receiverRegex).matcher(text);
            if (receiverMatcher.find()) {
                stringBuilder.append(""+ receiverMatcher.group(1)+"\n");
                log.info("收款方: {}", receiverMatcher.group(1).trim());
            }
            Telegram telegram = new Telegram();

            telegram.sendYdoubaoMessage(stringBuilder.toString());
        } catch (Exception e) {
            log.error("解析 PDF 文件时出错:", e);
        }
    }

    public static void main(String[] args) {
        String a = "编号: 2025010700085000032576858992990080162326\n" +
                " \n" +
                "币种：人民币 / 单位：元                       \n" +
                "注：\n" +
                "本《支付宝电子回单》仅证明用户在申请该电子回单时间之前通过其支付宝账户的支付行为。\n" +
                "本《支付宝电子回单》有任何修改或涂改的，均为无效证明。\n" +
                "本《支付宝电子回单》仅供参考，如与用户支付宝账户记录不一致的，以支付宝账户记录为准。\n" +
                " \n" +
                " \n" +
                " \n" +
                "支付宝（中国）网络技术有限公司\n" +
                " \n" +
                " \n" +
                "业务凭证专用章盖章处\n" +
                "支付宝转账电子回单\n" +
                "回单生成时间：2025-01-07 00:16:02\n" +
                "付款方\n" +
                "账户名：黎绍源\n" +
                "账号：13535273613\n" +
                "账户类型：支付宝账户\n" +
                "开户机构：支付宝（中国）网络技术有限公司\n" +
                "收款方\n" +
                "账户名：骆上超\n" +
                "账号：177******69\n" +
                "账户类型：支付宝账户\n" +
                "开户机构：支付宝（中国）网络技术有限公司\n" +
                "支付宝流水号 20250107200040011100990076358501\n" +
                "付款时间 2025-01-07 00:14:40\n" +
                "付款金额\n" +
                "小写：14000.00\n" +
                "大写：壹万肆仟元整\n" +
                "摘要 朋友来往\n" +
                "\n" +
                "17:36:56.492 [scheduling-1] INFO  c.c.b.s.EmailService - [extractPdfData,102] - 付款金额（小写）: 14000.00\n" +
                "17:36:56.492 [scheduling-1] INFO  c.c.b.s.EmailService - [extractPdfData,111] - 付款时间: 2025-01-07 00:14:40";
        String receiverRegex = "收款方\n账户名：([\\u4e00-\\u9fa5a-zA-Z0-9]+)";
        Matcher receiverMatcher = Pattern.compile(receiverRegex).matcher(a);
        if (receiverMatcher.find()) {
            log.info("收款方: {}", receiverMatcher.group(1).trim());
        }
    }
}
