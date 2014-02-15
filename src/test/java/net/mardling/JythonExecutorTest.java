package net.mardling;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import net.mardling.azure.Subscription;
import net.mardling.utils.AzureRestTemplate;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class JythonExecutorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testExecuteAWS() {
		//AWS
		
		HashMap<String, Object> awsMap = new HashMap<String, Object>();
		
		AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();

		AmazonEC2 ec2 = new AmazonEC2Client(credentialsProvider);
		AmazonS3 s3  = new AmazonS3Client(credentialsProvider);
		
		awsMap.put("ec2",ec2);
		awsMap.put("s3", s3);
		JythonExecutor.execute("/aws/console.jy",awsMap);
	}
	
	@Test
	public void testExecuteAzure() {
		
		//Azure

		AzureRestTemplate template = new AzureRestTemplate();
		Subscription subscription = template.getSubscription("72e280a7-f53d-4199-be45-3063afec8240");
		
		Map<String,Object> azMap = new HashMap<String,Object>();
		azMap.put("subscription", subscription);
		
		JythonExecutor.execute("/azure/manager.jy", azMap);
		
	}

}
