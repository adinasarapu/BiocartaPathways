package pid.biocarta;

import java.util.List;
import java.util.Map;

public class Molecule {
	private String moleculeID;
	private String moleculeType;
	private Map<String,MoleculeAnnotation> moleculeAnnotation;
	private String entrezGeneID;
	private Map<String, ComplexComponent> complexMembers;
	private List<String> familyMembers;

	public List<String> getFamilyMembers() {
		return familyMembers;
	}
	public void setFamilyMembers(List<String> familyMember) {
		this.familyMembers = familyMember;
	}
	public Map<String, ComplexComponent> getComplexMembers() {
		return complexMembers;
	}
	public void setComplexMembers(Map<String, ComplexComponent> complex) {
		this.complexMembers = complex;
	}
	public String getEntrezGeneID() {
		return entrezGeneID;
	}
	public Map<String,MoleculeAnnotation> getMoleculeAnnotation() {
		return moleculeAnnotation;
	}
	public void setMoleculeAnnotation(Map<String,MoleculeAnnotation> molecule_name) {
		this.moleculeAnnotation = molecule_name;
	}
	public void setEntrezGeneID(String entrezGeneID) {
		this.entrezGeneID = entrezGeneID;
	}	
	public String getMoleculeID() {
		return moleculeID;
	}
	public void setMoleculeID(String molecule_id) {
		this.moleculeID = molecule_id;
	}
	public String getMoleculeType() {
		return moleculeType;
	}
	public void setMoleculeType(String molecule_type) {
		this.moleculeType = molecule_type;
	}
}
