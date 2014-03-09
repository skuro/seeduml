(ns seeduml.test-utils
  (:require [seeduml.store     :as s]
            [seeduml.plant-uml :as p]))

#_("DummyStore expects to be given a map as follows:"

  {:category [{:propA "foo"
               :propB "tst"}
              {:propC "ars"
               :propD "ara"}]}

  "A more concrete example:"

  {"Plantuml" [{:source "@startuml\nAlice->Bob\n@enduml"}]
   "Users" [{:login "alice" :name "Alice de User" :pass "dummy"}]})

(deftype DummyStore [cats]
  s/PadStore
  (get-category [this cat]
    (@cats cat))
  (one-from-category [this cat key value]
    (if-let [cat (s/get-category this cat)]
      (do
        (println "cat is: " cat)
        (first
         (filter #(= value (% key)) cat)))))
  (update [this node props])
  (create-in-category [this cat props rel]
    (swap! cats (fn [old new]
                  (merge-with into old new)) {cat props})))

(defn set-store
  "Registers a dummy stores with the given nodes"
  [& nodes]
  (let [merge-nodes (fn [nodes]
                      (atom (apply merge-with into nodes)))]
    (s/register-store :dummy (DummyStore. (merge-nodes nodes)))))
