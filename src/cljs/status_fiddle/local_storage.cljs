(ns status-fiddle.local-storage
  (:require [alandipert.storage-atom :refer [local-storage]]))

(defonce sources (local-storage (atom {}) :sources))

(defn get-source []
  (:source @sources))

(defn save-to-local-storage [source]
  (when source
    (swap! sources assoc :source source)))