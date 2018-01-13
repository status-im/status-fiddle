(ns status-fiddle.events
  (:require [re-frame.core :as re-frame]
            [status-fiddle.editor :refer [start-editor-sync!]]
            [status-fiddle.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
  :update-text
  (fn [state [_ new-text]]
    (assoc-in state [:text] new-text)))

(re-frame/reg-event-db
  :dissoc
  (fn [state [_ key]]
    (dissoc state key)))

(re-frame/reg-event-db
  :upadiet
  (fn [state [_ data]]
    (merge state data)))
