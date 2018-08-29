package com.core.zander.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.collections4.CollectionUtils;

/**
 * 
 * @author zander.zhang
 *
 */
public class JavaMailUtil {
	
	public static final String SENDER_ADDRESS = "xxx@qq.com";
	public static final String RECIPIENT_ADDRESS = "abc@qq.com";
	public static final String SENDER_ACCOUNT = "xxx@qq.com";
	public static final String SENDER_PASSWORD = "xxxxxx";
	
	private static final String MAIL_SUBJECT = "mailSubject";
	private static final String MAIL_CONTENT = "mailContent";
	private static final String MAIL_IMAGE = "mailImage";
	private static final String MAIL_IMAGE_ID = "mailImageId";
	private static final String MAIL_ATTACHMENTS = "mailAttachment";
	
	private static final String HTML_TYPE = "text/html;charset=UTF-8";
	
	@SuppressWarnings("static-access")
	public static void processSendEmail(Map<String, Object> map) throws Exception {
		// 1. 初始化连接邮件服务器的配置参数
		Properties props = new Properties();
		initMailConfigurationInfo(props);

		// 2. 创建定义整个应用程序所需要的环境信息的Session 对象
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SENDER_ACCOUNT, SENDER_PASSWORD);
			}
		});

		// 3. 创建邮件的实例对象
		Message msg = getMimeMessage(session, map);

		// 4. 根据session对象获取邮件传输对象Transport
		Transport transport = session.getTransport();
		
		// 5. 设置邮件发送人的账户和密码
		transport.connect(SENDER_ACCOUNT, SENDER_PASSWORD);
		
		// 6. 发送邮件
		transport.send(msg, msg.getAllRecipients());

		// 只发送给指定的人
		// transport.send(msg, new Address[]{new InternetAddress("xxx@qq.com")});

		// 7. 关闭邮件连接
		transport.close();
	}
	
	/**
	 * Initialize mail server configuration information
	 * @param prop
	 */
	public static void initMailConfigurationInfo(Properties props) {
		if (props == null) {
			props = new Properties();
		}
		
		// 是否开启 debug 调试
		props.setProperty("mail.debug", "true");
		// 发送服务器是否需要身份认证
        props.setProperty("mail.smtp.auth", "true"); 
        // 邮件服务器主机名
		props.setProperty("mail.host", "smtp.qq.com");
		// 发送邮件协议名称
		props.setProperty("mail.transport.protocol", "smtp");
		// 邮件发送端口
		props.setProperty("mail.smtp.port", "465");
		// 如果设置，指定实现javax.net.SocketFactory 接口的类的名称，这个类将被用于创建SMTP的套接字
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // 如果设置为true，未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket 创建的套接字类，默认值为true
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        // 指定的端口连接到在使用指定的套接字工厂。如果没有设置，将使用默认端口
        props.setProperty("mail.smtp.socketFactory.port", "465");
	}
	
	/**
	 * Create and return an instance of a mail message object
	 * @param session
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static MimeMessage getMimeMessage(Session session, Map<String, Object> map) throws Exception {
		String subject = "";
		String content = "";
		// 创建一封邮件的实例对象
		MimeMessage msg = new MimeMessage(session);
		// 设置发件人信息
		msg.setFrom(new InternetAddress(SENDER_ADDRESS));
		
		/**
		 * 设置收件人地址（可增加多个收件人、抄送、密送）
		 * MimeMessage.RecipientType.TO 发送
		 * MimeMessage.RecipientType.CC 抄送
		 * MimeMessage.RecipientType.BCC 密送
		 */
		msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(RECIPIENT_ADDRESS));
		if (map.get(MAIL_SUBJECT) != null) {
			subject = map.get(MAIL_SUBJECT).toString();
		}
		if (map.get(MAIL_CONTENT) != null) {
			content = map.get(MAIL_CONTENT).toString();
		}
		// 设置邮件主题
		msg.setSubject(subject, "UTF-8");
		
		// 设置邮件正文部分
		MimeMultipart mm = new MimeMultipart();
		BodyPart textPart = new MimeBodyPart();
		textPart.setContent(content, HTML_TYPE);
		if (map.get(MAIL_IMAGE) != null && map.get(MAIL_IMAGE_ID) != null) {
			textPart = addImageToMailContent(map, textPart);
		}
		mm.addBodyPart(textPart);
		
		// 添加附件
		if (map.get(MAIL_ATTACHMENTS) != null) {
			addAttachmentsToMail((ArrayList<File>) map.get(MAIL_ATTACHMENTS), mm);
			mm.setSubType("mixed"); // 混合关系
		}
		msg.setContent(mm);
		// 设置邮件发送时间，默认立即发送
		msg.setSentDate(new Date());
		
		return msg;
	}
	
	public static void addAttachmentsToMail(List<File> attachments, Multipart multipart) throws MessagingException, UnsupportedEncodingException {
		if (CollectionUtils.isNotEmpty(attachments)) {
			for(File attachment : attachments) {
				
				if (!attachment.exists()) {
					continue;
				}
				
				MimeBodyPart part = new MimeBodyPart();
				DataHandler dh = new DataHandler(new FileDataSource(attachment));
				part.setDataHandler(dh);
				part.setFileName(MimeUtility.encodeText(dh.getName()));
				multipart.addBodyPart(part);
			}
		}
	}
	
	public static BodyPart addImageToMailContent(Map<String, Object> map, BodyPart textPart) throws MessagingException {
		File image = (File) map.get(MAIL_IMAGE);
		String imageId = map.get(MAIL_IMAGE_ID).toString();
		if (image != null && image.exists() && image.isFile()) {
			// 图片节点
			MimeBodyPart imagePart = new MimeBodyPart();
			DataHandler dh = new DataHandler(new FileDataSource(image));
			imagePart.setDataHandler(dh);
			imagePart.setContentID(imageId);
			// 文本+图片的混合节点
			MimeMultipart mp = new MimeMultipart();
			mp.addBodyPart(textPart);
			mp.addBodyPart(imagePart);
			mp.setSubType("related"); // 关联关系
			// 将混合节点封装成一个普通节点
			MimeBodyPart textImagePart = new MimeBodyPart();
			textImagePart.setContent(mp);
			
			return textImagePart;
		}
		return textPart;
	}
	
	public static void main(String[] args) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String content = "该图片直接显示到邮件内容上：<br/> <a href='http://www.cnblogs.com'><img src='cid:imageId'></a>";
		
		File dir = new File("E:/test/");
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					return (pathname.exists() && pathname.isFile());
				}
			});
			map.put(MAIL_ATTACHMENTS, new ArrayList<File>(Arrays.asList(files)));
		}
		
		File image = new File("E:/test/image.jpg");
		if (image.exists() && image.isFile()) {
			map.put(MAIL_IMAGE, image);
			map.put(MAIL_IMAGE_ID, "imageId");
		}
		
		map.put(MAIL_SUBJECT, "邮件主题！");
		//map.put(MAIL_CONTENT, "发送一份简单的邮件！");
		map.put(MAIL_CONTENT, content);
		processSendEmail(map);
	}
	
//	public static void attachImageToMailContent(Message msg) throws Exception {
//		// 1. 添加图片节点
//		MimeBodyPart image = new MimeBodyPart();
//		// 读取本地文件
//		DataHandler dh = new DataHandler(new FileDataSource("E:/test/image.jpg"));
//		// 将图片添加到节点
//		image.setDataHandler(dh);
//		// 为该节点设置一个唯一编号 （在文本节点将引用该id）
//		image.setContentID("mailImageId");
//		
//		// 2. 创建文本节点
//		MimeBodyPart text = new MimeBodyPart();
//		// 这里添加图片的方式是将整个图片包含到邮件内容中，实际上也可以以http链接的形式添加网络图片
//		text.setContent("该图片直接显示到邮件内容上：<br/> <a href='http://www.cnblogs.com'><img src='cid:mailImageId'></a>", "text/html;charset=UTF-8");
//		
//		// 3. (文本+图片) 设置文本 和 图片节点的关系（将文本和图片节点合成一个混合节点）
//		MimeMultipart mmTextImage = new MimeMultipart();
//		mmTextImage.addBodyPart(text);
//		mmTextImage.addBodyPart(image);
//		mmTextImage.setSubType("related"); // 关联关系
//		
//		// 4. 将文本+图片 混合的节点封装成一个普通节点
//		// 最终添加到邮件的Content 是由多个BodyPart 组成的Multipart，所以我们需要的是BodyPart，
//		// 上面的mailImageId并非BodyPart，所以要把mmTextImage封装成一个BodyPart
//		MimeBodyPart textImage = new MimeBodyPart();
//		textImage.setContent(mmTextImage);
//		
//		// 5. 创建附件节点
//		MimeBodyPart attachment = new MimeBodyPart();
//		// 读取本地文件
//		DataHandler dh2 = new DataHandler(new FileDataSource("E:/test/freemarker.txt"));
//		// 将附件数据添加到节点
//		attachment.setDataHandler(dh2);
//		// 设置附件的文件名（需要编码）
//		attachment.setFileName(MimeUtility.encodeText(dh2.getName()));
//		
//		// 6. 设置文本+图片 和 附件 的关系（合成一个大的混合节点/ Multipart ）
//		MimeMultipart mm = new MimeMultipart();
//		mm.addBodyPart(textImage);
//		mm.addBodyPart(attachment);	// 如有多个附件，可以附加多次
//		mm.setSubType("mixed"); // 混合关系
//		
//		// 7. 设置整个邮件关系 （将最后的混合节点作为邮件的内容添加到邮件对象）
//		msg.setContent(mm);
//	}
		
}
