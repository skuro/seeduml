(ns seeduml.store
  "Interacts with the server to store the latest sources of a graph"
  (:require [ajax.core :refer [GET POST]]
            [seeduml.model :as model]))

(defn update-plantuml
  "Sends an update to the server to store the latest version of a plantuml file"
  [code]
  (let [id (model/get-pad-id)]
    (POST (str "/" id) {:params {:plantuml code}
                        :format :raw})))
