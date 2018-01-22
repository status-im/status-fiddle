(ns status-im.utils.gfycat.core
  (:require [clojure.string :as str]))

(defn- pick-random
  [gen vector]
  (str/capitalize ""))

(def unknown-gfy "Unknown")

(defn generate-gfy
  ([public-key]
   (case public-key
     nil unknown-gfy
     "0" unknown-gfy
     ""))
  ([] (generate-gfy "")))
