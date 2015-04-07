from py4j.java_gateway import JavaGateway

"""
This is an example Python script that interfaces with the CoreNLP Annotator pipeline
JVM using py4j. The two functionalities (i.e. annotators) this lets you call are the 
Sentiment annotator and the Named Entities annotator.

The first example demos on how to get and write to file just the sentiments 
(same procedure goes for just the named entities). The second example demos getting 
both sentiments and entities in one function call. This retrieves both the (sentences,Sentiments) 
and (sentences,Named Entities) map using the get_sentiments_entities(inputType, input) 
method and writes both to file. 
"""

# These three statements allow Python to start communicating with the JVM
# that you started 
gateway = JavaGateway()
app = gateway.entry_point # Initialize an instance of the NLPEntryPoint class
annotator = gateway.entry_point.getPipelineAnnotator()

# --- Example of getting just the sentiments written to file
# in the format: {sentence}={score},{label} 
# score is in [1,5], 5 being most positive and 1 being most negative
# and label is "Very negative", "Negative", "Neutral","Positive","Very positive"
# (so they pretty much carry the same information)
path_prefix = "/Users/elizachang/projects/stanford_corenlp_py/texts"
input_path_1 = "{0}/{1}".format(path_prefix, "op.txt")
sent_path_1 = "{0}/{1}".format(path_prefix, "op_sentiments.txt")

# first argument must be either "file" or "text"
# if "file", 2nd arg is the path
# if "text", 2nd arg is the sentence to annotate
# returns an object of type Annotation
annotated_doc = annotator.annotateText("file", input_path_1)
sentiments = annotator.get_sentiments(annotated_doc)

app.write_map_to_file(sentiments, sent_path_1)

# --- Example of getting both sentiments and entities ---

# Arguments are input type ("file","text") and path of input file or String to process
# Returns an array of Java LinkedHashMap objects 
input_path_2 = "{0}/{1}".format(path_prefix, "bizet_02_08_2015.txt")
sents_ents = annotator.get_sentiments_entities("file", input_path_2)

sents = sents_ents[0]
ents = sents_ents[1]

out1 = "{0}/{1}".format(path_prefix, "bizet_02_08_2015_sentiments.txt")
out2 = "{0}/{1}".format(path_prefix, "bizet_02_08_2015_entities.txt")

# Writes the sentiment analysis and recognized entities (person, locatiion, time, etc.) 
# as contained in the LinkedHashMap's to the specified output paths
app.write_map_to_file(sents, out1)
app.write_map_to_file(ents, out2)
