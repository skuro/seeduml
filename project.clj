(defproject seeduml "0.1.0-SNAPSHOT"
  :description "Growing your PlantUML graphics"
  :url "http://seeduml.net"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:dependencies [[midje "1.5.1"]]
                   :env {:neo4j-url "http://localhost:7474/db/data"}}}
  :uberjar-name "seeduml.jar"
  :min-lein-version "2.0.0"
  :plugins [[lein-cljsbuild "1.0.2"]]
  ; automatically compiles clojurescript sources during clean, compile, test, and jar
  :hooks [leiningen.cljsbuild]
  :cljsbuild {
    :builds [{:source-paths ["cljs"]
              :compiler {:output-to "js/seeduml.js"
                         :optimizations :whitespace
                         :pretty-print true}}]}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2194"
                                   :exclusions [org.apache.ant/ant]]
                 [om "0.5.3"]
                 [cljs-ajax "0.2.3"]
                 [compojure "1.1.5"]
                 [me.raynes/laser "1.1.1"]
                 [ring/ring-jetty-adapter "1.2.0"]
                 [clojurewerkz/neocons "2.0.0"]
                 [org.clojure/core.async "0.1.278.0-76b25b-alpha"]
                 [environ "0.4.0"]
                 [net.sourceforge.plantuml/plantuml "7995"]])
