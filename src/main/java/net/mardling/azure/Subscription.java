package net.mardling.azure;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Subscription")
public class Subscription {
	
	
	@XmlElement(name="SubscriptionID")
	private String SubscriptionID;
	@XmlElement(name="SubscriptionName")
	private String SubscriptionName;
	@XmlElement(name="SubscriptionStatus")
	private String SubscriptionStatus;
	@XmlElement(name="AccountAdminLiveEmailId")
	private String AccountAdminLiveEmailId;
	@XmlElement(name="MaxCoreCount")
	private int MaxCoreCount;
	@XmlElement(name="MaxStorageAccounts")
	private int MaxStorageAccounts;
	@XmlElement(name="MaxHostedServices")
	private int MaxHostedServices;
	@XmlElement(name="CurrentCoreCount")
	private int CurrentCoreCount;
	@XmlElement(name="CurrentHostedServices")
	private int CurrentHostedServices;
	@XmlElement(name="CurrentStorageAccounts")
	private int CurrentStorageAccounts;
	@XmlElement(name="MaxVirtualNetworkSites")
	private int MaxVirtualNetworkSites;
	@XmlElement(name="MaxLocalNetworkSites")
	private int MaxLocalNetworkSites;
	@XmlElement(name="MaxDnsServers")
	private int MaxDnsServers;
	

	
	
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
	public int getMaxCoreCount() {
		return MaxCoreCount;
	}
	public void setMaxCoreCount(int maxCoreCount) {
		MaxCoreCount = maxCoreCount;
	}
	public int getMaxStorageAccounts() {
		return MaxStorageAccounts;
	}
	public void setMaxStorageAccounts(int maxStorageAccounts) {
		MaxStorageAccounts = maxStorageAccounts;
	}
	public int getMaxHostedServices() {
		return MaxHostedServices;
	}
	public void setMaxHostedServices(int maxHostedServices) {
		MaxHostedServices = maxHostedServices;
	}
	public int getCurrentCoreCount() {
		return CurrentCoreCount;
	}
	public void setCurrentCoreCount(int currentCoreCount) {
		CurrentCoreCount = currentCoreCount;
	}
	public int getCurrentHostedServices() {
		return CurrentHostedServices;
	}
	public void setCurrentHostedServices(int currentHostedServices) {
		CurrentHostedServices = currentHostedServices;
	}
	public int getCurrentStorageAccounts() {
		return CurrentStorageAccounts;
	}
	public void setCurrentStorageAccounts(int currentStorageAccounts) {
		CurrentStorageAccounts = currentStorageAccounts;
	}
	public int getMaxVirtualNetworkSites() {
		return MaxVirtualNetworkSites;
	}
	public void setMaxVirtualNetworkSites(int maxVirtualNetworkSites) {
		MaxVirtualNetworkSites = maxVirtualNetworkSites;
	}
	public int getMaxLocalNetworkSites() {
		return MaxLocalNetworkSites;
	}
	public void setMaxLocalNetworkSites(int maxLocalNetworkSites) {
		MaxLocalNetworkSites = maxLocalNetworkSites;
	}
	public int getMaxDnsServers() {
		return MaxDnsServers;
	}
	public void setMaxDnsServers(int maxDnsServers) {
		MaxDnsServers = maxDnsServers;
	}

	
}
