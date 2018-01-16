(ns status-fiddle.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  :text
  (fn [db]
    (:text db)))
