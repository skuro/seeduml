(ns seeduml.store.neo4j
  (:use environ.core
        seeduml.store)
  (:require [seeduml.config :as config]
            [clojurewerkz.neocons.rest :as rest]
            [clojurewerkz.neocons.rest.nodes :as node]
            [clojurewerkz.neocons.rest.relationships :as rel]
            [clojurewerkz.neocons.rest.cypher :as cyph]
            [clojurewerkz.neocons.rest.records :as rec]))

(def connected (atom nil))

(defn connect []
  (let [user (config/getenv :neo4j-login)
        pass (config/getenv :neo4j-pass)
        uri  (config/getenv :neo4j-url)]
    (try
      (rest/connect! uri user pass)
      (swap! connected (constantly true))
      (catch Exception e
        (println "Cannot connect to the server: " e)))))

(defn find-index [name]
  (first (filter #(= name (:name %))
                 (node/all-indexes))))

(defn get-category-index []
  (if-let [idx (find-index "category")]
    idx
    (node/create-index "category")))

(defn find-in-index [idx key value]
  (node/find (:name idx) key value))

(defn create-category
  "Creates a node that represents a category (e.g. Users)"
  [category]
  (let [cat-idx (get-category-index)
        props   {:name category
                 :type "category"}
        cat     (node/create props)
        _       (node/add-to-index cat (:name cat-idx) "category" category true)]
    cat))

(defn get-neo-category
  [cat]
  (let [cat-idx (get-category-index)
        cat (find-in-index cat-idx "category" name)]
    (if (seq cat)
      (first cat)
      (create-category name))))

(defn update-neo-node [node props]
  (node/update node props))

(defn get-neo-node [cat key value]
  (let [query (str "START cat=node({cid})
                    MATCH cat<--found
                    WHERE found." key " = {value}
                    RETURN found")
        res (cyph/query query {:cid (:id cat)
                               :value value})]
    (if (seq (:data res))
      (rec/instantiate-node-from (-> res
                                     :data
                                     first
                                     first)))))

(defn new-node [cat props role]
  (let [n (node/create props)]
    (rel/create n cat role)
    n))

(defmacro with-connection
  "Executes Neo4j queries making sure the connection to the Database was made"
  [fun & args]
  `(do (if-not @connected (connect))
       (~fun ~@args)))

(deftype Neo4jStore []
    PadStore
    (get-category [this cat] (with-connection get-neo-category cat))
    (one-from-category [this cat key value] (with-connection get-neo-node key value))
    (update [this node props] (with-connection update-neo-node node props))
    (create-in-category [this cat props role] (with-connection (new-node cat props role))))

(register-store :neo4j-remote (Neo4jStore.))

;;;;;;;;;;;;;;;;;;
;; Unused funcs ;;
;;;;;;;;;;;;;;;;;;
(comment defn all-from-category
         "Retrieves all the nodes that belong to a category
   and have a matching key-value property"
         [category]
         (let [cat (get-category category)]
           (cyph/query "START cat=node({cid})
                 MATCH cat<--found
                 RETURN found" {:cid (:id cat)})))

(comment (defn delete-node
           "Delete a single node"
           [node]
           (cyph/query "START r=relationship(*), n=node({nid})
               MATCH n-[r]-()
               DELETE r" {:nid (:id node)})
           (node/delete node)))
