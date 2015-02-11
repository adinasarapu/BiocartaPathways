package pid.biocarta;

import java.util.List;

public class MoleculeAnnotation {
	private String nameType;
	private String longNameType;
	private String value;
	
	public String getNameType() {
		return nameType;
	}
	public void setNameType(String nameType) {
		this.nameType = nameType;
	}
	public String getLongNameType() {
		return longNameType;
	}
	public void setLongNameType(String longNameType) {
		this.longNameType = longNameType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
