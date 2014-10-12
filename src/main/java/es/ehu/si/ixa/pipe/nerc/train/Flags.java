package es.ehu.si.ixa.pipe.nerc.train;

import opennlp.tools.util.TrainingParameters;
import es.ehu.si.ixa.pipe.nerc.CLI;
import es.ehu.si.ixa.pipe.nerc.features.XMLFeatureDescriptor;

public class Flags {

  private Flags() {
    
  }
  
  public static String getLanguage(TrainingParameters params) {
    String lang = null;
    if (params.getSettings().get("Language") == null) {
      langException();
    } else {
      lang = params.getSettings().get("Language");
    }
    return lang;
  }

  public static String getDataSet(String dataset, TrainingParameters params) {
    String trainSet = null;
    if (params.getSettings().get(dataset) == null) {
      datasetException();
    } else {
      trainSet = params.getSettings().get(dataset);
    }
    return trainSet;
  }

  public static String getDictOption(TrainingParameters params) {
    String dictOption = null;
    if (params.getSettings().get("DirectDictionaryTagging") != null) {
      dictOption = params.getSettings().get("DirectDictionaryTagging");
    } else {
      dictOption = CLI.DEFAULT_DICT_OPTION;
    }
    return dictOption;
  }

  public static String getModel(TrainingParameters params) {
    String model = null;
    if (params.getSettings().get("OutputModel") == null) {
      modelException();
    } else if (params.getSettings().get("OutputModel") != null
        && params.getSettings().get("OutputModel").length() == 0) {
      modelException();
    } else {
      model = params.getSettings().get("OutputModel");
    }
    return model;
  }

  public static String getCorpusFormat(TrainingParameters params) {
    String corpusFormat = null;
    if (params.getSettings().get("CorpusFormat") == null) {
      corpusFormatException();
    } else {
      corpusFormat = params.getSettings().get("CorpusFormat");
    }
    return corpusFormat;
  }

  public static String getOutputFormat(TrainingParameters params) {
    String outFormatOption = null;
    if (params.getSettings().get("OutputFormat") != null) {
      outFormatOption = params.getSettings().get("OutputFormat");
    } else {
      outFormatOption = CLI.DEFAULT_OUTPUT_FORMAT;
    }
    return outFormatOption;
  }

  public static Integer getBeamsize(TrainingParameters params) {
    Integer beamsize = null;
    if (params.getSettings().get("Beamsize") == null) {
      beamsize = CLI.DEFAULT_BEAM_SIZE;
    } else {
      beamsize = Integer.parseInt(params.getSettings().get("Beamsize"));
    }
    return beamsize;
  }
  
  public static String getSequenceCodec(TrainingParameters params) {
    String seqCodec = null;
    if (params.getSettings().get("SequenceCodec") == null) {
      seqCodec = CLI.DEFAULT_SEQUENCE_CODEC;
    } else {
      seqCodec = params.getSettings().get("SequenceCodec");
    }
    return seqCodec;
  }

  public static String getWindow(TrainingParameters params) {
    String windowFlag = null;
    if (params.getSettings().get("Window") == null) {
      windowFlag = XMLFeatureDescriptor.DEFAULT_WINDOW;
    } else {
      windowFlag = params.getSettings().get("Window");
    }
    return windowFlag;
  }

  public static String getTokenFeatures(TrainingParameters params) {
    String tokenFlag = null;
    if (params.getSettings().get("TokenFeatures") != null) {
      tokenFlag = params.getSettings().get("TokenFeatures");
    } else {
      tokenFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return tokenFlag;
  }

  public static String getTokenClassFeatures(TrainingParameters params) {
    String tokenClassFlag = null;
    if (params.getSettings().get("TokenClassFeatures") != null) {
      tokenClassFlag = params.getSettings().get("TokenClassFeatures");
    } else {
      tokenClassFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return tokenClassFlag;
  }
  
  public static String getTokenPatternFeatures(TrainingParameters params) {
    String tokenClassFlag = null;
    if (params.getSettings().get("TokenPatternFeatures") != null) {
      tokenClassFlag = params.getSettings().get("TokenPatternFeatures");
    } else {
      tokenClassFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return tokenClassFlag;
  }

  public static String getOutcomePriorFeatures(TrainingParameters params) {
    String outcomePriorFlag = null;
    if (params.getSettings().get("OutcomePriorFeatures") != null) {
      outcomePriorFlag = params.getSettings().get("OutcomePriorFeatures");
    } else {
      outcomePriorFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return outcomePriorFlag;
  }

  public static String getPreviousMapFeatures(TrainingParameters params) {
    String previousMapFlag = null;
    if (params.getSettings().get("PreviousMapFeatures") != null) {
      previousMapFlag = params.getSettings().get("PreviousMapFeatures");
    } else {
      previousMapFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return previousMapFlag;
  }

  public static String getSentenceFeatures(TrainingParameters params) {
    String sentenceFlag = null;
    if (params.getSettings().get("SentenceFeatures") != null) {
      sentenceFlag = params.getSettings().get("SentenceFeatures");
    } else {
      sentenceFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return sentenceFlag;
  }

  public static String getPreffixFeatures(TrainingParameters params) {
    String prefixFlag = null;
    if (params.getSettings().get("PrefixFeatures") != null) {
      prefixFlag = params.getSettings().get("PrefixFeatures");
    } else {
      prefixFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return prefixFlag;
  }

  public static String getSuffixFeatures(TrainingParameters params) {
    String suffixFlag = null;
    if (params.getSettings().get("SuffixFeatures") != null) {
      suffixFlag = params.getSettings().get("SuffixFeatures");
    } else {
      suffixFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return suffixFlag;
  }

  public static String getBigramClassFeatures(TrainingParameters params) {
    String bigramClassFlag = null;
    if (params.getSettings().get("BigramClassFeatures") != null) {
      bigramClassFlag = params.getSettings().get("BigramClassFeatures");
    } else {
      bigramClassFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return bigramClassFlag;
  }

  public static String getTrigramClassFeatures(TrainingParameters params) {
    String trigramClassFlag = null;
    if (params.getSettings().get("TrigramClassFeatures") != null) {
      trigramClassFlag = params.getSettings().get("TrigramClassFeatures");
    } else {
      trigramClassFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return trigramClassFlag;
  }

  public static String getFourgramClassFeatures(TrainingParameters params) {
    String fourgramClassFlag = null;
    if (params.getSettings().get("FourgramClassFeatures") != null) {
      fourgramClassFlag = params.getSettings().get("FourgramClassFeatures");
    } else {
      fourgramClassFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return fourgramClassFlag;
  }

  public static String getFivegramClassFeatures(TrainingParameters params) {
    String fivegramClassFlag = null;
    if (params.getSettings().get("FivegramClassFeatures") != null) {
      fivegramClassFlag = params.getSettings().get("FivegramClassFeatures");
    } else {
      fivegramClassFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return fivegramClassFlag;
  }

  public static String getCharNgramFeatures(TrainingParameters params) {
    String charNgramFlag = null;
    if (params.getSettings().get("CharNgramFeatures") != null) {
      charNgramFlag = params.getSettings().get("CharNgramFeatures");
    } else {
      charNgramFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return charNgramFlag;
  }

  public static String getCharNgramFeaturesRange(TrainingParameters params) {
    String charNgramRangeFlag = null;
    if (params.getSettings().get("CharNgramFeaturesRange") != null) {
      charNgramRangeFlag = params.getSettings().get("CharNgramFeaturesRange");
    } else {
      charNgramRangeFlag = XMLFeatureDescriptor.CHAR_NGRAM_RANGE;
    }
    return charNgramRangeFlag;
  }

  public static String getDictionaryFeatures(TrainingParameters params) {
    String dictionaryFlag = null;
    if (params.getSettings().get("DictionaryFeatures") != null) {
      dictionaryFlag = params.getSettings().get("DictionaryFeatures");
    } else {
      dictionaryFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return dictionaryFlag;
  }

  public static String getClarkFeatures(TrainingParameters params) {
    String distSimFlag = null;
    if (params.getSettings().get("ClarkClusterFeatures") != null) {
      distSimFlag = params.getSettings().get("ClarkClusterFeatures");
    } else {
      distSimFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return distSimFlag;
  }

  public static String getWord2VecClusterFeatures(TrainingParameters params) {
    String word2vecFlag = null;
    if (params.getSettings().get("Word2VecClusterFeatures") != null) {
      word2vecFlag = params.getSettings().get("Word2VecClusterFeatures");
    } else {
      word2vecFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return word2vecFlag;
  }

  public static String getBrownFeatures(TrainingParameters params) {
    String brownFlag = null;
    if (params.getSettings().get("BrownClusterFeatures") != null) {
      brownFlag = params.getSettings().get("BrownClusterFeatures");
    } else {
      brownFlag = XMLFeatureDescriptor.DEFAULT_FEATURE_FLAG;
    }
    return brownFlag;
  }

  public static void devSetException() {
    System.err
        .println("UseDevSet options in the parameters file if CrossEval is activated!");
    System.exit(1);
  }

  public static void modelException() {
    System.err
        .println("Please provide a model in the OutputModel field in the parameters file!");
    System.exit(1);
  }

  public static void langException() {
    System.err
        .println("Please fill in the Language field in the parameters file!");
    System.exit(1);
  }

  public static void datasetException() {
    System.err
        .println("Please specify your training/testing sets in the TrainSet and TestSet fields in the parameters file!");
    System.exit(1);
  }

  public static void corpusFormatException() {
    System.err
        .println("Please fill in CorpusFormat field in the parameters file!");
    System.exit(1);
  }

  public static void dictionaryException() {
    System.err
        .println("You need to specify the DictionaryPath in the parameters file to use the DictionaryFeatures!");
    System.exit(1);
  }

  public static void dictionaryFeaturesException() {
    System.err
        .println("You need to specify the DictionaryFeatures in the parameters file to use the DictionaryPath!");
    System.exit(1);
  }

  public static void distsimException() {
    System.err
        .println("You need to specify the DistSimPath in the parameters file to use the DistSimFeatures!");
    System.exit(1);
  }

  public static void distsimFeaturesException() {
    System.err
        .println("You need to specify the DistSimFeatures in the parameters file to use the DistSimPath!");
    System.exit(1);
  }

}
