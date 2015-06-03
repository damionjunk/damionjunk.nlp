# damionjunk.nlp

This library is a launching point for playing around with the [Stanford Core NLP](http://nlp.stanford.edu/software/corenlp.shtml) package in Clojure.
As of version 0.3.0, [CMU's ark-tweet-nlp](http://www.ark.cs.cmu.edu/TweetNLP/) was added.

## Implemented Features

CoreNLP has a lot of functionality that I don't use, and didn't implement.
I'm primarily interested in the parts of speech, sentiment, and named entity annotations.
I'll add other functionality as the need arises, or a request is made.

POS tagging with CMU's [ark-tweet-nlp](https://github.com/brendano/ark-tweet-nlp/) is implemented.
Models can be substituted with an explicit call to `(damionjunk.nlp.cmu-ark/initialize :model "modelname")`.

## Dependency Information

For convenience, this library is available on Clojars:

[![Clojars Project](http://clojars.org/damionjunk/nlp/latest-version.svg)]

```
[damionjunk/nlp  "0.2.0"]
```

This library is using Stanford's CoreNLP version `3.5.2`, which *requires* the Java `1.8` runtime.
See the [CoreNLP History](http://nlp.stanford.edu/software/corenlp.shtml#history) for more details.
Version `3.4.1` of the CoreNLP was the last to support Java `1.6` and `1.7`.
Carnegie Mellon's Ark Tweet NLP library is at version `0.3.2`.

## Code Examples

### CMU ark-tweet-nlp

Parts of speech with CMU's Ark Tweet:

```clojure
(require '[damionjunk.nlp.cmu-ark :as ark])

(ark/tag "ikr? u r my best friend. :) LOL amirite? #funzone")
;;=>
({:token "ikr", :tag "!"} {:token "?", :tag ","} {:token "u", :tag "O"}
 {:token "r", :tag "V"} {:token "my", :tag "D"} {:token "best", :tag "A"}
 {:token "friend", :tag "N"} {:token ".", :tag ","} {:token ":)", :tag "E"}
 {:token "LOL", :tag "!"} {:token "amirite", :tag "!"} {:token "?", :tag ","}
 {:token "#funzone", :tag "#"})
```

### CoreNLP Sentiment Annotator

```clojure
(require '[damionjunk.nlp.stanford :as nlp])

(nlp/sentiment-maps "Hi there. I really hated that movie. Just kidding, I loved it!")

;; => ({:sentiment 2, :text "Hi there."}
;;     {:sentiment 1, :text "I really hated that movie."}
;;     {:sentiment 3, :text "Just kidding, I loved it!"})

```

### CoreNLP Sentiment, POS, and NER

```clojure
(require '[damionjunk.nlp.stanford :as nlp])

(nlp/sentiment-ner-maps "Here, let me Google that for you.")

;; => ({:sentiment 1,
;;      :text "Here, let me Google that for you.",
;;      :tokens
;;        ({:pos "RB", :ner "O", :token "Here"}
;;         {:pos ",", :ner "O", :token ","}
;;         {:pos "VB", :ner "O", :token "let"}
;;         {:pos "PRP", :ner "O", :token "me"}
;;         {:pos "NNP", :ner "ORGANIZATION", :token "Google"}
;;         {:pos "IN", :ner "O", :token "that"}
;;         {:pos "IN", :ner "O", :token "for"}
;;         {:pos "PRP", :ner "O", :token "you"}
;;         {:pos ".", :ner "O", :token "."})})
```

## Pipelines and Memory

CoreNLP loads a lot of stuff into memory, and has to do a bit of startup initialization.
This library stores the built up pipeline in an `atom` for reuse.
The atom (`damionjunk.nlp.stanford/pipelines`) is a keyword to CoreNLP (java object) mapping.

A different pipeline is built for each type of annotation that takes place, because
not all annotators are needed for each type of annotation.
For example, if you are only interested in sentiment analysis, there is no need to load the extra models for CoreNLP execution.

You will see this on STDERR when making the first call to `sentiment-ner-maps`:

```
Adding annotator tokenize
Adding annotator ssplit
Adding annotator pos
Reading POS tagger model from edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger ... done [1.7 sec].
Adding annotator lemma
Adding annotator ner
Loading classifier from edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz ... done [3.4 sec].
Loading classifier from edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz ... done [1.9 sec].
Loading classifier from edu/stanford/nlp/models/ner/english.conll.4class.distsim.crf.ser.gz ... done [3.5 sec].
Initializing JollyDayHoliday for SUTime from classpath: edu/stanford/nlp/models/sutime/jollyday/Holidays_sutime.xml as sutime.binder.1.
Reading TokensRegex rules from edu/stanford/nlp/models/sutime/defs.sutime.txt
Reading TokensRegex rules from edu/stanford/nlp/models/sutime/english.sutime.txt
Reading TokensRegex rules from edu/stanford/nlp/models/sutime/english.holidays.sutime.txt
Adding annotator parse
Adding annotator sentiment
```

This is dumped to STDERR when making the first call to `sentiment-maps`:

```
Adding annotator tokenize
TokenizerAnnotator: No tokenizer type provided. Defaulting to PTBTokenizer.
Adding annotator ssplit
Adding annotator parse
Loading parser from serialized file edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz ... done [0.4 sec].
Adding annotator sentiment
```

Subsequent calls do not reload the models and use the constructed pipeline from the atom.
As you can see, there are a different number of models loaded depending on which annotations you need.
If you call a different function after the models are loaded, a new pipeline is constructed, but the models do not have to be reloaded.

```clojure
(nlp/pos-ner-maps "What part of speech is this?")

;Adding annotator tokenize
;Adding annotator ssplit
;Adding annotator pos
;Adding annotator lemma
;Adding annotator ner
```

## License

Copyright © 2015 Damion Junk

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.