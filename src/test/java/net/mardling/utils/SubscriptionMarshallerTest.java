package net.mardling.utils;

import static org.junit.Assert.*;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.mardling.azure.Subscription;

import org.junit.Before;
import org.junit.Test;

public class SubscriptionMarshallerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		String xml="<Subscription xmlns=\"http://schemas.microsoft.com/windowsazure\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><SubscriptionID>72e280a7-f53d-4199-be45-3063afec8240</SubscriptionID><SubscriptionName>Free Trial</SubscriptionName><SubscriptionStatus>Active</SubscriptionStatus><AccountAdminLiveEmailId>paulmardling@hotmail.com</AccountAdminLiveEmailId><ServiceAdminLiveEmailId>paulmardling@hotmail.com</ServiceAdminLiveEmailId><MaxCoreCount>20</MaxCoreCount><MaxStorageAccounts>20</MaxStorageAccounts><MaxHostedServices>20</MaxHostedServices><CurrentCoreCount>0</CurrentCoreCount><CurrentHostedServices>0</CurrentHostedServices><CurrentStorageAccounts>0</CurrentStorageAccounts><MaxVirtualNetworkSites>10</MaxVirtualNetworkSites><MaxLocalNetworkSites>10</MaxLocalNetworkSites><MaxDnsServers>9</MaxDnsServers></Subscription>";
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Subscription.class);
			
			Unmarshaller unmarsh = jaxbContext.createUnmarshaller();
			
			//unmarsh.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			
			Subscription subs = (Subscription) unmarsh.unmarshal(new File("src/test/resources/subscription.xml"));
			
			System.out.println(subs.getSubscriptionName());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	


}
