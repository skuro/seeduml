(ns seeduml.editor
  "Creates an interactive source editor, based on CodeMirror"
  (:require [seeduml.model :as model]
            [seeduml.store :as store]))

(defn version-code
  "Creates a new version of the application state from the current content of the editor, which is then returned"
  [editor]
  (let [code (.getValue editor)]
    (store/update-plantuml code)
    (model/update [:editor :code] code)))

(defn set-timer
  "Resets the repainting timer"
  [editor]
  (aset editor "timer" (js/setTimeout #(version-code editor) 100)))

(defn clear-timer
  "Cancels the repainting timer"
  [editor]
  (js/clearTimeout (.-timer editor)))

(defn reset-watch
  "Creates a function that repaints the graph after an edit"
  [editor]
  (fn []
    (clear-timer editor)
    (set-timer editor)))

(defn init
  "Initializes CodeMirror attaching it to the provided DOM element"
  [elem]
  (let [config #js {:value "@startuml\nBob -> Alice : hello\n@enduml"
                    :mode "plantuml"
                    :lineNumbers true}
        editor (.fromTextArea js/CodeMirror elem config)]
    (.setSize editor "auto" "80%")
    (.on editor "change" (reset-watch editor))))
