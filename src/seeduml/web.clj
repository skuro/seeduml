(ns seeduml.web
  (:use compojure.core
        ring.adapter.jetty
        ring.util.response
        ring.middleware.params)
  (:require [compojure.route :as route]
            [seeduml.render :as render]))

(def id-length 5)

(def ^:dynamic *max-size* 100000)

(defn random-string [length]
  (let [ascii-codes (concat (range 48 58) (range 66 91) (range 97 123))]
    (apply str (repeatedly length #(char (rand-nth ascii-codes))))))

(def plantumls (atom {:ids (clojure.lang.PersistentQueue/EMPTY)
                      :src {}}))

(defn EMPTY []
  (reset! plantumls {:ids (clojure.lang.PersistentQueue/EMPTY)
                      :src {}}))

(defn update [{:keys [ids src]} id plantuml]
  (let [new-ids (conj ids id)
        new-src (assoc src id plantuml)]
    (if (< *max-size* (count new-ids))
      (let [elem (peek new-ids)]
        ; one too many
        {:ids (pop new-ids)
         :src (dissoc new-src elem)})
      {:ids new-ids
       :src new-src})))

(defn retrieve [id]
  (get-in @plantumls [:src id] "@startuml
Bob -> Alice
@enduml"))

(def cwd (System/getProperty "user.dir"))

(def css-dir (str cwd "/compass/stylesheets"))

(def js-dir (str cwd "/js"))

(def static-dir (str cwd "/static"))

(defn retrieve-plantuml [id]
  (if-let [plantuml (retrieve id)]
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
          (store-plantuml id plantuml)))

  ; 404
  (route/not-found (render/not-found)))

(def app
  (-> seeduml-routes
      wrap-params))

(defn server []
  (run-jetty #'app {:port 8080 :join? false}))

(defn -main [port]
  (run-jetty #'app {:port (Integer. port) :join? false}))
