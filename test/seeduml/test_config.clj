(ns seeduml.test_config
  (:require [seeduml.config :as config])
  (:use [clojure.test]))

(deftest getenv-test
  (let [value (str "getenv-" (System/currentTimeMillis))
        key   (keyword value)
        _     (System/setProperty value value) ; sets the sysprop
        _     (use '[environ.core] :reload)]   ; reloads the env
    (is (= value (config/getenv key)))))
