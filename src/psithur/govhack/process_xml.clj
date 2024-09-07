(ns psithur.govhack.process-xml
  (:require
    [charred.api :as charred]
    [clojure.data.xml :as xml]
    [clojure.data.zip :as dz]
    [clojure.data.zip.xml :as z]
    [clojure.java.io :as io]
    [clojure.string]
    [clojure.zip :as zip]))

(defn find-all-files
  [path]
  (let [file (io/file path)]
    (if (.isDirectory file)
      (mapcat find-all-files (.listFiles file))
      (when (.isFile file)
        [(.getAbsolutePath file)]))))

(defn loc->map
  [loc]
  (into {}
        (keep (fn [{:keys [content tag]}]
                (when content
                  [tag (first content)])))
        (zip/children loc)))

(defn clean-text
  [ts]
  (->> ts
       (map clojure.string/trim)
       (clojure.string/join " ")))

(defn speech
  [loc]
  (let [s (z/xml1-> loc :talk.start :talker loc->map)]
    {:speaker-name       (:name s)
     :speaker-electorate (:electorate s)
     :speaker-party      (:party s)
     :text               (clean-text (z/xml-> loc :talk.text :body :p z/text))}))

(defn ->debate
  [loc]
  (let [info (z/xml1-> loc :debateinfo loc->map)]
    (map #(assoc % :info-title (:title info) :info-type (:type info)) (z/xml-> loc dz/descendants :speech speech))))


(defn process
  [xml-file-path]
  (let [reader (clojure.java.io/reader xml-file-path)]
    (let [xml-data (xml/parse reader)
          zipper   (zip/xml-zip xml-data)
          meta     (z/xml1-> zipper :hansard :session.header loc->map)]
      (map #(merge % meta) (z/xml-> zipper :hansard :chamber.xscript :debate ->debate)))))


(comment
  ; process the xmls and write out to json
  (charred/write-json "speech.json" (mapcat process (find-all-files "resources/chamber"))))
