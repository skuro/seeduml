(ns seeduml.model)

(def app (atom {}))

(defn update
  "Sample usage:
    (update [:context :subcontext ... :key] value)
  Sets the application state at the given path to value, similarly to assoc-in"
  [path value]
  (swap! app assoc-in path value))

(defn fetch
  "Sample usage:
     (fetch [:context :subcontext ... :key])
  Gets the application state at the given path, similar to get-in"
  [path]
  (get-in @app path))

(defn get-pad-id
  "Retrieves the ID of the current plant UML graph"
  []
  (aget (js/document.location.pathname.split "/") 1))
