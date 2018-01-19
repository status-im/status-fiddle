(ns status-fiddle.events
  (:require [re-frame.core :as re-frame]
            [status-fiddle.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
  :update-source
  (fn [state [_ new-text]]
    (assoc-in state [:source] new-text)))

(re-frame/reg-event-db
  :update-result
  (fn [state [_ new-result]]
    (assoc-in state [:result] new-result)))

(re-frame/reg-event-db
  :delete-error-message
  (fn [state _]
    (dissoc state :error)))

(re-frame/reg-event-db
  :set-error
  (fn [state [_ new-error]]
    (assoc-in state [:error] new-error)))