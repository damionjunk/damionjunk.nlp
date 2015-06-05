(defproject damionjunk/nlp "0.3.0-SNAPSHOT"
  :description "Code examples using Stanford CoreNLP"
  :url "https://github.com/damionjunk/damionjunk.nlp"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :jvm-opts ["-Xmx1024m" "-server"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [edu.stanford.nlp/stanford-corenlp "3.5.2"]
                 [edu.stanford.nlp/stanford-corenlp "3.5.2" :classifier "models"]
                 [edu.cmu.cs/ark-tweet-nlp "0.3.2"]]
  :profiles {:dev {:dependencies [[expectations "2.1.1"]]}}
  :plugins [[lein-expectations "0.0.8"]])
