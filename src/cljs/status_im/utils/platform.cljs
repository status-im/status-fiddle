(ns status-im.utils.platform
  (:require [status-fiddle.db :as db]))

(defn platform [] @db/current-os)

(defn android? [] (= @db/current-os "android"))
(defn ios? [] (= @db/current-os "ios"))

(def platform-specific
  (cond
    android? {}
    ios? {}
    :else {}))