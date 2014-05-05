(ns seeduml.web
  (:import [java.io ByteArrayInputStream])
  (:use compojure.core
        ring.adapter.jetty
        ring.util.response
        ring.middleware.params)
  (:require [compojure.route   :as route]
            [seeduml.render    :as render]
            [seeduml.plant-uml :as puml]
            [seeduml.store     :as store]))

(def id-length 5)

(defn random-string [length]
  (let [ascii-codes (concat (range 48 58) (range 66 91) (range 97 123))]
    (apply str (repeatedly length #(char (rand-nth ascii-codes))))))

(def cwd (System/getProperty "user.dir"))

(def css-dir (str cwd "/compass/stylesheets"))

(def js-dir (str cwd "/js"))

(def static-dir (str cwd "/static"))

(defn diagram-response [id]
  (if-let [plantuml (puml/get-puml id)]
    (-> plantuml
        render/render
        (ByteArrayInputStream.)
        response
        (content-type "image/png"))
    (not-found "Cannot retrieve the requested image, please try again later.")))

(defn page-response [pad]
  (render/render-page pad (puml/get-puml pad)))

(defn health-check []
  ; so far, only check if the store is alive
  (if-let [store (store/select-store)]
    (response (str {:store "ok"}))))

(defroutes seeduml-routes

  ; health check
  (POST "/ping" [] (health-check))

  ; the homepage redirects to a random pad
  (GET "/" [] (redirect (str "/" (random-string id-length))))

  ; static resources
  (GET "/static/*"        [*] (resource-response (str "static/" *)))
  (GET "/style/*"         [*] (file-response (str css-dir "/" *)))
  (GET "/script/*"        [*] (file-response (str js-dir "/" *)))
  (GET "/img/default.png" []  (resource-response "default.png"))

  ; dynamic requests
  (GET "/img/:id.png*"                  [id] (diagram-response id))
  (GET  ["/:id" :id #"[a-zA-Z0-9]{5}"]  [id] (page-response id))
  (POST ["/:id" :id #"[a-zA-Z0-9]{5}"]  [id :as {params :params}]
        (let [plantuml (params "plantuml")]
          (puml/store-puml id plantuml)
          (response "OK")))

  ; 404
  (route/not-found (render/not-found)))

(def app
  (-> seeduml-routes
      wrap-params))

(defn server []
  (run-jetty #'app {:port 8080 :join? false}))

(defn init-store []
  (if (nil? (store/select-store))
    ; default store is Neo4j
    (require 'seeduml.store.neo4j)))

(defn -main [port]
  (init-store)
  (run-jetty #'app {:port (Integer. port) :join? false}))
