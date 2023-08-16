(ns rinha-backend.routes
  (:require [rinha-backend.pessoas :refer [post-pessoa
                                           get-pessoa
                                           search-pessoa
                                           get-pessoas-len]]
            [schema.core :as s]))

(def pessoa-routes
  ["pessoas"
   ["" {:get search-pessoa
        :post {:parameters {:body {:apelido s/Str
                                   :nome s/Str
                                   :nascimento s/Str
                                   :stack [s/Str]}}
                :handler post-pessoa}}]
   ["/:id" {:get get-pessoa}]])

(def count-routes
  ["contagem-pessoas" {:get get-pessoas-len}])