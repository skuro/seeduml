(ns seeduml.test-plant-uml
  (:use [clojure.test])
  (:require [seeduml.plant-uml :as p]
            [seeduml.store :as s]
            [seeduml.config :as c]))

(deftype DummyStore [cats]
  s/PadStore
  (get-category [this cat]
    (cats cat))
  (one-from-category [this cat key value]
    (if-let [cat (s/get-category this cat)]
      (first
       (filter #(= value (% key)) cat))))
  (update [this node props])
  (create-in-category [this cat props rel]))

(c/setenv :store-engine :dummy)

(defn set-store
  "Registers a dummy stores with the given nodes"
  [& nodes]
  (s/register-store :dummy (DummyStore. {p/category nodes})))

(deftest get-puml
  (let [_ (set-store {"pad" :__test-node-id
                      :data {:source "foobar"}})]
    (is (= "foobar" (p/get-puml :__test-node-id)))))
