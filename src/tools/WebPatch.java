package tools;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * A program that fixes all the localhost indexes in the rtb4free,com web site
 * @author ben
 *
 */
public class WebPatch {
	/** List of files to modify */
	static List<String> files = new ArrayList();
	static {
		files.add("database.json");
		files.add("stub.json");
		files.add("Campaigns/extendedDevice-test.json");
		files.add("Campaigns/payday.json");
		files.add("Campaigns/README.md");
		files.add("Campaigns/rtbfree-payday.json");
		files.add("Campaigns/Source.txt");
		files.add("web/login.html");
		files.add("web/exchange.html");
		files.add("XXXwww/index.html");
		files.add("XXXwww/video-sample.html");
		files.add("XXXwww/banner-sample.html");
		files.add("XXXwww/details.html");
		files.add("XXXwww/faq.html");
		files.add("XXXwww/vast.html");
		files.add("XXXwww/privatex/x_index.html");
		files.add("XXXwww/privatex/x_details.html");
	}
	public static void main(String [] args) throws Exception {
		boolean write = false;
		WebPatch p = new WebPatch();
		String fix = "";
		if (args.length > 0) {
			fix = "/usr/share/nginx/";
		}
		String computername=InetAddress.getLocalHost().getHostName();
		System.out.println("System Name = " + computername);
		if (computername.equals("ip-172-31-51-243")) {
			write = true;
			System.out.println("*** NOTE *** FILES WILL BE MODIFIED ***");
		} else {
			System.out.println("*** NO FILES WILL BE MODIFIED HERE ***");
		}
		for (String file : files) {
			file = file.replace("XXX", fix);
			String content = null;
			try {
			     content = new String(Files.readAllBytes(Paths.get(file))); 
			     StringBuilder sb = new StringBuilder(content);
			     int k = p.perform("localhost", "rtb4free.com", sb);
			     if (write)
			    	 Files.write(Paths.get(file), sb.toString().getBytes());
			     System.out.println(file + " had " + k + " replacements.");
			} catch (Exception error) {
				 System.out.println(file + " does not exist, SKIPPED...");
			}
		}
		
	}
	
	public int perform(String from, String to, StringBuilder sb) {
		int k = 0;
		while(patch(from,to,sb)) {
			k++;
		}
		return k;
	}
	
	public boolean patch(String from, String to, StringBuilder sb) {
		int index = sb.indexOf(from);
		if (index > -1) {
			
			sb.replace(index, index+from.length(), to);
			return true;
			
		}
		return false;
	}
}
