(ns status-fiddle.ui.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :get
 (fn [db [_ k]]
   (get db k)))

(re-frame/reg-sub
 :get-in
 (fn [db [_ path]]
   (get-in db path)))