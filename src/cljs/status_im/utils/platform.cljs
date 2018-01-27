(ns status-im.utils.platform
  (:require [re-frame.core :as re-frame]
            [status-fiddle.db :as db]))

(defn platform [] @db/current-os)

(defn android? [] (= @db/current-os "android"))
(defn ios? [] (= @db/current-os "ios"))

(def platform-specific
  (cond
    android? {}
    ios? {}
    :else {}))