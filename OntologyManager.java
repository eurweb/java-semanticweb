import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.expression.ParserException;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLTypedConstant;

public class OntologyManager {

	protected OWLOntology myOntology = null;
	private OWLOntologyManager owlmanager;
	private Reasoner reasoner;
	private String ontologyURI;
	//private HashMap<String, OWLClass> mapClasses = new HashMap<String, OWLClass>();

	public OntologyManager(String ontologyURI)
			throws OWLOntologyCreationException {
		this.ontologyURI = ontologyURI;
		loadOntology();
		createReasoner();
	}

	// ///////////// Properties ////////////

	private Map<OWLDataPropertyExpression, Set<OWLConstant>> getDataProperties(
			String individualName) {
		OWLIndividual individual = getIndividual(individualName);
		if (individual == null) return null;
		Map<OWLDataPropertyExpression, Set<OWLConstant>> dataProperties = individual
				.getDataPropertyValues(myOntology);
		return dataProperties;
	}

	public LinkedList<String> getDataPropertiesKeys(String individualName) {
		
		if (individualName == null) return null;
		LinkedList<String> dataPropertiesKeys = new LinkedList<String>();
		Map<OWLDataPropertyExpression, Set<OWLConstant>> dataProperties = getDataProperties(individualName);
		if (dataProperties == null) return null;
		Set<OWLDataPropertyExpression> k = dataProperties.keySet();
		Iterator<OWLDataPropertyExpression> iterator = k.iterator();
		while (iterator.hasNext()) {
			dataPropertiesKeys.add(iterator.next().toString());
		}
		return dataPropertiesKeys;
	}

	public LinkedList<OWLPropertyValues> getAllDataProperties(String individualName) {
		LinkedList<OWLPropertyValues> dataProperties = new  LinkedList<OWLPropertyValues>();
		LinkedList<String> strDataValues = new LinkedList<String>();
		String type = null;
		Map<OWLDataPropertyExpression, Set<OWLConstant>> dataPropertiesMap = getDataProperties(individualName);
		Set<OWLDataPropertyExpression> keyset = dataPropertiesMap.keySet();
		for (OWLDataPropertyExpression oprop : keyset) {
			Set<OWLConstant> setc = dataPropertiesMap.get(oprop);
			Iterator<OWLConstant> iteratorOWLConstant = setc.iterator();
			while (iteratorOWLConstant.hasNext()) {
				OWLTypedConstant owlTypedConstant = iteratorOWLConstant.next().asOWLTypedConstant();
				strDataValues.add(owlTypedConstant.getLiteral());
				type = owlTypedConstant.getDataType().toString();
			}
			dataProperties.add(createOWLDataPropertyValues(strDataValues,type,individualName));
		}

		return dataProperties;
	}

	public OWLPropertyValues getDataProperty(String individualName, String propertyName) {

		LinkedList<String> dataValues = new LinkedList<String>();
		String type = null;
		Map<OWLDataPropertyExpression, Set<OWLConstant>> dataProperties = getDataProperties(individualName);
		if (dataProperties == null) return null;
		Set<OWLDataPropertyExpression> keyset = dataProperties.keySet();
		for (OWLDataPropertyExpression oprop : keyset) {
			if (oprop.toString().equals(propertyName)) {
				Set<OWLConstant> setc = dataProperties.get(oprop);
				Iterator<OWLConstant> iteratorOWLConstant = setc.iterator();
				while (iteratorOWLConstant.hasNext()) {
					OWLTypedConstant owlTypedConstant = iteratorOWLConstant
							.next().asOWLTypedConstant();
					dataValues.add(owlTypedConstant.getLiteral());
					type = owlTypedConstant.getDataType().toString();
				}
			}
		}

		return createOWLDataPropertyValues(dataValues,type,individualName);
	}
	
	private OWLPropertyValues createOWLDataPropertyValues(LinkedList<String> strDataValues,String type,String individualName )
	{
		
		if (type == null || strDataValues == null || individualName == null) return null;
		
		LinkedList<Object> listOWLDataPropertyValues = new LinkedList<Object>();
		OWLPropertyValues owlDataPropertyValues = new OWLPropertyValues();
		owlDataPropertyValues.setIndividualName(individualName);
		
		if (type.equals("boolean")) {
			LinkedList<Boolean> dataValues = new LinkedList<Boolean>();
			for (String s : strDataValues) {
				dataValues.add(Boolean.valueOf(s));
			}
			owlDataPropertyValues.setType(type);

			listOWLDataPropertyValues.addAll(dataValues);
			owlDataPropertyValues.setValues(listOWLDataPropertyValues);
		} else if (type.equals("int")) {
			LinkedList<Integer> dataValues = new LinkedList<Integer>();
			for (String s : strDataValues) {
				dataValues.add(Integer.valueOf(s));
			}
			owlDataPropertyValues.setType(type);
			listOWLDataPropertyValues.addAll(dataValues);
			owlDataPropertyValues.setValues(listOWLDataPropertyValues);
		}else if (type.equals("float")) {
			LinkedList<Float> dataValues = new LinkedList<Float>();
			for (String s : strDataValues) {
				dataValues.add(Float.valueOf(s));
			}
			owlDataPropertyValues.setType(type);
			listOWLDataPropertyValues.addAll(dataValues);
			owlDataPropertyValues.setValues(listOWLDataPropertyValues);
		}else if (type.equals("double")) {
			LinkedList<Double> dataValues = new LinkedList<Double>();
			for (String s : strDataValues) {
				dataValues.add(Double.valueOf(s));
			}
			owlDataPropertyValues.setType(type);
			listOWLDataPropertyValues.addAll(dataValues);
			owlDataPropertyValues.setValues(listOWLDataPropertyValues);
		} 
		else if (type.equals("string")) {
			LinkedList<String> dataValues = new LinkedList<String>();
			for (String s : strDataValues) {
				dataValues.add(s);
			}
			owlDataPropertyValues.setType(type);
			listOWLDataPropertyValues.addAll(dataValues);
			owlDataPropertyValues.setValues(listOWLDataPropertyValues);
		}
		return owlDataPropertyValues;
		
	}

	private Map<OWLObjectPropertyExpression, Set<OWLIndividual>> getObjectProperties(String individualName) {
		OWLIndividual individual = getIndividual(individualName);
		if (individual == null) return null;
		Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectProperties = individual
				.getObjectPropertyValues(myOntology);
		return objectProperties;
	}
	
	public LinkedList<String> getObjectPropertiesKeys(String individualName) {
		LinkedList<String> objectPropertiesKeys = new LinkedList<String>();
		Map<OWLDataPropertyExpression, Set<OWLConstant>> dataProperties = getDataProperties(individualName);
		if (dataProperties  == null) return null;
		Set<OWLDataPropertyExpression> k = dataProperties.keySet();
		Iterator<OWLDataPropertyExpression> iterator = k.iterator();
		while (iterator.hasNext()) {
			objectPropertiesKeys.add(iterator.next().toString());
		}
		return objectPropertiesKeys;
	}
	
	public OWLPropertyValues getObjectProperty(String individualName, String propertyName) {

		LinkedList<OWLIndividual> listIndividuals = new LinkedList<OWLIndividual>();
		Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectProperties = getObjectProperties(individualName);
		if (objectProperties == null) return null;
		Set<OWLObjectPropertyExpression> keyset = objectProperties.keySet();
		for (OWLObjectPropertyExpression oprop : keyset) {
			if (oprop.toString().equals(propertyName)) {
				Set<OWLIndividual> setc = objectProperties.get(oprop);
				Iterator<OWLIndividual> iteratorOWLIndividual = setc.iterator();
				while (iteratorOWLIndividual.hasNext()) {
					listIndividuals.add(iteratorOWLIndividual.next());
				}
			}
		}
		OWLPropertyValues owlPropertyValues = new OWLPropertyValues();
		owlPropertyValues.setIndividualName(individualName);
		owlPropertyValues.setType("OWLIndividual");
		LinkedList<Object> listOWLDataPropertyValues = new LinkedList<Object>();
		listOWLDataPropertyValues.addAll(listIndividuals);
		owlPropertyValues.setValues(listOWLDataPropertyValues);
		return owlPropertyValues;
	}
	
	

	/////////////// Individuals ////////////

	public LinkedList<String> getAllIndividuals() {
		LinkedList<String> allIndividuals = new LinkedList<String>();
		Set<OWLIndividual> individuals = myOntology.getReferencedIndividuals(); // reasoner.getIndividuals();
		for (OWLIndividual ind : individuals) {
			allIndividuals.add(ind.toString());
		}
		return allIndividuals;
	}

	public LinkedList<String> getIndividuals(String className) {
		LinkedList<String> listIndividualsForClass = new LinkedList<String>();
		OWLClass clazz = getClass(className);
		if (clazz == null) return null;
		Set<OWLIndividual> individuals = clazz.getIndividuals(myOntology);
		for (OWLIndividual ind : individuals) {
			listIndividualsForClass.add(ind.toString());
		}
		return listIndividualsForClass;
	}

	public OWLIndividual getIndividual(String individualName) {
		
		if (individualName == null) return null;
		Set<OWLIndividual> individuals = myOntology.getReferencedIndividuals();
		for (OWLIndividual individual : individuals) {
			if (individualName.equals(individual.toString())) return  individual; 
		}
//		OWLDataFactory factory = owlmanager.getOWLDataFactory();
//		String u = myOntology.getURI().toString() + "#" + strIndividual;
//		OWLIndividual individual = factory.getOWLIndividual(URI.create(u));
		return null;
	}

	// /////// Classes ////////////////////////////

//	private void setMapClasses() {
//		Set<OWLClass> classes = myOntology.getReferencedClasses(); // reasoner.getClasses();
//		for (OWLClass cl : classes) {
//			mapClasses.put(cl.toString(), cl);
//		}
//	}

	public LinkedList<String> getAllClasses() {

		LinkedList<String> listAllClasses = new LinkedList<String>();
		Set<OWLClass> classes = myOntology.getReferencedClasses();
		for (OWLClass cl : classes) {
			listAllClasses.add(cl.toString());
		}
		return listAllClasses;
	}

	public LinkedList<String> getSubClasses(String scls) {
		LinkedList<String> listSubClasses = new LinkedList<String>();
		OWLClass cls = getClass(scls);
		if (cls == null)
			return null;
		Set<OWLDescription> subClasses = cls.getSubClasses(myOntology);
		for (OWLDescription sc : subClasses) {
			listSubClasses.add(sc.toString());
		}
		return listSubClasses;
	}
	
	public OWLClass getClass(String className) {

		if (className == null) return null;
		Set<OWLClass> classes = myOntology.getReferencedClasses();
		for (OWLClass cl : classes) {
			if(className.equals(cl.toString()))
					return cl;
		}
		return null;
		//OWLClass cls = mapClasses.get(scls);
		// OWLDataFactory factory = owlmanager.getOWLDataFactory(); String u =
		//myOntology.getURI().toString()+ "#"+scls; OWLClass cls =factory.getOWLClass(URI.create(u));	
	}

	// / Util //////////////////

	public void printClassTree() {
		KnowledgeBase kb = reasoner.getKB();
		kb.printClassTree();
		// TaxonomyBuilder taxB = kb.getTaxonomyBuilder();
		// System.out.println(kb.isConsistent());
	}

	boolean explainInconsistency() throws ParserException, OWLException {
		if (reasoner.isConsistent()) {
			// System.out.println("It is  Consistent");
			return true;
		} else {
			// System.out.println("It is NOT Consistent");
			// explanation = reasoner.getExplanation();
			Set<OWLClass> inconsistentClasses = reasoner
					.getInconsistentClasses();
			for (OWLClass cl : inconsistentClasses) {
				System.out.println(cl.getClass().getName());
			}
			return false;
		}
	}

	private void createReasoner() {
		reasoner = new Reasoner(owlmanager);
		reasoner.loadOntology(myOntology);
	}

	private void loadOntology() throws OWLOntologyCreationException {

		owlmanager = OWLManager.createOWLOntologyManager();
		try {
			myOntology = owlmanager.loadOntology(URI.create("file:"
					+ ontologyURI));
			// OWLOntologyFormat owlFormat = owlmanager.getOntologyFormat(myOntology);
		} catch (OWLOntologyCreationException ex) {
			System.out.println("The ontology could not be created: "
					+ ex.getMessage());
			ex.printStackTrace();
		}
	}
}
