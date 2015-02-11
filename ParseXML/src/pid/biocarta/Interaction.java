package pid.biocarta;

import java.util.List;
import java.util.Map;

public class Interaction {
	private String interactionType;
	private String interactionID;
	private Map<String ,InteractionComponent> interactionComponentMap;
	private List<InteractionComponent> interactionComponentList;
	
	public Map<String, InteractionComponent> getInteractionComponentMap() {
		return interactionComponentMap;
	}
	public void setInteractionComponentMap(
			Map<String, InteractionComponent> interactionComponentMap) {
		this.interactionComponentMap = interactionComponentMap;
	}
	public List<InteractionComponent> getInteractionComponentList() {
		return interactionComponentList;
	}
	public void setInteractionComponentList(
			List<InteractionComponent> interactionComponentList) {
		this.interactionComponentList = interactionComponentList;
	}
	public Map<String ,InteractionComponent> getInteractionComponents() {
		return interactionComponentMap;
	}
	public void setInteractionComponents(Map<String ,InteractionComponent> interactionComponents) {
		this.interactionComponentMap = interactionComponents;
	}
	public String getInteractionType() {
		return interactionType;
	}
	public void setInteractionType(String interactionType) {
		this.interactionType = interactionType;
	}
	public String getInteractionID() {
		return interactionID;
	}
	public void setInteractionID(String interactionID) {
		this.interactionID = interactionID;
	}
	
}
