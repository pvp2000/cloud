package net.mardling.azure;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Subscription")
//@XmlType(name="Subscription",namespace="http://schemas.microsoft.com/windowsazure")
public class Subscription {
	
	public Subscription() {
		System.out.println("Creating Subs...");
	}
	
	@XmlElement(name="SubscriptionID")
	private String SubscriptionID;
	@XmlElement(name="SubscriptionName")
	private String SubscriptionName;
	@XmlElement(name="SubscriptionStatus")
	private String SubscriptionStatus;
	@XmlElement(name="AccountAdminLiveEmailId")
	private String AccountAdminLiveEmailId;
	@XmlElement(name="MaxStorageAccounts")
	private String MaxStorageAccounts;
	
	
	public String getSubscriptionID() {
		return SubscriptionID;
	}
	public void setSubscriptionID(String subscriptionID) {
		SubscriptionID = subscriptionID;
	}
	public String getSubscriptionName() {
		return SubscriptionName;
	}
	public void setSubscriptionName(String subscriptionName) {
		SubscriptionName = subscriptionName;
		System.out.println("Setting Name...");
	}
	public String getSubscriptionStatus() {
		return SubscriptionStatus;
	}
	public void setSubscriptionStatus(String subscriptionStatus) {
		SubscriptionStatus = subscriptionStatus;
	}
	public String getAccountAdminLiveEmailId() {
		return AccountAdminLiveEmailId;
	}
	public void setAccountAdminLiveEmailId(String accountAdminLiveEmailId) {
		AccountAdminLiveEmailId = accountAdminLiveEmailId;
	}
	public String getMaxStorageAccounts() {
		return MaxStorageAccounts;
	}
	public void setMaxStorageAccounts(String maxStorageAccounts) {
		MaxStorageAccounts = maxStorageAccounts;
	}

	
}
