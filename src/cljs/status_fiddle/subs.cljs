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
  :error
  (fn [db]
    (:error db)))
