package it.polito.nexa.pc;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;

import java.util.List;

/**
 * Created by giuseppe on 26/02/15.
 */
public class DefaultTriplesAdder implements TriplesAdder {

    @Override
    public Model addTriples(Model model, List<Statement> statementList) {
        Model result = ModelFactory.createDefaultModel();
        result.add(statementList);
        return result;
    }
}
