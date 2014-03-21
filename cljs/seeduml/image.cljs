(ns seeduml.image
  "Renders the plantuml image on screen and updates it"
  (:require [seeduml.model :as model]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn gen-src
  "Generates a new image src URL to trigger browser reload"
  [pad]
  (let [millis (.getTime (js/Date.))
        src (str "/img/" pad ".png#" millis)]
    src))

(defn render-image
  "Om component constructor function. Takes care of re-rendering the image."
  [app-state owner-state]
  (let [src (gen-src (model/get-pad-id))]
    (.log js/console "src is " src)
    (dom/img #js {:src src} (model/fetch [:editor :code]))))

(defn find-image
  "Locates where to render the image in the DOM"
  []
  (.getElementById js/document "graph-container"))

(om/root render-image model/app {:target (find-image)})
