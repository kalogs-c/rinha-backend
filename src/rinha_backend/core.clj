(ns rinha-backend.core
  (:gen-class)
  (:require [reitit.ring :as ring]
            [org.httpkit.server :refer [run-server]]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [rinha-backend.routes :refer [count-routes pessoa-routes]]))

(defonce server (atom nil))

(def app
  (ring/ring-handler
   (ring/router
    ["/"
     ["" (fn [_] {:status 200 :body "Oi mãe"})]
     pessoa-routes
     count-routes]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware
                         parameters-middleware]}})))

(defn -main []
  (println "Running server on port 80")
  (reset! server (run-server app {:port 80})))
