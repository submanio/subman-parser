(defproject subman-parser "0.1.0-SNAPSHOT"
            :description "service for fast searching subtitles"
            :url "https://github.com/nvbn/subman"
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [enlive "1.1.5"]
                           [clojurewerkz/elastisch "2.1.0"]
                           [clj-http "1.1.0"]
                           [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                           [overtone/at-at "1.2.0"]
                           [swiss-arrows "1.0.0"]
                           [environ "1.0.0"]
                           [test-sugar "2.1"]
                           [org.clojure/tools.logging "0.3.1"]
                           [itsy "0.1.1"]
                           [clj-di "0.5.0"]
                           [com.novemberain/monger "2.1.0"]
                           [com.cemerick/piggieback "0.1.5"]]
            :plugins [[lein-environ "1.0.0"]]
            :profiles {:dev {:env {:is-debug true
                                   :ga-id ""
                                   :site-url "http://localhost:3000/"
                                   :db-host "http://127.0.0.1:9200"
                                   :index-name "subman7"
                                   :raw-db-host "localhost"
                                   :raw-db-port "27017"
                                   :raw-db-name "subman7"}}
                       :uberjar {:aot :all
                                 :env {:is-debug false
                                       :ga-id "UA-54135564-1"
                                       :site-url "http://subman.io/"
                                       :db-host "http://127.0.0.1:9200"
                                       :index-name "subman7"}}}
            :source-paths ["src/clj",]
            :test-paths ["test/clj"]
            :main subman-parser.core)
