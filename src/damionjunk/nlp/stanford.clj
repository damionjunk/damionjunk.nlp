(ns damionjunk.nlp.stanford
  (:import [java.util Properties]
           [edu.stanford.nlp.pipeline Annotation StanfordCoreNLP]
           [edu.stanford.nlp.sentiment SentimentCoreAnnotations$SentimentAnnotatedTree]
           [edu.stanford.nlp.neural.rnn RNNCoreAnnotations]
           [edu.stanford.nlp.ling CoreAnnotations$SentencesAnnotation
                                  CoreAnnotations$TextAnnotation
                                  CoreAnnotations$NamedEntityTagAnnotation
                                  CoreAnnotations$PartOfSpeechAnnotation
                                  CoreAnnotations$TokensAnnotation]))

;; An atom to hold initialized pipelines for re-use.
(defonce pipelines (atom {}))

(defn- initialize-pipeline
  "Creates a StanfordCoreNLP object, which is required to run text through the
   pipeline properties that are passed in."
  [pipeline-properties]
  (let [p (Properties.)]
    (.put p "annotators" pipeline-properties)
    (StanfordCoreNLP. p true)))

;; Some pre-defined likely pipelines. There are other annotators available,
;; see: http://nlp.stanford.edu/software/corenlp.shtml
;;      (table about 1/3 of the way down)
(def pipeline-annotators {:sentiment "tokenize, ssplit, parse, sentiment"
                          :pos       "tokenize, ssplit, pos"
                          :pos-ner   "tokenize, ssplit, pos, lemma, ner"
                          :all       "tokenize, ssplit, pos, lemma, ner, parse, sentiment"})

(defn drop-pipeline
  "Removes a CoreNLP pipeline from the `pipelines` atom. Function is here for
   visibility, but you could just do this directly.

   Valid `pipeline-kw`s: :sentiment, :pos, :pos-ner, :all"
  [pipeline-kw]
  (swap! pipelines dissoc pipeline-kw))

(defn- annotate
  "Runs the provided text through the pre-configured CoreNLP pipeline.

   Returns an edu.stanford.nlp.pipeline.Annotation object."
  [pipeline-kw text]
  (.process (or (pipeline-kw @pipelines)
                (pipeline-kw (swap! pipelines assoc pipeline-kw (initialize-pipeline (pipeline-kw pipeline-annotators)))))
            text))

(defn- pos-ner-token-mapper [tok-ann]
  {:pos   (.get tok-ann CoreAnnotations$PartOfSpeechAnnotation)
   :ner   (.get tok-ann CoreAnnotations$NamedEntityTagAnnotation)
   :token (.get tok-ann CoreAnnotations$TextAnnotation)})

(defn- sentence-map
  [sentence-a tok-m-fn]
  (map tok-m-fn (.get sentence-a CoreAnnotations$TokensAnnotation)))

;;
;;
;;

(defn- sentiment-text-mapper [sen-ann]
  (let [tree (.get sen-ann SentimentCoreAnnotations$SentimentAnnotatedTree)]
    {:sentiment (RNNCoreAnnotations/getPredictedClass tree)
     :text      (.get sen-ann CoreAnnotations$TextAnnotation)
     }))

(defn- sentiment-ner-mapper [sen-ann]
  (let [tree (.get sen-ann SentimentCoreAnnotations$SentimentAnnotatedTree)]
    {:sentiment (RNNCoreAnnotations/getPredictedClass tree)
     :text      (.get sen-ann CoreAnnotations$TextAnnotation)
     :tokens    (sentence-map sen-ann pos-ner-token-mapper)}))

(defn- pos-ner-mapper [sen-ann]
  {:text   (.get sen-ann CoreAnnotations$TextAnnotation)
   :tokens (sentence-map sen-ann pos-ner-token-mapper)})

;;
;;
;;

(defn pos-ner
  [^Annotation doc]
  (map pos-ner-mapper (.get doc CoreAnnotations$SentencesAnnotation)))

(defn sentiment-ner
  "A sequence of maps is returned containing the `:sentiment` for each
   sentence in the document, along with a sequence of `:tokens` from that
   sentence representing the POS and NER detection."
  [^Annotation doc]
  (map sentiment-ner-mapper (.get doc CoreAnnotations$SentencesAnnotation)))

(defn sentiment
  "A sequence of maps is returned containing the `:sentiment` score from 1 to 5
   and the `:text` that was analyzed.

   0 - very negative
   1 - negative
   2 - neutral
   3 - positive
   4 - very positive"
  [^Annotation doc]
  (map sentiment-text-mapper (.get doc CoreAnnotations$SentencesAnnotation)))

;;
;;
;;

(def ^{:doc "Return a sequence of sentiment-maps, one for each sentence in `text`.
             Each sentiment map contains a `:sentiment` key, and a `:text` key.

             `sentiment` is [0, 4], from very negative to very positive.

             `text` is plain text. If it is a paragraph of sentences, they are
                    split by the Stanford parser."
       :arglists '([text])}
  sentiment-maps (comp sentiment (partial annotate :sentiment)))

(def ^{:doc "Return a sequence of sentiment-ner-maps, one for each sentence in `text`.
             Each sentiment map contains a `:sentiment` key, a ':tokens` key,
             and a `:text` key.

             `sentiment` is [0, 4], from very negative to very positive.

             `tokens` is a sequence of tokens with POS and NER markup from each
                      bit of `text`. A token looks like:

                      {:pos \"NNP\", :ner \"ORGANIZATION\", :token \"Google\"}

             `text` is plain text. If it is a paragraph of sentences, they are
                    split by the Stanford parser."
       :arglists '([text])}
  sentiment-ner-maps (comp sentiment-ner (partial annotate :all)))

(def ^{:doc "Return a sequence of pos-ner-maps, one for each sentence in `text`.
             Each sentiment map contains a ':tokens` key, and a `:text` key.

             `tokens` is a sequence of tokens with POS and NER markup from each
                      bit of `text`. A token looks like:

                      {:pos \"NNP\", :ner \"ORGANIZATION\", :token \"Google\"}

             `text` is plain text. If it is a paragraph of sentences, they are
                    split by the Stanford parser."
       :arglists '([text])}
  pos-ner-maps (comp pos-ner (partial annotate :pos-ner)))