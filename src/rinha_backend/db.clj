(ns rinha-backend.db
  (:require [hugsql.core :as hugsql]
            [java-time.api :as jt]
            java-time.repl))

(declare create-pessoa-table)
(declare drop-pessoa-table!)
(declare get-pessoa)
(declare list-pessoas)
(declare count-pessoas)
(declare insert-pessoa)
(declare search-pessoa)
(hugsql/def-db-fns "pessoa.sql")

(def config
  {:classname "org.postgres.Driver"
   :subprotocol "postgresql"
   :subname "//db:5432/rinha"
   :user "rinha"
   :password "rinha"})

(defn parse-pg-array [pg-array]
  (-> pg-array
      .getArray
      seq))

(defn uuid-to-string [uuid]
  (.toString uuid))

(defn convert-string-to-date [string-date]
  (jt/local-date "yyyy-MM-dd" string-date))

(defn parse-pessoa! [pessoa]
  (if-not (nil? (:stack pessoa))
    (-> pessoa
        (assoc :stack (-> pessoa
                          :stack
                          parse-pg-array))
        (assoc :id (-> pessoa
                       :id
                       uuid-to-string))
        (assoc :nascimento (-> pessoa
                               :nascimento
                               jt/local-date)))
    (-> pessoa
        (assoc :id (-> pessoa
                       :id
                       uuid-to-string))
        (assoc :nascimento (-> pessoa
                               :nascimento
                               jt/local-date)))))

(defn format-pessoa-req [pessoa]
  (if-not (nil? (:stack pessoa))
    (-> pessoa
        (assoc :nascimento (-> pessoa
                               :nascimento
                               convert-string-to-date))
        (assoc :stack (-> pessoa
                          :stack
                          into-array)))
    (-> pessoa
        (assoc :nascimento (-> pessoa
                               :nascimento
                               convert-string-to-date)))))