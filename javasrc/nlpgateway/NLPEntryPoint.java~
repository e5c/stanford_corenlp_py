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
import py4j.GatewayServer;

public class NLPEntryPoint {

	private PipelineAnnotator pipelineAnnotator;

	public NLPEntryPoint() {
		pipelineAnnotator = new PipelineAnnotator();
	}

	public PipelineAnnotator getPipelineAnnotator() {
		return pipelineAnnotator;
	}

	public static void main(String[] args) {
		GatewayServer gatewayServer = new GatewayServer(new NLPEntryPoint());
		gatewayServer.start();
		System.out.println("Gateway Server Started");

	}
}