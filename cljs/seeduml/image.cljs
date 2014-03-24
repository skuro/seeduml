(ns seeduml.image
  "Renders the plantuml image on screen and updates it"
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [seeduml.model :as model]
            [seeduml.store :as store]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [chan <!]]))

(def app-state (atom {}))

(defn gen-src
  "Generates a new image src URL to trigger browser reload"
  [pad stamp]
  (str "/img/" pad ".png#" stamp))

(defn render-image
  "Om component constructor function. Takes care of re-rendering the image."
  [app-state owner-state]
  (reify
    om/IInitState
    (init-state [_]
      (let [ch (chan)
            _ (store/subscribe ch)]
        {:updates ch}))
    om/IWillMount
    (will-mount [_]
      (let [updates (om/get-state owner-state :updates)]
        (go
         (loop []
           (let [update (<! updates)]
             (when update
               (om/transact! app-state :updated
                             (fn [_] (.getTime (js/Date.))))
               (recur)))))))
    om/IRender
    (render [_]
      (let [src (gen-src (model/get-pad-id) (:updated app-state))]
        (dom/img #js {:src src
                      :id "graph"} (model/fetch [:editor :code]))))))

(defn find-image
  "Locates where to render the image in the DOM"
  []
  (.getElementById js/document "graph-container"))

(om/root render-image app-state {:target (find-image)})
