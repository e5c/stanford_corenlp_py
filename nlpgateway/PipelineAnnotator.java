package nlpgateway;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ie.*;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.sentiment.*;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PipelineAnnotator {

	private StanfordCoreNLP pipeline;
	private Properties props = new Properties();

	// Default constructor: notably includes NER, sentiment, entitymentions
	public PipelineAnnotator() {
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,ner,sentiment,entitymentions");
		pipeline = new StanfordCoreNLP(props);
	}

	// Custom constructor: annotatorStr must be a comma-separated list of annotators
	public PipelineAnnotator(String annotatorStr) {
		props.setProperty("annotators", annotatorStr);
		pipeline = new StanfordCoreNLP(props);
	}

	// Returns the Annotation object after the pipeline has annotated the text input of type inputType
	public Annotation annotateText(String inputType, String input) throws IOException {
		//Dummy initialization
		String text = "Maria is the best opera singer I have ever seen! However, she peaked at an early age.";

		if (inputType.equals("file")) {
			// To do: put in try/catch block
			text = new String(Files.readAllBytes(Paths.get(input))); //, StandardCharsets.UTF_8);
		}
		else if (inputType.equals("text")) {
			text = input;
		}
		else { 
			System.out.println("First argument to process_text() must be 'file' or 'text'");
			// To do: insert proper Exception handling (stylistic with py4j?)
		}

		Annotation document = new Annotation(text);
		this.pipeline.annotate(document);
		return document;
	}

	public LinkedHashMap<CoreMap,String> get_sentiments(Annotation document) {
		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
		LinkedHashMap<CoreMap,String> sentenceSentimentMap = new LinkedHashMap<CoreMap,String>();
//		String[] sentimentInfo = new String[2];
		String sentimentInfo = "";

		for(CoreMap sentence: sentences) {
			//sentenceSentimentMap.put(sentence, sentence.get(SentimentCoreAnnotations.ClassName.class));
			Tree sentimentTree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
			if (sentimentTree != null) {
				int sentiment = RNNCoreAnnotations.getPredictedClass(sentimentTree);
				String sentimentString = Integer.toString(sentiment);
				String sentimentClass = sentence.get(SentimentCoreAnnotations.ClassName.class);
				sentimentInfo = sentimentString + "," + sentimentClass;
//				sentimentInfo[0] = sentimentString;
//				sentimentInfo[1] = sentimentClass;
			}
			sentenceSentimentMap.put(sentence, sentimentInfo);
		}

		return sentenceSentimentMap;
	}

	public LinkedHashMap<CoreMap,String> get_entities(Annotation document) {
		List<CoreMap> entityMentions = document.get(CoreAnnotations.MentionsAnnotation.class);

		LinkedHashMap<CoreMap,String> entityTypeMap = new LinkedHashMap<CoreMap,String>();

		for(CoreMap entity: entityMentions) {
			String type = entity.get(CoreAnnotations.NamedEntityTagAnnotation.class);
			entityTypeMap.put(entity, type);
		}

		return entityTypeMap;
	}

	public LinkedHashMap<CoreMap,String>[] get_sentiments_entities(String inputType, String input) throws IOException {
		Annotation document = annotateText(inputType, input);
		LinkedHashMap<CoreMap,String> sents = get_sentiments(document);
		LinkedHashMap<CoreMap,String> ents = get_entities(document);
		LinkedHashMap[] sents_ents = new LinkedHashMap[]{sents,ents};
		return sents_ents;
	}

	public static void write_map_to_file(LinkedHashMap map, String outputFile) throws IOException {
		PrintWriter writer = new PrintWriter(outputFile);

		Set entrySet = map.entrySet();
		Iterator it = entrySet.iterator();

		while (it.hasNext()) {
			writer.println(it.next());
		}
		writer.close();
	}
}
