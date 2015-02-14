package nlpgateway;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.sentiment.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PipelineAnnotator {

	private StanfordCoreNLP pipeline;
	private Properties props = new Properties();

	public PipelineAnnotator() {
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,sentiment");
		pipeline = new StanfordCoreNLP(props);
	}

	public LinkedHashMap<CoreMap,String> process_text(String inputType, String input) throws IOException {
		//Dummy initialization
		String text = "Maria is the best opera singer I have ever seen! However, she peaked at an early age.";

		if (inputType == "file") {
			// To do: put in try/catch block
			text = new String(Files.readAllBytes(Paths.get(input))); //, StandardCharsets.UTF_8);
		}
		else if (inputType == "text") {
			text = input;
		}
		else { 
			System.out.println("First argument to process_text() must be 'file' or 'text'");
			// To do: insert proper Exception handling (stylistic with py4j?)
		}

		Annotation document = new Annotation(text);
		this.pipeline.annotate(document);

		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
		
		LinkedHashMap<CoreMap,String> sentenceSentimentMap = new LinkedHashMap();

		for(CoreMap sentence: sentences) {
			sentenceSentimentMap.put(sentence, sentence.get(SentimentCoreAnnotations.ClassName.class));
		}

		return sentenceSentimentMap;
	}

}
