(ns status-fiddle.events
  (:require [re-frame.core :as re-frame]
            [status-fiddle.db :as db]
            [status-fiddle.local-storage :as local-storage]
            [status-fiddle.gist :as gist]))

(re-frame/reg-event-db
  :set
  (fn [db [_ k v]]
    (assoc db k v)))

(re-frame/reg-event-db
  :set-in
  (fn [db [_ path v]]
    (assoc-in db path v)))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
  :load-default-template
  (fn [db _]
    (assoc db :source db/default-template)))

(re-frame/reg-event-db
  :update-source
  (fn [db [_ new-text]]
    (assoc db :source new-text)))

(re-frame/reg-event-db
  :set-cm
  (fn [db [_ cm]]
    (assoc db :cm cm)))

(re-frame/reg-event-db
  :load-source
  (fn [db [_ new-text]]
    (let [cm (:cm db)]
      (when cm (.setValue cm new-text))
      (assoc db :source new-text))))

(re-frame/reg-event-fx
  :save-the-source
  (fn [{db :db} _]
    (local-storage/save-to-local-storage (:source db))
    nil))

(re-frame/reg-event-db
  :update-result
  (fn [db [_ new-result]]
    (assoc db :result new-result)))

(re-frame/reg-event-db
  :delete-error-message
  (fn [db _]
    (dissoc db :error)))

(re-frame/reg-event-db
  :set-error
  (fn [db [_ new-error]]
    (assoc db :error new-error)))

(re-frame/reg-event-db
  :show-color
  (fn [db [_ color label]]
    (assoc db :color {:color color :label label})))

(re-frame/reg-event-db
  :show-icon
  (fn [db [_ icon]]
    (assoc db :icon icon)))

(re-frame/reg-event-fx
  :share-source-on-gist
  (fn [{db :db}]
    (gist/save (:source db))
    {:db (assoc db :url "Uploading on gist...")}))

(re-frame/reg-event-db
  :set-url
  (fn [db [_ url]]
    (assoc db :url url)))

(re-frame/reg-event-db
  :switch-device
  (fn [db [_ device]]
    (merge db (select-keys device [:phone-name :screen-width :screen-height :os]))))
