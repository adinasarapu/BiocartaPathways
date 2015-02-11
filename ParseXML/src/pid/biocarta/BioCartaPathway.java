package pid.biocarta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/*
 * author: Ashok R. Dinasarapu
 * University of Florida
 */
public class BioCartaPathway {
	// Actually Java supports 4 methods to parse XML out of the box
	// 1. DOM Parser/Builder 2. SAX Parser 3. StAx Reader/Writer and 4. JAXB
	// You don't need an external library for parsing XML in Java. Java has come
	// with built-in implementations for SAX and DOM for ages.
	// D:\NCI-Nature [xml file path]
	// http://www.java-samples.com/showtutorial.php?tutorialid=152
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// actory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("D:/NCI-Nature/BioCarta.xml");
			Document doc = builder.parse(file);
			doc.getDocumentElement().normalize();
			// System.out.println(doc.getTextContent());
			// Get a list of pathway elements
			// Get the rootElement from the DOM object.
			// From the root element get all pathway elements.
			// Iterate through each pathway element to load the data.
			parseDocument(doc);
			// System.out.print(elm.getLocalName());
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		}
	}

	private static void parseDocument(Document doc) {
		// get the root element
		Element docEle = doc.getDocumentElement();
		// get a nodelist of elements
		NodeList nl_onto = docEle.getElementsByTagName("Ontology");
		// Ontology
		// list: LabelType
		// name=("reversible","activity-state","molecule-type","edge-type",
		// "process-type" or "location"), id="1"
		// LabelValueList
		// list: LabelValue name="reversible" id="33" parent_idref="33"
		// GO="GO:0006309"
		NodeList nl_model = docEle.getElementsByTagName("Model");
		// Model
		// MoleculeList
		// list: Molecule molecule_type=c("protein",) id="100478"
		// list: Name name_type="LL" long_name_type="EntrezGene" value="8877"
		// InteractionList
		// list: Interaction interaction_type="degradation",
		// "modification","cell survival","protein ubiquitination" etc,
		// id="100478", Source id="2",
		// EvidenceList
		// list: Evidence value="NIL"
		// InteractionComponentList
		// list: InteractionComponent role_type="agent" molecule_idref="100735"
		//
		// PathwayList
		// list: Pathway id="100251" subnet="false"
		// Organism, LongName, ShortName, Source
		// PathwayComponentList
		// list: PathwayComponent interaction_idref="103753"
		parseModel(nl_model, doc);
	}
	
	//static List<Pathway> pathwayList = PathwayModel.getPathways();
	//static Map<String, Molecule> moleculeMap = PathwayModel.getMolecules();
	//static Map<String, Interaction> interactionMap = PathwayModel.getInteractions();

	private static void parseModel(NodeList nl_model, Document doc) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("D:/NCI-Nature/2015 0207 biocarta pathways.txt", "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (nl_model != null && nl_model.getLength() > 0) {
			// get the Model element
			Element el = (Element) nl_model.item(0);
			// get the Model object
			// System.out.println(el.getNodeName());
			PathwayModel e = new PathwayModel();
			e.setPathwayModel(el, doc, "BioCarta");

			List<Pathway> pathwayList = PathwayModel.getPathways();
			Map<String, Molecule> moleculeMap = PathwayModel.getMolecules();
			Map<String, Interaction> interactionMap = PathwayModel.getInteractions();

			Iterator<Pathway> path_it = pathwayList.iterator();

			while (path_it.hasNext()) {
				Pathway pathWay = path_it.next();
				String pathID = pathWay.getPathway_id();
				//if("100121".equals(pathID)){
				String org = pathWay.getOrganism();
				String pathName = pathWay.getShortName();
				List<String> pathComponentIDs = pathWay
						.getPathwayComponentList();
				Iterator<String> interaction_it = pathComponentIDs.iterator();
				System.out.println(pathID+"\t"+pathName);
				while (interaction_it.hasNext()) {
					String interaction_id = interaction_it.next();
					Interaction interaction = interactionMap
							.get(interaction_id);
					if (interaction != null) {
						List<InteractionComponent> icList = interaction.getInteractionComponentList();
						if (icList != null && icList.size() > 0) {
							Iterator<InteractionComponent> icIt = icList
									.iterator();
							System.out.println(pathID+"\t"+pathName+"\t"+interaction_id+"\t"+icList.size());
							while (icIt.hasNext()) {
								InteractionComponent inte_mol = icIt.next();
								String interactMoleculeID = inte_mol.getMoleculeID();
									Molecule molecule = moleculeMap.get(interactMoleculeID);
									if(molecule.getComplexMembers() == null && molecule.getFamilyMembers() == null){
									String parent_molecule = getMoleculeDetailsForFamilyOrComplexMember(molecule);
									if (parent_molecule != null){
										//writer.write(pathID + "\t" + pathName + "\t" + parent_molecule+"\n");
										System.out.println(pathID+"\t"+pathName+"\t"+parent_molecule);
									}
									}
									if (molecule.getComplexMembers() != null && molecule.getComplexMembers().size() > 0) {
										Map<String, ComplexComponent> mcc = molecule.getComplexMembers();
										Set<String> ccKeys = mcc.keySet();
										Iterator<String> ccIt = ccKeys.iterator();
										while (ccIt.hasNext()) {
											String complexMemberID = ccIt.next();
											ComplexComponent cc = mcc.get(complexMemberID);
											Molecule c_molecule = moleculeMap.get(cc.getComplexComponentID());
											String molType = c_molecule.getMoleculeType();
											String member_complex = getMoleculeDetailsForFamilyOrComplexMember(c_molecule);
											if ("protein".equals(molType) && member_complex != null){	
												//writer.write(pathID+"\t"+pathName+"\t"+member_complex+"\n");
												System.out.println(pathID+"\t"+pathName+"\t"+member_complex);
											} 
											if("complex".equals(molType)) {	
												Map<String, ComplexComponent> inn_mcc = c_molecule.getComplexMembers();
												Set<String> inn_ccKeys = inn_mcc.keySet();
												Iterator<String> inn_ccIt = inn_ccKeys.iterator();
												while(inn_ccIt.hasNext()){
													String inncomplexMemberID = inn_ccIt.next();
													ComplexComponent inn_cc = inn_mcc.get(inncomplexMemberID);													
													Molecule innc_molecule = moleculeMap.get(inn_cc.getComplexComponentID());
													String innmolType = innc_molecule.getMoleculeType();													
													if("protein".equals(innmolType)){
														String inn_member_complex = getMoleculeDetailsForFamilyOrComplexMember(innc_molecule);
														if (inn_member_complex != null){
														//writer.write(pathID+"\t"+pathName+"\t"+inn_member_complex+"\n");
															System.out.println(pathID+"\t"+pathName+"\t"+cc.getComplexComponentID()+"\t"+inncomplexMemberID+"\t"+inn_member_complex);
														}
													}
												}
											}
										}
									}
									if (molecule.getFamilyMembers() != null
											&& molecule.getFamilyMembers()
													.size() > 0) {
										Iterator<String> familyMemberID = molecule.getFamilyMembers().iterator();
										while (familyMemberID.hasNext()) {
											String member_id = familyMemberID
													.next().trim();
											Molecule fm_molecule = moleculeMap
													.get(member_id);
											
											String member_molecule = getMoleculeDetailsForFamilyOrComplexMember(fm_molecule);
											if (member_molecule != null){
												System.out.println(pathID+"\t"+pathName+"\t"+member_molecule);
											//	writer.write(pathID + "\t" + pathName	+ "\t"	+ member_molecule+"\n");
											}
										}
									}

								//}
							}
						}
					}
				}
			//} 
			}
		//	writer.close();
		}
	}

	private static String getMoleculeDetailsForFamilyOrComplexMember(Molecule molecule) {
		String member = null;
		
		if (molecule.getMoleculeType().equals("protein")) {
			Map<String, MoleculeAnnotation> intermapName = molecule
					.getMoleculeAnnotation();
		if (intermapName != null) {
				Set<String> mem_key = intermapName.keySet();
				Iterator<String> mem_it = mem_key.iterator();
				String entrez = null, symbol = null;

				while (mem_it.hasNext()) {
					String mem_key_val = mem_it.next();
					if ("LL".equals(mem_key_val))
						entrez = intermapName.get(mem_key_val).getValue();
					if ("OF".equals(mem_key_val))
						symbol = intermapName.get(mem_key_val).getValue();
					if (symbol != null && entrez != null) {
						member = entrez + "\t" + symbol;
					}
				}

			}
		}
		return member;
	}
}
