/**
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.appengine.mail;

// [START simple_includes]
import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
// [END simple_includes]

// [START multipart_includes]
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import javax.activation.DataHandler;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
// [END multipart_includes]

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class MailServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String type = req.getParameter("type");
    
      resp.getWriter().print("Sending simple email............");
      sendMultipartMail();
    
  }

   private void sendMultipartMail() {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    String msgBody = "...";

    try {
      Message msg = new MimeMessage(session);
      System.out.println("session===>"+session);

      msg.setFrom(new InternetAddress("venkatesh.radhakrishnan@valeo.com", "venkatesh"));
      msg.addRecipient(Message.RecipientType.TO,
                       new InternetAddress("venkatesh.radhakrishnan@valeo.com", "venkatesh"));
      
      
      msg.setSubject("Test mail for - gmail actions");
      
      //msg.setText("This is a test");
      msg.setText(msgBody);
      // [START multipart_example]
      String htmlBody = "";          // ...
      StringBuffer contentBuffer = new StringBuffer();
      
      
      contentBuffer.append("<div itemscope itemtype='http://schema.org/EmailMessage'>");
      
      contentBuffer.append("<div itemprop='potentialAction' itemscope itemtype='http://schema.org/ConfirmAction'>");
      contentBuffer.append("<meta itemprop='name' content='Approve This Request'/>");
      contentBuffer.append(" <div itemprop='handler' itemscope itemtype='http://schema.org/HttpActionHandler'>");
      contentBuffer.append("<link itemprop='url' href='https://valeo-cp0550-gaeprototypes.appspot.com/approve?param1=test_param1'/>");
      contentBuffer.append("</div>");
      contentBuffer.append("</div>");
      contentBuffer.append("<meta itemprop='description' content='Approval request for XXXXXXXX'/>");
      
      
      contentBuffer.append("<div itemprop='potentialAction' itemscope itemtype='http://schema.org/RejectAction'>");
      contentBuffer.append("<meta itemprop='name' content='Reject This Request'/>");
      contentBuffer.append(" <div itemprop='handler' itemscope itemtype='http://schema.org/HttpActionHandler'>");
      contentBuffer.append("<link itemprop='url' href='https://valeo-cp0550-gaeprototypes.appspot.com/reject?param1=test_param1'/>");
      contentBuffer.append("</div>");
      contentBuffer.append("</div>");
      contentBuffer.append("<meta itemprop='description' content='Reject request for XXXXXXXX'/>");
      
      contentBuffer.append("</div>");

      

	  htmlBody=contentBuffer.toString();
	  
      byte[] attachmentData = null;  // ...
      Multipart mp = new MimeMultipart();

      MimeBodyPart htmlPart = new MimeBodyPart();
      
      //htmlPart.setText(contnt,"utf-8");
      htmlPart.setHeader("Content-Type","text/plain; charset='utf-8'");
      htmlPart.setHeader("Content-Transfer-Encoding", "quoted-printable");
	    
	    
      htmlPart.setContent(htmlBody, "text/html");
      //htmlPart.saveChanges();
      mp.addBodyPart(htmlPart);

      /*
      MimeBodyPart attachment = new MimeBodyPart();
      InputStream attachmentDataStream = new ByteArrayInputStream(attachmentData);
      attachment.setFileName("manual.pdf");
      attachment.setContent(attachmentDataStream, "application/pdf");
      mp.addBodyPart(attachment);
      */

      msg.setContent(mp);
      // [END multipart_example]

      Transport.send(msg);

    } catch (AddressException e) {
    	e.printStackTrace();
    } catch (MessagingException e) {
    	e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
    	e.printStackTrace();
    }
  }
}
