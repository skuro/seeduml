(ns seeduml.web
  (:use compojure.core
        ring.adapter.jetty
        ring.util.response
        ring.middleware.params)
  (:require [compojure.route :as route]
            [seeduml.render :as render]))

(defn random-string [length]
  (let [ascii-codes (concat (range 48 58) (range 66 91) (range 97 123))]
    (apply str (repeatedly length #(char (rand-nth ascii-codes))))))

(def plantumls (atom {}))

(def cwd (System/getProperty "user.dir"))

(def css-dir (str cwd "/compass/stylesheets"))

(def js-dir (str cwd "/js"))

(defn retrieve-plantuml [id]
  (if-let [plantuml (@plantumls id)]
    (-> (response (java.io.ByteArrayInputStream. (render/render plantuml)))
        (content-type "image/png"))
    (not-found "Cannot retrieve the requested image, please try again later.")))

(defn store-plantuml [id plantuml]
  (swap! plantumls assoc id plantuml))

(defroutes seeduml-routes
  (GET "/" [] (redirect (str "/" (random-string 5))))
  (GET "/style/*" [*] (file-response (str css-dir "/" *)))
  (GET "/script/*" [*] (file-response (str js-dir "/" *)))
  (GET "/img/default.png" [] (resource-response "default.png"))
  (GET "/img/:id.png*" [id] (retrieve-plantuml id))
  (GET "/:id" [id] (resource-response "sketch.html"))
  (POST "/:id" [id :as {params :params}] (let [plantuml (params "plantuml")] (store-plantuml id plantuml)))
  (route/not-found (render/not-found)))

(def app
  (-> seeduml-routes
      wrap-params))

(defonce server (run-jetty #'app {:port 8080 :join? false}))
