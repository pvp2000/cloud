package net.mardling;

import org.python.util.PythonInterpreter;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;

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
		
		awsMap.put("ec2",ec2);
		execute("/aws/console.jy",awsMap);
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
