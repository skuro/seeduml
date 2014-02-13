(ns seeduml.config
  (:require [environ.core :as env]))

(defn getenv
  "Returns the value of the system property found at the given key"
  [key]
  (env/env key))
