package it.polito.nexa.pc;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import it.polito.nexa.pc.importers.DefaultJSONImporter;
import it.polito.nexa.pc.triplifiers.PropStructLabelsTriplifier;
import it.polito.nexa.pc.triplifiers.PublicContractsTriplifier;
import it.polito.nexa.pc.triplifiers.SPCDataTriplifier;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by giuseppe on 19/05/15.
 */
public class TriplesGenerator {

    public static void main(String[] args) throws FileNotFoundException {

        DefaultJSONImporter dji = new DefaultJSONImporter();

        // Generate labels of proposing structures;
        String psJson = dji.getJSON("src/main/resources/proposingStructures.json", "FILE");
        PropStructLabelsTriplifier pslt = new PropStructLabelsTriplifier();
        RDFforProposingStructureLabels(pslt, psJson, createBaseModel());

    }

    private static Model createBaseModel(){
        Model result = ModelFactory.createDefaultModel();
        Map<String, String> prefixMap = new HashMap<String, String>();

        prefixMap.put("rdfs", RDFS.getURI());
        prefixMap.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
        prefixMap.put("schema", "http://schema.org/");
        prefixMap.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        prefixMap.put("gn", "http://www.geonames.org/ontology#");
        prefixMap.put("rdf", RDF.getURI());
        prefixMap.put("dcterms", DCTerms.getURI());

        result.setNsPrefixes(prefixMap);

        return result;
    }

    private static void publishRDF(String filePath, Model model) throws FileNotFoundException {
        File file = new File(filePath.replaceAll("(.+)/[^/]+", "$1"));
        file.mkdirs();
        OutputStream outTurtle = new FileOutputStream(new File(filePath));
        RDFDataMgr.write(outTurtle, model, RDFFormat.NTRIPLES);
    }

    private static void RDFforProposingStructureLabels(PropStructLabelsTriplifier pslt, String inputJson, Model model) throws FileNotFoundException {
        List<Statement> statements = pslt.triplifyJSON(inputJson);
        model.add(statements);
        publishRDF("output/proposing-strctures-label.nt", model);
    }
}
