(ns seeduml.store
  (:require [clojurewerkz.neocons.rest :as rest]
            [clojurewerkz.neocons.rest.nodes :as node]
            [clojurewerkz.neocons.rest.relationships :as rel]
            [clojurewerkz.neocons.rest.cypher :as cyph]
            [clojurewerkz.neocons.rest.records :as rec]))

(defn- getenv
  "Returns the value of a system property"
  [prop]
  (System/getenv prop))

(def ^:dynamic *login* (getenv "NEO4J_USER"))

(def ^:dynamic *password* (getenv "NEO4J_PASSWORD"))

(def ^:dynamic *url* (getenv "NEO4J_URL"))

(def ^:dynamic *connection*
  (rest/connect! *url* *login* *password*))

(defn find-index [name]
  (first (filter #(= name (:name %))
                 (node/all-indexes))))

(defn find-in-index [idx key value]
  (node/find (:name idx) key value))

(defn get-category-index []
  (if-let [idx (find-index "category")]
    idx
    (node/create-index "category")))

(defn create-category
  "Creates a node that represents a category (e.g. Users)"
  [category]
  (let [cat-idx (get-category-index)
        props   {:name category
                 :type "category"}
        cat     (node/create props)
        _       (node/add-to-index cat (:name cat-idx) "category" category true)]
    cat))

(defn create-in-category
  "Creates a new node in the specified category"
  [cat props role]
  (let [n (node/create props)]
    (rel/create n cat role)
    n))

(defn delete-node
  "Delete a single node"
  [node]
  (cyph/query "START r=relationship(*), n=node({nid})
               MATCH n-[r]-()
               DELETE r" {:nid (:id node)})
  (node/delete node))

(defn get-category
  "Retrieves a node representing a category"
  [name]
  (let [cat-idx (get-category-index)
        cat (find-in-index cat-idx "category" name)]
    (if (seq cat)
      (first cat)
      (create-category name))))

(defn all-from-category
  "Retrieves all the nodes that belong to a category
   and have a matching key-value property"
  [category]
  (let [cat (get-category category)]
    (cyph/query "START cat=node({cid})
                 MATCH cat<--match
                 RETURN match" {:cid (:id cat)})))

(defn one-from-category
  "Retrieves the first node in a category that matches the
   key value pair provided"
  [cat key value]
  (let [query (str "START cat=node({cid})
                    MATCH cat<--match
                    WHERE match." key " = {value}
                    RETURN match")
        res (cyph/query query {:cid (:id cat)
                               :value value})]
    (if (seq (:data res))
      (rec/instantiate-node-from (-> res
                                     :data
                                     first
                                     first)))))

(defn update
  "Updates a node with new properties values"
  [node data]
  (node/update node data))
