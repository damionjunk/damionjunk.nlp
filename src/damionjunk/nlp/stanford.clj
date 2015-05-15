(ns damionjunk.nlp.stanford
  (:import [java.util Properties]
           [edu.stanford.nlp.pipeline Annotation StanfordCoreNLP]
           [edu.stanford.nlp.sentiment SentimentCoreAnnotations$SentimentAnnotatedTree]
           [edu.stanford.nlp.neural.rnn RNNCoreAnnotations]
           [edu.stanford.nlp.ling CoreAnnotations$SentencesAnnotation
                                  CoreAnnotations$TextAnnotation]))

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

(defn- annotate
  "Runs the provided text through the pre-configured CoreNLP pipeline.

   Returns an edu.stanford.nlp.pipeline.Annotation object."
  [pipeline-kw text]
  (.process (or (pipeline-kw @pipelines)
                (pipeline-kw (swap! pipelines assoc pipeline-kw (initialize-pipeline (pipeline-kw pipeline-annotators)))))
            text))

(defn- sentiment-text-mapper [sen-ann]
  (let [tree (.get sen-ann SentimentCoreAnnotations$SentimentAnnotatedTree)]
    {:sentiment (RNNCoreAnnotations/getPredictedClass tree)
     :text      (.get sen-ann CoreAnnotations$TextAnnotation)
     }))


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

(def ^{:doc "Return a sequence of sentiment-maps, one for each sentence in `text`.
             Each sentiment map contains a `:sentiment` key, and a `:text` key.
             Sentiment is [0, 4], from very negative to very positive.

             `text` is plain text. If it is a paragraph of sentences, they are
                    split by the Stanford parser."
       :arglists '([text])}
  sentiment-maps (comp sentiment (partial annotate :sentiment)))