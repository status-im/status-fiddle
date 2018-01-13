(ns status-fiddle.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
  :text
  (fn [db]
    (:text db)))

(re-frame/reg-sub
  :cm-instance
  (fn [db]
    (:cm db)))

(re-frame/reg-sub
  :db
  (fn [db] db))
