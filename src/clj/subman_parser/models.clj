(ns subman-parser.models
  (:require [clojure.set :refer [union]]
            [clojurewerkz.elastisch.rest.document :as esd]
            [monger.collection :as mc]
            [environ.core :refer [env]]
            [clj-di.core :refer [register! get-dep]]
            [monger.core :as mg]
            [clojurewerkz.elastisch.rest :as esr]
            [subman-parser.helpers :refer [defsafe]]))

(defn get-raw-db
  []
  (mg/get-db (get-dep :raw-db-connection) (env :raw-db-name)))

(defn connect!
  "Connect to databases"
  []
  (register! :db-connection (esr/connect (env :db-host)))
  (register! :raw-db-connection (mg/connect {:host (env :raw-db-host)
                                             :port (-> :raw-db-port env Integer.)})))

(defn prepare-to-index
  "Prepare document to putting in index."
  [doc]
  doc)

(defsafe create-document!
  "Put document into elastic"
  [doc]
  (let [raw-db (get-raw-db)]
    (mc/insert raw-db "subtitle" doc))
  (esd/create (get-dep :db-connection)
              (env :index-name) "subtitle" (prepare-to-index doc)))

(defn in-db
  "Check subtitle already in db"
  [subtitle]
  (mc/any? (get-raw-db) "subtitle" {:url (:url subtitle)}))
