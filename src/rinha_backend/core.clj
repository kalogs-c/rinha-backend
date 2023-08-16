(ns rinha-backend.core
  (:gen-class)
  (:require [reitit.ring :as ring]
            [org.httpkit.server :refer [run-server]]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [rinha-backend.routes :refer [count-routes pessoa-routes]]
            [rinha-backend.db :as db]))

(defonce server (atom nil))

(def app
  (ring/ring-handler
   (ring/router
    ["/"
     pessoa-routes
     count-routes]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware
                         parameters-middleware]}})))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main []
  (println "Running server on port 8080")
  (db/create-pessoa-table db/config)
  (reset! server (run-server app {:port 8080})))

(comment
  (-main)

  (stop-server)
  )