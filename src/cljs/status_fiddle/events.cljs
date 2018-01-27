(ns status-fiddle.events
  (:require [re-frame.core :as re-frame]
            [status-fiddle.db :as db]
            [status-fiddle.local-storage :as local-storage]
            [status-fiddle.gist :as gist]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
  :load-default-template
  (fn [state _]
    (assoc-in state [:source] db/default-template)))

(re-frame/reg-event-db
  :update-source
  (fn [state [_ new-text]]
    (assoc-in state [:source] new-text)))

(re-frame/reg-event-db
  :set-cm
  (fn [state [_ cm]]
    (assoc-in state [:cm] cm)))

(re-frame/reg-event-db
  :load-source
  (fn [state [_ new-text]]
    (let [cm (:cm state)]
      (when cm (.setValue cm new-text))
      (assoc-in state [:source] new-text))))

(re-frame/reg-event-db
  :save-the-source
  (fn [state _]
    (local-storage/save-to-local-storage (:source state))
    state))

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

(re-frame/reg-event-db
  :show-color
  (fn [state [_ color label]]
    (assoc-in state [:color] {:color color :label label})))

(re-frame/reg-event-db
  :show-icon
  (fn [state [_ icon]]
    (assoc-in state [:icon] icon)))

(re-frame/reg-event-fx
  :share-source-on-gist
  (fn [{db :db}]
    (gist/save (:source db))
    {:db (assoc db :url "Uploading on gist...")}))

(re-frame/reg-event-db
  :set-url
  (fn [state [_ url]]
    (assoc state :url url)))