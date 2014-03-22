(ns seeduml.menu
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(def app-state (atom {:active false
                      :entries [{:label "About"
                                 :link #js{:id "about-menu-link"
                                           :href "#"}}]}))

(defn find-menu
  "Locates the menu in the DOM"
  []
  (.getElementById js/document "help-container"))

(defn menu-entry
  "Om component constuctor function. Renders a single menu entry"
  [entry owner]
  (reify
    om/IRender
    (render [_]
      (let [link (:link entry)]
        (dom/li nil
         (dom/a link (:label entry)))))))

(defn switch-visible
  [active?]
  (if active?
    #js {:id "settings-menu"
         :style #js {"visibility" "visible"}}
    #js {:id "settings-menu"}))

(defn switch-active
  [active?]
  (if active?
    #js {:id "help"
         :className "help active"}
    #js {:id "help"
         :className "help"}))

(defn toggle
  "Creates a function that can alter the cursor state as an onclick handler"
  [cursor]
  (fn [e]
    (om/transact! cursor :active (fn [active?]
                                   (if active?
                                     false
                                     true)))))

(defn menu
  [app owner]
  (let [active? (:active app)
        attrs (switch-visible active?)]
    (dom/span (switch-active active?)
              (dom/a #js {:href "#"
                          :onClick (toggle app)} "\u2630")
              (dom/div attrs
                       (dom/div nil
                                (apply dom/ul nil
                                       (om/build-all menu-entry
                                                     (:entries app)
                                                     {})))))))

(om/root menu app-state {:target (find-menu)})
