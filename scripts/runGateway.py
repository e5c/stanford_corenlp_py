from py4j.java_gateway import JavaGateway

"""
Example Python interface to the CoreNLP Annotator pipeline using py4j 

"""

gateway = JavaGateway()

app = gateway.entry_point # Initialize an instance of the NLPEntryPoint class
annotator = gateway.entry_point.getPipelineAnnotator()

# Arguments are input type ("file","text") and path of input file or String to process
# Returns an array of Java LinkedHashMap objects 
sents_ents = annotator.get_sentiments_entities("file", "../texts/bizet_02_08_2015.txt")

sents = sents_ents[0]
ents = sents_ents[1]

outpath = "/Users/elizachang/projects/stanford_corenlp_py/texts/"

out1 = outpath + "bizet_02_08_2015_sents.txt"
out2 = outpath + "bizet_02_08_2015_entities.txt"

# Writes the sentiment analysis and recognized entities (person, locatiion, time, etc.) 
# as contained in the LinkedHashMap's to the specified output paths
app.write_map_to_file(sents, out1)
app.write_map_to_file(ents, out2)