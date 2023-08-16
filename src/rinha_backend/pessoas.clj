(ns rinha-backend.pessoas
  (:require [rinha-backend.db :as db]))

(defn get-pessoas-len
  [_]
  {:status 200
   :body (-> (db/count-pessoas db/config)
             first
             :count
             str)})

(defn post-pessoa
  [req]
  (let [payload (-> req
                    :body-params
                    db/format-pessoa-req)
        pessoa-id (db/insert-pessoa db/config payload)]
    {:status 201
     :headers {"Location" (str "/pessoas/" (-> pessoa-id
                                               first
                                               :id
                                               db/uuid-to-string))}}))
(defn get-pessoa
  [req]
  (let [id (get-in req [:path-params :id])
        user (db/get-pessoa db/config {:id (java.util.UUID/fromString id)})]
    {:status 200
     :body (db/parse-pessoa! user)}))

(defn search-pessoa
  [req]
  (let [q (get-in req [:query-params "t"])
        results (db/search-pessoa db/config {:substring (str q)})]
    {:status 200
     :body (map db/parse-pessoa! results)}))