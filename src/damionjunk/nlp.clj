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


  (nlp/sentiment-ner-maps "Here, let me Google that for you.")

  ;; =>
  ;; ({:sentiment 1,
  ;;   :text "Here, let me Google that for you.",
  ;;   :tokens ({:pos "RB", :ner "O", :token "Here"}
  ;;            {:pos ",", :ner "O", :token ","}
  ;;            {:pos "VB", :ner "O", :token "let"}
  ;;            {:pos "PRP", :ner "O", :token "me"}
  ;;            {:pos "NNP", :ner "ORGANIZATION", :token "Google"}
  ;;            {:pos "IN", :ner "O", :token "that"}
  ;;            {:pos "IN", :ner "O", :token "for"}
  ;;            {:pos "PRP", :ner "O", :token "you"}
  ;;            {:pos ".", :ner "O", :token "."})})


  (nlp/pos-ner-maps "What part of speech is this? I would like to know.")

  ;; =>
  ;;
  ;; ({:text "What part of speech is this?",
  ;;   :tokens ({:pos "WDT", :ner "O", :token "What"}
  ;;            {:pos "NN", :ner "O", :token "part"}
  ;;            {:pos "IN", :ner "O", :token "of"}
  ;;            {:pos "NN", :ner "O", :token "speech"}
  ;;            {:pos "VBZ", :ner "O", :token "is"}
  ;;            {:pos "DT", :ner "O", :token "this"}
  ;;            {:pos ".", :ner "O", :token "?"})}
  ;;  {:text "I would like to know.",
  ;;   :tokens ({:pos "PRP", :ner "O", :token "I"}
  ;;            {:pos "MD", :ner "O", :token "would"}
  ;;            {:pos "VB", :ner "O", :token "like"}
  ;;            {:pos "TO", :ner "O", :token "to"}
  ;;            {:pos "VB", :ner "O", :token "know"}
  ;;            {:pos ".", :ner "O", :token "."})})


  )