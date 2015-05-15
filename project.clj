(defproject damionjunk/nlp "0.2.0-SNAPSHOT"
  :description "Code examples using Stanford CoreNLP"
  :url "http://damionjunk.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :jvm-opts ["-Xmx1024m" "-server"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [edu.stanford.nlp/stanford-corenlp "3.5.2"]
                 [edu.stanford.nlp/stanford-corenlp "3.5.2" :classifier "models"]
                 ])
