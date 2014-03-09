(ns seeduml.plant-uml
  (:require [seeduml.store :as s]))

(def category "Plantuml")

(defn get-cat
  []
  (s/with-store (s/get-category category)))

(def default-puml "@startuml
Bob->Alice: hello
@enduml")

(defn- get-puml-node [pad]
  (s/with-store (s/one-from-category category "pad" pad)))

(defn get-puml
  "Retrieves a PlantUML source file from the store, or the default one if not found."
  [pad]
  (if-let [stored-puml (-> pad
                           get-puml-node)]
    (-> stored-puml :data :source)
    default-puml))

(defn store-puml
  "Stores a new version of the plant uml source, or creates a new one"
  [pad puml]
  (if-let [stored-puml (get-puml-node pad)]
    (s/with-store (s/update stored-puml (merge (:data stored-puml)
                                               {:source puml})))
    (s/with-store (s/create-in-category (get-cat) {:source puml
                                                   :pad    pad} "is_a"))))
