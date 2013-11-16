(ns seeduml.store
  (:require [clojurewerkz.neocons.rest :as nr]))

(defn- getenv
  "Returns the value of a system property"
  [prop]
  (System/getProperty prop))

(def ^:dynamic *login* (getenv "NEO4J_USER"))

(def ^:dynamic *password* (getenv "NEO4J_PASSWORD"))

(def ^:dynamic *url* (getenv "NEO4J_URL"))

(def ^:dynamic *connection*
  (nr/connect! *url* *login* *password*))
