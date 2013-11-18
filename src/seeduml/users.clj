(ns seeduml.users
  (:require [seeduml.store :as s]))

(defonce -users (s/get-category "Users"))

(defn get-user
  "Retrieves a single user given his login"
  [login]
  (s/one-from-category -users "login" login))

(defn delete-user
  "Removes a user"
  [login]
  (if-let [user (get-user login)]
    (s/delete-node user)))

(defn create-user
  "Creates a new user"
  [login options]
  (let [user (get-user login)]
    (if user
      nil ;users already exists, do nothing
      (s/create-in-category -users (assoc options :login login) :has-role))))
