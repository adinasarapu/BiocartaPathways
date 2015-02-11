package pid.biocarta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PathwayModel {
	// private NodeList nl_paths;
	private static Map<String, Interaction> interaction_list;
	private static List<Pathway> path_list;
	private static Map<String, Molecule> moleculeMap;

	public void setPathwayModel(Element el, Document doc, String path_db) {
		if ("BioCarta".equals(path_db)) {
			NodeList nl_paths = doc.getElementsByTagName("Pathway");
			setPathways(nl_paths);
			NodeList nl_molecules = doc.getElementsByTagName("Molecule");
			setMolecules(nl_molecules);
			NodeList nl_interactions = doc.getElementsByTagName("Interaction");
			setInteractions(nl_interactions);
		}
	}

	public void setPathways(NodeList nl_paths) {
		List<Pathway> paths = new ArrayList<Pathway>();
		System.out.println("Pathways total = " + nl_paths.getLength());
		for (int i = 0; i < nl_paths.getLength(); i++) {
			Pathway path = new Pathway();
			String longName = null, shortName = null, source_id = null;
			Node node = nl_paths.item(i); // Pathway
			if (node.getNodeType() != Node.TEXT_NODE) {
				Node child = node.getFirstChild();
				while (child != null) {
					if (node.getNodeName().equals("Pathway")) {
						Element att = (Element) node;
						path.setPathway_id(att.getAttribute("id"));
						path.setSubpath(att.getAttribute("subnet"));
						if (child.getNodeName().equals("Organism")) {
							String organism = child.getFirstChild().getNodeValue();
							path.setOrganism(organism);
						}
						if (child.getNodeName().equals("LongName")) {
							longName = child.getFirstChild().getNodeValue();
							path.setLongName(longName);
						}
						if (child.getNodeName().equals("ShortName")) {
							shortName = child.getFirstChild().getNodeValue();
							path.setShortName(shortName);
						}
						if (child.getNodeName().equals("Source")) {
							source_id = child.getFirstChild().getNodeValue();
							path.setSource_id(source_id);
						}
						if (child.getNodeName().equals("PathwayComponentList")) {
							Element el = (Element) child;
							NodeList comp_list = el.getElementsByTagName("PathwayComponent");
							if (comp_list != null & comp_list.getLength() > 0) {
								List<String> pc = new ArrayList<String>();
								addComponentDetails(comp_list, path, pc);
								path.setPathwayComponentList(pc);
							}
						}
					}
					child = child.getNextSibling();
				}
			}
			paths.add(path);
		}
		path_list = paths;
	}

	public void setInteractions(NodeList nl_interactions) {
		Map<String, Interaction> map = new HashMap<String, Interaction>();
		System.out.println("Interactions total = " + nl_interactions.getLength());	
		for (int i = 0; i < nl_interactions.getLength(); i++) {
			Interaction interaction = new Interaction();
			Node node = nl_interactions.item(i); // Interaction
			if (node.getNodeType() != Node.TEXT_NODE) {
				Node child = node.getFirstChild();
				while (child != null) {
					if (node.getNodeName().equals("Interaction")) {						
						Element att = (Element) node;
						String interactionID = att.getAttribute("id");
						interaction.setInteractionID(interactionID);
						interaction.setInteractionType(att.getAttribute("interaction_type"));
						if (child.getNodeName().equals("InteractionComponentList")) {
							Element el = (Element) child;							
							NodeList comp_list = el.getElementsByTagName("InteractionComponent");
							if (comp_list != null & comp_list.getLength() > 0) {
								//System.out.println("Dinasarapu\t"+att.getAttribute("id")+"\t"+comp_list.getLength());
								List<InteractionComponent> interactionComponents = new ArrayList<InteractionComponent>();
								//Map<String, InteractionComponent> interactionComponents = new HashMap<String, InteractionComponent>();
								addInteractionComponents(comp_list, interactionComponents);
								interaction.setInteractionComponentList(interactionComponents);
								map.put(interactionID, interaction);
							}
						}
					}					
					child = child.getNextSibling();
				}
			}
		}
		interaction_list = map;
	}
	
	private void addInteractionComponents(NodeList nodeList,
			List<InteractionComponent> interactionComponents) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			InteractionComponent component = new InteractionComponent();
			if (childNode.getNodeName().equals("InteractionComponent")) {
				Element att2 = (Element) childNode;
				String moleculeID = att2.getAttribute("molecule_idref");
				String componentRole = att2.getAttribute("role_type");
				//System.out.println("Ashok\t"+moleculeID+"\t"+componentRole);
				List<ComponentLebel> interactionCompomentLebels = new ArrayList<ComponentLebel>();
				component.setComponentRole(componentRole);
				component.setMoleculeID(moleculeID);
				//component.setInteractionCompomentLebel(interactionCompomentLebels);
				addInteractionComponentRole(childNode.getChildNodes(),component, interactionCompomentLebels);
				interactionComponents.add(component);
			}
			/*NodeList children = childNode.getChildNodes();
			if (children != null) {
				doNodeRecursionWithAll(children, path_way, idref_list);
			}*/
		}
	}

	private void addInteractionComponentRole(NodeList nodeList,
			InteractionComponent component,List<ComponentLebel> list) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			ComponentLebel clabel = new ComponentLebel();
			if (childNode.getNodeName().equals("Label")) {
				Element att = (Element) childNode;
				String labelType = att.getAttribute("label_type");
				clabel.setLabelType(labelType);
				String labelValue = att.getAttribute("value");
				clabel.setLabelValue(labelValue);	
				list.add(clabel);
				//System.out.println("reddy\t"+labelType+"\t"+labelValue);
			}
			component.setInteractionCompomentLebels(list);
		}
	}


	public void setMolecules(NodeList nl_molecules) {
		Map<String, Molecule> map = new HashMap<String, Molecule>();
		System.out.println("Molecules total = " + nl_molecules.getLength());
		for (int i = 0; i < nl_molecules.getLength(); i++) {
			Molecule molecule = new Molecule(); // protein, complex, rna, compound
			Node node = nl_molecules.item(i); // Molecule
			if (node.getNodeType() != Node.TEXT_NODE) {
				Node child = node.getFirstChild();
				while (child != null) {
					if (node.getNodeName().equals("Molecule")) {
						Element att = (Element) node;
						//System.out.println(att.getAttribute("id")+"\t"+att.getAttribute("molecule_type"));
						molecule.setMoleculeType(att.getAttribute("molecule_type"));
						molecule.setMoleculeID(att.getAttribute("id"));
						// removes redundant name_type, i.e synonym values 
						Map<String,MoleculeAnnotation> moleculeName = new HashMap<String,MoleculeAnnotation>();
						//NodeList name_list = att.getChildNodes();
						NodeList name_list = node.getChildNodes();
						//NodeList name_list = att.getElementsByTagName("Name");
						addMoleculeDetails(name_list, molecule, moleculeName);
						molecule.setMoleculeAnnotation(moleculeName);
						map.put(att.getAttribute("id"), molecule);
					}
					child = child.getNextSibling();
				}
			}
		}
		moleculeMap = map;
	}

	private void addComponentDetails(NodeList nodeList, Pathway path_way,
			List<String> idref_list) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			if (childNode.getNodeName().equals("PathwayComponent")) {
				Element att2 = (Element) childNode;
				String idref = att2.getAttribute("interaction_idref");
				idref_list.add(idref);
			}
			/*NodeList children = childNode.getChildNodes();
			if (children != null) {
				doNodeRecursionWithAll(children, path_way, idref_list);
			}*/
		}
	}
	private void addMoleculeDetails(NodeList nodeList, Molecule molecule,
			Map<String, MoleculeAnnotation> map) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			if (childNode.getNodeType() != Node.TEXT_NODE) {
				String idref = null;
				MoleculeAnnotation mname = new MoleculeAnnotation();
				if (childNode.getNodeName().equals("Name")) {
					Element att2 = (Element) childNode;
					idref = att2.getAttribute("name_type");
					mname.setNameType(idref);
					String longNameType = att2.getAttribute("long_name_type");
					mname.setLongNameType(longNameType);
					String value = att2.getAttribute("value");
					mname.setValue(value);
				}
				if (childNode.getNodeName().equals("FamilyMemberList")) {
					List<String> fm = new ArrayList<String>();
					Node gchild = childNode.getFirstChild();
					if (gchild != null) {
						Element att3 = (Element) childNode;
						NodeList name_list3 = att3.getChildNodes();
						addFamilyMembers(name_list3, fm);
						molecule.setFamilyMembers(fm);
						gchild = gchild.getNextSibling();
					}
				}

				if (childNode.getNodeName().equals("ComplexComponentList")) {
					Node gchild = childNode.getFirstChild();
					if (gchild != null) {
						Element att3 = (Element) childNode;
						NodeList name_list3 = att3.getChildNodes();
						Map<String, ComplexComponent> ccMap = new HashMap<String, ComplexComponent>();
						addComplexMembers(name_list3, ccMap);
						molecule.setComplexMembers(ccMap);
						gchild = gchild.getNextSibling();
					}
				}

				if (idref != null)
					map.put(idref, mname);
			}
		}
	}
	
	private void addComplexMembers(NodeList nodeList,
			Map<String, ComplexComponent> ccMap) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			if (childNode.getNodeName().equals("ComplexComponent")) {
				Element att2 = (Element) childNode;
				ComplexComponent cc = new ComplexComponent();
				String idref = att2.getAttribute("molecule_idref");
				cc.setComplexComponentID(idref);
				addComplexLabels(childNode.getChildNodes(), cc);
				ccMap.put(idref, cc);
			}
		}
	}
	
	private void addComplexLabels(NodeList nodeList, ComplexComponent cc) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			if (childNode.getNodeName().equals("Label")) {
				Element att2 = (Element) childNode;
				String idref1 = att2.getAttribute("label_type");
				String idref2 = att2.getAttribute("value");
				cc.setLabelType(idref1);
				cc.setLabelValue(idref2);
			}
		}
	}

	private void addFamilyMembers(NodeList nodeList, List<String> fm) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			if (childNode.getNodeName().equals("Member")) {
				Element att2 = (Element) childNode;
				String idref = att2.getAttribute("member_molecule_idref");
				fm.add(idref);
			}
			/* recursive
			 * NodeList children = childNode.getChildNodes(); 
			 * if (children != null){
			 * 	addFamilyMembers(children, path_way, idref_list); 
			 * }
			 */
		}
	}

	public static Map<String, Molecule> getMolecules() {
		return moleculeMap;
	}

	public static Map<String, Interaction> getInteractions() {
		return interaction_list;
	}

	public static List<Pathway> getPathways() {
		return path_list;
	}
}
