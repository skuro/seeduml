(ns seeduml.store
  "Interacts with the server to store the latest sources of a graph"
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [ajax.core :refer [GET POST]]
            [seeduml.model :as model]
            [cljs.core.async :refer [chan mult tap >!]]))

(def events
  "The core channel of store events. Don't use directly if you don't want to
   prevend registered listeners from receiving events."
  (chan))

(def published-events
  "Events are published in a mult, to allow for multiple readers"
  (mult events))

(defn subscribe
  "Registers a new subscriber channel to the store events"
  [ch]
  (tap published-events ch))

(defn request-handler
  "Handles a successful POST request to the server"
  [_]
  (go (>! events :plantuml_updated)))

(defn update-plantuml
  "Sends an update to the server to store the latest version of a plantuml file"
  [code]
  (let [id (model/get-pad-id)]
    (POST (str "/" id) {:params {:plantuml code}
                        :format :raw
                        :handler request-handler})))
