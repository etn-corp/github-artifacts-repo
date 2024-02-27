/*
 * Class.java
 *
 * Created on March 10, 2004, 6:58 AM
 */

package com.eaton.electrical.smrc.util;

import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.eaton.electrical.smrc.service.*;

/**
 * This class performs the email function for the Target Account Planner
 * @author  Jason Lubbert
 * @date March 10, 2004
 * 
 */
public class TAPMail {
	
	//  these fields could be altered by calling program
	private ArrayList recipients = new ArrayList();
	private String sender_address = "";
	private ArrayList ccRecipients = new ArrayList();
	private String sender_name = "";
	private String msg_html = "";
	private ArrayList attachments = new ArrayList();
	private String text = null;
	private String subject=null;
	
	
	/** Creates a new instance of Class TAPMail*/
	public TAPMail() {
		text="";
		subject="";
	}
	
	public void sendMessage(){
		sendMessage(text, subject);
	}
	
	/**  
	 * This method sends the email message
	 * @param String msg_text text message to be sent
	 * @param String subject  subject of the message to be sent
	 */
	public void sendMessage(String msg_text, String subject){
		// if no specific html message has been set
		//   msg_text += "THIS ONE SENT THROUGH TAPMail!!!!";
		SMRCLogger.debug("in sendMessage");
		if(recipients.size()==0 || sender_address.equals("") || msg_text.equals("")){
			SMRCLogger.debug("something missing, returning");
			if (recipients.size()==0){
				SMRCLogger.debug("recipients.size()==0");
			}
			if (sender_address.equals("")){
				SMRCLogger.debug("sender_address.equals spaces");
			}
			if (msg_text.equals("")){
				SMRCLogger.debug("msg_text is spaces");
			}
			return;
		}

		if (msg_html == ""){
			//     msg_html = msg_text;
			setHtmlMessage(msg_text);
		}
		try {
			Properties mailProp = System.getProperties();
			ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.smrc.util.TAPmail");
			Enumeration mailKeyList = rb.getKeys();
			String nextKey;
			// overwrite default properties with any properties from TAPmail.properties
			do {
				nextKey = (String) mailKeyList.nextElement();
				mailProp.setProperty(nextKey, rb.getString(nextKey));
			} while (mailKeyList.hasMoreElements());
			// default senders set in TAPmail.properties
			InternetAddress from = new InternetAddress(rb.getString("mail.from"), rb.getString("mail.from.personal"));
			Session session = Session.getDefaultInstance(mailProp, null);
			
			Message message = new MimeMessage(session);
			MimeMultipart multipart = new MimeMultipart("alternative");
			message.setSentDate(new java.util.Date());
			for (int i=0; i < recipients.size(); i++){
			    SMRCLogger.debug("To: " + recipients.get(i));
			    // only add email addresses of length>0
			    if(((String)recipients.get(i)).trim().length()!=0) {
			        try {
			            message.addRecipient(Message.RecipientType.TO, new InternetAddress((String) recipients.get(i)));
			        }catch(AddressException ae) {
			            SMRCLogger.warn(this.getClass().getName() + ".sendMessage() - Problem with email address", ae);
			        }
			    }
				
			}
			
			/*
			 * If there are no To email addresses then return from the method
			 */
			if((message.getRecipients(Message.RecipientType.TO))==null || (message.getRecipients(Message.RecipientType.TO)).length==0) {
			    return;
			}
			
			for (int i=0; i < ccRecipients.size(); i++){
			    if(((String)ccRecipients.get(i)).trim().length()!=0) {
			        SMRCLogger.debug("CC: " + ccRecipients.get(i));
			        try {
			        	message.addRecipient(Message.RecipientType.CC, new InternetAddress((String) ccRecipients.get(i)));
			        }catch(AddressException ae) {
			            SMRCLogger.warn(this.getClass().getName() + ".sendMessage() - Problem with email address", ae);
			        }
			    }
			}
			// if a sender is specified by calling program, if not, use default
			if (sender_name != ""){
				message.setFrom(new InternetAddress(sender_address, sender_name));
			} else {
				message.setFrom(from);
			}

			BodyPart txtMsg = new MimeBodyPart();
			txtMsg.setText(msg_text);
			multipart.addBodyPart(txtMsg);
			BodyPart htmlMsg = new MimeBodyPart();
			htmlMsg.setContent(msg_html, "text/html");
			multipart.addBodyPart(htmlMsg);
			if (attachments.size() > 0){
				for (int i=0; i < attachments.size(); i++){
					MimeBodyPart attachPart = new MimeBodyPart();
					// attach the file to the message
					FileDataSource fds = new FileDataSource((String) attachments.get(i));
					attachPart.setDataHandler(new DataHandler(fds));
					attachPart.setFileName(fds.getName()); 
					multipart.addBodyPart(attachPart);       
				}
			}
			message.setSubject(subject);
			message.setContent(multipart);
			message.saveChanges();
			
			try {
			    SMRCLogger.debug("TAPMail.sendMessage() - first attempt at transport ... ") ;
				Transport.send(message);
			} catch (Exception e){
			    SMRCLogger.error("TAPMail.sendMessage() - first attempt at transport failed ", e) ;
				// wait 2 seconds and try again in case transport cannot connect to server
			    Thread.sleep(2000);
				try {
				    SMRCLogger.debug("TAPMail.sendMessage() - second attempt at transport ... ") ;
					Transport.send(message);
				} catch (Exception e2){
				    SMRCLogger.error("TAPMail.sendMessage() - second attempt at transport failed ", e2) ;
					// wait 2 seconds and try again in case transport cannot connect to server
					Thread.sleep(2000);
					try {
					    SMRCLogger.debug("TAPMail.sendMessage() - third attempt at transport ... ") ;
						Transport.send(message);
					} catch (Exception e3){                     
					    SMRCLogger.error("TAPMail.sendMessage() - third and final attempt at transport failed ", e3) ;
					}
				}
			}
		} catch (Exception e){
		    SMRCLogger.error(this.getClass().getName() + ".sendMessage() ", e) ;
		}
		
		
	}
	
	/**  
	 * This method sets the HTML message and converts all newline chars to break tags
	 * @param String htmlMessage HTML version of the message to be sent in sendMessage
	 */
	public void setHtmlMessage (String htmlMessage){
		StringBuffer tempmsg = new StringBuffer(); 
		// ascii code for carriage return
		char returncode = 13;
		//char whitespace = 32;
		Character chReturnCode = new Character(returncode);
		//Character chWhiteSpace = new Character(whitespace);
		for (int i=0; i < htmlMessage.length(); i++){
			Character ch = new Character(htmlMessage.charAt(i));
			// current character is equal to carriage return
			if (ch.compareTo(chReturnCode) == 0){
				tempmsg.append("<BR>");   
			} else {
//				if (ch.compareTo(chWhiteSpace) == 0){
//				tempmsg.append("&nbsp;");
//				} else {
				tempmsg.append(ch);
			}
//			}
		}
		msg_html = tempmsg.toString();   
	}
	
	/**  
	 * This method adds a single recipient to the recipient ArrayList
	 * @param String recipient new recipient for the email
	 */    
	public void addRecipient (String recipient){
		recipients.add(recipient);
	}
	
	
	/**  
	 * This method adds a single recipient to the CCrecipient ArrayList
	 * @param String ccRecipient new recipient to be copied on the email
	 */  
	
	public void addCCRecipient (String ccRecipient){
		ccRecipients.add(ccRecipient);
	}
	
	/**  
	 * This method replaces the default sender name and email address
	 * @param String senderName Personal name on outgoing email
	 * @param String senderEmail sender email address on outgoing email
	 */   
	public void setSenderInfo (String senderName, String senderEmail){
		sender_name = senderName;   
		sender_address = senderEmail;
	}
	
	/**  
	 * This method places the filename into the ArrayList used by SendMessage to attach
	 * files to emails
	 * @param String fileName Location and name of file being attached to email
	 */   
	public void addAttachment (String filename){
		attachments.add(filename);
		
	}
	
	
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
