package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class RestClient {
	String host = "localhost";
	int port = 8080;

	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static String logpath = "/accessdevice/log/"; // la på ein skråstrek her fordi det mangla på slutten.

	public void doPostAccessEntry(String message) {

		// TODO: implement a HTTP POST on the service to post the message
		
		try (Socket s = new Socket(host, port)) {
			// construct the GET request **
			String messagejson = "{\n   \"message\": \""+message+"\"\n}";
			String httppostrequest = "POST " + logpath + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
					+ "Host: localhost\r\n" + "Connection: close\r\n" + "Content-Type: application/json\r\n"
					+ "Content-Length: "+messagejson.length()+"\r\n"+"\r\n" + messagejson;
			System.out.println(httppostrequest);
			// sent the HTTP request
			OutputStream output = s.getOutputStream();

			PrintWriter pw = new PrintWriter(output, false);

			pw.print(httppostrequest);
			pw.flush();

			// read the HTTP response
			InputStream in = s.getInputStream();

			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (scan.hasNext()) {

				String nextline = scan.nextLine();

				if (header) {
					System.out.println(nextline);
				} else {
					jsonresponse.append(nextline);
				}

				// simplified approach to identifying start of body: the empty line
				if (nextline.isEmpty()) {
					header = false;
				}
			}
			String jsonres = jsonresponse.toString();
			System.out.println("Post-connection reply: " + jsonres);
			scan.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String codepath = "/accessdevice/code";

	public AccessCode doGetAccessCode() {

		AccessCode code = null;

		// TODO: implement a HTTP GET on the service to get current access code

		try (Socket s = new Socket(host, port)) {
			// construct the GET request
			String httpgetrequest = "GET " + codepath + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
					+ "Host: localhost\r\n" + "Connection: close\r\n" + "\r\n";

			// sent the HTTP request
			OutputStream output = s.getOutputStream();

			PrintWriter pw = new PrintWriter(output, false);

			pw.print(httpgetrequest);
			pw.flush();

			// read the HTTP response
			InputStream in = s.getInputStream();

			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (scan.hasNext()) {

				String nextline = scan.nextLine();

				if (header) {
					System.out.println(nextline);
				} else {
					jsonresponse.append(nextline);
				}

				// simplified approach to identifying start of body: the empty line
				if (nextline.isEmpty()) {
					header = false;
				}
			}
			String jsonres = jsonresponse.toString();
			System.out.println("json: " + jsonres); //**
			String jsoncode = jsonres.substring(jsonres.indexOf("accesscode")+13, jsonres.indexOf("accesscode")+16);
			System.out.println("accesscode-only: " + jsoncode); //**
			String[] deler = jsoncode.split(",");
			System.out.println("accesscode-aftersplit: "+ deler[0]+deler[1]); //**
			int[] kodeint = new int[deler.length];
			int i = 0;
			for (String siff : deler) {
				kodeint[i] = Integer.parseInt(siff);
				i++;
			}
			code = new AccessCode();
			code.setAccesscode(kodeint);
			scan.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}
}
