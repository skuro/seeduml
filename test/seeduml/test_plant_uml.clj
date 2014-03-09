(ns seeduml.test-plant-uml
  (:use [clojure.test]
        [seeduml.test-utils])
  (:require [seeduml.plant-uml :as p]
            [seeduml.store :as s]
            [seeduml.config :as c]))

(c/setenv :store-engine :dummy)

(deftest get-puml
  (let [_ (set-store {p/category [{"pad" :__test-node-id
                                    :data {:source "foobar"}}]})]
    (is (= "foobar" (p/get-puml :__test-node-id)))))
