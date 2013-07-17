(ns seeduml.render
  (:import [net.sourceforge.plantuml SourceStringReader]))

(defn home []
  "Home")

(defn sketch [id]
  (str "Sketching" id))

(defn not-found []
  "Not found")

(defn render [^String puml]
  (let [reader (SourceStringReader. puml)]
    (with-open [buffer (java.io.ByteArrayOutputStream.)]
      (.generateImage reader buffer)
      (.toByteArray buffer))))
