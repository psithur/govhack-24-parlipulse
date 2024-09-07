(ns psithur.govhack.get-data
  (:require
    [charred.api :as charred]
    [clj-http.client :as http]
    [clojure.string]
    [hickory.core :as h]
    [hickory.select :as s]
    [hickory.zip :as hzip]
    [tick.core :as t])
  (:import [java.net URLEncoder]))

(def parse-json (charred/parse-json-fn))
(defn escape-uri
  [s]
  (URLEncoder/encode s "UTF-8"))

(defn convert-url
  [url]
  (clojure.string/replace
    url
    #"http[s]?://www\.aph\.gov\.au/Parliamentary_Business/Hansard/Hansard_Display\?bid=([^&]+)&sid=(.*)"
    "$1$2"))

(defn list-hansards
  []
  (let [page (-> "https://www.aph.gov.au/Parliamentary_Business/Hansard/Hansreps_2011"
                 (http/get)
                 :body
                 (h/parse)
                 (h/as-hickory))]

    (->> (s/select (s/tag :a) page)
         (keep (fn [{:keys [attrs]}]
                 (when-let [href (:href attrs)]
                   (when (clojure.string/starts-with? (last (clojure.string/split href #"//"))
                                                      "www.aph.gov.au/Parliamentary_Business/Hansard/Hansard_Display")
                     href))))
         (map convert-url))))

(def base-url "https://www.aph.gov.au/api/hansard/transcript?id=")

(defn get-hansard
  [hansard-tok]
  (let [uri     (str base-url (escape-uri hansard-tok))
        page    (-> uri
                    (http/get)
                    :body
                    parse-json)
        xml-uri (get page "ViewSaveXMLLink")]
    (prn "Getting" xml-uri)
    (-> xml-uri
        (http/get
          {:headers
           ; we get a 403 without this UA...
           {"User-Agent"
            "Mozilla/5.0 (X11); Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"}})
        :body)))

(defn file-path-exists?
  [path]
  (let [file (java.io.File. (str "resources/" path))]
    (if (.exists file)
      true
      (do (.mkdirs (.getParentFile file)) ; Create parent directories if needed
          file)))) ; Create the file

(defn get-all-hansards!
  []
  (let [hansards (list-hansards)]
    (doseq [hansard hansards]
      (let [fp (file-path-exists? hansard)]
        (if (true? fp) (prn "Already have" hansard) (spit fp (get-hansard hansard)))))))
