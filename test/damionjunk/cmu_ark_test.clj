(ns damionjunk.cmu-ark-test
  (:require [expectations :refer :all]
            [damionjunk.nlp.cmu-ark :refer :all]))

;;
;; Make sure the tagger is working and generating output.
(expect [{:token "LOL", :pos "!"} {:token ".", :pos ","} {:token "ur", :pos "L"}
         {:token "so", :pos "R"} {:token "funny", :pos "A"} {:token "!", :pos ","}
         {:token ":)", :pos "E"} {:token ":)", :pos "E"} {:token ":)", :pos "E"}
         {:token "<3", :pos "E"}]
        (tag "LOL. ur so funny! :) :) :) <3"))