(ns subman-parser.helpers
  (:import (java.io StringReader))
  (:require [clojure.stacktrace :refer [print-cause-trace]]
            [clojure.tools.logging :as log]
            [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [subman-parser.const :as const]))

(defn make-safe
  "Make fnc call safe"
  [fnc fallback]
  (fn [& args]
    (try (apply fnc args)
         (catch Exception e (do (log/debug e (str "When called " fnc " with " args))
                                fallback)))))

(defmacro defsafe
  "Define safe function"
  [name & body]
  (if (string? (first body))
    `(defsafe ~name ~@(rest body))
    `(def ~name (make-safe (fn ~@body)
                           nil))))

(defn remove-first-0
  "Remove first 0 from string"
  [query]
  (clojure.string/replace query #"^(0+)" ""))

(defn get-from-line
  "Get parsed html from line"
  [line]
  (html/html-resource (StringReader. line)))

(defsafe download
  [url]
  (:body (client/get url {:socket-timeout const/conection-timeout
                          :conn-timeout const/conection-timeout})))

(defn download-with-url
  [url]
  (when-let [downloaded (download url)]
    {:url url
     :content downloaded}))

(defsafe fetch
  "Fetch url content"
  [url]
  (get-from-line (download url)))

(defn nil-to-blank
  "Replace nil with blank string"
  [item]
  (if (nil? item)
    ""
    item))

(defn get-season-episode-part
  [text]
  (->> (map #(re-find % text) [#"[sS](\d+)[eE](\d+)"
                               #"(\d+)[xX](\d+)"])
       (remove nil?)
       first))

(defn get-season-episode
  "Add season and episode filters"
  [text]
  (if-let [[_ season episode] (get-season-episode-part text)]
    [(remove-first-0 season)
     (remove-first-0 episode)]
    ["" ""]))

(defn get-from-file
  "Get parsed html from file"
  [path]
  (html/html-resource (StringReader.
                        (slurp path))))

(defn remove-spec-symbols
  "Remove spec symbols"
  [text]
  (clojure.string/replace text #"[\t\n]" ""))
