package no.hvl.dat110.ac.rest;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		// example objects for testing
//		accesslog.add("Test1");
//		accesslog.add("Test2");
//		accesslog.add("Test3");
//		accesslog.add("Test4");
		
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
		 	return gson.toJson("IoT Access Control Device");
		});
		
		// TODO: implement the routes required for the access control service
		post("/accessdevice/log/", (req, res) -> {
			System.out.println(req.body());
			AccessMessage am = gson.fromJson(req.body(), AccessMessage.class);
			accesslog.add(am.getMessage());
			return gson.toJson(accesslog.getLast());
		});
		
		get("/accessdevice/log/", (req, res) -> {
			return gson.toJson(accesslog);
		});
		
		get("/accessdevice/log/*", (req, res) -> {
			return gson.toJson(accesslog.get(Integer.parseInt(req.splat()[0])));
			
		});
		
		put("/accessdevice/code", (req, res) -> {
			accesscode = gson.fromJson(req.body(), AccessCode.class);
			return gson.toJson("Code was updated.");
		});
		
		get("/accessdevice/code", (req, res) -> {
			return gson.toJson(accesscode);
		});
		
		delete("/accessdevice/log/", (req, res) -> {
			accesslog.clear();
			res.body(gson.toJson(accesslog));
			return "";
		});
    }
    
}
