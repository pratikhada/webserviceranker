/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.ontologyDB;

import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.db.ModelRDB;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.ArrayList;

/**
 *
 * @author webservicerank
 */

public class MatchMakerImpl implements MatchMaker{

    private Model model, infModel;

    //All matched user properties/synonyms, 2-dimensional,
    //one dimension for storing the type (I,O,P,E, or G) of property,
    //Another dimension for representing different properties of the type
    //ArrayList for storing possible matched synonyms of a input property of certain type
    private ArrayList<RDFNode> properties[][];
    
    private Reasoner reasoner;
    private Selector selector;
    private StmtIterator stmtIterator;
    private Resource resource;
    private RDFNode rdfNode;

    //Enum representing different types of match between properties
    private enum relation {EXACT, PLUG_IN, UNCERTAIN, SUBSUME, NO_MATCH};

    //Represents whether the properties have priorities in the order of input
    private final boolean userPreference;

    //If the priority is to be considered, then the weights to be applied
    private final double preferences[] = {1.0, 0.9, 0.82, 0.74, 0.65};

    //Array to represent degree of match of a database property with each of input property
    private double degreesOfMatch[];

    //Array to represent the number of each type of input properties
    private int size[];

    //Namespaces of the databse ontology model
    private String[] namespaces;
    
    private IDBConnection connection = null;


    private void initiate(){
        selector = null;
        stmtIterator = null;
        resource = null;
        rdfNode = null;
        reasoner = ReasonerRegistry.getTransitiveReasoner();
        properties = new ArrayList[5][];
        size = new int[5];        
    }


    /**
     * Constructor that opens database model, initializes the uerpreference and namespaces
     * @param connection
     * @param dbModel
     * @param isPreference
     * @param namespaces
     */
    public MatchMakerImpl(IDBConnection connection, String dbModel, boolean isPreference
            , String[] namespaces){
        initiate();
        userPreference = isPreference;
        this.connection = connection;

        //Open the ontology model
        model = ModelRDB.open(this.connection, dbModel);

        this.namespaces = namespaces;

        infModel = ModelFactory.createInfModel(reasoner, model);
    }
    

    //initialize reasoner, userPreference and model
    MatchMakerImpl(Model persistent, String[] NSs) {
        namespaces = NSs;
        userPreference = false;
        model = persistent;
        initiate();
    }


    //initialize model and infModel
    @Override
    public void openModel(String url){
        model = ModelFactory.createDefaultModel();
        model.read(url);
        infModel = ModelFactory.createInfModel(reasoner, model);
    }

  
   
    //checks if the entity is in the model and if yes, returns RDFNode containing the entity
    private RDFNode getRDFNode(String entity){
        boolean contains = false;
        resource = null;
        String connector;
        for (int i=0; i<namespaces.length; i++){
            connector = "";
            if (!namespaces[i].endsWith("#"))
                connector = "#";
            resource = model.getResource(namespaces[i]+connector+entity);
            if (model.containsResource(resource)){
//                System.out.println("Found="+resource.toString());
                contains = true;
                break;
            }
        }
        if ( !contains )
            resource = null;
        return resource;
    }



    /**
     * checks if the user properties are contained in the ontology model.
     * If contains, prepares list of matched words in RDFNode
     * Initialized properties
     * @param property
     * @return, true if the ontology contains the properties (some or all)
     */
    
    @Override
    public boolean doesContainUserProperties(Properties property, int type){
        if (property == null)
            return false;
        boolean contains = false;
        size[type] = property.getSize();
        properties[type] = (ArrayList<RDFNode>[])new ArrayList[size[type]];
        for (int i=0; i<size[type]; i++){
            System.out.println("\n\nTurn of "+i);
            long start = System.nanoTime();
            properties[type][i] = new ArrayList<RDFNode>();
            for (int j =0; j<property.getSynonymSize(i); j++){
                rdfNode = getRDFNode(property.getPropertyName(i, j)); 
                if (rdfNode != null){
                    contains = true;
                    properties[type][i].add(rdfNode);
                    System.out.print(rdfNode+"\t");
                    rdfNode = null;
//                    break;
                }
            }
            System.out.println("\nTime="+(System.nanoTime()-start)/1000000+" ms");
        }
        for (int i=0; i<properties[type].length; i++){
            System.out.println("Here: "+properties[type][i]);
        }
        return contains;
    }
      
   
    //returns true if there exists a direct path between two nodes
    private boolean isDirectPath(Resource child, RDFNode parent){
        selector = null;
        stmtIterator = null;
        selector = new SimpleSelector(child, RDFS.subClassOf, parent);
        stmtIterator = model.listStatements(selector);
        return stmtIterator.hasNext();
    }


    //returns true if there exists a indirect path between two nodes
    private boolean isIndirectPath(Resource child, RDFNode parent){
        selector = null;
        stmtIterator = null;
        selector = new SimpleSelector(child, RDFS.subClassOf, parent);
        stmtIterator = infModel.listStatements(selector);
        return stmtIterator.hasNext();
    }


    //returns the minimum depth of a node (from root, i.e.,'Thing')
    private float computeMinDepth(RDFNode concept){ 
        float depth = -1;
        if (!model.containsResource(concept))
            return depth;
        Selector s = null;
        StmtIterator iterator = null;
        s = new SimpleSelector((Resource)concept, RDFS.subClassOf, (RDFNode)null);
        iterator = model.listStatements(s);
        float temp_depth;
        if (!iterator.hasNext()){ //If the node has no parents (i.e., child of 'Thing')
            depth = 1.0f;
        }else{
            while(iterator.hasNext()){
                temp_depth = 1 + computeMinDepth(iterator.nextStatement().getObject());
                if (depth == -1)
                    depth = temp_depth;
                else if (temp_depth < depth)
                    depth = temp_depth;
            }
        }
        return depth;
    }


    //returns semantic distance between two concepst.
    private float semanticDistance(RDFNode concept1, RDFNode concept2){
        if (concept1.equals(concept2)){
            return 0.0f;
        }
        else if ( concept1 instanceof Resource && isDirectPath((Resource)concept1, concept2)){
            return 1.0f;
        }else if(concept2 instanceof Resource && isDirectPath((Resource)concept2, concept1)){
            return 1.0f;
        }else if ( concept1 instanceof Resource && isIndirectPath((Resource)concept1, concept2)){
            Selector s = new SimpleSelector((Resource)concept1,RDFS.subClassOf,(Resource)null);
            StmtIterator iterator = model.listStatements(s);
            Statement stmt = null;
            float sd = -1.0f;
            float temp_sd;
            while(iterator.hasNext()){
                stmt = iterator.nextStatement();
                temp_sd = semanticDistance(stmt.getObject(), concept2);
                if (temp_sd != -1.0f)
                    if (sd == -1.0f)
                        sd = temp_sd +1;
                    else if(temp_sd + 1 < sd)
                        sd = temp_sd+1;
            }
            return sd;
        }else if ( concept2 instanceof Resource && isIndirectPath((Resource)concept2, concept1)){
            Selector s = new SimpleSelector((Resource)concept2,RDFS.subClassOf,(Resource)null);
            StmtIterator iterator = model.listStatements(s);
            Statement stmt = null;
            float sd = -1.0f;
            float temp_sd;
            while(iterator.hasNext()){
                stmt = iterator.nextStatement();
                temp_sd = semanticDistance(stmt.getObject(), concept1);
                if (temp_sd != -1.0f)
                    if (sd == -1.0f)
                        sd = temp_sd +1;
                    else if(temp_sd + 1 < sd)
                        sd = temp_sd+1;
            }
            return sd;
        }else if (concept1 instanceof Resource && concept2 instanceof Resource){
            Selector s = new SimpleSelector((Resource)concept1,RDFS.subClassOf,(RDFNode)null);
            StmtIterator si = model.listStatements(s);
            Selector s2 = new SimpleSelector((Resource)concept2,RDFS.subClassOf,(RDFNode)null);
            StmtIterator si2 = model.listStatements(s2);
            float sd = -1.0f;
            float temp_sd1, temp_sd2;
            if (!si.hasNext() && !si2.hasNext())
                sd = 2.0f;                    //semantic distance through 'Thing'
            else if (!si.hasNext()){
                temp_sd1 = computeMinDepth(concept2);
                if (temp_sd1 != -1.0f)
                    sd = 1+ temp_sd1;                
            }else if (!si2.hasNext()){
                temp_sd1 = computeMinDepth(concept1);
                if (temp_sd1 != -1.0f)
                    sd = 1+ temp_sd1;
            }else{
                resource = (Resource) concept2;
                s = new SimpleSelector((Resource)concept1,RDFS.subClassOf,(RDFNode)null){
                    @Override
                    public boolean selects(Statement s){
                        StmtIterator it = infModel.listStatements(resource,RDFS.subClassOf, s.getObject());
                        return it.hasNext();
                    }
                };
                si = infModel.listStatements(s);
                while (si.hasNext()){
                    rdfNode = si.nextStatement().getObject();
                    temp_sd1 = semanticDistance(concept1, rdfNode);
                    if (temp_sd1 != -1.0f){
                        temp_sd2 = semanticDistance(concept2, rdfNode);
                        if(temp_sd2 != -1.0f){
                            if (sd == -1.0f)
                                sd = temp_sd1 + temp_sd2;
                            else if (temp_sd1 + temp_sd2 < sd)
                                sd = temp_sd1 + temp_sd2;
                        }
                    }
                }
            }
            return sd;
        }else
            return -1.0f; //Indicates semantic distance is impossible, i.e., infinite.
    }

    

    /**
     * identifies the type of match and returns corresponding
     * degree of match between two nodes
     * @param query, Ontology concept of the user input property
     * @param ad, Ontology concept of the advertised property
     * @return
     */
    
    private double degreeOfMatch(RDFNode query, RDFNode ad){

//        System.out.print("\n\nComparison of:"+query+" :: "+ad+" ");
        relation match;

        //Find the type of match between concepts

        //check for exact match
        if (query.equals(ad) || model.contains((Resource)query, RDFS.subClassOf, ad))
            match = relation.EXACT;

        //check for plug-in match
        else if (infModel.contains((Resource)query, RDFS.subClassOf, ad))
            match = relation.PLUG_IN;

        //check for subsume match
        else if (infModel.contains((Resource)ad, RDFS.subClassOf, query))
            match = relation.SUBSUME;

        //check for uncertain match
        else if (ad == null)
            match = relation.UNCERTAIN;

        //no-match
        else match = relation.NO_MATCH;


        //Addign the degree of match based on the type of match

        double dom = -1.0;
        float sd;

        switch (match){
            case EXACT:
                dom = 1.0;                          //sd = 0 or 1
                break;
            case PLUG_IN:
                sd = Math.abs(semanticDistance(query, ad));
                if (sd != -1.0)                     //check if semantic distance is infinite
                    dom = 0.5 + 1/Math.exp(sd - 1); //sd >=2
                break;
            case UNCERTAIN:
                dom = 0.5;
                break;
            case SUBSUME:
                sd = Math.abs(semanticDistance(query, ad));
                if (sd != -1.0)                     //check if semantic distance is infinite
                    dom = 1/(2*Math.exp(sd - 1));   //sd >=1
                break;
            case NO_MATCH:
                dom = 0;
                break;
            default:
        }
//        System.out.println(" match type = "+match+" dom="+dom);
        return dom;

    }



    /**
     * A method that computes the degree of match of the database project with
     * each of the input properties of the given property type pType
     * @param property
     * @param db_property
     * @return
     */

    @Override
    public double[] degreeOfMatch(Properties property, String db_property, int pType, boolean[] areMatched){
//               long start = System.nanoTime();
        //initialize the degree of match
        degreesOfMatch = new double[size[pType]];
        for (int i=0; i<degreesOfMatch.length; i++)
            degreesOfMatch[i] = 0.0;

        //Multiplication factor
        double multiplier = 1.0f;


        //try to matchmake semantically

        RDFNode rnode = (Resource) getRDFNode(db_property);

        if (rnode != null){
            //The property is found in ontology model
            double dom = 0, temp_dom;
            for (int i=0; i<properties[pType].length; i++){
                //Compute degree of match between two concepts
                dom = 0;
                for (int j=0; j<properties[pType][i].size(); j++){
                    temp_dom = degreeOfMatch(rnode, properties[pType][i].get(j));
                    if (temp_dom > dom)
                        dom = temp_dom;
                }
                //consider user preferences
                if (userPreference){
                    if ( i<5)
                        multiplier = preferences[i];
                    else multiplier = 1*(i+1)/((i+0.01)*2);
                }
                dom *= multiplier;
                if (dom != 0){
                    areMatched[0] = true;
                }
                if (dom > degreesOfMatch[i]){
                    degreesOfMatch[i] = dom;
                    property.setIsMatchFound(i, true);
                }
            }
        }
//         System.out.println("Time for matchmaking ="+(System.nanoTime() - start)/1000000+" ms");
        return degreesOfMatch;
        
    }

    

    @Override
    public void closePersistentOntModel(){
        if (infModel != null)
            infModel.close();
        infModel = null;
        if (model != null)
            model.close();
        model = null;
        properties = null;
    }

    /**
     * Returns true if the ontology model 'model' contains the property
     * @param property, the property to be checked
     * @return, true if model contains property
     */
    @Override
    public boolean containsProperty(String property) {
        return getRDFNode(property) == null ? false : true;
    }

}
