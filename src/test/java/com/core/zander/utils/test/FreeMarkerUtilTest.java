package com.core.zander.utils.test;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.core.zander.utils.FreeMarkerUtil;

public class FreeMarkerUtilTest {

	@Test
	public void testResolveText() {
		
		final Properties props = new Properties();
        final Map<String, Object> params = new HashMap<>();
        final Map<String, Object> message = new HashMap<>();
        final List<String> errorMessage = new ArrayList<String>();
        errorMessage.add("test error message 01");
        errorMessage.add("test error message 02");
        
        try (InputStream inStream = new FileInputStream("src/test/resources/Message.properties")) {
            props.load(inStream);
            message.put("processedOn", new Date());
            message.put("errorMessages", errorMessage);
            params.put("message", message);

            final String content = FreeMarkerUtil.resolveText(props.getProperty("mail.content"), params);
            Assert.assertNotNull(content);
            
            File file = new File("E:/test/freemarker.txt");
            if (!file.exists()) {
            	file.createNewFile();
            	FileOutputStream fos = new FileOutputStream(file);
                fos.write(content.getBytes());
                fos.close();
            }
            
        } catch (final Exception e) {
            e.printStackTrace();
            fail();
        }
	}

}
