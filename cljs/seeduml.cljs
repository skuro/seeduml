(ns seeduml
  (:require [clojure.browser.repl :as repl]
            [seeduml.editor :as editor]))

(repl/connect "http://localhost:9000/repl")

(let [textArea (. js/document (getElementById "plantuml"))]
  (editor/init textArea))
