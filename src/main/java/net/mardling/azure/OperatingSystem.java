package net.mardling.azure;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="OperatingSystem")
@XmlAccessorType(XmlAccessType.FIELD)
public class OperatingSystem {
	
	@XmlElement(name="Version")
	private String version;
	@XmlElement(name="Label")
	private String label;
	private boolean isDefault;
	private boolean isActive;
	private int family;
	private String familyLabel;
	
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public int getFamily() {
		return family;
	}
	public void setFamily(int family) {
		this.family = family;
	}
	public String getFamilyLabel() {
		return familyLabel;
	}
	public void setFamilyLabel(String familyLabel) {
		this.familyLabel = familyLabel;
	}

}
