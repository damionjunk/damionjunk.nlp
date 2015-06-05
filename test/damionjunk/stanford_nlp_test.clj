(ns damionjunk.stanford-nlp-test
  (:require [expectations :refer :all]
            [damionjunk.nlp.stanford :refer :all]))

;;
;; basic check of sentiment
(expect empty? @pipelines)
(expect '({:sentiment 4, :text "This is very happy."}) (sentiment-maps "This is very happy."))
(expect 1 (count @pipelines))
(expect 0 (do (drop-pipeline :sentiment) (count @pipelines)))


;;
;; Check sentiment-ner
(expect '({:sentiment 4, :text "This is very happy.", :tokens [{:pos "DT", :ner "O", :token "This"}
                                                               {:pos "VBZ", :ner "O", :token "is"}
                                                               {:pos "RB", :ner "O", :token "very"}
                                                               {:pos "JJ", :ner "O", :token "happy"}
                                                               {:pos ".", :ner "O", :token "."}]})
        (sentiment-ner-maps "This is very happy."))
(expect 1 (count @pipelines))
(expect '({:sentiment 4, :text "This is very happy."}) (sentiment-maps "This is very happy."))
(expect 2 (count @pipelines))

;;
;; Check POS/NER
(expect [{:text "This is very happy.", :tokens [{:pos "DT", :ner "O", :token "This"}
                                                {:pos "VBZ", :ner "O", :token "is"}
                                                {:pos "RB", :ner "O", :token "very"}
                                                {:pos "JJ", :ner "O", :token "happy"}
                                                {:pos ".", :ner "O", :token "."}]}]
        (pos-ner-maps "This is very happy."))
(expect 3 (count @pipelines))

