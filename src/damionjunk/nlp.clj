(ns damionjunk.nlp
  (:require [damionjunk.nlp.stanford :as nlp]))

;; Nothing here but some examples for now.

(comment


  (nlp/sentiment-maps "I'm not sure if I like the movie. I'm quite certain I do not like the movie. I really loved the movie, it was great.")

  ;; =>
  ;;
  ;; ({:sentiment 1, :text "I'm not sure if I like the movie."}
  ;;  {:sentiment 1, :text "I'm quite certain I do not like the movie."}
  ;;  {:sentiment 4, :text "I really loved the movie, it was great."})
  )