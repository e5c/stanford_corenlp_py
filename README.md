# stanford_corenlp_py

This provides a Python interface for calling the "sentiment" and "entitymentions" annotators of Stanford's CoreNLP Java package, current as of v. 3.5.1. It uses py4j to interact with the JVM; as such, in order to run a script like scripts/runGateway.py, you must first compile and run the Java classes creating the JVM gateway.

### Requirements:  
1. Download the CoreNLP jars here:
http://nlp.stanford.edu/software/stanford-corenlp-full-2015-01-29.zip

1. You can download py4j with `pip install py4j`. More info here:
http://py4j.sourceforge.net/

1. To start the JVM, run from this project's home directory:
```bash
$ javac -cp .:$PY4JJAR:$CORENLP/* nlpgateway/*.java
$ java -cp .:$PY4JJAR:$CORENLP/* nlpgateway/NLPEntryPoint
```
where you have set the bash environment variables `CORENLP` and `PY4JJAR` as locations of the CoreNLP jars and py4j jar, respectively.
E.g. in your .bash_profile:
```bash
export PY4JJAR=/System/Library/Frameworks/Python.framework/Versions/2.7/share/py4j/py4j0.8.2.1.jar
export CORENLP=/Users/elizachang/projects/corenlp
```
You may then start interacting with the Java objects and calling the methods in `PipelineAnnotator` and `NLPEntryPoint` in Python. 

### Running
Run/edit the example script provided:
```bash
python scripts/runGateway.py
```
