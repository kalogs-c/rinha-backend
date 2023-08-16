(ns rinha-backend.routes)

(def pessoa-routes
  ["" {:get (fn [_]
              {:status 200
               :body {:hello "pessoas"}})}])

(def count-routes
  ["" {:get (fn [_]
              {:status 200
               :body {:hello "count"}})}])