package pid.biocarta;

import java.util.List;

public class InteractionComponent {
	private String componentRole;
	private String moleculeID;
	private List<ComponentLebel> interactionCompomentLebels;

	public String getComponentRole() {
		return componentRole;
	}

	public void setComponentRole(String componentRole) {
		this.componentRole = componentRole;
	}

	public String getMoleculeID() {
		return moleculeID;
	}

	public void setMoleculeID(String moleculeID) {
		this.moleculeID = moleculeID;
	}

	public List<ComponentLebel> getInteractionCompomentLebels() {
		return interactionCompomentLebels;
	}

	public void setInteractionCompomentLebels(
			List<ComponentLebel> interactionCompomentLebels) {
		this.interactionCompomentLebels = interactionCompomentLebels;
	}
}
