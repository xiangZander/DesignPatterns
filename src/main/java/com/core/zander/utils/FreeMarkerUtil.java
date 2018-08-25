package com.core.zander.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerUtil {
	
	public static final String FREEMARKER_TEMPLATE_NAME = "strTemplate";
	
	public static String resolveText(String source, Map<String, Object> params) {
		
		try (StringWriter writer = new StringWriter()) { //try-with-resource
			
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
			Template template = new Template(FREEMARKER_TEMPLATE_NAME, new StringReader(source), cfg);
			Map<String, Object> root = new HashMap<String, Object>();
			if (params != null) {
				root.putAll(params);
			}
			
			template.process(root, writer);
			return writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public static void main(final String[] args) {
        final Properties props = new Properties();
        final Map<String, Object> params = new HashMap<>();
        final Map<String, Object> message = new HashMap<>();
        final List<String> errorMessage = new ArrayList<String>();
        errorMessage.add("test error message 01");
        errorMessage.add("test error message 02");
        InputStream inStream = null;
        try {
            inStream = new FileInputStream("src/test/resources/Message.properties");
            props.load(inStream);
            System.out.println(props.getProperty("mail.content"));
            message.put("processedOn", new Date());
            message.put("errorMessages", errorMessage);
            params.put("message", message);

            final String content = resolveText(props.getProperty("mail.content"), params);
            System.out.println(content);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
