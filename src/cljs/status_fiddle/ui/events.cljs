(ns status-fiddle.ui.events
  (:require [re-frame.core :as re-frame]
            [status-fiddle.ui.db :as db]
            [status-fiddle.local-storage :as local-storage]
            [status-fiddle.gist :as gist]
            [status-fiddle.ipfs :as ipfs]
            [status-fiddle.ui.compiler.compiler :as compiler]
            [status-fiddle.utils :as utils]
            [reagent.core :as reagent]
            [status-fiddle.local-storage :as ls]
            [status-fiddle.templates :as templates]
            [status-fiddle.ui.compiler.extensions :as extensions]
            [pluto.reader.hooks :as hooks]))

;; HELPERS

(re-frame/reg-event-db
  :set
  (fn [db [_ k v]]
    (assoc db k v)))

(re-frame/reg-event-db
  :set-in
  (fn [db [_ path v]]
    (assoc-in db path v)))

(re-frame/reg-event-db
  :set-cm
  (fn [db [_ target-div cm]]
    (assoc-in db [:cm target-div] cm)))

;; HANDLERS

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(defn update-source-and-compile [source]
  (re-frame/dispatch [:update-source :device-dom-target source])
  (re-frame/dispatch [:compile-and-render :device-dom-target]))

(re-frame/reg-event-fx
  :load-code
  (fn [_ _]
    (let [gist-id (utils/get-anchor "gist")
          hash (utils/get-anchor "ipfs")]
      (if (or gist-id hash)
        (if gist-id
          (gist/load gist-id update-source-and-compile)
          (ipfs/load hash update-source-and-compile))
        (update-source-and-compile (or (ls/get-source) templates/default)))
      nil)))

(re-frame/reg-event-fx
  :update-and-compile-component
  (fn [_ [_ source]]
    {:dispatch-n [[:update-source :component-dom-target source]
                  [:compile-and-render :component-dom-target]]}))

(re-frame/reg-event-fx
 :update-and-compile-screen
 (fn [_ [_ source]]
   {:dispatch-n [[:update-source :screen-dom-target source]
                 [:compile-and-render :screen-dom-target]]}))

(re-frame/reg-event-fx
  :update-source
  (fn [{db :db} [_ target-div new-text]]
    (let [cm (get-in db [:cm target-div])]
      (when cm (.setValue cm new-text))
      {:db (assoc-in db [:source target-div] new-text)})))

(re-frame/reg-event-fx
  :save-source
  (fn [{db :db} [_ source target]]
    (when (= target :device-dom-target)
      (local-storage/save-to-local-storage source))
    {:db (assoc-in db [:source target] source)}))

(re-frame/reg-event-db
  :show-color
  (fn [db [_ color label]]
    (assoc db :color {:color color :label label})))

(re-frame/reg-event-db
  :show-icon
  (fn [db [_ icon]]
    (assoc db :icon icon)))

(re-frame/reg-event-fx
 :publish-source-to-ipfs
 (fn [{{:keys [source forms] :as db} :db}]
   (ipfs/save (:device-dom-target source) (:extensions forms))
   {:db (assoc db :publish {:in-progress? true})}))

(re-frame/reg-event-fx
  :switch-device
  (fn [{{:keys [os] :as db} :db} [_ device]]
    (merge
      {:db (merge db (select-keys device [:phone-name :screen-width :screen-height :os]))}
      (when (not= (:os device) os)
        {:dispatch [:compile-and-render :device-dom-target]}))))

(re-frame/reg-event-fx
  :compile-and-render
  (fn [{{:keys [os source forms]} :db} [_ target event-source]]
    (let [source (or event-source (get source target))
          dom-target (.getElementById js/document (name target))]
      {:dispatch-n
       [[:save-source source target]
        (when (= :device-dom-target target)
          [:set :publish nil])
        (if (:extensions forms)
          (let [readed (extensions/read source)
                errors (:errors readed)]
            (if errors
              [:set-in [:error target] (:pluto.reader.errors/message (first errors))]
              (let [parsed-ext (extensions/parse (:data readed))
                    first-in-hook (first (vals (get-in parsed-ext [:data :hooks])))
                    {:keys [parsed hook-ref]} (first (vals first-in-hook))]
                (do
                  (reagent/render-component (hooks/hook-in (:hook hook-ref) (first (keys first-in-hook)) nil parsed nil) dom-target)
                  [:set-in [:error target] nil]))))
          (when dom-target
            (compiler/compile os source
              #(if (and % (utils/valid-hiccup? %))
                 (do
                   (reagent/render-component % dom-target)
                   (re-frame/dispatch [:set-in [:error target] nil]))
                 (re-frame/dispatch [:set-in [:error target] "Hiccup expression is invalid"])))))]})))