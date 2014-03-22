(ns seeduml.about
  "Draws the about overlay"
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [chan]]))

(def about-state (atom {:active false}))

(def trigger-channel (chan))

(defn show
  "Triggers the about overlay to show"
  []
  (go (>! trigger-channel :show)))

(defn hide
  "Triggers the about overlay to hide"
  []
  (go (>! trigger-channel :hide)))

(defn find-about-overlay
  "Locates the about overlay in the DOM"
  []
  (.getElementById js/document "about-overlay-wrapper"))

(defn switch-active
  "Switches the class name to 'active' depending on the app state"
  [state]
  (if (:active state)
    #js {:id "about-overlay"
         :onClick (fn [e] (hide))
         :style #js {:visibility "visible"}}
    #js {:id "about-overlay"
         :onClick (fn [e] (show))}))

(defn about
  "Om component that draws the about overlay"
  [app owner]
  (reify
    om/IRender
    (render [this]
      (dom/div (switch-active app)
               (dom/div nil
                        (dom/h2 nil "Syntax help")
                        (dom/p nil "The full syntax reference can be found at the official "
                               (dom/a #js {:href "http://plantuml.sourceforge.net"}
                                      "PlantUML")
                               " website.")
                        (dom/hr nil nil)
                        (dom/h3 nil "Powered by")
                        (dom/a #js {:href "http://plantuml.sourceforge.net"}
                               (dom/img #js {:className "poweredby"
                                             :src "/static/puml.png"
                                             :alt "PlantUML logo"}))
                        (dom/a #js {:href "http://clojure.org"}
                               (dom/img #js {:className "poweredby"
                                             :src "/static/clojure.png"
                                             :alt "Clojure logo"}))
                        (dom/hr nil nil)
                        (dom/p nil "\251 Carlo Sciolla - "
                               (dom/a #js {:href "http://skuro.tk"} "skuro.tk")))))
    om/IWillMount
    (will-mount [this]
      (go (loop []
            (let [msg (<! trigger-channel)]
              (when msg
                (let [value (if (= msg :show) true false)]
                  (om/transact! app :active (fn [_] value))))
              (recur)))))))

(om/root about about-state {:target (find-about-overlay)})
