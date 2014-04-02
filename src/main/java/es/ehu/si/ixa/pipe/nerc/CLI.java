/*
 * Copyright 2014 Rodrigo Agerri

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package es.ehu.si.ixa.pipe.nerc;

import ixa.kaflib.KAFDocument;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.TrainingParameters;

import org.apache.commons.io.FilenameUtils;
import org.jdom2.JDOMException;

import es.ehu.si.ixa.pipe.nerc.eval.Evaluate;
import es.ehu.si.ixa.pipe.nerc.train.BaselineNameFinderTrainer;
import es.ehu.si.ixa.pipe.nerc.train.Dict3NameFinderTrainer;
import es.ehu.si.ixa.pipe.nerc.train.DictCoNLLNameFinderTrainer;
import es.ehu.si.ixa.pipe.nerc.train.DictLbjNameFinderTrainer;
import es.ehu.si.ixa.pipe.nerc.train.InputOutputUtils;
import es.ehu.si.ixa.pipe.nerc.train.NameFinderTrainer;

/**
 * 
 *
 * @author ragerri
 * @version 1.0
 *
 */

public class CLI {

  /**
   *
   * @param args
   * @throws IOException
   * @throws JDOMException
   */
  
  
  public static void main(String[] args) throws IOException, JDOMException {

    Namespace parsedArguments = null;
    ArgumentParser parser = ArgumentParsers
        .newArgumentParser("ixa-pipe-nerc-1.0.jar")
        .description(
            "ixa-pipe-nerc-1.0 is a multilingual NERC module developed by IXA NLP Group.\n");
    Subparsers subparsers = parser.addSubparsers().help("sub-command help");
    
    ////////////////////////
    //// Annotation CLI ////
    ////////////////////////
    
    Subparser annotateParser = subparsers.addParser("tag").help("Tagging CLI");
    annotateParser
        .addArgument("-l", "--lang")
        .choices("en", "es")
        .required(false)
        .help(
            "Choose a language to perform annotation with ixa-pipe-nerc");
    annotateParser.addArgument("-m","--model")
        .choices("baseline","dict3","dictlbj")
        .required(false)
        .help("Choose model to perform NERC annotation");
    annotateParser.addArgument("--beamsize")
        .setDefault(3)
        .type(Integer.class)
        .help("Choose beam size for decoding: 1 is faster and amounts to greedy search");
    annotateParser.addArgument("-g","--gazetteers")
        .choices("tag","post")
        .required(false).help("Use gazetteers directly for tagging or " +
    		"for post-processing the probabilistic NERC output.\n");
    
    //////////////////////
    //// Training CLI ////
    //////////////////////
    
    Subparser trainParser = subparsers.addParser("train").help("Training CLI");
    trainParser.addArgument("--decoding")
        .setDefault(3)
        .type(Integer.class)
        .help("Choose beam size for decoding: 1 is faster and amounts to greedy search");
    trainParser.addArgument("-f","--features")
        .choices("baseline","dict3","dict4","dictlbj").required(true).help("Train NERC models");
    trainParser.addArgument("-p", "--params").required(true)
        .help("load the parameters file");
    trainParser.addArgument("-i", "--input").required(true)
        .help("Input training set");
    trainParser.addArgument("-e", "--evalSet").required(true)
        .help("Input testset for evaluation");
    trainParser.addArgument("-d", "--devSet").required(false)
        .help("Input development set for cross-evaluation");
    trainParser.addArgument("-o", "--output").required(false)
        .help("choose output file to save the annotation");
    trainParser.addArgument("-c","--corpus")
        .setDefault("opennlp")
        .choices("conll","opennlp")
        .help("choose format input of corpus");
    
    ////////////////////////
    //// Evaluation CLI ////
    ////////////////////////
    
    Subparser evalParser = subparsers.addParser("eval").help("Evaluation CLI");
    evalParser.addArgument("--inputModel")
        .choices("baseline","dict3","dictlbj")
        .required(true)
        .help("Choose model to evaluate");
    evalParser.addArgument("--beam")
        .setDefault(3)
        .type(Integer.class)
        .help("Choose beam size for evaluation: 1 is faster and amounts to greedy search");
    evalParser.addArgument("-t","--testSet")
        .required(true)
        .help("Input testset for evaluation");
     evalParser.addArgument("-l","--language")
        .required(true)
        .choices("en","es")
        .help("Choose language to load model for evaluation");
     evalParser.addArgument("--evalReport")
        .required(false)
        .choices("brief","detailed","error")
        .help("choose type of evaluation report; defaults to detailed");
     evalParser.addArgument("-c","--corpus")
        .setDefault("opennlp")
        .choices("conll","opennlp")
        .help("choose format input of corpus");
    
    try {
      parsedArguments = parser.parseArgs(args);
      System.err.println("CLI options: " + parsedArguments);
    } catch (ArgumentParserException e) {
      parser.handleError(e);
      System.out
          .println("Run java -jar target/ixa-pipe-nerc-1.0.jar (tag|train|eval) -help for details");
      System.exit(1);
    }
    
    try {
      
      //////////////////
      //// Training ////
      //////////////////
      
      if (parsedArguments.get("features") != null) {
    	  
        NameFinderTrainer nercTrainer = null;
        int decoding = parsedArguments.getInt("decoding");
        String trainFile = parsedArguments.getString("input");
        String testFile = parsedArguments.getString("evalSet");
        String devFile = parsedArguments.getString("devSet");
        String outModel = null;
        String corpusFormat = parsedArguments.getString("corpus");
        // load training parameters file
        String paramFile = parsedArguments.getString("params");
        TrainingParameters params = InputOutputUtils.loadTrainingParameters(paramFile);
        String lang = params.getSettings().get("Language");
        String evalParam = params.getSettings().get("CrossEval");
        String[] evalRange = evalParam.split("[ :-]");
        
        if (parsedArguments.get("output") != null) {
          outModel = parsedArguments.getString("output");
        } else {
          outModel = FilenameUtils.removeExtension(trainFile) + "-"
              + parsedArguments.getString("features").toString() + "-model" + ".bin";
        }
        
        if (parsedArguments.getString("features").equalsIgnoreCase("baseline")) {
            nercTrainer = new BaselineNameFinderTrainer(trainFile, testFile, lang, decoding, corpusFormat);
        }
        else if (parsedArguments.getString("features").equalsIgnoreCase("dict3")) {
          nercTrainer = new Dict3NameFinderTrainer(trainFile,testFile,lang, decoding,corpusFormat);
        }
        else if (parsedArguments.getString("features").equalsIgnoreCase("dict4")) {
          nercTrainer = new DictCoNLLNameFinderTrainer(trainFile,testFile,lang,decoding,corpusFormat);
        }
        else if (parsedArguments.getString("features").equalsIgnoreCase("dictlbj")) {
          nercTrainer = new DictLbjNameFinderTrainer(trainFile,testFile,lang, decoding,corpusFormat);
        }
            
        TokenNameFinderModel trainedModel = null;
          if (evalRange.length==2) {
            if (parsedArguments.get("devSet") == null) {
              InputOutputUtils.devSetException();
            } else {
              trainedModel = nercTrainer.trainCrossEval(trainFile, devFile, params, evalRange);
              }
            } else {
              trainedModel = nercTrainer.train(params);
            }
            InputOutputUtils.saveModel(trainedModel, outModel);
            System.out.println();
            System.out.println("Wrote trained NERC model to " + outModel);
      }
      
      ////////////////////
      //// Evaluation ////
      ////////////////////
      
      else if (parsedArguments.get("inputModel") != null) {
        String testFile = parsedArguments.getString("testSet");
        String model = parsedArguments.getString("inputModel");
        String lang = parsedArguments.getString("language");
        int beam = parsedArguments.getInt("beam");
        String corpusFormat = parsedArguments.getString("corpus");
        
        Evaluate evaluator = new Evaluate(testFile,model,lang,beam,corpusFormat);
        if (parsedArguments.getString("evalReport")!= null) {
          if (parsedArguments.getString("evalReport").equalsIgnoreCase("brief")) {
            evaluator.evaluate();
          }
          else if (parsedArguments.getString("evalReport").equalsIgnoreCase("error")) {
            evaluator.evalError();
          }
          else if (parsedArguments.getString("evalReport").equalsIgnoreCase("detailed")) {
            evaluator.detailEvaluate();
          }
        }
        else {
         evaluator.detailEvaluate();
        }
      }
    
    ////////////////////
    //// Annotation ////
    ////////////////////
    else {
      int beamsize = parsedArguments.getInt("beamsize");
      String gazetteer = parsedArguments.getString("gazetteers");
      String model; 
      if (parsedArguments.get("model") == null) {
        model = "baseline";
      }
      else {
        model = parsedArguments.getString("model");
      }
      BufferedReader breader = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
      BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(System.out, "UTF-8"));
      // read KAF document from inputstream
      KAFDocument kaf = KAFDocument.createFromStream(breader);
      // language parameter
      String lang;
      if (parsedArguments.get("lang") == null) {
	    lang = kaf.getLang();
      }
      else {
	    lang =  parsedArguments.getString("lang");
      }
      if (parsedArguments.get("gazetteers") != null) {
        Annotate annotator = new Annotate(lang,gazetteer,model,beamsize);
        annotator.annotateNEsToKAF(kaf);
      }
      else { 
        Annotate annotator = new Annotate(lang,model,beamsize);
        annotator.annotateNEsToKAF(kaf);
      }
      kaf.addLinguisticProcessor("entities","ixa-pipe-nerc-"+lang, "1.0");
      bwriter.write(kaf.toString());
      bwriter.close();
      breader.close();
     }
    }
      catch (IOException e) {
      e.printStackTrace();
    }

  }
}