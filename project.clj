(defproject seeduml "0.1.0-SNAPSHOT"
  :description "Growing your PlantUML graphics"
  :url "http://seeduml.net"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:dependencies [[midje "1.5.1"]]}}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [ring/ring-jetty-adapter "1.2.0"]
                 [net.sourceforge.plantuml/plantuml "7972-SNAPSHOT"]])
