(ns status-fiddle.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  :source
  (fn [db]
    (:source db)))

(re-frame/reg-sub
  :result
  (fn [db]
    (:result db)))

(re-frame/reg-sub
  :color
  (fn [db]
    (:color db)))

(re-frame/reg-sub
  :icon
  (fn [db]
    (:icon db)))

(re-frame/reg-sub
  :error
  (fn [db]
    (:error db)))

(re-frame/reg-sub :url :url)

(re-frame/reg-sub
  :screen-width
  (fn [db]
    (:screen-width db)))

(re-frame/reg-sub
  :screen-height
  (fn [db]
    (:screen-height db)))

(re-frame/reg-sub
  :current-os
  (fn [db]
    (:os db)))
