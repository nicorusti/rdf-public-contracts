package it.polito.nexa.pc;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;

import java.util.List;

/**
 * Created by giuseppe on 19/05/15.
 */
public interface TriplesAdder {

    public Model addTriples(Model model, List<Statement> statementList);

}
