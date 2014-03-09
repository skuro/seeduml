(ns seeduml.test-users
  (:use clojure.test
        seeduml.test-utils)
  (:require [seeduml.users  :as u]
            [seeduml.config :as c]))

(c/setenv :store-engine :dummy)

(deftest get-user-by-login
  (set-store {u/category [{:login "alice"
                           :name "Alice de User"
                           :email "alice@acme.com"
                           :pass "dummy"}]})
  (is (= "Alice de User" (:name (u/get-user "alice")))))

(deftest create-user
  ; start with an empty store
  (set-store {})
  (u/create-user "bob" {:email "bob@acme.com"})
  (is (= "bob@acme.com" (:email (u/get-user "bob")))))
