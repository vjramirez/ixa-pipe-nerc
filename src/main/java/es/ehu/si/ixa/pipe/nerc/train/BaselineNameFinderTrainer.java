/*
 *  Copyright 2014 Rodrigo Agerri

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

package es.ehu.si.ixa.pipe.nerc.train;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.util.TrainingParameters;

import es.ehu.si.ixa.pipe.nerc.features.AdaptiveFeatureGenerator;
import es.ehu.si.ixa.pipe.nerc.features.BigramFeatureGenerator;
import es.ehu.si.ixa.pipe.nerc.features.CachedFeatureGenerator;
import es.ehu.si.ixa.pipe.nerc.features.CharacterNgramFeatureGenerator;
import es.ehu.si.ixa.pipe.nerc.features.OutcomePriorFeatureGenerator;
import es.ehu.si.ixa.pipe.nerc.features.Prefix34FeatureGenerator;
import es.ehu.si.ixa.pipe.nerc.features.PreviousMapFeatureGenerator;
import es.ehu.si.ixa.pipe.nerc.features.SentenceFeatureGenerator;
import es.ehu.si.ixa.pipe.nerc.features.SuffixFeatureGenerator;
import es.ehu.si.ixa.pipe.nerc.features.TokenClassFeatureGenerator;
import es.ehu.si.ixa.pipe.nerc.features.TokenFeatureGenerator;
import es.ehu.si.ixa.pipe.nerc.features.WindowFeatureGenerator;
import es.ehu.si.ixa.pipe.nerc.train.AbstractNameFinderTrainer;
import es.ehu.si.ixa.pipe.nerc.train.DefaultNameFinderTrainer;

/**
 * Training NER based on Apache OpenNLP Machine Learning API for English.
 * This class implements baseline shape features on top of the {@link DefaultNameFinderTrainer}
 * features for English CoNLL 2003.
 *
 * @author ragerri 2014/06/25
 * @version 2014-07-11
 */
public class BaselineNameFinderTrainer extends AbstractNameFinderTrainer {

  /**
   * Construct a Baseline trainer.
   * @param trainData the training data
   * @param testData the test data
   * @param lang the language
   * @param beamsize the beamsize for decoding
   * @param corpusFormat the corpus format
   * @param netypes the NE classes
   * @throws IOException the data exception
   */
  public BaselineNameFinderTrainer(final String trainData, final String testData, final TrainingParameters params)
      throws IOException {
    super(trainData, testData, params);
    String windowParam = params.getSettings().get("Window");
    String[] windowArray = windowParam.split("[ :-]");
    if (windowArray.length == 2) {
      int leftWindow = Integer.parseInt(windowArray[0]);
      int rightWindow = Integer.parseInt(windowArray[1]);
      setFeatures(createFeatureGenerator(leftWindow, rightWindow));
    }
    else {
      setFeatures(createDefaultFeatureGenerator());
    }
    
  }

  /**
   * Construct a baseline trainer with only beamsize specified.
   * @param beamsize the beamsize
   */
  public BaselineNameFinderTrainer(final int beamsize) {
    super(beamsize);
    setFeatures(createFeatureGenerator());
  }
  
  /* (non-Javadoc)
   * @see es.ehu.si.ixa.pipe.nerc.train.NameFinderTrainer#createFeatureGenerator()
   */
  public final AdaptiveFeatureGenerator createFeatureGenerator(int leftWindow, int rightWindow) {
    List<AdaptiveFeatureGenerator> featureList = createWindowFeatureList(leftWindow, rightWindow);
    addTokenFeatures(featureList);
    addCharNgramFeatures(featureList, MIN_CHAR_NGRAM_LENGTH, DEFAULT_CHAR_NGRAM_LENGTH);
    AdaptiveFeatureGenerator[] featuresArray = featureList
        .toArray(new AdaptiveFeatureGenerator[featureList.size()]);
    return new CachedFeatureGenerator(featuresArray);
  }

  /* (non-Javadoc)
   * @see es.ehu.si.ixa.pipe.nerc.train.NameFinderTrainer#createFeatureGenerator()
   */
  public final AdaptiveFeatureGenerator createDefaultFeatureGenerator() {
    List<AdaptiveFeatureGenerator> featureList = createWindowFeatureList(DEFAULT_WINDOW_SIZE, DEFAULT_WINDOW_SIZE);
    addTokenFeatures(featureList);
    addCharNgramFeatures(featureList, MIN_CHAR_NGRAM_LENGTH, DEFAULT_CHAR_NGRAM_LENGTH);
    AdaptiveFeatureGenerator[] featuresArray = featureList
        .toArray(new AdaptiveFeatureGenerator[featureList.size()]);
    return new CachedFeatureGenerator(featuresArray);
  }
  
  /**
   * Create a list of {@link AdaptiveFeatureGenerator} features.
   *
   * @return the list of features
   */
  public static List<AdaptiveFeatureGenerator> createWindowFeatureList(int leftWindow, int rightWindow) {
    List<AdaptiveFeatureGenerator> featuresList = new ArrayList<AdaptiveFeatureGenerator>(Arrays.asList(
        new WindowFeatureGenerator(new TokenFeatureGenerator(), leftWindow, rightWindow),
        new WindowFeatureGenerator(new TokenClassFeatureGenerator(true), leftWindow, rightWindow),
        new OutcomePriorFeatureGenerator(), new PreviousMapFeatureGenerator(),
        new BigramFeatureGenerator(), new SentenceFeatureGenerator(true,
            false)));
    return featuresList;
  }

  /**
   * Adds the Baseline features to the feature list.
   * @param featureList
   *          the feature list containing the baseline features
   */
  public static void addTokenFeatures(final List<AdaptiveFeatureGenerator> featureList) {
    featureList.add(new Prefix34FeatureGenerator());
    featureList.add(new SuffixFeatureGenerator());
  }
  
  //TODO
  //1. put window feature values in trainParams; read, perhaps put into properties, unify trainParams and Properties
  //2. For each feature loop over the values: for (i=1 i< 2; ++i) for (j=1 j<2 ++j) List<Integer> windowOptions i_i,j_i etc.
  //3. for windowOptions.size(), new WindowFeatureGenerator(windowOptions.get(0), windowOptions.get(1)
  //4. same for charNgrams
  //5. same for prefix suffix
  //6. combine in createFeatureGenerator but create list of those iterating over every list of each type feature
  //7. pass the list to the train method instead of of only one AdaptiveFeatureGenerator
  public static void addCharNgramFeatures(final List<AdaptiveFeatureGenerator> featureList, int minLength, int maxLength) {
    featureList.add(new CharacterNgramFeatureGenerator(minLength, maxLength));
  }

}
