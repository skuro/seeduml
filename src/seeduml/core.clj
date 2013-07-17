(ns seeduml.core)

(def store (atom {:empty "@startuml\n@enduml"}))

(defn retrieve [id]
  (@store id))

(defn save [id uml]
  (swap! store assoc id uml))
