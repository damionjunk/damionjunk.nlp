# damionjunk.nlp

This library is a launching point for playing around with the [Stanford Core NLP](http://nlp.stanford.edu/software/corenlp.shtml) package in Clojure.
Not all of the NLP functionality of the Stanford library is being passed though, but more will be added over time.

## Dependency Information

```
[damionjunk/nlp  "0.1.0"]
```

This library is using Stanford's CoreNLP version `3.5.2`, which *requires* the Java `1.8` runtime.
See the [CoreNLP History](http://nlp.stanford.edu/software/corenlp.shtml#history) for more details.
Version `3.4.1` of the CoreNLP was the last to support Java `1.6` and `1.7`.

## Code Examples


```clojure
(require '[damionjunk.nlp.stanford :as nlp])

(nlp/sentiment-maps "Hi there. I really hated that movie. Just kidding, I loved it!")

;; => ({:sentiment 2, :text "Hi there."}
;;     {:sentiment 1, :text "I really hated that movie."}
;;     {:sentiment 3, :text "Just kidding, I loved it!"})

```


## License

Copyright © 2015 Damion Junk

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.