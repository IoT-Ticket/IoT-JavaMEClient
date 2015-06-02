package com.iotticket.me.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.bouncycastle.util.encoders.Base64;

public class HttpClient {
	
	public static final int MAX_REDIRECTS = 5;
	
	String username = null;
	String password = null;
	String userAgent = "";
	int timeout = 5 * 1000; // 5 seconds
	int waitTime = 500;

	public HttpClient() {
		userAgent = "Profile/" + System.getProperty("microedition.profiles")
				+ " Configuration/"
				+ System.getProperty("microedition.configuration");
	}
	
	public HttpResponse get(String url) {
		return get(url, "text/plain");
	}
	
	public HttpResponse get(String url, String acceptType) {
			
		HttpResponse response = new HttpResponse();
		HttpConnection con = null;
		DataInputStream dis = null;
		StringBuffer responseMessage = new StringBuffer();
		boolean redirect = false;
		int redirects = 0;
		try {
			do {
				con = getConnection(url, acceptType);
				dis = new DataInputStream(con.openInputStream());
				int slept = 0;
				while (dis.available() == 0) {
					slept += waitTime;
					if (slept >= timeout) {
						response.statusCode = -1;
						return response;
					}
					Thread.sleep(waitTime);
				}
				int ch;
				while ((ch = dis.read()) != -1) {
					responseMessage.append((char)ch);
				}
				int status = con.getResponseCode();
				response.statusCode = status;
				
				switch (status) {
				case HttpConnection.HTTP_OK: // Success!
					break;
				case HttpConnection.HTTP_TEMP_REDIRECT:
				case HttpConnection.HTTP_MOVED_TEMP:
				case HttpConnection.HTTP_MOVED_PERM:
					url = con.getHeaderField("location");
					if (dis != null) dis.close();
					if (con != null) con.close();
	
					con = null;
					redirects++;
					redirect = true;
					break;
				default:
					con.close();
					throw new IOException("Response status not OK:" + status);
				}
			} while (redirect == true && redirects < MAX_REDIRECTS);
			if (redirects == MAX_REDIRECTS) {
				throw new IOException("Too many redirects");
			}
		} catch(Exception e) {
			response.exception = e;
		} finally {
			response.body = responseMessage.toString();
			try {
				if (con != null)
					con.close();
				if (dis != null)
					dis.close();
			} catch (IOException ioe) {
				response.exception = ioe;
				response.statusCode = -1;
			}
		}
		return response;
	}

	public HttpResponse post(String url, String contentType, String data, String acceptType) {
		HttpResponse response = new HttpResponse();
		HttpConnection con = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		StringBuffer responseMessage = new StringBuffer();
		boolean redirect = false;
		int redirects = 0;
		
		try {
			do {
				con = getConnection(url, acceptType, Connector.READ_WRITE);
				con.setRequestMethod(HttpConnection.POST);
				con.setRequestProperty("Content-Type", contentType);
				// set message length
				if (data != null) {
					con.setRequestProperty("Content-Length", ""
							+ data.length());
				}

				if (data != null) {
					dos = new DataOutputStream(con.openOutputStream());
					byte[] request_body = data.getBytes();
					for (int i = 0; i < request_body.length; i++) {
						dos.writeByte(request_body[i]);
					}
					dos.flush();
				}

				dis = new DataInputStream(con.openInputStream());
				int slept = 0;
				while (dis.available() == 0) {
					slept += waitTime;
					if (slept >= timeout) {
						response.statusCode = -1;
						return response;
					}
					Thread.sleep(waitTime);
				}
				int ch;
				while ((ch = dis.read()) != -1) {
					responseMessage.append((char)ch);
				}
				int status = con.getResponseCode();
				response.statusCode = status;

				switch (status) {
				case HttpConnection.HTTP_OK: // Success!
				case HttpConnection.HTTP_CREATED:
					break;
				case HttpConnection.HTTP_TEMP_REDIRECT:
				case HttpConnection.HTTP_MOVED_TEMP:
				case HttpConnection.HTTP_MOVED_PERM:
					url = con.getHeaderField("location");
					if (dis != null) dis.close();
					if (con != null) con.close();
	
					con = null;
					redirects++;
					redirect = true;
					break;
				default:
					con.close();
					throw new IOException("Response status not OK:" + status);
				}
			} while (redirect == true && redirects < MAX_REDIRECTS);
			if (redirects == MAX_REDIRECTS) {
				throw new IOException("Too many redirects");
			}
		} catch(Exception e) {
			response.exception = e;
		} finally {
			response.body = responseMessage.toString();
			try {
				if (con != null)
					con.close();
				if (dis != null)
					dis.close();
			} catch (IOException ioe) {
				response.exception = ioe;
				response.statusCode = -1;
			}
		}
		return response;
	}
	
	public void setCredentials(String user, String pwd) {
		username = user;
		password = pwd;
	}
	
	private void configureConnection(HttpConnection conn, String acceptType) throws IOException {
		conn.setRequestProperty("User-Agent", userAgent);
		String locale = System.getProperty("microedition.locale");
		if (locale == null) { locale = "en-US";	}
		conn.setRequestProperty("Accept-Language", locale);
		conn.setRequestProperty("Accept", acceptType);
		if (username != null && password != null) {
			byte[] bytes = (username + ":" + password).getBytes();
			conn.setRequestProperty("Authorization", "Basic " + Base64.toBase64String(bytes));
		}
	}

	public HttpConnection getConnection(String url, String acceptType) throws IOException {
		HttpConnection conn = (HttpConnection) Connector.open(url);
		configureConnection(conn, acceptType);
		return conn;
	}

	private HttpConnection getConnection(String url, String acceptType, int access) throws IOException {
		HttpConnection conn = (HttpConnection) Connector.open(url, access);
		configureConnection(conn, acceptType);
		return conn;
	}
}