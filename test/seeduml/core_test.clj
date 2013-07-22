(ns seeduml.core-test
  (:use midje.sweet
        seeduml.core
        seeduml.web)
  (:import [net.sourceforge.plantuml SourceStringReader]))

(set! *warn-on-reflection* true)

(defn to-graph [^String puml]
  (let [reader (SourceStringReader. puml)]
    (with-open [buffer (java.io.ByteArrayOutputStream.)]
      (.generateImage reader buffer)
      (.toByteArray buffer))))

(def bytes?
  (let [bytes-class (Class/forName "[B")]
    (fn [x]
      (= bytes-class (class x)))))

(fact "As Archie the Architect I can retrieve a stored plantuml descriptor"
      (retrieve :empty)                 => "@startuml\n@enduml"
      (retrieve :nothere)               => nil
      (save :piga "@startuml\n@enduml") => (contains {:piga "@startuml\n@enduml"}))

(fact "As Seeduml I can transubstantiate plantuml descriptors into awesome graphs (thanks to plantuml, of course)"
      (to-graph "@startuml\na -> b\n@enduml") => bytes?)

(fact "As Seeduml
       I can host only max-size elements
       So that I can survive the Internet"
      (binding [*max-size* 1]
        (do
          (EMPTY)
          (store-plantuml :a 1)
          (store-plantuml :b 1))) => (contains {:src {:b 1}}))
