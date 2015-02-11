package pid.biocarta;

import java.util.List;

public class Pathway {
	private String pathway_id;
	private String subpath;
	private String organism;
	private String longName;
	private String shortName;
	private String source_id;
	private List<String> pathwayComponentList;
	
	public String getSubpath() {
		return subpath;
	}
	public void setSubpath(String subpath) {
		this.subpath = subpath;
	}
	public String getPathway_id() {
		return pathway_id;
	}
	public void setPathway_id(String pathway_id) {
		this.pathway_id = pathway_id;
	}	
	public String getOrganism() {
		return organism;
	}
	public void setOrganism(String organism) {
		this.organism = organism;
	}
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getSource_id() {
		return source_id;
	}
	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}
	public List<String> getPathwayComponentList() {
		return pathwayComponentList;
	}
	public void setPathwayComponentList(List<String> pathwayComponentList) {
		this.pathwayComponentList = pathwayComponentList;
	}
}
