(ns seeduml.test_config
  (:require [seeduml.config :as config])
  (:use [clojure.test]))

(deftest getenv-test
  (let [value (str "getenv-" (System/currentTimeMillis))
        key   (keyword value)
        _     (System/setProperty value value) ; sets the sysprop
        _     (use '[environ.core] :reload)]   ; reloads the env
    (is (= value (config/getenv key)))))

(deftest setenv-test
  (let [test-key :__test_config]
    (is (nil? (config/getenv test-key))
        "The test key is already found in the config, cannot test")
    (config/setenv test-key 42)
    (is (= 42 (config/getenv test-key))
        "The test key was not found in the config after setenv")))
