(ns subman-parser.models-test
  (:require [clojure.test :refer [deftest testing]]
            [clojurewerkz.elastisch.rest.document :as esd]
            [test-sugar.core :refer [is= is-do]]
            [subman-parser.models :as models]
            [monger.collection :as mc]
            [monger.core :as mg]))

(deftest test-in-db
  (with-redefs [mc/any? (fn [_ _ {:keys [url]}]
                          (= url "test"))
                mg/get-db (constantly nil)]
    (is-do true? (models/in-db {:url "test"}))))
