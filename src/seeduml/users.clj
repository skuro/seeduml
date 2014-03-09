(ns seeduml.users
  (:require [seeduml.store :as s]))

(def category "Users")

(defn get-user
  "Retrieves a single user given his login"
  [login]
  (s/with-store
    (s/one-from-category category :login login)))

(defn create-user
  "Creates a new user"
  [login options]
  (let [user (get-user login)]
    (if user
      nil ;users already exists, do nothing
      (s/with-store
        (s/create-in-category category (assoc options :login login) :has-role)))))
