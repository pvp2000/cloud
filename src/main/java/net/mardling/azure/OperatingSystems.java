package net.mardling.azure;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="OperatingSystems")
@XmlAccessorType(XmlAccessType.FIELD)
public class OperatingSystems {
	
	@XmlElement(name="operatingSystem")
	private List<OperatingSystem> operatingSystems=null;

	public List<OperatingSystem> getOperatingSystems() {
		return operatingSystems;
	}

	public void setOperatingSystems(List<OperatingSystem> operatingSystems) {
		this.operatingSystems = operatingSystems;
	}
	
	

}
