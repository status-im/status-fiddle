(ns status-fiddle.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  :get
  (fn [db [_ k]]
    (get db k)))