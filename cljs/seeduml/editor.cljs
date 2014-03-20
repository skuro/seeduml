(ns seeduml.editor)

(defn init
  "Initializes CodeMirror attaching it to the provided DOM element"
  [elem]
  (let [config #js {:value "@startuml\nBob -> Alice : hello\n@enduml"
                    :mode "plantuml"
                    :lineNumbers true}
        editor (.fromTextArea js/CodeMirror elem config)]
    (.setSize editor "auto" "80%")))
