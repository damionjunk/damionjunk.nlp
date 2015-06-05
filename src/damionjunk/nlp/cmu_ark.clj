(ns damionjunk.nlp.cmu-ark
  (:import [cmu.arktweetnlp Tagger]))

;; This is the model that ships with CMU Ark-Tweet
(def default-model "/cmu/arktweetnlp/model.20120919")

(defonce tagger (atom nil))

(defn initalize
  "Initializes the ArkTweet tagger. With no parameters, the default model
   will be used."
  [& {:keys [model] :or {model default-model}}]
  (reset! tagger (Tagger.))
  (.loadModel @tagger model))

(defn tag
  "Given the input `text`, return a sequence of maps of the form:

   {:token \"sometoken\" :pos \"T\"}

   For a list of tags, see:
   https://github.com/brendano/ark-tweet-nlp/blob/master/docs/annot_guidelines.pdf

   Quick Ref:
    Nominal:
    N – common noun
    O – pronoun (personal/WH; not possessive) ˆ – proper noun
    S – nominal + possessive
    Z – proper noun + possessive

    Other open-class words:
    V – verb incl. copula, auxiliaries A – adjective
    R – adverb
    ! – interjection

    Other closed-class words:
    D – determiner
    P – pre- or postposition, or subordinating con- junction
    & – coordinating conjunction
    T – verb particle
    X – existential there, predeterminers

    Twitter/online-specific:
    # – hashtag (indicates topic/category for tweet) @ – at-mention (indicates another user as a re- cipient of a tweet)
    ~ – discourse marker, indications of continua- tion of a message across multiple tweets
    U – URL or email address
    E – emoticon

    Miscellaneous:
    $ – numeral
    , – punctuation
    G – other abbreviations, foreign words, posses- sive endings, symbols, garbage

    Other compounds:
    L – nominal + verbal (e.g. i’m), verbal + nominal (let’s, lemme)
    M – proper noun + verbal
    Y – X + verbal"
  [text]
  (when (nil? @tagger) (initalize))
  (map (fn [tt] {:token (.token tt) :pos (.tag tt)}) (.tokenizeAndTag @tagger text)))


