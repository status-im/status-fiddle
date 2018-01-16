(ns status-fiddle.events
  (:require [re-frame.core :as re-frame]
            [status-fiddle.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
  :update-text
  (fn [state [_ new-text]]
    (assoc-in state [:text] new-text)))
