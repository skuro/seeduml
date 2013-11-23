(ns seeduml.plant-uml
  (:require [seeduml.store :as store]))

(def *pumls* (store/get-category "Plantuml"))

(defn- get-puml-node [pad]
  (store/one-from-category *pumls* "pad" pad))

(defn get-puml
  "Retrieves a PlantUML source file from the store"
  [pad]
  (-> pad
      get-puml-node
      :data
      :source))

(defn store-puml
  "Stores a new version of the plant uml source, or creates a new one"
  [pad puml]
  (if-let [stored-puml (get-puml-node pad)]
    (store/update stored-puml (merge (:data stored-puml)
                                     {:source puml}))
    (store/create-in-category *pumls* {:source puml
                                       :pad    pad} "is_a")))
