# RDF L. 190 Public Contracts
This project allows you to create an RDF representation of Italian public contracts, in compliance with the Transparency Act, L.190/2012.

## Module Usage
rdf-public-contracts is a module of the software pipeline to generate RDF data of the Italian public contracts. To work with this module you must use the intermediate JSON files generated by the [ANAC-converter module](https://github.com/nicorusti/ANAC-converter) developed by @nicorusti from the original XML files published by [ANAC (Autorità Nazionale Anticorruzione)](http://www.anticorruzione.it/portal/public/classic/). 

For "triplifying" the JSON data you can follow the steps below:
* Set the directory of input JSON files.
* Define an iterator to read all JSON files in the specified directory.
* Create a ```PublicContractsTriplifier``` object for building the RDF file and mapping the keys of the JSON files on standard ontologies.

A detailed usage of code is available below.

``` java

File dir = new File("../download");
Collection files = FileUtils.listFiles(dir, new RegexFileFilter("([^\\s]+(\\.(?i)(json))$)"), DirectoryFileFilter.DIRECTORY);
System.out.println(files.size());
Iterator itr = files.iterator();

long startTime = System.currentTimeMillis();
long endTime = 0;

int processedFiles = 0;

PublicContractsTriplifier pcTriplifier = new PublicContractsTriplifier();
Model pcModel = createBaseModel();

while (itr.hasNext()) {
    String value = itr.next().toString();
    Path path = Paths.get(value);
    String fileName = path.getFileName().toString();
    if(!fileName.equals("stats.json") && !fileName.equals("proposingStructure.json") && !fileName.equals("downloadStats.json") && !fileName.contains("_index")){
        String pcJson = dji.getJSON(value, "FILE");
        List<Statement> pcStatements = pcTriplifier.triplifyJSON(pcJson);
        pcModel.add(pcStatements);
        processedFiles += 1;
        if (processedFiles %100 == 0) {
            System.out.println("Processed " + processedFiles +" files");
        }
        if (processedFiles %20000 == 0) {
            System.out.println("Publish RDF!");
            publishRDF("output/rdf_" + processedFiles + ".nt", pcModel);
            pcModel = ModelFactory.createDefaultModel();
        }
    }
}
publishRDF("output/rdf.nt", pcModel);

```

In this repository other "triplifiers" are available. For instance with ```SPCDataTriplifier``` you can create the sameAs triples with SPCData RDFs.

