(ns rinha-backend.core
  (:gen-class)
  (:require [reitit.ring :as ring]
            [org.httpkit.server :refer [run-server]]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [rinha-backend.routes :refer [count-routes pessoa-routes]]))

(defonce server (atom nil))

(def app
  (ring/ring-handler
   (ring/router
    ["/"
     ["pessoas" pessoa-routes]
     ["contagem-pessoas" count-routes]]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware]}})))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main []
  (println "Running server on port 8080")
  (reset! server (run-server app {:port 8080})))

(comment
  (-main)
  (stop-server)
  )