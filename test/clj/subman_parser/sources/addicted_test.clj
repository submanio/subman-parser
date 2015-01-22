(ns subman-parser.sources.addicted-test
  (:require [clojure.test :refer [deftest testing is]]
            [test-sugar.core :refer [is= is-do]]
            [subman-parser.sources.addicted :as addicted]
            [subman-parser.helpers :as helpers :refer [get-from-file get-from-line]]))

(def release-file-name "resources/fixtures/addicted_release.html")

(def release-html (slurp release-file-name))

(def release (get-from-file release-file-name))

(def episode-file-name "resources/fixtures/addicted_episode.html")

(def episode-html (slurp episode-file-name))

(def episode (get-from-file episode-file-name))

(def release-urls ["http://www.addic7ed.com/serie/American_Horror_Story/4/13/Curtain_Call"
                   "http://www.addic7ed.com/serie/Star_Wars_REBELS/1/10/Idiot%27s_Array"
                   "http://www.addic7ed.com/serie/American_Horror_Story/4/13/Curtain_Call"
                   "http://www.addic7ed.com/serie/The_Game_%282014%29/1/5/Series_1%2C_Episode_5"
                   "http://www.addic7ed.com/serie/Murdoch_Mysteries/8/11/All_That_Glitters"
                   "http://www.addic7ed.com/serie/American_Horror_Story/4/13/Curtain_Call"
                   "http://www.addic7ed.com/serie/Archer/6/2/Three_to_Tango"
                   "http://www.addic7ed.com/serie/Arrow/3/10/Left_Behind"
                   "http://www.addic7ed.com/serie/Person_of_Interest/4/12/Control-Alt-Delete"
                   "http://www.addic7ed.com/serie/Resurrection_%282014%29/2/12/Steal_Away"])

(deftest test-is-version-line?
  (testing "when version line passed"
    (is-do true? (#'addicted/is-version-line? (get-from-line
                                                "<td class='NewsTitle'><img src='/images/folder_page.png' /></td>"))))
  (testing "when not"
    (is-do nil? (#'addicted/is-version-line? (get-from-line "<td></td>")))))

(deftest test-is-language-line?
  (testing "when language line passed"
    (is-do true? (#'addicted/is-language-line?
                   (get-from-line "<td class='language'><a href='#'><b></b></a></td>"))))
  (testing "when not"
    (is-do false? (#'addicted/is-language-line? (get-from-line "<td></td>")))))

(deftest test-get-version
  (testing "when has name"
    (is= (#'addicted/get-version (get-from-line
                                   "<td class='NewsTitle'><b><span></span>test</b></td>"))
         {:name "test"
          :langs []}))
  (testing "when not"
    (is= (#'addicted/get-version (get-from-line
                                   "<td class='NewsTitle'><b><span></span></b></td>"))
         {:name ""
          :langs []})))

(deftest test-get-language
  (is= (#'addicted/get-lang (get-from-line
                              "<td class='language'><span>test</span></td>
                              <td><a href='test-url' class='buttonDownload'></a></td>"))
       {:name "test"
        :url "http://www.addic7ed.com/test-url"}))

(deftest test-add-lang
  (with-redefs [addicted/get-lang (constantly "test")]
    (is= (#'addicted/add-lang "" {:langs []})
         {:langs ["test"]})))

(deftest test-make-url
  (testing "when start with /"
    (is= (#'addicted/make-url "/test/") "http://www.addic7ed.com/test/"))
  (testing "when not"
    (is= (#'addicted/make-url "test/") "http://www.addic7ed.com/test/")))

(deftest test-get-release-url
  (is= (#'addicted/get-releases-url 1)
       "http://www.addic7ed.com/log.php?mode=versions&page=1"))

(deftest test-get-urls-from-list
  (is (= (addicted/get-urls-from-list release)
         release-urls)))

(deftest test-get-htmls-for-parse
  (with-redefs [helpers/fetch (fn [_] release)
                helpers/download (fn [_] release-html)]
    (is= (addicted/get-htmls-for-parse 1)
         (for [url release-urls] {:url url
                                  :content release-html}))))

(deftest test-get-episode-name-string
  (is (= (addicted/get-episode-name-string episode)
         "American Horror Story - 04x13 - Curtain Call")))

(deftest test-get-episode-information
  (is (= (addicted/get-episode-information episode)
         {:episode "13"
          :name "Curtain Call"
          :season "4"
          :show "American Horror Story"})))

(deftest test-get-versions
  (testing "return all versions"
    (is (= 1 (count (#'addicted/get-versions episode)))))
  (testing "return all languages"
    (is (= 9 (-> (#'addicted/get-versions episode)
                 first
                 :langs
                 count)))))

(deftest test-get-version-langs
  (with-redefs [addicted/is-version-line? #(= % 1)
                addicted/get-version (constantly {:name "test"
                                                  :langs []})
                addicted/is-language-line? #(= % 2)
                addicted/add-lang (constantly {:name "test"
                                               :langs ["us"]})]
    (is= (#'addicted/get-version-langs [1 2])
         [{:name "test"
           :langs ["us"]}])))

(deftest test-get-subtitles
  (is (= (addicted/get-subtitles episode-html "")
         [{:episode "13"
           :lang "Italian"
           :name "Curtain Call"
           :season "4"
           :show "American Horror Story"
           :url "http://www.addic7ed.com/original/96100/3"
           :version nil}
          {:episode "13"
           :lang "Italian"
           :name "Curtain Call"
           :season "4"
           :show "American Horror Story"
           :url "http://www.addic7ed.com/original/96100/2"
           :version nil}
          {:episode "13"
           :lang "French"
           :name "Curtain Call"
           :season "4"
           :show "American Horror Story"
           :url "http://www.addic7ed.com/updated/8/96100/1"
           :version nil}
          {:episode "13"
           :lang "Portuguese (Brazilian)"
           :name "Curtain Call"
           :season "4"
           :show "American Horror Story"
           :url "http://www.addic7ed.com/updated/10/96100/1"
           :version nil}
          {:episode "13"
           :lang "Bulgarian"
           :name "Curtain Call"
           :season "4"
           :show "American Horror Story"
           :url "http://www.addic7ed.com/updated/35/96100/1"
           :version nil}
          {:episode "13"
           :lang "English"
           :name "Curtain Call"
           :season "4"
           :show "American Horror Story"
           :url "http://www.addic7ed.com/original/96100/1"
           :version nil}
          {:episode "13"
           :lang "English"
           :name "Curtain Call"
           :season "4"
           :show "American Horror Story"
           :url "http://www.addic7ed.com/original/96100/0"
           :version nil}
          {:episode "13"
           :lang "Dutch"
           :name "Curtain Call"
           :season "4"
           :show "American Horror Story"
           :url "http://www.addic7ed.com/original/96100/5"
           :version nil}
          {:episode "13"
           :lang "Dutch"
           :name "Curtain Call"
           :season "4"
           :show "American Horror Story"
           :url "http://www.addic7ed.com/original/96100/4"
           :version nil}])))
