(ns status-fiddle.local-storage
  (:require [alandipert.storage-atom :refer [local-storage]]
            [re-frame.core :as re-frame]))

(defonce sources (local-storage (atom {}) :sources))

(defn code-saved-locally? []
  (:source @sources))

(defn load-from-local-storage []
  (re-frame/dispatch-sync [:update-source (:source @sources)]))

(defn save-to-local-storage [source]
  (when source
    (swap! sources assoc :source source)))

