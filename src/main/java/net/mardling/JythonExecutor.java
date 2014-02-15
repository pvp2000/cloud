package net.mardling;

import net.mardling.utils.ServiceManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.python.util.PythonInterpreter;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.identitymanagement.model.Role;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.microsoft.windowsazure.serviceruntime.RoleEnvironment;
import com.microsoft.windowsazure.serviceruntime.RoleInstance;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JythonExecutor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PythonInterpreter.initialize(System.getProperties(), null, args);
		
		//PythonInterpreter interpreter = new PythonInterpreter();

		//InputStream script = JythonExecutor.class.getResourceAsStream("/HelloWorld.jy");
		//interpreter.set("Words","Hello World!");
		//interpreter.execfile(script);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("Words", "Hello World");
		execute("/HelloWorld.jy",map);
		
		
		//AWS
		
		HashMap<String, Object> awsMap = new HashMap<String, Object>();
		
		AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();

		AmazonEC2 ec2 = new AmazonEC2Client(credentialsProvider);
		AmazonS3 s3  = new AmazonS3Client(credentialsProvider);
		
		awsMap.put("ec2",ec2);
		awsMap.put("s3", s3);
		execute("/aws/console.jy",awsMap);
		
		//Azure

		ServiceManager manager = new ServiceManager();
		
		Map<String,Object> azMap = new HashMap<String,Object>();
		azMap.put("manager", manager);
		
		execute("/azure/manager.jy", azMap);
		
		HttpClient client = new DefaultHttpClient();
		
		HttpGet get = new HttpGet("https://management.core.windows.net/72e280a7-f53d-4199-be45-3063afec8240/operatingsystems");
		try {
			HttpResponse res = client.execute(get);
			System.out.println(res.getStatusLine().getReasonPhrase());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	public static void execute(String path, Map<String, Object> objs) {
		
		PythonInterpreter interpreter = new PythonInterpreter();
		
		InputStream script = JythonExecutor.class.getResourceAsStream(path);
		
		for(Map.Entry<String, Object> entry : objs.entrySet()) {
			interpreter.set(entry.getKey(), entry.getValue());
		}
		
		interpreter.execfile(script);
	}

}
