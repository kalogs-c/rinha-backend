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
   :subname "//localhost:5432/pessoa"
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
  (-> pessoa
      (assoc :stack (-> pessoa
                        :stack
                        parse-pg-array))
      (assoc :id (-> pessoa
                     :id
                     uuid-to-string))
      (assoc :nascimento (-> pessoa
                             :nascimento
                             jt/local-date))))

(defn format-pessoa-req [pessoa]
  (-> pessoa
      (assoc :nascimento (-> pessoa
                             :nascimento
                             convert-string-to-date))
      (assoc :stack (-> pessoa
                        :stack
                        into-array))))

(comment
  (create-pessoa-table config)
  (convert-string-to-date "2023-08-15")
  (drop-pessoa-table! config)
  ;; :apelido, :nome, :nascimento, :stack
  (def pessoa {:apelido "oyutro"
               :nome "Putro"
               :nascimento (convert-string-to-date "2023-08-15") ;; Data no formato AAAA-MM-DD
               :stack (into-array ["Go" "Scala"])})
  (-> (insert-pessoa config pessoa)
      first
      :id
      uuid-to-string)
  (-> (get-pessoa config {:id (java.util.UUID/fromString "38266ed3-8e22-4abd-be1f-e5fa1c0a0fa2")})
      parse-pessoa!)
  (parse-pessoa! {:stack (list "oi" "carlos")})
  (search-pessoa config {:substring "tro"})
  (list-pessoas config)
  (count-pessoas config))