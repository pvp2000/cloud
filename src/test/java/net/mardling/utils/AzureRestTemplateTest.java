package net.mardling.utils;

import static org.junit.Assert.*;

import java.net.URISyntaxException;

import net.mardling.azure.OperatingSystem;
import net.mardling.azure.OperatingSystems;
import net.mardling.azure.Subscription;

import org.junit.Before;
import org.junit.Test;

public class AzureRestTemplateTest extends AzureRestTemplate {
	
	private AzureRestTemplate template;

	@Before
	public void setUp() throws Exception {
		template=new AzureRestTemplate();
	}

	
	@Test
	public void testGetSubscription() {
	
		
		try {
			Subscription subs = template.getSubscription("72e280a7-f53d-4199-be45-3063afec8240");
			System.out.println(subs.getSubscriptionName());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetOperatingSystems() {

		
		try {
			OperatingSystems os = template.getOperatingSystems("72e280a7-f53d-4199-be45-3063afec8240");
			System.out.println("OS " + os.getOperatingSystems().size());

			for (OperatingSystem myOs : os.getOperatingSystems()) {
				System.out.println(myOs.getVersion());
			}

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
