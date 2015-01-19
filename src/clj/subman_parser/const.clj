(ns subman-parser.const)

(def type-all -1)

(def type-addicted 0)

(def type-podnapisi 1)

(def type-opensubtitles 2)

(def type-subscene 3)

(def type-notabenoid 4)

(def type-uksubtitles 5)

(def type-none -2)

(def type-names {type-addicted "Addicted"
                 type-podnapisi "Podnapisi"
                 type-opensubtitles "OpenSubtitles"
                 type-subscene "Subscene"
                 type-notabenoid "Notabenoid"
                 type-uksubtitles "UKsubtitles"
                 type-all "all"})

(def update-deep 10)

(def update-period (* 5 60 1000))

(def crawl-period (* 24 60 60 1000))

(def result-size 100)

(def conection-timeout 1000)

(def static-path "/static/")

(def languages-limit 100)

(def default-language "english")

(def default-type type-all)

(def crawl-workers 1)

(def crawl-limit 100000)
