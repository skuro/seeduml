(ns seeduml.editor
  ;(:require [ajax.core :refer [POST]])
  )

(defn submit
  "Not implemented, will post data to the server"
  []
  (.log js/console "Called submit"))

(defn set-timer
  [editor]
  (aset editor "timer" (js/setTimeout submit 300)))

(defn clear-timer
  [editor]
  (.log js/console "Called clear-timer")
  (js/clearTimeout (.-timer editor)))

(defn repaint
  "Creates a function that repaints the graph after an edit"
  [editor]
  (fn []
    (clear-timer editor)
    (set-timer editor)
    (.log js/console "Called repaint")))

(defn init
  "Initializes CodeMirror attaching it to the provided DOM element"
  [elem]
  (let [config #js {:value "@startuml\nBob -> Alice : hello\n@enduml"
                    :mode "plantuml"
                    :lineNumbers true}
        editor (.fromTextArea js/CodeMirror elem config)]
    (.setSize editor "auto" "80%")
    (.on editor "change" (repaint editor))))
