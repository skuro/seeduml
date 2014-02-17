(ns seeduml.config
  (:require [environ.core :as env]))

(def overrides (atom {}))

(defn setenv
  "Overrides the configuration option at key with the provided value"
  [key value]
  (swap! overrides assoc key value))

(defn getenv
  "Returns the value of the system property found at the given key"
  [key]
  ((merge @overrides env/env) key))
