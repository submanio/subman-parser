(ns subman.web.views
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [environ.core :refer [env]]
            [subman.helpers :refer [as-static make-static]]))

(def debug-js
  ["components/jquery/dist/jquery.js"
   "components/bootstrap/dist/js/bootstrap.js"
   "components/typeahead.js/dist/typeahead.jquery.js"
   "components/react/react.js"
   "cljs-target/goog/base.js"
   "main.js"])

(def production-js
  ["components/jquery/dist/jquery.min.js"
   "components/bootstrap/dist/js/bootstrap.min.js"
   "components/typeahead.js/dist/typeahead.jquery.min.js"
   "components/react/react.min.js"
   "main.js"])

(def debug-css
  ["components/bootstrap/dist/css/bootstrap.css"
   "components/bootstrap/dist/css/bootstrap-theme.css"
   "components/font-awesome/css/font-awesome.css"
   "components/typeahead.js-bootstrap3.less/typeahead.css"
   "main.css"])

(def production-css
  ["components/bootstrap/dist/css/bootstrap.min.css"
   "components/bootstrap/dist/css/bootstrap-theme.min.css"
   "components/font-awesome/css/font-awesome.min.css"
   "components/typeahead.js-bootstrap3.less/typeahead.css"
   "main.css"])

(defn get-ga-code
  [ga-id]
  (str "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                                    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                   m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

        ga('create', '" ga-id "', 'auto');
        ga('send', 'pageview');"))

(defn index-page []
  (let [is-debug (env :is-debug)
        ga-id (env :ga-id)]
    (html5 [:head
            [:link {:rel  "icon"
                    :type "image/png"
                    :href (first (make-static "favicon.png"))}]
            [:link {:type "application/opensearchdescription+xml"
                    :rel  "search"
                    :href (first (make-static "opensearch.xml"))}]
            [:title "Subman - subtitle search service"]
            (apply as-static include-css (if is-debug
                                           debug-css
                                           production-css))
            (apply as-static include-js (if is-debug
                                          debug-js
                                          production-js))
            (when is-debug
              [:script "goog.require('subman.core');"])]
           [:body [:div#main]
            [:script (str "subman.core.run();"
                          (get-ga-code ga-id))]])))
