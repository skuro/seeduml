(ns seeduml.web
  (:use compojure.core
        ring.adapter.jetty
        ring.util.response
        ring.middleware.params)
  (:require [compojure.route   :as route]
            [seeduml.render    :as render]
            [seeduml.plant-uml :as puml]))

(def id-length 5)

(defn random-string [length]
  (let [ascii-codes (concat (range 48 58) (range 66 91) (range 97 123))]
    (apply str (repeatedly length #(char (rand-nth ascii-codes))))))

(def cwd (System/getProperty "user.dir"))

(def css-dir (str cwd "/compass/stylesheets"))

(def js-dir (str cwd "/js"))

(def static-dir (str cwd "/static"))

(defn retrieve-plantuml [id]
  (if-let [plantuml (puml/get-puml id)]
    (-> (response (java.io.ByteArrayInputStream. (render/render plantuml)))
        (content-type "image/png"))
    (not-found "Cannot retrieve the requested image, please try again later.")))

(defn store-plantuml [id plantuml]
  (swap! plantumls update id plantuml))

(defn page-response [id]
  (render/render-page id (retrieve id)))

(defroutes seeduml-routes
  (GET "/" [] (redirect (str "/" (random-string id-length))))

  ; static resources
  (GET "/static/*"        [*] (resource-response (str "static/" *)))
  (GET "/style/*"         [*] (file-response (str css-dir "/" *)))
  (GET "/script/*"        [*] (file-response (str js-dir "/" *)))
  (GET "/img/default.png" []  (resource-response "default.png"))

  ; dynamic requests
  (GET "/img/:id.png*"                  [id] (retrieve-plantuml id))
  (GET  ["/:id" :id #"[a-zA-Z0-9]{5}"]  [id] (page-response id))
  (POST ["/:id" :id #"[a-zA-Z0-9]{5}"]  [id :as {params :params}]
        (let [plantuml (params "plantuml")]
          (puml/store-puml id plantuml)))

  ; 404
  (route/not-found (render/not-found)))

(def app
  (-> seeduml-routes
      wrap-params))

(defn server []
  (run-jetty #'app {:port 8080 :join? false}))

(defn -main [port]
  (run-jetty #'app {:port (Integer. port) :join? false}))
