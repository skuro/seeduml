(ns seeduml.render
  (:import [net.sourceforge.plantuml SourceStringReader OptionFlags])
  (:require [clojure.java.io :as io]
            [me.raynes.laser :as laser]))

;; init code
(do
  (-> (OptionFlags/getInstance)
      (.setDotExecutable ".graphviz/bin/dot")))

(def raw-template (-> "sketch.html" io/resource laser/parse))

(defn home []
  "Home")

(defn sketch [id]
  (str "Sketching" id))

(defn not-found []
  "Not found")

(defn render
  "Renders a plantuml source into a graphic diagram"
  [^String puml]
  (let [reader (SourceStringReader. puml)]
    (with-open [buffer (java.io.ByteArrayOutputStream.)]
      (.generateImage reader buffer)
      (.toByteArray buffer))))

(defn render-page [id puml]
  (println "debug" puml)
  (let [img (str "/img/" id ".png")]
    (laser/document raw-template
                    (laser/and
                     (laser/element= :textarea)
                     (laser/id= "plantuml")) (laser/content puml)
                     (laser/and
                      (laser/element= :img)
                      (laser/id= "graph"))    (laser/attr :src img))))
