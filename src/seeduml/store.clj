(ns seeduml.store
  (:use environ.core)
  (:require [seeduml.config :as config]))

(def stores (atom {}))

(defn register-store [id store]
  (swap! stores assoc id store))

(defprotocol PadStore
  (get-category       [this cat]           "Retrieves a category from the store")
  (one-from-category  [this cat key value]     "Retrieves a single element from the store that matches the key-value search")
  (update             [this node props]    "Updates the given node to hold the new properties")
  (create-in-category [this cat props rel] "Creates a new node in the given category, establishing the given relationship between the category and the new node"))

(defn select-store []
  (let [store-id (or (config/getenv :store-engine) :neo4j-remote)]
    (@stores store-id)))

(defmacro with-store
  [form]
  (cons (first form)
        (cons (select-store)
              (rest form))))
